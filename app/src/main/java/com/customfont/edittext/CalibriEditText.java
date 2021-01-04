package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CalibriEditText extends EditText {

    public CalibriEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CalibriEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalibriEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/calibri.ttf");
            setTypeface(tf);
        }
    }
}
