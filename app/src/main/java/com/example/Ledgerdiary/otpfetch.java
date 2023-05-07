package com.example.Ledgerdiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.TextView;

public class otpfetch extends BroadcastReceiver {
    private  static TextView textView;

    public otpfetch() {
    }

    public otpfetch(TextView tv){
        textView=tv;
    }
    public void setEditText(TextView meditText)
    {
        textView =meditText;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage sms : messages) {
            String msg = sms.getMessageBody().replaceAll("[\\D]", "");
            textView.setText(msg);
        }
    }
}