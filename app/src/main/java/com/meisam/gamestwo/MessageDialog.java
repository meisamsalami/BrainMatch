package com.meisam.gamestwo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessageDialog extends Dialog {

    private TextView messageTxt;
    private String message;
    public MessageDialog(@NonNull Context context,String message) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.message=message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);
        getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        findViews();
        configureViews(message);
    }

    private void configureViews(String message){
        messageTxt.setText(message);
    }

    private void findViews(){
        messageTxt=(TextView) findViewById(R.id.message_text);
    }
}
