package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class KarlaRegularEditText extends EditText {

    public KarlaRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public KarlaRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KarlaRegularEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/karla_regular.ttf");
            setTypeface(tf);
        }
    }
}
