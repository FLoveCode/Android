package fzb.learnenghlish.activity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.bmob.v3.BmobUser;

import com.example.learnenglish.R;

import fzb.learnenghlish.tools.NetTool;
import fzb.learnenglish.adapter.SimpleNewsListAdapter;
import fzb.learnenglish.entity.ArticleClass;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 主界面
 */
public class MainActivity extends Activity {

	private EditText et_content;
	private Button bn_search;
	private Button bn_person;
	private TextView tv_more;
	private ListView ls_news;
	
	SimpleNewsListAdapter nla;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		
		et_content=(EditText) findViewById(R.id.et_content);
		bn_search=(Button) findViewById(R.id.bn_search);
		bn_person=(Button) findViewById(R.id.bn_person);
		tv_more=(TextView) findViewById(R.id.tv_more);
		ls_news=(ListView) findViewById(R.id.lv_news);
		
		nla=new SimpleNewsListAdapter(MainActivity.this, null);
		ls_news.setAdapter(nla);
		
		bn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent();
				i.putExtra("word", et_content.getText().toString());
				i.setAction(getResources().getString(R.string.WordsActivity));
				i.addCategory(Intent.CATEGORY_DEFAULT);
				startActivity(i);
			}
		});
		
		bn_person.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent();
				
					i.setAction(getResources().getString(R.string.LoginActivity));
					
				i.addCategory(Intent.CATEGORY_DEFAULT);
				startActivity(i);
			}
		});
		
		tv_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent();
				
				i.setAction(getResources().getString(R.string.NewsActivity));
				i.addCategory(Intent.CATEGORY_DEFAULT);
				startActivity(i);
			}
		});
		
		ls_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			Intent i=new Intent();
			i.putExtra("article", nla.getData().get(arg2));
			i.setAction(getResources().getString(R.string.ArticleActivity));
			i.addCategory(Intent.CATEGORY_DEFAULT);
			startActivity(i);
			}
		});
		
	if(!NetTool.isNetworkConnected(this))
		Toast.makeText(this, "请连接到网络", Toast.LENGTH_SHORT).show();
	else
		try {
			new TopNewsTask().execute(new URL(getResources().getString(R.string.cn_china)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	
	Log.d(getPackageName(), "onCreate");
		
	}
	
	class TopNewsTask extends AsyncTask<URL, Integer, String>{

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
				
				if(sb==null)
					return null;
				return sb.toString();
			}
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			Document doc=Jsoup.parse(result);
			
			Element ul=doc.select("ul.inline-block").first();
			
			Elements lis=ul.select("li a");
			
			List<ArticleClass> news_list=new ArrayList<ArticleClass>();
			ArticleClass ac;
			
			for(Element li:lis){
				ac=new ArticleClass(li.text(),li.attr("href"));
				news_list.add(ac);
			}
			
			nla.setData(news_list);
			nla.notifyDataSetInvalidated();
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

		
}
