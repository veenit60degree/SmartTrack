package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class FuturaMediumEditText extends EditText {

    public FuturaMediumEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FuturaMediumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FuturaMediumEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/futura_medium_bt.ttf");
            setTypeface(tf);
        }
    }
}
