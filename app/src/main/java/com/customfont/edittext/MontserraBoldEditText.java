package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class MontserraBoldEditText extends EditText {

    public MontserraBoldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MontserraBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MontserraBoldEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/montserra_bold.ttf");
            setTypeface(tf);
        }
    }
}
