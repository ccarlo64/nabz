package com.homeproject.nabz;
//import com.example.sqlite.*;

//package it.devapp.database.db;

import java.text.MessageFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "nabaztag.db";

	private static final int SCHEMA_VERSION = 1;

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
			String sql = "CREATE TABLE DATI ( _ID "
				+ "INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ "ID INTEGER NOT NULL, UTENTE TEXT NOT NULL," 
				+ " PASSWORD TEXT NOT NULL," 
				+ " SERVER TEXT NOT NULL,"
			    + " MAC TEXT NOT NULL);";

				Log.d("db",sql);
				db.execSQL(sql);	
			insertDati(db,"usertest","pwdtest","http://openznab.it","1122334455");
	}

	public void insertDati( SQLiteDatabase db, String a1, String a2, String a3, String a4 )
	{
		ContentValues v = new ContentValues();
		v.put("ID", 1);
		v.put("UTENTE", a1);
		v.put("PASSWORD", a2);
		v.put("SERVER", a3);
		v.put("MAC", a4);
		db.insert("DATI", null, v);
	
	}

	public void updateDati( String a1, String a2, String a3, String a4 )
	{
		String strFilter = "ID=1";
		SQLiteDatabase db = getWritableDatabase();
		ContentValues v = new ContentValues();
		v.put("UTENTE", a1);
		v.put("PASSWORD", a2);
		v.put("SERVER", a3);
		v.put("MAC", a4);
		db.update("DATI", v, strFilter, null);
				
	}
	
	public Cursor getSetting()
	{
		SQLiteDatabase db = getReadableDatabase();
		/*String[] COLUMNS = new String[]
				{ "_ID", "UTENTE", "PASSWORD", "SERVER" };
		return (getReadableDatabase().query("SETTING", COLUMNS, null, null,
				null, null, null));*/
		String selectQuery = "SELECT  * FROM DATI";
		Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		Log.d("onUpgrade","ok");
		onCreate(arg0);
	}
	
	
	public String[] getC()
	{
      String[] reso = new String[4];
      String selectQuery = "SELECT  * FROM DATI WHERE ID = 1";
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);
      if (cursor.moveToFirst()) {
          reso[0] = cursor.getString(2);   
          reso[1] = cursor.getString(3);   
          reso[2] = cursor.getString(4);   
          reso[3] = cursor.getString(5);   
      }

 	  return reso;
	}
}