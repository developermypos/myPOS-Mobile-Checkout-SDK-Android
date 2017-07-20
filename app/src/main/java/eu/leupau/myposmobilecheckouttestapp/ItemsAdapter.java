package eu.leupau.myposmobilecheckouttestapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by kamen.troshev on 19.12.2016 г..
 */

public class ItemsAdapter extends ArrayAdapter<CartItemModel> {

    private ArrayList<CartItemModel> mItems = new ArrayList<>();
    private onItemAddedToCartListener mListener;

    public ItemsAdapter(Context context, ArrayList<CartItemModel> items) {
        super(context, R.layout.item_layout, items);
        mItems = items;
    }

    public interface onItemAddedToCartListener {
        void onItemAddedToCart(int position);
    }

    public void setOnItemAddedToCardListener(onItemAddedToCartListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater oLayInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = oLayInflater.inflate(R.layout.item_layout, null);
        }

        ImageView image         = (ImageView) view.findViewById(R.id.item_image);
        TextView title          = (TextView)  view.findViewById(R.id.item_title);
        TextView description    = (TextView)  view.findViewById(R.id.item_description);
        TextView price          = (TextView)  view.findViewById(R.id.item_price);
        TextView addBtn         = (TextView)  view.findViewById(R.id.add_button);

        title.setText(mItems.get(position).getmTitle());
        description.setText(mItems.get(position).getmDescription());
        price.setText(mItems.get(position).getmPrice() + " " + mItems.get(position).getmCurrency());
        image.setImageResource(mItems.get(position).getmImageResource());

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mListener != null )
                    mListener.onItemAddedToCart(position);
            }
        });

        return view;
    }
}
