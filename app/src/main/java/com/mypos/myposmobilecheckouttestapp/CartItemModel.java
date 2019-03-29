package com.mypos.myposmobilecheckouttestapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kamen.troshev on 19.12.2016 г..
 */

public class CartItemModel implements Parcelable {
    private int     mId;
    private String mTitle;
    private String mDescription;
    private String mPrice;
    private String mCurrency;
    private int     mImageResource;

    public CartItemModel(int id, String title, String desc, String price, String currency, int imageResource){
        mId             = id;
        mTitle          = title;
        mDescription    = desc;
        mPrice          = price;
        mCurrency       = currency;
        mImageResource  = imageResource;
    }

    public CartItemModel(Parcel in){
        mId             = in.readInt();
        mTitle          = in.readString();
        mDescription    = in.readString();
        mPrice          = in.readString();
        mCurrency       = in.readString();
        mImageResource  = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mPrice);
        dest.writeString(mCurrency);
        dest.writeInt(mImageResource);
    }

    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        public CartItemModel createFromParcel(Parcel in) {
            return new CartItemModel(in);
        }

        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmCurrency() {
        return mCurrency;
    }

    public void setmCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
