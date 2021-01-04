package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EuropaRegularEditText extends EditText {

    public EuropaRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EuropaRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EuropaRegularEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/europa_regular.ttf");
            setTypeface(tf);
        }
    }
}
