package com.customfont.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class SourceSansProBoldEditText extends EditText {

    public SourceSansProBoldEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SourceSansProBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceSansProBoldEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/source_sans_pro_bold.ttf");
            setTypeface(tf);
        }
    }
}
