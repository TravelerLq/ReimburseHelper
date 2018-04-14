package cn.unitid.spark.cm.sdk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.unitid.spark.cm.sdk.R;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.common.UrlParser;

import java.security.cert.CertificateException;
import java.util.Map;

/**
 * 内置浏览器界面
 * 处理证书注册 和延期业务
 */
public class WebViewActivity extends FragmentActivity {
    private static final String TAG = WebViewActivity.class.getName();
    private Button finishButton;
    private WebView webView;
    private FrameLayout previousView;
    private ImageView previousImageView;
    private FrameLayout nextView;
    private ImageView nextImageView;
    private ImageView refreshImageView;
    private ImageView shareView;
    private Animation animation;

    public ValueCallback<Uri> mUploadMessage;

    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywebview);

        final String baseUrl = getIntent().getStringExtra("baseUrl");

        final String type = getIntent().getStringExtra("type");
        if( DataProcessType.REGISTER.name().equals(type)){
            url=baseUrl+"/anonymous/template/list";
        }
        if( DataProcessType.DELAY.name().equals(type)){
            String certificate = getIntent().getStringExtra("certificate");
            try {
                String sn = CertificateConverter.fromBase64(certificate).getSerialNumber().toString(10);
                url=baseUrl+"/anonymous/delay/"+sn+"/10";
            } catch (CertificateException e) {
                e.printStackTrace();
            }
        }


        String name = getIntent().getStringExtra("name");

        finishButton = (Button) findViewById(R.id.mywebview_finishbutton);
        webView = (WebView) findViewById(R.id.mywebview_webView);
        previousView = (FrameLayout) findViewById(R.id.mywebview_previous);
        previousImageView = (ImageView) findViewById(R.id.mywebview_previous_image);
        nextView = (FrameLayout) findViewById(R.id.mywebview_next);
        nextImageView = (ImageView) findViewById(R.id.mywebview_next_image);
        FrameLayout refreshView = (FrameLayout) findViewById(R.id.mywebview_refresh);
        refreshImageView = (ImageView) findViewById(R.id.mywebview_refresh_image);
        shareView = (ImageView) findViewById(R.id.mywebview_share);
        TextView titleView = (TextView) findViewById(R.id.mywebview_title);

        animation = AnimationUtils.loadAnimation(this, R.anim.web_refresh);
        animation.setInterpolator(new LinearInterpolator());





            WebSettings webSettings = webView.getSettings();

//            webSettings.setUserAgentString(
//                    "User-Agent:Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setSupportMultipleWindows(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSavePassword(false);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setMinimumFontSize(webSettings.getMinimumFontSize() + 8);

            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setDefaultTextEncodingName("utf-8");

        webView.setWebChromeClient(new WebChromeClient(){

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                if (mUploadMessage != null) return;
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult( Intent.createChooser(i, "File Chooser"), MyWebViewClient.FILECHOOSER_RESULTCODE );
            }
            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }
        });

            webView.setWebViewClient(new MyWebViewClient());





        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                webView.loadUrl(url);
            }
        }.sendEmptyMessageDelayed(0, 500);
        if (name != null && !name.equals("")) {
            if (name.length() > 15) {
                name = name.substring(0, 15) + "...";
            }
            titleView.setText(name);
        }


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        previousView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        public final static int FILECHOOSER_RESULTCODE = 1;


        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            // TODO need to check
//                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边

              Log.e("shouldOverrideUrlLoading",url);

            //注册成功返回
            if(url.indexOf("/anonymous/reg/ok")>-1) {

                Map<String, String> params = UrlParser.parser(url);
                String your_issueId = params.get("your_issueId");
                String your_nonce = params.get("your_nonce");
                String subject = params.get("subject");
                String algorithm = params.get("algorithm");
                String ret_code=params.get("ret_code");

                if (your_issueId != null) {
                    Intent intent = new Intent();
                    intent.putExtra("ret", 0);
                    intent.putExtra("type", DataProcessType.REGISTER.name());
                    intent.putExtra("your_issueId", your_issueId);
                    intent.putExtra("your_nonce", your_nonce);
                    intent.putExtra("subject", subject);
                    intent.putExtra("algorithm", algorithm);
                    intent.putExtra("ret_code", ret_code);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }else if(url.indexOf("/anonymous/delay/ok")>-1) {     //延期成功返回

                Map<String, String> params = UrlParser.parser(url);
                String your_issueId = params.get("your_issueId");
                String your_nonce = params.get("your_nonce");
                String subject = params.get("subject");
                String algorithm = params.get("algorithm");
                String ret_code=params.get("ret_code");
                if (your_issueId != null) {
                    Intent intent = new Intent();
                    intent.putExtra("ret", 0);
                    intent.putExtra("type", DataProcessType.DELAY.name());
                    intent.putExtra("your_issueId", your_issueId);
                    intent.putExtra("your_nonce", your_nonce);
                    intent.putExtra("subject", subject);
                    intent.putExtra("algorithm", algorithm);
                    intent.putExtra("ret_code", ret_code);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }else{
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            previousView.setEnabled(false);
            nextView.setEnabled(false);
            refreshImageView.clearAnimation();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            refreshImageView.startAnimation(animation);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyWebViewClient.FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }
    }

}



