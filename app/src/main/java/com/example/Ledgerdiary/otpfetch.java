package com.example.Ledgerdiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;

public class otpfetch extends BroadcastReceiver {
    private  static EditText editText;
    public void setEditText(EditText meditText)
    {
        editText=meditText;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage sms : messages) {
            String msg = sms.getMessageBody().replaceAll("[\\D]", "");
            editText.setText(msg);
        }
    }
}
