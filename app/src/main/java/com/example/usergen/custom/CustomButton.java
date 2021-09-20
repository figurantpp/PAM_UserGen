package com.example.usergen.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usergen.R;

public class CustomButton extends LinearLayout {

    Button custbutton;

    private String text;

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (attrs != null) {

            TypedArray array = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.CustomButton, 0, 0
            );

            try {
                text = array.getString(R.styleable.CustomButton_textCustom);
            } finally {
                array.recycle();
            }
        }

        if (text == null) {
            text = "Custom Button";
        }

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.cust_button, this);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        custbutton = (Button) this.findViewById(R.id.multipleusersbutton);
        custbutton.setBackgroundResource(R.drawable.format);

        custbutton.setText(text);

        custbutton.setOnClickListener(view -> {
            CustomButton.this.callOnClick();
            Animation updown = AnimationUtils.loadAnimation(getContext(), R.anim.up_down);
            custbutton.setAnimation(updown);
        });

    }
}
