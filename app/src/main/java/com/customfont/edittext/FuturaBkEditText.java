package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class FuturaBkEditText extends EditText {

    public FuturaBkEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FuturaBkEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FuturaBkEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/futura_bk_bt_book.ttf");
            setTypeface(tf);
        }
    }
}
