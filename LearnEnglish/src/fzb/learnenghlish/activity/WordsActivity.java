package fzb.learnenghlish.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.learnenglish.R;

import fzb.learnenghlish.tools.NetTool;
import fzb.learnenglish.db.WordDao;
import fzb.learnenglish.entity.WordClass;
import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.graphics.Path.FillType;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 单词界面
 */
public class WordsActivity extends Activity {

	private TextView tv_word;
	private TextView tv_explain;
	private TextView tv_change;
	private TextView tv_pharse;
	private TextView tv_sentence;
	private Button bn_add_word;
	private ProgressBar pb_word;
	
	WordClass wc;
	Boolean isLocal=false;
	WordDao wd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.words_layout);
		
		tv_word=(TextView) findViewById(R.id.tv_word);
		tv_explain=(TextView) findViewById(R.id.tv_explain);
		tv_change=(TextView) findViewById(R.id.tv_change);
		tv_pharse=(TextView) findViewById(R.id.tv_pharse);
		tv_sentence=(TextView) findViewById(R.id.tv_sentence);
		bn_add_word=(Button) findViewById(R.id.bn_add_word);
		pb_word=(ProgressBar) findViewById(R.id.pb_word);
		
		wd=new WordDao(WordsActivity.this);
		String word=getIntent().getStringExtra("word");
		List<WordClass> wcList=wd.query(word);
		
		if(wcList!=null&&wcList.size()>0){
			wc=wcList.get(0);
			isLocal=true;
		}
		else
			wc=new WordClass(word);
		tv_word.setText(word);
		
		if(!NetTool.isNetworkConnected(this))
			Toast.makeText(this, "请连接到网络", Toast.LENGTH_SHORT).show();
		else
			try {
				
				if(isLocal){
					pb_word.setVisibility(View.INVISIBLE);
					fillTextView(wc);
				}
				else
					new WordTask().execute(new URL(getResources().getString(R.string.translate_word)+wc.getWord()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		
	}
	
	
	public void fillTextView(WordClass w){
		
		if(!TextUtils.isEmpty(w.getExplain()))
			tv_explain.setText(w.getExplain());
		else
			tv_explain.setText("无");
		
		if(!TextUtils.isEmpty(w.getChange()))
			tv_change.setText(w.getChange());
		else
			tv_change.setText("无");
		
		if(!TextUtils.isEmpty(w.getPharse()))
			tv_pharse.setText(w.getPharse());
		else
			tv_pharse.setText("无");
		
		if(!TextUtils.isEmpty(w.getSentence()))
			tv_sentence.setText(w.getSentence());
		else
			tv_sentence.setText("无");
		
		
	}
	
	/**
	 * 添加单词
	 */
	public void add_word(View v){
		
		if(isLocal){
			Toast.makeText(WordsActivity.this, "该单词已经存在", Toast.LENGTH_SHORT).show();
		}
		else{
			boolean isInsert=wd.insert(wc);
			
			if(isInsert)
				Toast.makeText(WordsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(WordsActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	class WordTask extends AsyncTask<URL, Integer, String>{

		@Override
		protected String doInBackground(URL... params) {
			
			URLConnection con=null;
			StringBuilder sb=null;
			
			try {
				con=params[0].openConnection();
				
				BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
				sb=new StringBuilder();
				
				String line=null;
				
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
			
			pb_word.setVisibility(View.INVISIBLE);
			
			List<String> messageList;
			
			messageList=parseMessage(result);
			
			wc.setExplain(messageList.get(0));
			wc.setChange(messageList.get(1));
			wc.setPharse(messageList.get(2));
			wc.setSentence(messageList.get(3));
			
			Log.d(getPackageName(), "测试:"+result);
			
			fillTextView(wc);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		public List<String> parseMessage(String response){
			List<String> messageList=new ArrayList<String>();
			String s=null;
			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
			
			Document doc=Jsoup.parse(response);
			//解释
			s=doc.select("div#panel_comment.word_text").text();
			messageList.add(s);
			//词形变化
			s=doc.select("div#dictc_PWDECMEC").text();
			messageList.add(s);
			//常用短语
			Elements li=doc.select("div#panel_phrases_content ol").select("li");
			for(int i=0;i<li.size();i++){
				sb.append((i+1)+"、"+li.get(i).text());
				sb.append("\r\n");
			}
			messageList.add(sb.toString());
			
			//例句
			Elements li_sent=doc.select("li.li_sent");
			for(int i=0;i<li_sent.size();i++){
				sb1.append((i+1)+"、"+li_sent.get(i).text());
				sb1.append("\r\n");
			}
			messageList.add(sb1.toString());
			
			return messageList;
		}
		
		
	}
}
