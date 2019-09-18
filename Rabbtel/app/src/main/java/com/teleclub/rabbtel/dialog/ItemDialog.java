package com.teleclub.rabbtel.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.teleclub.rabbtel.R;

public class ItemDialog extends Dialog {
    private Context mContext;
    private OnSetValueListener m_Listener;
    private TextView m_txtTitle;
    private EditText m_txtValue;
    private String m_strTitle = "";

    public ItemDialog(Context context, String title) {
        super(context);
        mContext = context;
        m_strTitle = title;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_item);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        initialize();
    }

    private void initialize() {
        m_txtTitle = findViewById(R.id.txt_title);
        m_txtTitle.setText(m_strTitle);
        m_txtValue = findViewById(R.id.txt_value);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.btn_okay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                m_Listener.onSetValue(m_txtValue.getText().toString().trim());
                dismiss();
            }
        });
    }

    public void setListener(OnSetValueListener listener) {
        this.m_Listener = listener;
    }

    public interface OnSetValueListener {
        void onSetValue(String value);
    }
}
