package com.example.accountmanager;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button b;
	EditText et;
	TextView tv;
	HttpPost httpPost;
	StringBuilder buffer;
	EditText pass;
	HttpResponse response;
	HttpClient httpClient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		b = (Button) findViewById(R.id.btnLogin);
		et = (EditText)findViewById(R.id.username);
		pass= (EditText)findViewById(R.id.password);
		tv = (TextView)findViewById(R.id.tv);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(MainActivity.this, "", 
						"Validating user...", true);
				try {
					login();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//						new Thread(new Runnable() {
//							public void run() {
//								Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
//								login();
//								}
//							}).start();
						}
			});
	}
public void login() throws ClientProtocolException, IOException{
	Toast.makeText(MainActivity.this,"Vao", Toast.LENGTH_SHORT).show();
try{
	httpClient=new DefaultHttpClient();
	httpPost= new HttpPost("http://10.0.2.2/check.php"); // make sure the url is correct.
	nameValuePairs = new ArrayList<NameValuePair>(2);
	nameValuePairs.add(new BasicNameValuePair("username",et.getText().toString().trim()));
	nameValuePairs.add(new BasicNameValuePair("password",pass.getText().toString().trim()));
	httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	Toast.makeText(MainActivity.this,et.getText().toString().trim(), Toast.LENGTH_SHORT).show();
	response = httpClient.execute(httpPost);
	
	ResponseHandler<String> responseHandler = new BasicResponseHandler();
	final String response = httpClient.execute(httpPost, responseHandler);
	runOnUiThread(new Runnable() {
		public void run() {
			tv.setText("Response from PHP : " + response);
			dialog.dismiss();
			}
		});
	if(response.equalsIgnoreCase("User Found")){
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_LONG).show();
				}
			});
		
		startActivity(new Intent(MainActivity.this, UserPage.class));
		}else{
			showAlert();
			}
	}catch(Exception e){
		dialog.dismiss();
		e.printStackTrace();
		Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
}

}
public void showAlert(){
	MainActivity.this.runOnUiThread(new Runnable() {
		public void run() {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("Login Error.");
			builder.setMessage("User not Found.").setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
			}
			});
			AlertDialog alert = builder.create();
			alert.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}