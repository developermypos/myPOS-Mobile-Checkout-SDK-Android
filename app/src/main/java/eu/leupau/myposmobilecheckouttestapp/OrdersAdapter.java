package eu.leupau.myposmobilecheckouttestapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by kamen.troshev on 23.1.2017 г..
 */

public class OrdersAdapter extends ArrayAdapter<OrderModel> {

    private ArrayList<OrderModel> mItems = new ArrayList<>();

    public OrdersAdapter(Context context, ArrayList<OrderModel> items) {
        super(context, R.layout.order_item_layout, items);
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater oLayInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = oLayInflater.inflate(R.layout.order_item_layout, null);
        }

        TextView tranType       = (TextView)  view.findViewById(R.id.transaction_type);
        TextView orderId        = (TextView)  view.findViewById(R.id.order_id);
        TextView tranRef        = (TextView)  view.findViewById(R.id.transaction_reference);
        TextView amount         = (TextView)  view.findViewById(R.id.order_amount);
        TextView date           = (TextView)  view.findViewById(R.id.order_date);

        orderId.setText(mItems.get(position).getmOrderId());
        tranType.setText(mItems.get(position).getmTransactionType());
        tranRef.setText(mItems.get(position).getmTransactionRef());
        amount.setText(mItems.get(position).getmAmount() + " " + mItems.get(position).getmCurrency());
        date.setText(dateToStringFormat(mItems.get(position).getmDate()));

        return view;
    }

    public static String dateToStringFormat(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy\nHH:mm");
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(date);
    }
}
