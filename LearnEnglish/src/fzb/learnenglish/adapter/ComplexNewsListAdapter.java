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

public class ComplexNewsListAdapter extends BaseAdapter {

	List<ArticleClass> data;
	LayoutInflater inflater;
	
	
	public ComplexNewsListAdapter(Context context,List<ArticleClass> data) {
		this.inflater=LayoutInflater.from(context);
		
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
		
		arg1=inflater.inflate(R.layout.complex_article_layout, null);
		
		TextView tv_item2=(TextView) arg1.findViewById(R.id.tv_item2);
		TextView tv_item3=(TextView) arg1.findViewById(R.id.tv_item3);
		
		tv_item2.setText(data.get(arg0).getTitle());
		tv_item3.setText(data.get(arg0).getInfo());
		
		return arg1;
	}
	
	public void setData(List<ArticleClass> data){
		
		this.data=data;
	}
	
	public List<ArticleClass> getData(){
		return data;
	}

}
