package com.example.yoony.opensourceandroidproject;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingerViewer extends LinearLayout {

    TextView textView;
    TextView textView2;
    ImageView imageView;

    public SingerViewer(Context context) {
        super(context);
        init(context);
    }

    public SingerViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.singer_shop_item, this, true);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void setItem(SingerShopItem singerItem) {
        textView.setText(singerItem.getName());
        textView2.setText(String.valueOf(singerItem.getCost()));
        imageView.setImageResource(singerItem.getImage());
    }
}