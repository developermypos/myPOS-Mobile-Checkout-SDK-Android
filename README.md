# myPOS™ Mobile Checkout Android SDK
Accepting mobile payments for merchants

### Table of Contents

* [Security and availability](#security-and-availability)

* [Introduction](#introduction)

* [Integration](#integration)
  
  * [Requirements](#requirements)
  
  * [Setup](#setup)
  
  * [Perform a Payment](#perform-a-payment)
  
  * [Add a Card](#add-a-card)
  
  * [Perform a Payment with stored card](#perform-a-payment-with-stored-card)
  
  * [Perform a Refund](#perform-a-refund)
  
  * [Check transaction status](#check-transaction-status)
  
* [UI customization](#ui-customization)

  * [Hide custom name field](#hide-custom-name-field)
  
  * [Set custom banner](#set-custom-banner)

  * [Configuring displayed colors](#configuring-displayed-colors)
  
  * [Configuring displayed text](#configuring-displayed-text)
  
  
  # Security and availability
  
  Connection between Merchant and iCARD is handled through internet using HTTPS protocol (SSL over HTTP). Requests and responses are digitally signed both. iCARD host is located at tier IV datacenter in Luxembourg. The system is designed specifically for the unique challenges of mobile fraud and comes as standard in our SDK. It is powered by the latest machine learning algorithms, as well as trusted methodologies. The SDK comes with built-in checks and security features, including real-time error detection, 3D Secure, data and address validation for frictionless card data capture.
  
  Exchange folder for partners (if needed) is located at a SFTP server which enables encrypted file sharing between parties. The partner receives the account and password for the SFTP directory via fax, email or SMS.
  
  # Introduction
  
  This document describes the methods and interface for mobile checkout. The Merchants should integrate the myPOS™ Mobile Checkout Android SDK to their mobile apps to accept card payments. The myPOS™ Mobile Checkout Android SDK will gain access to the entry point of the payment gateway managed by Intercard Finance AD (iCARD). The cardholder will be guided during the payment process and iCARD will check the card sensitive data and will process a payment transaction through card schemes (VISA and MasterCard).
  
  The myPOS™ Mobile Checkout Android SDK will provide:
  * Secured communication channel with the Merchant
  * Storing of merchant private data (shopping cart, amount, payment methods, transaction details etc.)
  * Financial transactions to VISA, MasterCard – transparent for the Merchant
  * Operations for the front-end: Purchase transaction
  * Operations for the back-end: Refund, Void, Payment
  * 3D processing
  * Card Storage

  Out of scope for this document: 
  * Merchant statements and payouts
  * Merchant back-end
  
  The purpose of this document is to specify the myPOS™ Mobile Checkout Android SDK Interface and demonstrate how it is used in the most common way.
  All techniques used within the interface are standard throughout the industry and should be very easy to implement on any platform.
  
  
  # Integration
  
  A “by appointment” test service is available which allows the validation of the myPOS™ Mobile Checkout Android SDK calls. Testers   should negotiate an exclusive access to the testing service and ensure monitoring by iCARD engineer.
  
  ## Requirements
  
  * Android Studio 2.0 or higher
  * Android SDK Build Tools 25.0.0
  * The minSdkVersion set to 16 (Jelly Bean) or higher
  * Latest Android Support Repository
  * Latest Android Support Library

  ## Setup
  
  Start using myPOS™ Mobile Checkout Android SDK by initializing the setup. Simply find your account’s identification token (Wallet ID) and wallet secret on the Dashboard and add them to your app's main Activity class.
  
```Java
protected void onCreate(Bundle savedInstanceState) {
...
 MyPos myPos = MyPos.getInstance();
 myPos.init(
            “51528”,              /*sid*/
            “40064699727”,        /*wallet number*/
            “EUR”,                /*currency ISO code*/
            “MIICXAIBAAKBg ...”,  /*client private key*/
            ”MIIBkDCB+q ...”,     /*server public key*/
            true,                 /*is Sandbox*/
 );
 myPos.setKeyIndex(1); 
 myPos.setLanguage(“EN”);
...
}
```

The SDK allows further configuration by using the existing settings. These are the options:
  * Supported card networks – Allows you to determine the accepted card networks when using your app. The default value includes Visa, Visa Electron, MasterCard, Maestro and VPay.
  * Address Verification Service (AVS) – You will be able to capture the consumer’s country and postcode as an additional security layer.
  
  ## Perform a Payment
    
    Create an intent for the Purchase Activity with the required:
 
```Java
ArrayList<CartItem> mIPCCartItems;
public void onPayBtnClick(View view) {
...
  Intent intent = new Intent(this, PurchaseActivity.class);
  intent.putExtra(MyPos.INTENT_EXTRA_CART_ITEMS , mIPCCartItems);
  intent.putExtra(MyPos.INTENT_EXTRA_ORDER_ID   , “12345678”);
  startActivityForResult(intent, MyPos.REQUEST_CODE_PURCHASE);
...
}
```
Note: Please make sure that you are using a unique Order ID.

  In your calling Activity, override the onActivityResult method to receive a reference of the payment card, customer ID and transaction reference from Performing a Payment:
  
```Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_PURCHASE ) {
      int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS, MyPos.STATUS_INTERNAL_API_ERROR);
        
      if( status == MyPos.STATUS_SUCCESS) {
        String tranRef = data.getStringExtra(MyPos.INTENT_EXTRA_TRANSACTION_REFERENCE);
      }
}

```
  
## Add a Card

 Create an Intent for the StoreCardActivity with the required Intent extras:

```Java
public void onAddCardBtnClick(View view) {
...
  Intent intent = new Intent(this, StoreCardActivity.class);
  intent.putExtra(MyPos.INTENT_EXTRA_VERIFICATION_AMOUNT, “verification amount”);
  startActivityForResult(intent, MyPos.REQUEST_CODE_STORE_CARD);
...
}
```
  
 Note: Please make sure that you are using a unique Reference ID for each different consumer.
 
 In your calling Activity, override the onActivityResult method to receive a card reference for the linked card:
 
 ```Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_STORE_CARD){
    int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS,  MyPos.STATUS_INTERNAL_API_ERROR);
    
    if( status == MyPos.STATUS_SUCCESS) {
        StoredCardModel storedCard = data.getParcelableExtra(MyPos.INTENT_EXTRA_STORED_CARD);
    }
 ```
 
 ## Perform a Payment with stored card
 
 Create an Intent for the PurchaseActivity with the required Intent extras:
 
```Java
public void onPayWithCardBtnClick(View view) {
...
  Intent intent = new Intent(this, PurchaseActivity.class);
  intent.putExtra(MyPos.INTENT_EXTRA_CART_ITEMS , mIPCCartItems);
  intent.putExtra(MyPos.INTENT_EXTRA_ORDER_ID   , “12345678”);
  intent.putExtra(MyPos.INTENT_EXTRA_CARD_TOKEN , “card token”);
  startActivityForResult(intent, MyPos.REQUEST_CODE_PURCHASE);
...
}
```
Note: Please make sure that you are using a unique Order ID.

In your calling Activity, override the onActivityResult method to receive a reference of the payment card, customer ID and transaction reference from Performing a Payment:

```Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
...
  if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_PURCHASE) {
    int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS,  MyPos.STATUS_INTERNAL_API_ERROR);
        
      if( status == MyPos.STATUS_SUCCESS) {
        String tranRef = data.getStringExtra(MyPos.INTENT_EXTRA_TRANSACTION_REFERENCE);
      }
    }
  ...
}
```

 ## Perform a Refund

Refunding a payment requires that you have the transactionRef of the payment transaction. Check that you have initialized the SDK before attempting to perform a refund.

Create an Intent for the RefundActivity with the required Intent extras:

```Java
public void onRefundBtnClick(View view) {
...
Intent intent = new Intent(this, RefundActivity.class);
intent.putExtra(MyPos.INTENT_EXTRA_TRANSACTION_REFERENCE  , "transactionRef");
intent.putExtra(MyPos.INTENT_EXTRA_AMOUNT                 , 10.00);

if( !orderId.equalsIgnoreCase(""))
  intent.putExtra(MyPos.INTENT_EXTRA_ORDER_ID, orderId);
    
startActivityForResult(intent, MyPos.REQUEST_CODE_REFUND);
...
}
```
Note: Please make sure that you are using the correct Transaction Reference ID for the transaction that you want to be refunded.

In your calling Activity, override the onActivityResult method to receive a transaction reference of the operation and the reference of the original transaction:

```Java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
...
  if( resultCode == RESULT_OK  && requestCode == MyPos.REQUEST_CODE_REFUND){
    int status = data.getIntExtra(MyPos.INTENT_EXTRA_STATUS,  MyPos.STATUS_INTERNAL_API_ERROR);
    
    if( status == MyPos.STATUS_SUCCESS) {
      float amount                = data.getFloatExtra(MyPos.INTENT_EXTRA_AMOUNT, 0.00f);
      String currency             = data.getStringExtra(MyPos.INTENT_EXTRA_CURRENCY);
      String transactionReference = data.getStringExtra(MyPos.INTENT_EXTRA_TRANSACTION_REFERENCE);
    }
  }
...
}
```

 ## Check transaction status
 
 You can choose between the transaction types of Purchase or Refund and to send the order ID of which transaction status needed to be checked. The method will retrieve the transaction type, order ID, transaction status and the transaction reference:
 
```Java
IPCGetTransactionStatus ipcGetTransactionStatus = new IPCGetTransactionStatus();
ipcGetTransactionStatus.setOrderId(orderId);
ipcGetTransactionStatus.setTxtType(txnType);
ipcGetTransactionStatus.setOnCommandCompleteListener(new IPCGetTransactionStatus.OnCommandCompleteListener() {
    @Override
    public void onCommandComplete(final int transactionStatus, final String transactionReference) { }

    @Override
    public void onError(final int status) { }
});

ipcGetTransactionStatus.sendRequest();
```

# UI customization

Use myPOS™ Mobile Checkout Android SDK UI components for a frictionless checkout in your app. Minimize your PCI scope with a UI that can be themed to match your brand colors.

Built-in features include quick data entry, optional security checks and fraud prevention that let you focus on developing other areas of your app.

The myPOS™ Mobile Checkout Android SDK supports a range of UI customization options to allow you to match payment screen appearance to your app's branding.

## Hide custom name field

Pass true in the intent opening StoreCardActivity or PurchaseActivity if you don't want to show 'Custom name' field like this:
```Java
intent.putExtra(MyPos.INTENT_EXTRA_WITHOUT_CUSTOM_NAME, true);
```

## Set custom banner

There is a possibility to set your merchant brand logo or banner in the action bar on StoreCardActivity, UpdateCardActivity and PurchaseActivty. The image should be added in your project as a resource and the resource ID should be passed like this:
```Java
intent.putExtra(MyPos.INTENT_EXTRA_CUSTOM_LOGO_RESOURCE, R.drawable.merchang_banner);
```

## Configuring displayed colors

Create a new theme that has a theme as a parent, in this example we'll customize the button color:

```Java
<style name="AppTheme" parent="Theme.MyPOS">
    <item name="colorButtonNormal">#F68121</item>
    <item name="colorAccent">@android:color/white</item>
</style>
```
Depending on the styles used in your app, you could customize colors of the following elements:
  * Field on focus
  * Hint text in the fields
  * Entered text colors
  * Field under line color
  * Buttons colors
  
```Java
<color name="myposFocusedColor"       tools:override="true">#ffa500</color>
<color name="myposEditTextHintColor"  tools:override="true">#9c9c9c</color>
<color name="myposEditTextColor"      tools:override="true">#444444</color>
<color name="myposEdtTitleTextColor"  tools:override="true">#bbbbbb</color>
<color name="myposUnderlineColor"     tools:override="true">#bbbbbb</color>
<color name="myposButtonColor"        tools:override="true">#008000</color>
<color name="myposButtonTextColor"    tools:override="true">#ffffff</color>
<color name="myposTitleTextColor"     tools:override="true">#ffffff</color>
<color name="myposBackgroundColor"    tools:override="true">#ffffff</color>
```

## Configuring displayed text
  
  To provide the best user experience, clear activity titles and button labels are already provided. When changing text labels, consider using text that gives a clear intention to user actions.
  
  The activity title, button label text and option to show the secure server text can all be configured in the theme:
  
```Java
<style name="AppTheme" parent="Theme.MyPos">
   <item name="activityTitle">@string/enter_card_details</item>
   <item name="buttonLabel">@string/pay_now</item>
</style>
```


