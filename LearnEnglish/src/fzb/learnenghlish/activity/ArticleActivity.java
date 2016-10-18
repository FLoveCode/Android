package fzb.learnenghlish.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.example.learnenglish.R;

import fzb.learnenghlish.tools.NetTool;
import fzb.learnenglish.entity.ArticleClass;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleActivity extends Activity {

	private TextView tv_title;
	private TextView tv_info;
	private TextView tv_content;
	private Button bn_trans;
	
	Intent intent;
	ArticleClass ac;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.article_layout);
		
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_info=(TextView) findViewById(R.id.tv_info);
		tv_content=(TextView) findViewById(R.id.tv_content);
		bn_trans=(Button) findViewById(R.id.bn_trans);
		
		ac=(ArticleClass) getIntent().getSerializableExtra("article");
		
		bn_trans.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent();
				i.setAction(getResources().getString(R.string.TranslateActivity));
				i.addCategory(Intent.CATEGORY_DEFAULT);
				
				i.putExtra("content", ac.getContent());
				
				startActivity(i);
			}
		});
		
		if(!NetTool.isNetworkConnected(this))
			Toast.makeText(this, "请连接到网络", Toast.LENGTH_SHORT).show();
		else
			try {
				new ArticleTask().execute(new URL(ac.getHref()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	}
	
	class ArticleTask extends AsyncTask<URL, Integer, String>{

		@Override
		protected String doInBackground(URL... params) {
			StringBuilder sb=null;
			
			try {
				URLConnection con=params[0].openConnection();
				
				BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				String line;
				sb=new StringBuilder();
				
				while((line=br.readLine())!=null){
					sb.append(line);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				return sb.toString();
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			Document doc=Jsoup.parse(result);
			
			ac.setInfo(parseInfo(doc));
			ac.setContent(parseContent(doc));
			//填充数据
			fillTextView();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		public String parseInfo(Document doc){
			
			Element div=doc.select("div.span8.text-left").first();
			
			if(div!=null)
				return div.text();
			
			return null;
		}
		
		public String parseContent(Document doc){
			
			Element div=doc.select("div.span12.row-content").first();
			
			if(div!=null)
				return div.text();
			
			return null;
		}
		
		public void fillTextView(){
			
			tv_title.setText(ac.getTitle());
			tv_info.setText(ac.getInfo());
			tv_content.setText(ac.getContent());
			
		}
		
	}
	
	
}
