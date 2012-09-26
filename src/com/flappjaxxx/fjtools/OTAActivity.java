package com.flappjaxxx.fjtools;

import java.io.DataOutputStream;
import java.io.IOException;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.flappjaxxx.fjtools.R;

public class OTAActivity extends Activity {
	static final String CMD_SU="su";
	static final String CMD_SUC="-c";
	static final String CMD_FJTOOL="fjtool";
	static final String CMD_UPFJ="upfjtools";
	
	WebView mWebView;
	final Activity activity = this;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.webview);

	    final ProgressDialog progressDialog = new ProgressDialog(activity);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progressDialog.setCancelable(false);
	    
	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl("http://www.jdvhosting.com/OTA2/ota.php?ROMID=47&ID=44950871");
	    mWebView.setWebViewClient(new OTAWebViewClient() {
	    	@Override
		    public void onPageFinished(WebView view, String url) {
		    super.onPageFinished(view, url);
		    }
		    });
	    mWebView.setDownloadListener(new DownloadListener() {
	        public void onDownloadStart(String url, String userAgent,
	                String contentDisposition, String mimetype,
	                long contentLength) {
	            Intent intent = new Intent(Intent.ACTION_VIEW);
	            intent.setData(Uri.parse(url));
	            startActivity(intent);

	        }
	    });

	    mWebView.setWebChromeClient(new WebChromeClient() {
	        public void onProgressChanged(WebView view, int progress) {
	            progressDialog.show();
	            progressDialog.setProgress(0);
	            activity.setProgress(progress * 1000);

	            progressDialog.incrementProgressBy(progress);

	            if(progress == 100 && progressDialog.isShowing())
	                progressDialog.dismiss();
	        }
	    });
	}
	
	private class OTAWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	    
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu4) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.menu4, menu4);
	    return true;
	}
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.changelog:
	    	try {
				Runtime.getRuntime().exec("am start -a android.intent.action.MAIN -n com.flappjaxxx.fjtools/.ChangelogActivity");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return true;
	    case R.id.update_tools:
	    	Process   p = null;
        	try {
        		p = Runtime.getRuntime().exec(CMD_SU);
        		DataOutputStream os = new DataOutputStream(p.getOutputStream());  
        		   os.writeBytes(CMD_FJTOOL + " " + CMD_UPFJ + "\n" + "; exit\n");  
        		   os.flush();  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ProgressDialog.show(OTAActivity.this,
					
	                "Please wait...", "Updating...", true);
	        return true;
	    case R.id.exit_app:
	    	System.exit(0);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
    }
    
}