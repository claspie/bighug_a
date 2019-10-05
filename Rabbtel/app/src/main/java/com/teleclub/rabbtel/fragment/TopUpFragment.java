package com.teleclub.rabbtel.fragment;

import android.annotation.TargetApi;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Constants;
import com.teleclub.rabbtel.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopUpFragment extends Fragment {

    private WebView m_webview;
   // private TextView m_txtNotAvailable;
    private ProgressBar m_progressBar;

    public TopUpFragment() {
        // Required empty public constructor
    }

    public static TopUpFragment newInstance() {
        return new TopUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topup, container, false);
        m_webview = view.findViewById(R.id.webview);
      //  m_txtNotAvailable = view.findViewById(R.id.txt_not_available);
        m_progressBar = view.findViewById(R.id.progress);
        initialize();

        return view;
    }

    private void initialize() {
        if (AppData.account == null || m_webview == null) return;

//        m_webview.getSettings().setJavaScriptEnabled(true);
//        m_webview.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E233 Safari/601.1");

//        m_webview.setWebViewClient(new WebViewClient() {
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                m_progressBar.setVisibility(View.GONE);
//            }
//
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//                m_progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                // do your stuff here
//                m_progressBar.setVisibility(View.GONE);
//            }
//        });

     //   updateTopupStatus();
    }

    public void refresh() {
        initialize();
    }

    private void updateTopupStatus() {
//        if (AppData.canTopup) {
       //     m_txtNotAvailable.setVisibility(View.GONE);
            m_webview.setVisibility(View.VISIBLE);

            String url = Constants.BASE_URL + "/apptopup?imei=" + Util.getUniqueIMEI(getContext()) +
                    "&appphonenumber=" + AppData.account.getPhone() +
                    "&app_i_account=" + AppData.account.getIAccount() +
                    "&apptype=android";
            m_webview.loadUrl(url);
            m_progressBar.setVisibility(View.VISIBLE);
//        } else {
//            m_txtNotAvailable.setVisibility(View.VISIBLE);
//            m_webview.setVisibility(View.GONE);
//            m_progressBar.setVisibility(View.GONE);
//        }
    }
}
