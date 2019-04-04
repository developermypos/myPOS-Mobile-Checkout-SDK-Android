package com.mypos.myposmobilecheckouttestapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mypos.mobilepaymentssdk.Currency;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by kamen.troshev on 19.12.2016 г..
 */

class Utils {

    public static final String MYPOS_WALLET_NUMBER          = "61938166610";
    public static final String MYPOS_SID                    = "000000000000010";
    public static final String CURRENCY                     = Currency.EUR;
    public static final String LANGUAGE                     = "EN";
    public static final int KEY_INDEX                       = 1;
    public static final float VERIFICATION_AMOUNT           = 1.00f;

    /* public static final String CLIENT_PRIVATE_KEY =
             "MIICXAIBAAKBgQDDqLMaPIpg45YjCMfYsPHpWeQPvCagDCHYgiHr7mGlufA/yngs" +
                     "zz01KpbcCAB6Qr2hxMIdzV3qHAjfiQOF+9oxm9c7ol2MK8f8nVQ03rGUfs9fEYBR" +
                     "PjM84j23eaYjbxpc3MKjGBxSJ50gFbHjrRhLG5USnGzMN+4xp24V63x2TQIDAQAB" +
                     "AoGActnmOHJtMC6oBOy0kuhbOIVBsFLbwXsdqv/Idbu6qhOZiXiKTpNf9IOJhqNT" +
                     "1HC06+6Zm/DfPfsy4jSFUvLhwtJAvovOOp30iRkWfg3VzrSv0Sujnb2/UQd7+aFb" +
                     "oMkquUU4RtrdD6rRP3xhJgKaPmnaGV05ZQVnM98IeRTIpIECQQDmgOiJ8tr1lkAy" +
                     "2klaZeVogpy47H9az6EYRpPHNwWBF+EZyCFrKkBu0S6KRpOdTZhJg35pw49zV//7" +
                     "iZ0GKHC9AkEA2U0cAWZ1Bt9zynNgrKLocifT0w46P7hsDqj+lRsVHSPwhcKDyLxI" +
                     "osppNX3ZhPn6mIRO7b3dY8nPunoXkk7c0QJALyeJ6saa0ojVQ1HylaKoxrOJmi8P" +
                     "cqVtIEk37BYucxVOgHa3l3PCUKlMaw87HYAFOmzDDKHsE72Z6XxieiMFxQJAKQ/J" +
                     "GvqhYosW9kqXGggupGOiQ1+M4j2XLa4BbWuQsdD4wk3fWS87Cof1GYaOc/JIyEk4" +
                     "IPSfwRuBhVtM2PjOAQJBAMz+chO2F5NThwte/gOFVM+ZfkM/qTVJJHsnk/Bb+gUm" +
                     "RAymNw5EKoUFi0yyRR6vA57eaWi3oUlZqcHuXGkItjM=";
     */
    public static final String CLIENT_PRIVATE_KEY =
            "MIICXAIBAAKBgQCf0TdcTuphb7X+Zwekt1XKEWZDczSGecfo6vQfqvraf5VPzcnJ" +
                    "2Mc5J72HBm0u98EJHan+nle2WOZMVGItTa/2k1FRWwbt7iQ5dzDh5PEeZASg2UWe" +
                    "hoR8L8MpNBqH6h7ZITwVTfRS4LsBvlEfT7Pzhm5YJKfM+CdzDM+L9WVEGwIDAQAB" +
                    "AoGAYfKxwUtEbq8ulVrD3nnWhF+hk1k6KejdUq0dLYN29w8WjbCMKb9IaokmqWiQ" +
                    "5iZGErYxh7G4BDP8AW/+M9HXM4oqm5SEkaxhbTlgks+E1s9dTpdFQvL76TvodqSy" +
                    "l2E2BghVgLLgkdhRn9buaFzYta95JKfgyKGonNxsQA39PwECQQDKbG0Kp6KEkNgB" +
                    "srCq3Cx2od5OfiPDG8g3RYZKx/O9dMy5CM160DwusVJpuywbpRhcWr3gkz0QgRMd" +
                    "IRVwyxNbAkEAyh3sipmcgN7SD8xBG/MtBYPqWP1vxhSVYPfJzuPU3gS5MRJzQHBz" +
                    "sVCLhTBY7hHSoqiqlqWYasi81JzBEwEuQQJBAKw9qGcZjyMH8JU5TDSGllr3jybx" +
                    "FFMPj8TgJs346AB8ozqLL/ThvWPpxHttJbH8QAdNuyWdg6dIfVAa95h7Y+MCQEZg" +
                    "jRDl1Bz7eWGO2c0Fq9OTz3IVLWpnmGwfW+HyaxizxFhV+FOj1GUVir9hylV7V0DU" +
                    "QjIajyv/oeDWhFQ9wQECQCydhJ6NaNQOCZh+6QTrH3TC5MeBA1Yeipoe7+BhsLNr" +
                    "cFG8s9sTxRnltcZl1dXaBSemvpNvBizn0Kzi8G3ZAgc=";

    public static final String SERVER_PUBLIC_KEY =
            "MIIBsTCCARoCCQCCPjNttGNQWDANBgkqhkiG9w0BAQsFADAdMQswCQYDVQQGEwJC" +
                    "RzEOMAwGA1UECgwFbXlQT1MwHhcNMTgxMDEyMDcwOTEzWhcNMjgxMDA5MDcwOTEz" +
                    "WjAdMQswCQYDVQQGEwJCRzEOMAwGA1UECgwFbXlQT1MwgZ8wDQYJKoZIhvcNAQEB" +
                    "BQADgY0AMIGJAoGBAML+VTmiY4yChoOTMZTXAIG/mk+xf/9mjwHxWzxtBJbNncNK" +
                    "0OLI0VXYKW2GgVklGHHQjvew1hTFkEGjnCJ7f5CDnbgxevtyASDGst92a6xcAedE" +
                    "adP0nFXhUz+cYYIgIcgfDcX3ZWeNEF5kscqy52kpD2O7nFNCV+85vS4duJBNAgMB" +
                    "AAEwDQYJKoZIhvcNAQELBQADgYEACj0xb+tNYERJkL+p+zDcBsBK4RvknPlpk+YP" +
                    "ephunG2dBGOmg/WKgoD1PLWD2bEfGgJxYBIg9r1wLYpDC1txhxV+2OBQS86KULh0" +
                    "NEcr0qEY05mI4FlE+D/BpT/+WFyKkZug92rK0Flz71Xy/9mBXbQfm+YK6l9roRYd" +
                    "J4sHeQc=";

    /*public static final String SERVER_PUBLIC_KEY =
            "MIIBkDCB+qADAgECAgAwDQYJKoZIhvcNAQEFBQAwDzENMAsGA1UEChMEaVBheTAe" +
                    "Fw0xNjA5MjgxMTI2MjRaFw0yNjA5MjYxMTI2MjRaMA8xDTALBgNVBAoTBGlQYXkw" +
                    "gZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMGKgmD9E2EO4O6H8vyCuS/O9qxs" +
                    "YRy4QBbmK4z1HJ2pLr/d217Ifxrs5NlhHY8OpEKllXbFfRNjDTygrDwPRZ9HrweX" +
                    "5NbLOIYdRIPPr2kjbCxxPBJX+Fx7gE+o6e3WqegIHNTMOchZt+RghCFKV26mdnSf" +
                    "htXTChDKmgrPadGZAgMBAAEwDQYJKoZIhvcNAQEFBQADgYEAqsGqlGMb3fHi6Crm" +
                    "O2ufLlAoqAgOvimj+EzdrDbVt6yg4OZGjhu2AZL7K5w37oxrflGEefEELp1dli4E" +
                    "sZa0C8FEEZp8Fg4GnF5uizNEnf30i4jMnyRdWcu7M5xReLAl3JDLQ4XrW7wPMyEZ" +
                    "3JV3s+S37mucl07z+7FAU3sQDbg=";*/

    public static final boolean isSandbox = true;

    public static final String PREFERENCES_STORED_CARDS = "stored_cards";

    public static String formatAmount(float amount){
        try {
            String pattern = "0.00";
            NumberFormat numberFormat = new DecimalFormat(pattern);
            return numberFormat.format(amount).replace(',', '.');
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String formatAmount(String amount){
        try {
            String pattern = "0.00";
            NumberFormat numberFormat = new DecimalFormat(pattern);

            if (!amount.equalsIgnoreCase("")) {
                double dAmntFrom = Double.parseDouble(amount);
                return numberFormat.format(dAmntFrom).replace(',', '.');
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void showToastMessage(final Context context, final String message){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                if (textView != null) textView.setGravity(Gravity.CENTER);
                toast.show();
            }
        });
    }

    public static void addFocusChangeListener(final Context context, final TextView title, EditText editText, final View underline){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if( focused){
                    title.setTextColor(ContextCompat.getColor(context, com.mypos.mobilepaymentssdk.R.color.myposFocusedColor));
                    underline.setBackgroundColor(ContextCompat.getColor(context, com.mypos.mobilepaymentssdk.R.color.myposFocusedColor));
                }
                else{
                    title.setTextColor(ContextCompat.getColor(context, com.mypos.mobilepaymentssdk.R.color.myposEdtTitleTextColor));
                    underline.setBackgroundColor(ContextCompat.getColor(context, com.mypos.mobilepaymentssdk.R.color.myposUnderlineColor));
                }
            }
        });
    }

    public static void addOrderToPreferences(Context context, OrderModel order){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> orders = sharedPreferences.getStringSet("orders", null);

        if (orders == null)
            orders = new LinkedHashSet<>();

        Set<String> newOrders = new LinkedHashSet<>();
        newOrders.addAll(orders);
        newOrders.add(order.toJson());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("orders", newOrders);
        editor.apply();
    }
}
