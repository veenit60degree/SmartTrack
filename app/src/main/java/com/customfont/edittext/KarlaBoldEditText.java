package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class KarlaBoldEditText extends EditText {

    public KarlaBoldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public KarlaBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KarlaBoldEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/karla_bold.ttf");
            setTypeface(tf);
        }
    }
}
