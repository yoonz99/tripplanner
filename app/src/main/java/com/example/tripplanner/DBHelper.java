//������ ���̽� ���� ���ִ� �� 

package com.example.tripplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

 DBHelper(Context context) {
		super(context, "Money.db", null, 1);  // Money.db ����
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		  
		  
		  String sql4 = "create table sc(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,  memo TEXT , time TEXT)";  //�������̺��� ����� (��¥, �޸�,�ð�)
		  db.execSQL(sql4);   
		  
		  String sql5 = "create table income(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,   money TEXT , cate TEXT)";  //���� ���̺��� ����� (��¥, ��, ī�װ�)
		  db.execSQL(sql5);   
		  
		  String sql6 = "create table outcome(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,   money TEXT , cate TEXT)";  //���� ���̺��� ����� (��¥, ��, ī�װ�)
		  db.execSQL(sql6);   
		  
		  String sql7 = "create table yesan(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,   money TEXT )";  //���� ���̺��� ����� (��¥, ��)
		  db.execSQL(sql7);   
		  
		
		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST sc");
		onCreate(db);

	}
}
