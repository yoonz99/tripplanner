// 가계부 등록 엑티비티
package com.example.tripplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyInputActivity extends Activity {
	RbPreference pref; // 내장 DB 관리 클래스
	String selectDate = "";
	TextView in_out;
	TextView cate;
	EditText money;
	String moneyTypeArry[] = { "수입", "지출" };
	String moneyType = "";

	String CateArry[] = { "식비", "교통", "여가", "보험", "회비", "통신비" };
	String CateType = "";

	String CateArry1[] = { "월급", "용돈", "기타" };
	String CateType1 = "";

	String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cost);
		pref = new RbPreference(this); // 내장DB관리 클래스불러온다

		setMenu();

	}

	private void setMenu() {
		in_out = (TextView) findViewById(R.id.in_out);
		money = (EditText) findViewById(R.id.money);
		cate = (TextView) findViewById(R.id.cate);
		Intent gt = getIntent();

		type = gt.getStringExtra("type");
		if (type == null) {
			type = "";
		} else if (type.equals("in")) {
			type = "in";
			in_out.setText("수입");
			moneyType = "수입";
		} else if (type.equals("out")) {
			type = "out";
			in_out.setText("지출");
			moneyType = "지출";
		}

		in_out.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MoneyInputActivity.this);
					builder.setTitle("수입/지출 선택");
					builder.setItems(moneyTypeArry,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int pos) {
									dialog.dismiss();
									in_out.setText(moneyTypeArry[pos]);
									moneyType = moneyTypeArry[pos];
								}
							});
					AlertDialog alert = builder.create();
					alert.show();

				}

			}
		});

		cate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (moneyType.equals("지출")) {
					startActivityForResult((new Intent(MoneyInputActivity.this,
							SpendActivity.class)), 0);
				}
			}
		});

		Button btn_write = (Button) findViewById(R.id.btn_write);
		btn_write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(money.getWindowToken(), 0);

				if (moneyType.equals("")) {
					Toast.makeText(MoneyInputActivity.this,
							"수입/지출을 선택하지 않으셨습니다", Toast.LENGTH_SHORT).show();
				} else if (moneyType.equals("지출")) {
					if (CateType.equals("")) {
						Toast.makeText(MoneyInputActivity.this,
								"카테고리를 선택하지 않으셨습니다", Toast.LENGTH_SHORT).show();
					} else if (TextUtils.isEmpty(money.getText().toString())) {
						Toast.makeText(MoneyInputActivity.this, "금액이 없습니다!",
								Toast.LENGTH_SHORT).show();
					} else {
						new getWaterTask().execute();
					}

				} else if (moneyType.equals("수입")) {
					if (CateType1.equals("")) {
						Toast.makeText(MoneyInputActivity.this,
								"카테고리를 선택하지 않으셨습니다", Toast.LENGTH_SHORT).show();
					} else if (TextUtils.isEmpty(money.getText().toString())) {
						Toast.makeText(MoneyInputActivity.this, "금액이 없습니다!",
								Toast.LENGTH_SHORT).show();
					} else {
						new getWaterTask().execute();

					}
				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0 && resultCode == RESULT_OK) {
			Log.d("myLog", "data " + data.getStringExtra("tag"));
			if (data != null) {
				cate.setText(data.getStringExtra("tag"));
				CateType = data.getStringExtra("tag");

			}
		}

		else if (requestCode == 1 && resultCode == RESULT_OK) {
			if (data != null) {
				cate.setText(data.getStringExtra("tag"));
				CateType1 = data.getStringExtra("tag");

			}

		}

	}

	private class getWaterTask extends AsyncTask<Void, Void, Void> {// 이번달의 데이터를
		// 가져온다
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(MoneyInputActivity.this, "",
					"데이터 가져오는 중....", true);
			// 값을 가져온다
		}

		@Override
		protected Void doInBackground(Void... params) {
			Intent gt = getIntent();
			String date = gt.getStringExtra("date");
			insertDate("", date);

			return null;
		}

		protected void onPostExecute(Void result) {
			mProgressDialog.dismiss();
			setResult(RESULT_OK);
			finish();
		}
	}

	private void insertDate(String memo, String date) { // 가계부 인서트
		DBHelper helper = new DBHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase(); // DB를 오픈한다

		try {
			ContentValues row;
			row = new ContentValues();
			row.put("date", date);
			row.put("money", money.getText().toString());
			if (moneyType.equals("지출")) {
				row.put("cate", CateType);
				db.insert("outcome", null, row);
			} else {
				row.put("cate", CateType1);
				db.insert("income", null, row);
			}

		} catch (Exception e) {
			Log.e("Thread", "Insert Error", e);
			finish();

		} finally {
			helper.close();
			db.close();
		}

	}

}
