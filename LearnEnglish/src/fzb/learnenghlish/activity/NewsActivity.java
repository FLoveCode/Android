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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.learnenglish.R;

import fzb.learnenglish.adapter.ComplexNewsListAdapter;
import fzb.learnenglish.adapter.NewsPageAdapter;
import fzb.learnenglish.entity.ArticleClass;

/*
 * 新闻界面
 */

public class NewsActivity extends Activity{

	ViewPager vp_news;
	LayoutInflater inflater;
	
	List<View> viewList;
	List<String> sortList;
	
	NewsPageAdapter npa;
	ComplexNewsListAdapter nla;
	
	//标记是否已经加载新闻
	boolean isPolitics=false;
	boolean isSociety=false;
	boolean isDiplomacy=false;
	boolean isMilitary=false;
	boolean isLaw=false;
	boolean isEnvironment=false;
	
	Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_layout);
		
		vp_news=(ViewPager) findViewById(R.id.vp_news);
		
		inflater=LayoutInflater.from(this);
		
		//标题
		sortList=new ArrayList<String>();
		sortList.add(getResources().getString(R.string.politics));
		sortList.add(getResources().getString(R.string.society));
		sortList.add(getResources().getString(R.string.diplomacy));
		sortList.add(getResources().getString(R.string.military));
		sortList.add(getResources().getString(R.string.law));
		sortList.add(getResources().getString(R.string.environment));
		
		//布局
		viewList=new ArrayList<View>();
		viewList.add(inflater.inflate(R.layout.politics_layout, null));
		viewList.add(inflater.inflate(R.layout.society_layout, null));
		viewList.add(inflater.inflate(R.layout.diplomacy_layout, null));
		viewList.add(inflater.inflate(R.layout.military_layout, null));
		viewList.add(inflater.inflate(R.layout.law_layout, null));
		viewList.add(inflater.inflate(R.layout.environment_layout, null));
		
		npa=new NewsPageAdapter(viewList, sortList);
		
		vp_news.setAdapter(npa);
		
		vp_news.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				URL url=null;
				boolean isStart = true;
				
				try {
				
				switch(arg0){
				
				case 0:
					isStart=isPolitics;
					url=new URL(getResources().getString(R.string.cn_china)+"politics/");
					break;
				case 1:
					isStart=isSociety;
					url=new URL(getResources().getString(R.string.cn_china)+"society/");
					break;
				case 2:
					isStart=isDiplomacy;
					url=new URL(getResources().getString(R.string.cn_china)+"diplomacy/");
					break;
				case 3:
					isStart=isMilitary;
					url=new URL(getResources().getString(R.string.cn_china)+"military/");
					break;
				case 4:
					isStart=isLaw;
					url=new URL(getResources().getString(R.string.cn_china)+"law/");
					break;
				case 5:
					isStart=isEnvironment;
					url=new URL(getResources().getString(R.string.cn_china)+"environment/");
					break;
				}
				
				//启动线程，获取数据
				if(!isStart)
					new Thread(new LoadNewsRunnable(url, arg0)).start();
				
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				
				ArrayList<ArticleClass> articleList=null;
				articleList=pharseArticleList(msg.getData().getStringArrayList("articleList"));
				nla=new ComplexNewsListAdapter(NewsActivity.this, articleList);
				
				ListView lv=null;
				
				switch(msg.what){
				
				case 0:
					isPolitics=true;
					(npa.getViewList().get(0).findViewById(R.id.pb_politics)).setVisibility(View.INVISIBLE);
					lv=(ListView) npa.getViewList().get(0).findViewById(R.id.lv_politics);
					break;
				case 1:
					isSociety=true;
					(npa.getViewList().get(1).findViewById(R.id.pb_society)).setVisibility(View.INVISIBLE);
					lv=(ListView) npa.getViewList().get(1).findViewById(R.id.lv_society);
					break;
				case 2:
					isDiplomacy=true;
					(npa.getViewList().get(2).findViewById(R.id.pb_dialomacy)).setVisibility(View.INVISIBLE);
					lv=(ListView) npa.getViewList().get(2).findViewById(R.id.lv_diplomacy);
					break;
				case 3:
					isMilitary=true;
					(npa.getViewList().get(3).findViewById(R.id.pb_military)).setVisibility(View.INVISIBLE);
					lv=(ListView) npa.getViewList().get(3).findViewById(R.id.lv_military);
					break;
				case 4:
					isLaw=true;
					(npa.getViewList().get(4).findViewById(R.id.pb_law)).setVisibility(View.INVISIBLE);
					lv=(ListView) npa.getViewList().get(4).findViewById(R.id.lv_law);
					break;
				case 5:
					isEnvironment=true;
					(npa.getViewList().get(5).findViewById(R.id.pb_environment)).setVisibility(View.INVISIBLE);
					lv=(ListView) npa.getViewList().get(5).findViewById(R.id.lv_environment);
					break;
				}
				if(lv!=null){
					lv.setAdapter(nla);
					
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							
							Intent i=new Intent();
							i.putExtra("article", nla.getData().get(arg2));
							i.setAction(getResources().getString(R.string.ArticleActivity));
							i.addCategory(Intent.CATEGORY_DEFAULT);
							startActivity(i);
						}
					});
				}
			}
		};
		
		//第一次启动线程
		try {
			new Thread(new LoadNewsRunnable(new URL(getResources().getString(R.string.cn_china)+"politics/"), 0)).start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ArticleClass> pharseArticleList(ArrayList<String> list){
		ArticleClass ac=null;
		ArrayList<ArticleClass> articleList=new ArrayList<ArticleClass>(); 
		
		for(String str:list){
			String[] s=str.split(";");
			
			ac=new ArticleClass(s[0], s[1]);
			ac.setInfo(s[2]);
			
			articleList.add(ac);
		}
		
		return articleList;
	}
	
	class LoadNewsRunnable implements Runnable{

		URL url;
		int what;
		
		public LoadNewsRunnable(URL url,int what){
			this.url=url;
			this.what=what;
		}
		
		@Override
		public void run() {
			
			String line=null;
			StringBuilder sb=null;
			ArrayList<String> articleList=null;
			
			try {
				URLConnection con=url.openConnection();
				
				BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
				
				sb=new StringBuilder();
				
				while((line=br.readLine())!=null){
					sb.append(line);
				}
				
				articleList=parseMessage(sb.toString());
				
				Message m=new Message();
				m.what=what;
				Bundle bundle=new Bundle();
				bundle.putStringArrayList("articleList", articleList);
				m.setData(bundle);
				
				handler.sendMessage(m);
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
		
		public ArrayList<String> parseMessage(String response){
			
			ArrayList<String> msgList=new ArrayList<String>();
			StringBuffer sb=null;
			Document doc=Jsoup.parse(response);
			
			Elements contents=doc.select("div.row-content");
			
			for(Element content:contents){
				sb=new StringBuffer();
				Element a=content.select("div.span10 h4 a").first();
				sb.append(a.text()+";");
				sb.append(a.attr("href")+";");
				Element p=content.select("div.span10 p").first();
				sb.append(p.text());
				
				msgList.add(sb.toString());
			}
			
			return msgList;
		}
	}
	
}
