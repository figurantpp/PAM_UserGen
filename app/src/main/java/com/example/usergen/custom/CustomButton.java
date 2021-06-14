package com.example.usergen.custom;

import android.content.Context;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.usergen.R;

public class CustomButton extends LinearLayout {

    Button custbutton;


    public CustomButton(Context context) {
        super(context);
        init(context);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init( Context context)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.cust_button, this);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        custbutton = (Button) this.findViewById(R.id.multipleusersbutton);
        custbutton.setBackgroundResource(R.drawable.format);
        custbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomButton.this.callOnClick();
                Animation updown = AnimationUtils.loadAnimation(getContext(), R.anim.up_down);
                custbutton.setAnimation(updown);
            }
        });

    }
}
