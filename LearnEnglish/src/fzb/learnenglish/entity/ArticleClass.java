package fzb.learnenglish.entity;

import java.io.Serializable;

import android.net.sip.SipRegistrationListener;

/*
 * ÎÄÕÂÊµÌå
 */

public class ArticleClass implements Serializable{

	private String title;
	private String introduce;
	private String content;
	private String href;
	private String info;
	
	static final long serialVersionUID = 1L;
	
	public ArticleClass() {
	}
	
	public ArticleClass(String title){
		this.title=title;
	}
	
	public ArticleClass(String title,String href){
		this.title=title;
		this.href=href;
	}
	
	public ArticleClass(String title,String introduce,String href){
		this.title=title;
		this.introduce=introduce;
		this.href=href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	
	
	
}
