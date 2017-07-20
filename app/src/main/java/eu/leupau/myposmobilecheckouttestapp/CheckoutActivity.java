package eu.leupau.myposmobilecheckouttestapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import eu.leupau.mobilepaymentssdk.CartItem;
import eu.leupau.mobilepaymentssdk.MyPos;
import eu.leupau.mobilepaymentssdk.PurchaseActivity;

/**
 * Created by kamen.troshev on 19.12.2016 г..
 */

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTotalAmountTxt;
    private ListView mItemsListView;
    private RelativeLayout mPayBtn;

    private ArrayList<CartItemModel> mCartItems      = new ArrayList<>();
    private ArrayList<CartItem> mIPCCartItems   = new ArrayList<>();
    private String mOrderId;
    private float                       mTotalAmount    = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mCartItems = getIntent().getParcelableArrayListExtra("items");

        init();

        setData();
    }

    private void init(){
        mTotalAmountTxt = (TextView) findViewById(R.id.total_amount_text);
        mItemsListView  = (ListView) findViewById(R.id.items_list_view);
        mPayBtn         = (RelativeLayout) findViewById(R.id.pay_button);

        mPayBtn.setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
    }

    private void setData(){
        mTotalAmount = 0f;

        while( mCartItems.size() != 0){
            int itemId      = mCartItems.get(0).getmId();
            int quantity    = 1;

            for( int i = mCartItems.size() - 1; i > 0; i-- ){
                if( mCartItems.get(i).getmId() == itemId){
                    quantity ++;
                    mCartItems.remove(i);
                }
            }

            mIPCCartItems.add(new CartItem(mCartItems.get(0).getmTitle(), quantity, Float.parseFloat(mCartItems.get(0).getmPrice())));
            mTotalAmount += Float.parseFloat(mCartItems.get(0).getmPrice()) * quantity;
            mCartItems.remove(0);
        }

        mTotalAmountTxt.setText("Total amount: " + Utils.formatAmount(mTotalAmount) + " " + Utils.CURRENCY);

        CheckoutItemsAdapter adapter = new CheckoutItemsAdapter(this, mIPCCartItems);
        mItemsListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if( view.getId() == R.id.back_button){
            super.onBackPressed();
        }
        else if( view.getId() == mPayBtn.getId()){
            selectPaymentOption();
        }
    }

    private void selectPaymentOption(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Set<String> cardTokens = sharedPreferences.getStringSet("card_tokens", null);

        if( cardTokens == null ) {
            myPOSPurchase(null);
            return;
        }

        final ArrayList<String> sortedCardTokens = new ArrayList<>(cardTokens);
        Collections.sort(sortedCardTokens, new Comparator<String>(){
            public int compare(String obj1, String obj2) {
                return obj1.split(";")[1].compareToIgnoreCase(obj2.split(";")[1]);
            }
        });

        CharSequence paymentOptions[] = new CharSequence[sortedCardTokens.size() + 1];
        paymentOptions[0] = "New card";
        for( int i = 0; i < sortedCardTokens.size(); i++ ){
            paymentOptions[i+1] = sortedCardTokens.toArray()[i].toString().split(";")[1];
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select payment option");
        builder.setItems(paymentOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if( position == 0 )
                    myPOSPurchase(null);
                else
                    myPOSPurchase(sortedCardTokens.toArray()[position-1].toString().split(";")[0]);
            }
        });
        builder.show();
    }

    private void myPOSPurchase(String cardToken){
        mOrderId = String.valueOf(System.currentTimeMillis());
        MyPos myPos = MyPos.getInstance();
        myPos.init(
                Utils.MYPOS_SID,
                Utils.MYPOS_WALLET_NUMBER,
                Utils.CURRENCY,
                Utils.CLIENT_PRIVATE_KEY,
                Utils.SERVER_PUBLIC_KEY,
                Utils.isSandbox
        );
        myPos.setKeyIndex(Utils.KEY_INDEX);
        myPos.setLanguage(Utils.LANGUAGE);

        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.putExtra(MyPos.INTENT_EXTRA_CART_ITEMS,      mIPCCartItems);
        intent.putExtra(MyPos.INTENT_EXTRA_ORDER_ID,        mOrderId);
        if( cardToken != null )
            intent.putExtra(MyPos.INTENT_EXTRA_CARD_TOKEN,      cardToken);
        startActivityForResult(intent, MyPos.REQUEST_CODE_PURCHASE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_PURCHASE){
            int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS, -100);
            if( status == MyPos.STATUS_SUCCESS) {
                String tranRef = data.getStringExtra(MyPos.INTENT_EXTRA_TRANSACTION_REFERENCE);
                Utils.showToastMessage(this, "Purchase completed\nTransaction reference - " + tranRef);

                OrderModel orderModel = new OrderModel();
                orderModel.setmAmount(Utils.formatAmount(mTotalAmount));
                orderModel.setmCurrency(Utils.CURRENCY);
                orderModel.setmTransactionType("Purchase");
                orderModel.setmDate(Calendar.getInstance().getTime());
                orderModel.setmTransactionRef(tranRef);
                orderModel.setmOrderId(mOrderId);
                Utils.addOrderToPreferences(this, orderModel);

                setResult(RESULT_OK);
                finish();
            }
            else{
                Utils.showToastMessage(this, "Operation failed status: " + status);
            }
        }
    }
}
