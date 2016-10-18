package fzb.learnenglish.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.learnenglish.R;

import fzb.learnenglish.entity.ArticleClass;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleNewsListAdapter extends BaseAdapter {

	List<ArticleClass> data;
	LayoutInflater inflater;
	
	
	public SimpleNewsListAdapter(Context context,List<ArticleClass> data) {
		this.inflater=LayoutInflater.from(context);
		
		if(data==null){
			this.data=new ArrayList<ArticleClass>();
			this.data.add(new ArticleClass("正在获取中...."));
		}
		else
			this.data=data;
	}
	
	@Override
	public int getCount() {
		
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		arg1=inflater.inflate(R.layout.simple_article_item, null);
		
		TextView tv_item1=(TextView) arg1.findViewById(R.id.tv_item1);
		
		tv_item1.setText(data.get(arg0).getTitle());
		
		return arg1;
	}
	
	public void setData(List<ArticleClass> data){
		
		this.data=data;
	}
	
	public List<ArticleClass> getData(){
		return data;
	}

}
