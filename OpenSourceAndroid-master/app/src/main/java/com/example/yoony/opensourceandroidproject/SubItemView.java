package com.example.yoony.opensourceandroidproject;

import android.widget.EditText;
import android.widget.TextView;

public class SubItemView {
    public EditText etInput;
    public TextView tvInput;


    public SubItemView(EditText etInput) {
        this.etInput = etInput;
    }

    public SubItemView(TextView tvInput) {
        this.tvInput = tvInput;
    }

    public EditText getEtInput() {
        return etInput;
    }

    public TextView getTvInput() {
        return tvInput;
    }
}
