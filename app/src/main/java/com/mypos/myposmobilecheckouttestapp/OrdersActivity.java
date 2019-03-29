package com.mypos.myposmobilecheckouttestapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by kamen.troshev on 23.1.2017 г..
 */

public class OrdersActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView mOrdersListView;

    private ArrayList<OrderModel> mOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        init();

        setData();
    }

    private void init(){
        mOrdersListView = (ListView) findViewById(R.id.orders_list_view);
        findViewById(R.id.back_button).setOnClickListener(this);
    }

    private void setData(){
        mOrdersListView.setEmptyView(findViewById(R.id.empty_text));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> orders = sharedPreferences.getStringSet("orders", null);

        if( orders == null )
            return;

        ArrayList<String> ordersArrayList = new ArrayList<>();
        ordersArrayList.addAll(orders);
        for( int i = 0; i < ordersArrayList.size(); i++ ){
            mOrders.add(new OrderModel(ordersArrayList.get(i)));
        }

        Comparator<OrderModel> comparator = new Comparator<OrderModel>() {
            @Override
            public int compare(OrderModel orderModel1, OrderModel orderModel2) {
                return orderModel2.getmDate().compareTo(orderModel1.getmDate());
            }
        };

        Collections.sort(mOrders, comparator);

        OrdersAdapter adapter = new OrdersAdapter(this, mOrders);
        mOrdersListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if( view.getId() == R.id.back_button){
            super.onBackPressed();
        }
    }
}
