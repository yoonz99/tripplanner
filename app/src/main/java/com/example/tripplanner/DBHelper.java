//데이터 베이스 세팅 해주는 곳 

package com.example.tripplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

 DBHelper(Context context) {
		super(context, "Money.db", null, 1);  // Money.db 생성
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		  
		  
		  String sql4 = "create table sc(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,  memo TEXT , time TEXT)";  //일정테이블을 만든다 (날짜, 메모,시간)
		  db.execSQL(sql4);   
		  
		  String sql5 = "create table income(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,   money TEXT , cate TEXT)";  //수입 테이블을 만든다 (날짜, 돈, 카테고리)
		  db.execSQL(sql5);   
		  
		  String sql6 = "create table outcome(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,   money TEXT , cate TEXT)";  //지출 테이블을 만든다 (날짜, 돈, 카테고리)
		  db.execSQL(sql6);   
		  
		  String sql7 = "create table yesan(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		            "date  DATETIME,   money TEXT )";  //예산 테이블을 만든다 (날짜, 돈)
		  db.execSQL(sql7);   
		  
		
		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST sc");
		onCreate(db);

	}
}
