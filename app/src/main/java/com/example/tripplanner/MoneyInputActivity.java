// ����� ��� ��Ƽ��Ƽ
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
	RbPreference pref; // ���� DB ���� Ŭ����
	String selectDate = "";
	TextView in_out;
	TextView cate;
	EditText money;
	String moneyTypeArry[] = { "����", "����" };
	String moneyType = "";

	String CateArry[] = { "�ĺ�", "����", "����", "����", "ȸ��", "��ź�" };
	String CateType = "";

	String CateArry1[] = { "����", "�뵷", "��Ÿ" };
	String CateType1 = "";

	String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cost);
		pref = new RbPreference(this); // ����DB���� Ŭ�����ҷ��´�

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
			in_out.setText("����");
			moneyType = "����";
		} else if (type.equals("out")) {
			type = "out";
			in_out.setText("����");
			moneyType = "����";
		}

		in_out.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type.equals("")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MoneyInputActivity.this);
					builder.setTitle("����/���� ����");
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
				if (moneyType.equals("����")) {
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
							"����/������ �������� �����̽��ϴ�", Toast.LENGTH_SHORT).show();
				} else if (moneyType.equals("����")) {
					if (CateType.equals("")) {
						Toast.makeText(MoneyInputActivity.this,
								"ī�װ��� �������� �����̽��ϴ�", Toast.LENGTH_SHORT).show();
					} else if (TextUtils.isEmpty(money.getText().toString())) {
						Toast.makeText(MoneyInputActivity.this, "�ݾ��� �����ϴ�!",
								Toast.LENGTH_SHORT).show();
					} else {
						new getWaterTask().execute();
					}

				} else if (moneyType.equals("����")) {
					if (CateType1.equals("")) {
						Toast.makeText(MoneyInputActivity.this,
								"ī�װ��� �������� �����̽��ϴ�", Toast.LENGTH_SHORT).show();
					} else if (TextUtils.isEmpty(money.getText().toString())) {
						Toast.makeText(MoneyInputActivity.this, "�ݾ��� �����ϴ�!",
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

	private class getWaterTask extends AsyncTask<Void, Void, Void> {// �̹����� �����͸�
		// �����´�
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(MoneyInputActivity.this, "",
					"������ �������� ��....", true);
			// ���� �����´�
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

	private void insertDate(String memo, String date) { // ����� �μ�Ʈ
		DBHelper helper = new DBHelper(this);
		SQLiteDatabase db = helper.getWritableDatabase(); // DB�� �����Ѵ�

		try {
			ContentValues row;
			row = new ContentValues();
			row.put("date", date);
			row.put("money", money.getText().toString());
			if (moneyType.equals("����")) {
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
