package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class SourceSansProRegularEditText extends EditText {

    public SourceSansProRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SourceSansProRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceSansProRegularEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/source_sans_pro_regular.ttf");
            setTypeface(tf);
        }
    }
}
