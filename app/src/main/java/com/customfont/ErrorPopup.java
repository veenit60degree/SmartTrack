package com.customfont;

import android.widget.PopupWindow;
import android.widget.TextView;

import com.tracking.smarttrack.R;

public class ErrorPopup extends PopupWindow {

    private final TextView mView;
    private boolean mAbove = false;

    public ErrorPopup(TextView v, int width, int height) {
        super(v, width, height);
        mView = v;
        mView.setBackgroundResource(R.color.colorPrimaryDark); // TODO provide actual resource
    }

    public void fixDirection(boolean above) {
        mAbove = above;
        mView.setBackgroundResource(above ? R.color.white : R.color.white); // TODO provide actual resources
    }

    @Override
    public void update(int x, int y, int w, int h, boolean force) {
        super.update(x, y, w, h, force);

        boolean above = isAboveAnchor();
        if (above != mAbove) {
            fixDirection(above);
        }
    }
}