package fzb.learnenglish.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fzb.learnenglish.entity.WordClass;

public class WordDao {

	private static MyDBHelper helper;
	
	public WordDao(Context context) {
		helper=new MyDBHelper(context);
	}
	
	
	public boolean insert(WordClass wc){
		SQLiteDatabase db=helper.getWritableDatabase();
		long id;
		ContentValues values = new ContentValues();
		
		values.put("_word", wc.getWord());
		values.put("_explain", wc.getExplain());
		values.put("_change", wc.getChange());
		values.put("_pharse", wc.getPharse());
		values.put("_sentence", wc.getSentence());
		
		 id=db.insert("words", null, values);
		
		if(id!=-1)
			return true;
		
		return false;
	}
	
	public List<WordClass> query(String word){
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=null;
		WordClass wc=null;
		List<WordClass> wcList=null;
		
		wcList=new ArrayList<WordClass>();
		
		if(word==null)
			cursor=db.query("words", new String[]{"_word","_explain","_change","_pharse","_sentence"}, null, null, null, null, "_word");
		else
			cursor=db.query("words", new String[]{"_word","_explain","_change","_pharse","_sentence"}, "_word=?", new String[]{word}, null, null, null);
		
		while(cursor.moveToNext()){
			
			wc=new WordClass();
			wc.setWord(cursor.getString(0));
			wc.setExplain(cursor.getString(1));
			wc.setChange(cursor.getString(2));
			wc.setPharse(cursor.getString(3));
			wc.setSentence(cursor.getString(4));
			
			wcList.add(wc);
		}
		
		return wcList;
	}
	
}
