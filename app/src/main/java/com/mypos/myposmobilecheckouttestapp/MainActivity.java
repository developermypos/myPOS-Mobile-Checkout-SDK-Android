package com.mypos.myposmobilecheckouttestapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mypos.mobilepaymentssdk.IPCGetTransactionStatus;
import com.mypos.mobilepaymentssdk.MyPos;
import com.mypos.mobilepaymentssdk.RefundActivity;
import com.mypos.mobilepaymentssdk.StoreCardActivity;
import com.mypos.mobilepaymentssdk.StoredCardModel;
import com.mypos.mobilepaymentssdk.UpdateCardActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView mListView;
    private ImageButton mMenuBtn;
    private RelativeLayout mCheckOutBtn;
    private TextView mVersionTextView;


    ArrayList<CartItemModel> mAllItems  = new ArrayList<>();
    ArrayList<CartItemModel> mCartItems = new ArrayList<>();

    private Toast mToast;

    private String mOrderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setData();
    }

    private void init(){
        mListView           = (ListView)        findViewById(R.id.shop_items_listview);
        mMenuBtn            = (ImageButton)     findViewById(R.id.menu_button);
        mCheckOutBtn        = (RelativeLayout)  findViewById(R.id.checkout_button);
        mVersionTextView    = (TextView)        findViewById(R.id.version_text);

        mCheckOutBtn.setOnClickListener(this);
        mMenuBtn.setOnClickListener(this);

        mToast = Toast.makeText(MainActivity.this, "Item added to cart", Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View view) {
        if( view.getId() == mCheckOutBtn.getId()){
            goToPurchaseActivity();
        }
        else if( view.getId() == mMenuBtn.getId()){
            showMenuPopup(mMenuBtn);
        }
    }

    private void setData(){
        mAllItems.add(new CartItemModel(1, "Shoe 1", "Some basic description", "56.99", Utils.CURRENCY, R.drawable.shoe1));
        mAllItems.add(new CartItemModel(2, "Shoe 2", "Some basic description", "67.35", Utils.CURRENCY, R.drawable.shoe2));
        mAllItems.add(new CartItemModel(3, "Shoe 3", "Some basic description", "45.40", Utils.CURRENCY, R.drawable.shoe3));
        mAllItems.add(new CartItemModel(4, "Shoe 4", "Some basic description", "73.50", Utils.CURRENCY, R.drawable.shoe4));

        ItemsAdapter adapter = new ItemsAdapter(this, mAllItems);
        mListView.setAdapter(adapter);
        adapter.setOnItemAddedToCardListener(new ItemsAdapter.onItemAddedToCartListener() {
            @Override
            public void onItemAddedToCart(int position) {
                if( mCheckOutBtn.getVisibility() != View.VISIBLE)
                    showCheckoutButton();

                mCartItems.add(mAllItems.get(position));
                mToast.show();
            }
        });

        try {
            mVersionTextView.setText("v. " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showCheckoutButton(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVersionTextView.getLayoutParams();
        params.bottomMargin = (int) (70 * getResources().getDisplayMetrics().density);
        mVersionTextView.setLayoutParams(params);

        mCheckOutBtn.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 60*getResources().getDisplayMetrics().density, 0);
        translateAnimation.setDuration(200);
        mCheckOutBtn.startAnimation(translateAnimation);
        mVersionTextView.startAnimation(translateAnimation);
    }

    private void goToPurchaseActivity(){
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("items", mCartItems);
        startActivityForResult(intent, MyPos.REQUEST_CODE_PURCHASE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_PURCHASE){
            mCartItems.clear();
            mCheckOutBtn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVersionTextView.getLayoutParams();
            params.bottomMargin = (int) (10 * getResources().getDisplayMetrics().density);
            mVersionTextView.setLayoutParams(params);
        }
        else if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_STORE_CARD){
            int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS, -100);
            if( status == MyPos.STATUS_SUCCESS) {
                StoredCardModel storedCard = data.getParcelableExtra(MyPos.INTENT_EXTRA_STORED_CARD);
                saveStoredCardDataInPreferences(storedCard);
            }
            else{
                Utils.showToastMessage(MainActivity.this, "Operation failed status: " + status);
            }
        }
        else if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_UPDATE_CARD){
            int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS, -100);
            if( status == MyPos.STATUS_SUCCESS) {
                StoredCardModel newStoredCard = data.getParcelableExtra(MyPos.INTENT_EXTRA_STORED_CARD);
                String oldCardToken = data.getStringExtra(MyPos.INTENT_EXTRA_OLD_CARD_TOKEN);
                updateStoredCardListInPreferences(newStoredCard, oldCardToken);
            }
            else{
                Utils.showToastMessage(MainActivity.this, "Operation failed status: " + status);
            }
        }
        else if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_REFUND){
            int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS, -100);
            if( status == MyPos.STATUS_SUCCESS) {
                float amount = data.getFloatExtra(MyPos.INTENT_EXTRA_AMOUNT, 0.00f);
                String currency = data.getStringExtra(MyPos.INTENT_EXTRA_CURRENCY);
                String transactionReference = data.getStringExtra(MyPos.INTENT_EXTRA_TRANSACTION_REFERENCE);

                OrderModel orderModel = new OrderModel();
                orderModel.setmAmount(Utils.formatAmount(amount));
                orderModel.setmCurrency(currency);
                orderModel.setmTransactionType("Refund");
                orderModel.setmDate(Calendar.getInstance().getTime());
                orderModel.setmTransactionRef(transactionReference);
                orderModel.setmOrderId(mOrderId);
                Utils.addOrderToPreferences(this, orderModel);

                Utils.showToastMessage(MainActivity.this, getString(R.string.refund_successful) + ": " + transactionReference);
            }
            else{
                Utils.showToastMessage(MainActivity.this, "Operation failed status: " + status);
            }
        }
    }

    private void saveStoredCardDataInPreferences(StoredCardModel storedCard){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> storedCards = sharedPreferences.getStringSet(Utils.PREFERENCES_STORED_CARDS, null);

        if (storedCards == null)
            storedCards = new LinkedHashSet<>();

        Set<String> newStoredCards = new LinkedHashSet<>();
        newStoredCards.addAll(storedCards);
        newStoredCards.add(storedCard.toJSON().toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(Utils.PREFERENCES_STORED_CARDS, newStoredCards);
        editor.apply();

        Utils.showToastMessage(MainActivity.this, getString(R.string.card_stored_successfully_card_token, storedCard.getCardToken()));
    }

    private void updateStoredCardListInPreferences(StoredCardModel newStoredCard, String oldStoredCardToken){
        Utils.showToastMessage(MainActivity.this, getString(R.string.card_updated_successfully_card_token, newStoredCard.getCardToken()));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> storedCards = sharedPreferences.getStringSet(Utils.PREFERENCES_STORED_CARDS, null);

        for (String storedCard : storedCards) {
            if( new StoredCardModel(storedCard).getCardToken().equalsIgnoreCase(oldStoredCardToken)){
                storedCards.remove(storedCard);
            }
        }

        Set<String> newStoredCards = new LinkedHashSet<>();
        newStoredCards.addAll(storedCards);
        newStoredCards.add(newStoredCard.toJSON().toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(Utils.PREFERENCES_STORED_CARDS, newStoredCards);
        editor.apply();
    }

    public void showMenuPopup(View view){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Set<String> storedCards = sharedPreferences.getStringSet(Utils.PREFERENCES_STORED_CARDS, null);

        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if( menuItem.getItemId() == R.id.store_card){
                    myPOSStoreNewCard();
                }
                else if( menuItem.getItemId() == R.id.update_card){
                    selectCardToUpdate();
                }
                else if( menuItem.getItemId() == R.id.refund){
                    showAlertForRefAmount();
                }
                else if( menuItem.getItemId() == R.id.get_transaction_status){
                    showAlertForTransactionStatus();
                }
                else if( menuItem.getItemId() == R.id.orders){
                    goToLastOrderIDActivity();
                }
                return false;
            }
        });
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
        popup.show();

        if( storedCards == null )
            popup.getMenu().removeItem(R.id.update_card);
    }

    private void myPOSStoreNewCard(){
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

        Intent intent = new Intent(this, StoreCardActivity.class);
        intent.putExtra(MyPos.INTENT_EXTRA_VERIFICATION_AMOUNT, Utils.VERIFICATION_AMOUNT);
        startActivityForResult(intent, MyPos.REQUEST_CODE_STORE_CARD);
    }

    private void selectCardToUpdate(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Set<String> storedCards = sharedPreferences.getStringSet(Utils.PREFERENCES_STORED_CARDS, null);

        final ArrayList<StoredCardModel> storedCardsList = new ArrayList<>();

        for (String storedCard : storedCards) {
            storedCardsList.add(new StoredCardModel(storedCard));
        }

        Collections.sort(storedCardsList, new Comparator<StoredCardModel>(){
            public int compare(StoredCardModel obj1, StoredCardModel obj2) {
                return obj1.getCardCustomName().compareToIgnoreCase(obj2.getCardCustomName());
            }
        });

        CharSequence paymentOptions[] = new CharSequence[storedCardsList.size()];
        for( int i = 0; i < storedCardsList.size(); i++ ){
            paymentOptions[i] =
                    storedCardsList.get(i).getCardCustomName() +
                            "(*" + storedCardsList.get(i).getCardPanLastDigits() +
                            ", " + storedCardsList.get(i).getCardExpDate().substring(0,2) +
                            "/" + storedCardsList.get(i).getCardExpDate().substring(2,4) +
                            ", " + storedCardsList.get(i).getCardType() + ")";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select card to update");
        builder.setItems(paymentOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                myPOSUpdateStoredCard(storedCardsList.get(position).getCardToken());
            }
        });
        builder.show();
    }

    private void myPOSUpdateStoredCard(String cardToken){
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

        Intent intent = new Intent(this, UpdateCardActivity.class);
        intent.putExtra(MyPos.INTENT_EXTRA_CARD_TOKEN, cardToken);
        intent.putExtra(MyPos.INTENT_EXTRA_VERIFICATION_AMOUNT, MyPos.INTENT_EXTRA_AMOUNT);
        startActivityForResult(intent, MyPos.REQUEST_CODE_UPDATE_CARD);
    }

    private void showAlertForRefAmount(){
        LayoutInflater oLayInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view                   = oLayInflater.inflate(R.layout.dialog_reference_amount, null);
        TextView currencyTxt        = (TextView) view.findViewById(R.id.currency_txt);
        final EditText tranRefEdt   = (EditText) view.findViewById(R.id.transaction_reference_edt);
        final EditText amountEdt    = (EditText) view.findViewById(R.id.amount_edt);
        final EditText orderId      = (EditText) view.findViewById(R.id.order_id_edt);

        Utils.addFocusChangeListener(this, (TextView) view.findViewById(R.id.transaction_reference_title), tranRefEdt, view.findViewById(R.id.transaction_reference_underline));
        Utils.addFocusChangeListener(this, (TextView) view.findViewById(R.id.amount_title), amountEdt, view.findViewById(R.id.amount_underline));
        Utils.addFocusChangeListener(this, (TextView) view.findViewById(R.id.order_id_title), orderId, view.findViewById(R.id.order_id_underline));

        currencyTxt.setText(Utils.CURRENCY);

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Enter data for refund");
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("Refund",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if( tranRefEdt.getText().toString().length() > 0 && amountEdt.getText().toString().length() > 0) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(tranRefEdt.getWindowToken(), 0);
                            dialog.cancel();

                            myPOSRefund(tranRefEdt.getText().toString(), Float.parseFloat(amountEdt.getText().toString()), orderId.getText().toString());
                        }
                        else{
                            Utils.showToastMessage(MainActivity.this, "Transaction reference and amount cannot be empty");
                        }
                    }
                });
        alertBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(tranRefEdt.getWindowToken(), 0);
                        dialog.cancel();
                    }
                });
        alertBuilder.setView(view);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void myPOSRefund(String transactionRef, float amount, String orderId){
        mOrderId = orderId;

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

        Intent intent = new Intent(this, RefundActivity.class);
        intent.putExtra(MyPos.INTENT_EXTRA_TRANSACTION_REFERENCE, transactionRef);
        intent.putExtra(MyPos.INTENT_EXTRA_AMOUNT, amount);
        if( !orderId.equalsIgnoreCase(""))
            intent.putExtra(MyPos.INTENT_EXTRA_ORDER_ID, orderId);
        startActivityForResult(intent, MyPos.REQUEST_CODE_REFUND);
    }

    private void showAlertForTransactionStatus(){
        LayoutInflater oLayInflater     = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view                       = oLayInflater.inflate(R.layout.dialog_order_id, null);
        final EditText orderIdEdt       = (EditText) view.findViewById(R.id.order_id_edt);
        final CheckBox purchaseCheckbox = (CheckBox) view.findViewById(R.id.purchase_checkbox);
        final CheckBox refundCheckbox   = (CheckBox) view.findViewById(R.id.refund_checkbox);

        Utils.addFocusChangeListener(this, (TextView) view.findViewById(R.id.order_id_title), orderIdEdt, view.findViewById(R.id.order_id_underline));

        purchaseCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if( checked) {
                    refundCheckbox.setChecked(false);
                }
            }
        });

        refundCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if( checked ){
                    purchaseCheckbox.setChecked(false);
                }
            }
        });

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Enter data for transaction");
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(orderIdEdt.getWindowToken(), 0);
                        dialog.cancel();

                        if( orderIdEdt.getText().toString().length() > 0)
                            myPOSGetTransactionStatus(orderIdEdt.getText().toString(), refundCheckbox.isChecked() ? IPCGetTransactionStatus.TRANSACTION_TYPE_REFUND : IPCGetTransactionStatus.TRANSACTION_TYPE_PURCHASE);
                    }
                });
        alertBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(orderIdEdt.getWindowToken(), 0);
                        dialog.cancel();
                    }
                });
        alertBuilder.setView(view);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void myPOSGetTransactionStatus(final String orderId, final int txnType){
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

        IPCGetTransactionStatus ipcGetTransactionStatus = new IPCGetTransactionStatus();
        ipcGetTransactionStatus.setOrderId(orderId);
        ipcGetTransactionStatus.setTxtType(txnType);
        ipcGetTransactionStatus.setOnCommandCompleteListener(new IPCGetTransactionStatus.OnCommandCompleteListener() {
            @Override
            public void onCommandComplete(final int transactionStatus, final String transactionReference) {
                hideProgressBar();
                Log.d("Transaction status", "" + transactionStatus );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTransactionStatusAlert( orderId, transactionStatus, transactionReference, txnType);
                    }
                });
            }

            @Override
            public void onError(final int status) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        Toast.makeText(MainActivity.this, "Operation failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        ipcGetTransactionStatus.sendRequest();
    }

    private void hideProgressBar(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }
        });
    }

    private void showTransactionStatusAlert(String orderId, int transactionStatus, String transactionReference, int txnType){
        String statusMsg = "";
        if( transactionStatus == MyPos.TRANSACTION_NOT_PERMITTED)
            statusMsg = "Not permitted";
        else if( transactionStatus == MyPos.TRANSACTION_SUCCESSFUL)
            statusMsg = "Successful";
        else if( transactionStatus == MyPos.TRANSACTION_DECLINED)
            statusMsg = "Declined";
        else
            statusMsg = String.valueOf(transactionStatus);

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Transaction status");
        alertBuilder.setMessage("Transaction type: " + (txnType == IPCGetTransactionStatus.TRANSACTION_TYPE_REFUND ? "Refund" : "Purchase") + "\nOrder ID: " + orderId + "\nTransaction status: " + statusMsg + "\nTransaction Reference: " + transactionReference);
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton("Ok", null);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void goToLastOrderIDActivity(){
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }
}
