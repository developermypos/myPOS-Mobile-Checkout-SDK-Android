package eu.leupau.myposmobilecheckouttestapp;

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

import eu.leupau.mobilepaymentssdk.Currency;

/**
 * Created by kamen.troshev on 19.12.2016 г..
 */

class Utils {

    public static final String MYPOS_WALLET_NUMBER          = "40064699727";
    public static final String MYPOS_SID                    = "51528";
    public static final String CURRENCY                     = Currency.EUR;
    public static final String LANGUAGE                     = "EN";
    public static final int KEY_INDEX                       = 4;
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
            "MIICXgIBAAKBgQC7ddeVi9xU/Me1yk4rsSMhp02NERzK+X1xCb6KVV7Tx4aalSJx" +
                    "VPx3whrG94VVGX4SJjOmSseU/7WNhM41Hn6qKqWYsS8ls1W1W9mb8oekH0FA1TMU" +
                    "c/dJpLeqwT15WqbO0XqJ0ghue31y7plWNwdI8GdnQ3BtbBV8N4vVfW83hQIDAQAB" +
                    "AoGBAKyWHMo4/rxcEb3jqL5/EjocnsB+fcS9l+jnML6JRIm0C8/r987hkKLtWNvP" +
                    "7LhhhPlOpIS4GjGqjsrg9zxee/u5PvsXLxm6++ZC7BF9VVDn6lp7Gt5Gvi8IkTxk" +
                    "2qc2QLzjXIq6uE9ogkSywjqDJjfyS3UMhLfrSBpKEn4YLXWtAkEA4g3vdab3/Sbz" +
                    "wivogdbXmbiYIxB0URoii4/VheokIMNyHSkQGLBHJS3cknOf0lDYlJUGE9gB+nll" +
                    "B2emNli7RwJBANRLFb4MnE4YPzkTg7glIdkq4dLc7lvX3ORhzqV/gCSPQpFtdxAI" +
                    "p7R19oG59umhDfpVGqyg5jbo9blcHgYCRNMCQQCxFrOqGpSZyEMbsSQjWzhpn3fn" +
                    "uEM3Kd84MATZNNT6qcIi4AFRJf+GO8hZHqVssJMKyzgIoabjHuirhWJrntWPAkAd" +
                    "jH3wQA0ZiaJzLVYuMWzur1tTYFEFE5y5ZSvAOWj6rb4UQ+pInwvIwIqZL+tARZyO" +
                    "dX4K/TJN/ksFi97Z80LlAkEApkOsX6SwH/icYtSP1vR1UaGG0vlsp7yOkItrP6U/" +
                    "Q2kLCPxPfYlEkSVoRKAMI8AE0vhCrxXQyHVBNfg4zDlCKQ==";

    public static final String SERVER_PUBLIC_KEY =
            "MIIBqzCCARSgAwIBAgIAMA0GCSqGSIb3DQEBBQUAMBwxCzAJBgNVBAYTAkJHMQ0w" +
                    "CwYDVQQKEwRpUGF5MB4XDTE3MDMzMDExMjMxNloXDTI3MDMyODExMjMxNlowHDEL" +
                    "MAkGA1UEBhMCQkcxDTALBgNVBAoTBGlQYXkwgZ8wDQYJKoZIhvcNAQEBBQADgY0A" +
                    "MIGJAoGBALCLs0Qesp6k/X6udH0r0eqCMNOFm7At90/N9EdgJRqXxsJlue6kEE8E" +
                    "psX6E2R1YccJE1ccGZaWGjLvZesJobHeQpRXY1igYVgMXEjrrtZg/c00e6DwkvkG" +
                    "hgGmGJi6UnxPPWmSKb73apIyVovyfMlyvrAptFuq1ZMe6OF6n6vVAgMBAAEwDQYJ" +
                    "KoZIhvcNAQEFBQADgYEAoaFyXwDQyol1rMmP1YBCc57UWHbaS1jrpEm0vlfZMGMt" +
                    "fpRlPeYvAfdvPfnctxyWpCMqOBYyotFhGjrDigWOBT5W0tLoOVJ2YkVQvg5xAIy3" +
                    "GdmsXKU6PpGcbHNFwJMY1dTNGOBpZfe/AMjH2C7t6Q/E4N1XebmMNXLLCzJsnMM=";

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
                    title.setTextColor(ContextCompat.getColor(context, eu.leupau.mobilepaymentssdk.R.color.myposFocusedColor));
                    underline.setBackgroundColor(ContextCompat.getColor(context, eu.leupau.mobilepaymentssdk.R.color.myposFocusedColor));
                }
                else{
                    title.setTextColor(ContextCompat.getColor(context, eu.leupau.mobilepaymentssdk.R.color.myposEdtTitleTextColor));
                    underline.setBackgroundColor(ContextCompat.getColor(context, eu.leupau.mobilepaymentssdk.R.color.myposUnderlineColor));
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
