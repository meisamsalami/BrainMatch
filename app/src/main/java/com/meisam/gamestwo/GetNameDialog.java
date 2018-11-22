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
import android.widget.Toast;

public class GetNameDialog extends Dialog {

    EditText txtName;
    Button btnConfirm;
    OnNameSelectedListener listener;

    public GetNameDialog(@NonNull Context context, OnNameSelectedListener listener) {
        super(context);
        this.listener=listener;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_get_name);
        getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        findViews();
        configureViews();
    }

    private void configureViews(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtName.getText().toString().trim().length()>0){
                    listener.onNameSelected(txtName.getText().toString());
                    dismiss();
                }else{
                    Toast.makeText(getContext().getApplicationContext(),getContext().getApplicationContext().getString(R.string.enter_name_for_confirm),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findViews(){
        btnConfirm=(Button)findViewById(R.id.btn_confirm);
        txtName=(EditText)findViewById(R.id.txt_user_name);
    }
}
