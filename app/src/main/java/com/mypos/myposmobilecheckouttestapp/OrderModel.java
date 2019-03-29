package com.mypos.myposmobilecheckouttestapp;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by kamen.troshev on 23.1.2017 г..
 */

public class OrderModel {
    private String mOrderId;
    private String mTransactionRef;
    private Date mDate;
    private String mAmount;
    private String mCurrency;
    private String mTransactionType;

    public OrderModel(){
        mOrderId            = "";
        mTransactionRef     = "";
        mDate               = new Date();
        mAmount             = "";
        mCurrency           = "";
        mTransactionType    = "";
    }

    public OrderModel(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            mDate = new Date();
            mDate.setTime(jsonObject.getLong("date"));
            mOrderId            = jsonObject.getString("order_id");
            mTransactionRef     = jsonObject.getString("tran_ref");
            mAmount             = jsonObject.getString("amount");
            mCurrency           = jsonObject.getString("currency");
            mTransactionType    = jsonObject.getString("tran_type");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String toJson(){
        String json = "";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("order_id", mOrderId);
            jsonObject.put("tran_ref", mTransactionRef);
            jsonObject.put("date", mDate.getTime());
            jsonObject.put("amount", mAmount);
            jsonObject.put("currency", mCurrency);
            jsonObject.put("tran_type", mTransactionType);

            json = jsonObject.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        return json;
    }

    public String getmOrderId() {
        return mOrderId;
    }

    public void setmOrderId(String mOrderId) {
        this.mOrderId = mOrderId;
    }

    public String getmTransactionRef() {
        return mTransactionRef;
    }

    public void setmTransactionRef(String mTransactionRef) {
        this.mTransactionRef = mTransactionRef;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public String getmCurrency() {
        return mCurrency;
    }

    public void setmCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }

    public String getmTransactionType() {
        return mTransactionType;
    }

    public void setmTransactionType(String mTransactionType) {
        this.mTransactionType = mTransactionType;
    }
}
