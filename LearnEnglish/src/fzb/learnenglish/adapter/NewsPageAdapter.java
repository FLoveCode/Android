package fzb.learnenglish.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class NewsPageAdapter extends PagerAdapter {

	List<View> viewList;
	List<String> sortList;
	
	public NewsPageAdapter(List<View> viewList,List<String> sortList) {
		
		this.viewList=viewList;
		this.sortList=sortList;
	}
	
	
	
	public List<View> getViewList() {
		return viewList;
	}



	public void setViewList(List<View> viewList) {
		this.viewList = viewList;
	}



	public List<String> getSortList() {
		return sortList;
	}



	public void setSortList(List<String> sortList) {
		this.sortList = sortList;
	}



	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		container.addView(viewList.get(position));
		
		return viewList.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		container.removeView(viewList.get(position));
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return sortList.get(position);
	}

}
