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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mypos.mobilepaymentssdk.Currency;

/**
 * Created by kamen.troshev on 19.12.2016 г..
 */

class Utils {

    public static final String MYPOS_WALLET_NUMBER          = "40064699727";
    public static final String MYPOS_SID                    = "71888";
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
            "MIICXQIBAAKBgQDqUfIswZ+7WFkhFMqxvBz6dY2gV0WdOCYMEajYGMbLtfjtaY/l" +
                    "u2DIyBTyxhfdV1m+20Rm8tZx8sqSrglSwVXrwM0gxlPwLD5QXLrknmbB4JH1pYGV" +
                    "HR5RmTKCTNqIDvnR5c8cG8byIwOhhyTl3XxlKJEvjkZX3R6p/fESU7n1xQIDAQAB" +
                    "AoGAMNuPVHe295hPVU+BiBfCLxMNyc4IrOSFQmT4QL7BzL4PvmkRYQR2BMd7Xreh" +
                    "SDyrcNlFZPXjcvT9+iGhvgikAlMYFBJwE3I9LcJY/rlG5HJQ3CR4fb2btN0kH8fZ" +
                    "EgsCkS96PCxHgH4gQLcqNvJlJBnpa9K7WId4HkwjjkNjDCECQQD3CH+5S4AX7yzB" +
                    "FSJKGvmH7nZ30GdFmKRSW8DJXlZbepxd/mTwR+2ThaTDA6RKpeRLmZas0m+eOFq+" +
                    "9tYRV/vNAkEA8tNQPdUNefeAYzmubb51Y2Lf6iTp6RriXyfk8KtssWfwFTFe54kL" +
                    "dismDEtn0SVAdGcpv229JGqYOpPnHUiZ2QJASQgTMoWWnfzQn1iPJZdp8/zUu0E6" +
                    "dsHUuqBzk1S3dR3/gm5GCx9IpldZSWoXNRZsMsl6WSdxZjZE5bX1vXTkdQJBAKnB" +
                    "gL85SoqRtHepw3+FPpuUa+VMmRoVaVwMj8iQjxQXDYhDU42aCSE7WQYPUSytVjoK" +
                    "RO1G0ou62WFaNKAMOUECQQDdsLF7sWigPSlU7ymFsGrlYv3882mqqMhoCamNeg23" +
                    "Se2FWdX6MMIG335+A7zK+8Vi5aicv1GjQtTSdh5xtUCL";

    public static final String SERVER_PUBLIC_KEY =
            "MIIBqzCCARSgAwIBAgIAMA0GCSqGSIb3DQEBBQUAMBwxCzAJBgNVBAYTAkJHMQ0w" +
                    "CwYDVQQKEwRpUGF5MB4XDTE3MDkyNjA4MTYzNloXDTI3MDkyNDA4MTYzNlowHDEL" +
                    "MAkGA1UEBhMCQkcxDTALBgNVBAoTBGlQYXkwgZ8wDQYJKoZIhvcNAQEBBQADgY0A" +
                    "MIGJAoGBALZ3wiU7Y8gdyAQm3KObXsOVCVB5x80vC+EujdCYJ9X97o4TDK3lvfP6" +
                    "AHnXIQAqoNtwYV+8RMhYtGGb8J5ba3xUteLQy7abzQtwdaRrd2oyX36IbYR/0Vuk" +
                    "dAt6uai1C3JIDbv8yVCLCIm0m52cf+338H1zZ6V4ntoWUZ4iYDoFAgMBAAEwDQYJ" +
                    "KoZIhvcNAQEFBQADgYEAIsizY7xKZTaaAfCsS44W3a62QGGDhQ/GIH++CgCNH0L3" +
                    "tJJ1+CUlAXJ57NfX+t8d6tIuuNt2r3kcUeDtgCAq8k+FsvQETJ9L51eGBRaokzhZ" +
                    "kXRPx+1tdXs+bUGhDTPKImh/EH5+KZ5FSL39oViaVoLADZcLSpM6yk7MFjIc8uY=";

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
