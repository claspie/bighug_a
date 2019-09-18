package com.teleclub.rajatalk.fragment;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.teleclub.rajatalk.R;
import com.teleclub.rajatalk.network.RajaTalkAPI;
import com.teleclub.rajatalk.network.RetrofitBuilder;
import com.teleclub.rajatalk.network.result.BlockAccountResult;
import com.teleclub.rajatalk.util.AppData;
import com.teleclub.rajatalk.util.Constants;
import com.teleclub.rajatalk.util.Util;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CallbackFragment extends Fragment implements View.OnClickListener {

    private WebView m_webview;
    private ProgressBar m_progressBar;

    public CallbackFragment() {
        // Required empty public constructor
    }

    public static CallbackFragment newInstance() {
        return new CallbackFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_callback, container, false);

        m_webview = view.findViewById(R.id.webview);
        m_progressBar = view.findViewById(R.id.progress);
//        view.findViewById(R.id.btn_block).setOnClickListener(this);

        return view;
    }

    private void initialize() {
        if (AppData.account == null || m_webview == null) return;

        m_webview.getSettings().setJavaScriptEnabled(true);
        m_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        m_webview.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E233 Safari/601.1");
//        http://bighug.ca/msms/index
        String url = Constants.BASE_URL + "/msms/index?imei=" + Util.getUniqueIMEI(getContext()) +
                "&appphonenumber=" + AppData.account.getPhone() +
                "&app_i_account=" + AppData.account.getIAccount() +
                "&fromapp=1" +
                "&apptype=android";
        m_webview.loadUrl(url);
        m_progressBar.setVisibility(View.VISIBLE);

        m_webview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
                m_progressBar.setVisibility(View.GONE);
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                m_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                m_progressBar.setVisibility(View.GONE);
                view.clearCache(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        unblockUser();
        m_webview.setVisibility(View.VISIBLE);
        initialize();
    }

    public void refresh() {
        initialize();
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.btn_block) {
//            blockUser();
//        }
    }

    private void blockUser() {
        RetrofitBuilder.createRetrofitService(RajaTalkAPI.class).blockAccount(AppData.token, AppData.account.getPhone(),
                Util.getUniqueIMEI(getContext()), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlockAccountResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), getResources().getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BlockAccountResult accountResult) {
                        if (accountResult.getMessages() != null && accountResult.getMessages().size() > 0 && getContext() != null) {
                            Toast.makeText(getContext(), accountResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        if (accountResult.getStatus()) {
                            m_webview.setVisibility(View.GONE);
                        }
                    }
                });
        AppData.called = false;
    }

    private void unblockUser() {
        RetrofitBuilder.createRetrofitService(RajaTalkAPI.class).unblockAccount(AppData.token, AppData.account.getPhone(),
                Util.getUniqueIMEI(getContext()), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlockAccountResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), getResources().getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BlockAccountResult accountResult) {
                        if (accountResult.getMessages() != null && accountResult.getMessages().size() > 0 && getContext() != null) {
                            Toast.makeText(getContext(), accountResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        if (accountResult.getStatus()) {
                            m_webview.setVisibility(View.VISIBLE);
                            initialize();
                        }
                    }
                });
    }

}
