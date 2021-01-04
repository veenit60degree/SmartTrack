package com.customfont;

import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tracking.smarttrack.R;

public class ErrorPopupHelper {
    private ErrorPopup mErrorPopup;
        private TextView lastTextView;

        /**
         * Hide error if user starts to write in this view
         */
        private TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mErrorPopup != null && mErrorPopup.isShowing()) {
                    cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        public void setError(TextView mTextView, int stringResId) {
            setError(mTextView, mTextView.getContext().getString(stringResId));
        }

        public void setError(TextView mTextView, CharSequence error) {
            cancel();

            CharSequence mError = TextUtils.stringOrSpannedString(error);
            if (mError != null) {
                showError(mTextView, error.toString());
            }
            mTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    mError == null ? 0 : android.R.drawable.stat_notify_error, 0);
            mTextView.addTextChangedListener(textWatcher);

            lastTextView = mTextView;
        }

        public void cancel() {
            if (lastTextView != null && lastTextView.getWindowToken() != null) {
                hideError();
                lastTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                lastTextView.removeTextChangedListener(textWatcher);
                lastTextView = null;
            }
        }

        private void showError(TextView mTextView, String error) {
            if (mTextView.getWindowToken() == null) {
                return;
            }

            if (mErrorPopup == null) {
                LayoutInflater inflater = LayoutInflater.from(mTextView.getContext());
                final TextView err = (TextView) inflater.inflate(R.layout.popup_text_error, null);

                final float scale = mTextView.getResources().getDisplayMetrics().density;
                mErrorPopup = new ErrorPopup(err, (int) (200 * scale + 0.5f), (int) (50 * scale + 0.5f));
                mErrorPopup.setFocusable(false);
                // The user is entering text, so the input method is needed.  We
                // don't want the popup to be displayed on top of it.
                mErrorPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            }

            TextView tv = (TextView) mErrorPopup.getContentView();
            chooseSize(mErrorPopup, error, tv);
            tv.setText(error);

            mErrorPopup.showAsDropDown(mTextView, getErrorX(mTextView), 0);
            mErrorPopup.fixDirection(mErrorPopup.isAboveAnchor());
        }

        private void hideError() {
            if (mErrorPopup != null) {
                if (mErrorPopup.isShowing()) {
                    mErrorPopup.dismiss();
                }
                mErrorPopup = null;
            }
        }

        private int getErrorX(TextView mTextView) {
            return mTextView.getWidth() - mErrorPopup.getWidth() - mTextView.getPaddingRight();
        }

        private void chooseSize(PopupWindow pop, CharSequence text, TextView tv) {
            int wid = tv.getPaddingLeft() + tv.getPaddingRight();
            int ht = tv.getPaddingTop() + tv.getPaddingBottom();

            int defaultWidthInPixels = tv.getResources().getDimensionPixelSize(
                    R.dimen.text_size_13);
            Layout l = new StaticLayout(text, tv.getPaint(), defaultWidthInPixels,
                    Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            float max = 0;
            for (int i = 0; i < l.getLineCount(); i++) {
                max = Math.max(max, l.getLineWidth(i));
            }

            /*
             * Now set the popup size to be big enough for the text plus the border capped
             * to DEFAULT_MAX_POPUP_WIDTH
             */
            pop.setWidth(wid + (int) Math.ceil(max));
            pop.setHeight(ht + l.getHeight());
        }
}
