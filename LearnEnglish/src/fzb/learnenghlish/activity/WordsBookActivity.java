package fzb.learnenghlish.activity;

import java.util.List;

import com.example.learnenglish.R;

import fzb.learnenglish.db.WordDao;
import fzb.learnenglish.entity.WordClass;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
/*
 * 单词本界面
 */
public class WordsBookActivity extends Activity {

	private ListView lv_wb;
	List<WordClass> wcList=null;
	WordDao wd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.words_book_layout);
		
		lv_wb=(ListView) findViewById(R.id.lv_wb);
		
		wd=new WordDao(WordsBookActivity.this);
		wcList=wd.query(null);
		
		lv_wb.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				
				if(arg1==null){
					arg1=View.inflate(WordsBookActivity.this, R.layout.wb_item, null);
					arg1.setTag(arg1);
				}
				else
					arg1=(View) arg1.getTag();
				
				TextView tv_word=(TextView) arg1.findViewById(R.id.tv_word);
				TextView tv_explain=(TextView) arg1.findViewById(R.id.tv_explain);
				
				tv_word.setText(wcList.get(arg0).getWord());
				tv_explain.setText(wcList.get(arg0).getExplain());
				
				return arg1;
			}
			
			@Override
			public long getItemId(int arg0) {
				return arg0;
			}
			
			@Override
			public Object getItem(int arg0) {
				return wcList.get(arg0);
			}
			
			@Override
			public int getCount() {
				return wcList.size();
			}
		});
		
		lv_wb.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i=new Intent(WordsBookActivity.this,WordsActivity.class);
				
				i.putExtra("word", wcList.get(arg2).getWord());
				
				startActivity(i);
				finish();
			}
			
		});
	}
}
