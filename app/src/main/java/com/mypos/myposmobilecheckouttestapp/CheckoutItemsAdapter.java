package com.mypos.myposmobilecheckouttestapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.mypos.mobilepaymentssdk.CartItem;

/**
 * Created by kamen.troshev on 19.12.2016 г..
 */

public class CheckoutItemsAdapter extends ArrayAdapter<CartItem> {

    private ArrayList<CartItem> mItems = new ArrayList<>();

    public CheckoutItemsAdapter(Context context, ArrayList<CartItem> items) {
        super(context, R.layout.item_layout, items);
        mItems = items;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater oLayInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = oLayInflater.inflate(R.layout.checkout_item_layout, null);
        }

        ImageView image         = (ImageView) view.findViewById(R.id.item_image);
        TextView title          = (TextView)  view.findViewById(R.id.item_title);
        TextView quantity       = (TextView)  view.findViewById(R.id.item_quantity);

        if( mItems.get(position).getArticle().equalsIgnoreCase("Shoe 1")){
            image.setImageResource(R.drawable.shoe1);
        }
        else if( mItems.get(position).getArticle().equalsIgnoreCase("Shoe 2")){
            image.setImageResource(R.drawable.shoe2);
        }
        else if( mItems.get(position).getArticle().equalsIgnoreCase("Shoe 3")){
            image.setImageResource(R.drawable.shoe3);
        }
        else if( mItems.get(position).getArticle().equalsIgnoreCase("Shoe 4")){
            image.setImageResource(R.drawable.shoe4);
        }

        title.setText(mItems.get(position).getArticle());
        quantity.setText(mItems.get(position).getQuantity() + " x " + Utils.formatAmount(mItems.get(position).getPrice()) + " " + "EUR");

        return view;
    }
}