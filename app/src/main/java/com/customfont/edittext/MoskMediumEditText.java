package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class MoskMediumEditText extends EditText {

    public MoskMediumEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MoskMediumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoskMediumEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/mosk_medium_500.ttf");
            setTypeface(tf);
        }
    }
}
