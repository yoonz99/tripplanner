// ����� ����  ī�װ� ���� ��Ƽ��Ƽ
package com.example.tripplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SpendActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spend);

		setMenu();

	}

	private void setMenu() {
		LinearLayout tab01 = (LinearLayout) findViewById(R.id.tab01);

		tab01.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { //�� ���ý� ī�װ� �� �Ѱ��ְ� ȭ�� ����
				Intent gt = new Intent();
				gt.putExtra("tag", "�ĺ�");
				setResult(RESULT_OK, gt);
				finish();

			}
		});

		LinearLayout tab02 = (LinearLayout) findViewById(R.id.tab02);

		tab02.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent gt = new Intent();
				gt.putExtra("tag", "����");
				setResult(RESULT_OK, gt);
				finish();

			}
		});

		LinearLayout tab03 = (LinearLayout) findViewById(R.id.tab03);

		tab03.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent gt = new Intent();
				gt.putExtra("tag", "����");
				setResult(RESULT_OK, gt);
				finish();

			}
		});
		LinearLayout tab04 = (LinearLayout) findViewById(R.id.tab04);

		tab04.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent gt = new Intent();
				gt.putExtra("tag", "����");
				setResult(RESULT_OK, gt);
				finish();

			}
		});

	}

}
