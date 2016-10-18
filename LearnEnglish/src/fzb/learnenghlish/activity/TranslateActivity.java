package fzb.learnenghlish.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.learnenglish.R;

import fzb.learnenghlish.tools.NetTool;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class TranslateActivity extends Activity {

	private TextView tv_trans_all;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.translate_layout);
		
		tv_trans_all=(TextView) findViewById(R.id.tv_trans_all);
		
		if(!NetTool.isNetworkConnected(this))
			Toast.makeText(this, "ÇëÁ¬½Óµ½ÍøÂç", Toast.LENGTH_SHORT).show();
		else
			new TransTask().execute(getResources().getString(R.string.translate_all),getIntent().getStringExtra("content"));
		
	}
	
	class TransTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			
			URL url=null;
			HttpURLConnection con=null;
			StringBuilder sb=null;
			String line=null;
			
			try {
				url=new URL(params[0]);
				con=(HttpURLConnection) url.openConnection();
				
				con.setDoInput(true);
				con.setDoOutput(true);
				
				PrintWriter pw=new PrintWriter(con.getOutputStream());
				pw.print("do=trans&"+"q="+params[1]+"&fromto=auto-auto");
				pw.flush();
				
				BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
				sb=new StringBuilder();
				
				while((line=br.readLine())!=null){
					sb.append(line);
				}
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			finally{
				
				return sb.toString();
			}
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			Log.d(getPackageName(), result);
			
			try {
				JSONObject translate_result=new JSONObject(result);
				
				JSONArray array_result=translate_result.getJSONArray("trans_result");
				
				JSONObject object_json=(JSONObject) array_result.get(0);
				
				tv_trans_all.setText(object_json.getString("dst"));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	}
}
