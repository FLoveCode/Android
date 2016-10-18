package fzb.learnenglish.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	
	
	public MyDBHelper(Context context) {
		super(context, "word_book.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		//´´½¨words±í
		arg0.execSQL(""
		+ "CREATE TABLE words (_id INTEGER PRIMARY KEY AUTOINCREMENT, _word VARCHAR(20), "
		+ "_explain VARCHAR(100), _change VARCHAR(100), _pharse VARCHAR(100), _sentence VARCHAR(500))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
