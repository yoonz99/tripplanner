package com.example.tripplanner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class CostActivity extends AppCompatActivity {
    ImageView btn_schedule;
    ImageView btn_maps;
    ImageView btn_settings;

    ViewGroup calendarView;

    Calendar cal;
    ArrayList<String> weekData = new ArrayList<String>(); // 해당주의 월~일까지
    String queryStrMon = "";
    String queryMon = "";
    GridView mGvCalendar;
    TextView nowDate;

    int myYear;
    int myMonth;
    int myDay;
    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;

    ArrayList<DayInfo> mDayList = new ArrayList<DayInfo>(); // 달력 데이터
    ArrayList<MoneyData> incomeData = new ArrayList<MoneyData>(); //예상 경비데이터
    ArrayList<MoneyData> excomeData = new ArrayList<MoneyData>(); //지출데이터


    RbPreference pref;

    float[] graph1;
    private CalendarAdapterMoney mCalendarAdapter;
    Calendar mThisMonthCalendar;
    int gridViewpos=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        btn_schedule = findViewById(R.id.btn_schedule);

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        btn_maps = findViewById(R.id.btn_maps);

        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

        btn_settings = findViewById(R.id.btn_settings);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);                overridePendingTransition(0, 0);

            }
        });
        mThisMonthCalendar = Calendar.getInstance();// 캘린더 객체 얻어옮

        pref = new RbPreference(this); // 내장DB관리 클래스불러온다

        calendarView = (ViewGroup) findViewById(R.id.calendar);// 캘린더 뷰
        nowDate = (TextView) findViewById(R.id.date);// 현재 년월
        mGvCalendar = (GridView) findViewById(R.id.calendarView);// 캘린더
        // 그리드
        // 뷰
        mGvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {//달력 날짜 클릭 시 처리
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gridViewpos = position;
                if (mDayList.get(position).isInMonth()) { // 이번 달 여행의 날짜이면
                    String daysNew = "";
                    String monNew = "";

                    int dd = Integer.parseInt(mDayList.get(position)
                            .getDay());
                    if (dd < 10) {
                        daysNew = "0" + dd;
                    } else {
                        daysNew = Integer.toString(dd);
                    }

                    int mm = mThisMonthCalendar.get(Calendar.MONTH) + 1;
                    if (mm < 10) {
                        monNew = "0" + mm;
                    } else {
                        monNew = Integer.toString(mm);
                    }

                    selectDate = mThisMonthCalendar.get(Calendar.YEAR)
                            + "-" + monNew + "-" + daysNew;//선택된 곳의 년,월,일을 가져온다


                    if (mDayList.get(position).getSc_type().equals("")) { //날짜에 내용이 없으면

                        if(costMoney.equals("0")){//예산이 입력 안되었으면
                            Toast.makeText(CostActivity.this, "이번 달 여행의 예산을 먼저 입력해 주세요!", Toast.LENGTH_SHORT).show();
                        }else{//예산이 입력 되었으면 MoneyInputActivity 이동
                            Intent gt  = new Intent(CostActivity.this , MoneyInputActivity.class);
                            gt.putExtra("date", selectDate);
                            startActivityForResult(gt, 0);
                        }



                    } else{//날짜에 내용이 있으면
                        AlertDialog.Builder adialog = new AlertDialog.Builder(CostActivity.this);
                        adialog.setMessage("메뉴를 선택하세요")
                                .setPositiveButton("내용보기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//내용 보여준다
                                        showmemo(mDayList.get(gridViewpos).getSc_type());
                                    }
                                })
                                .setNegativeButton("내용입력", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) { //MoneyInputActivity 이동
                                        dialog.cancel();
                                        Intent gt  = new Intent(CostActivity.this , MoneyInputActivity.class);
                                        gt.putExtra("date", selectDate);
                                        startActivityForResult(gt, 0);
                                    }
                                });
                        AlertDialog alert = adialog.create();
                        alert.setTitle("메뉴선택");
                        alert.show();

                    }



                } else {//이번 달 여행의 날짜가 아닐때
                    Toast.makeText(CostActivity.this, "이번 달 여행의 내용만 등록 가능합니다.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        new getWaterTask().execute();

    }

    String selectDate = "";


    private void showmemo(String memo) {//내용 보여준다
        AlertDialog.Builder adialog = new AlertDialog.Builder(
                CostActivity.this);

        adialog.setMessage(memo).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        AlertDialog alert = adialog.create();
        alert.setTitle("내용 확인");
        alert.show();

    }


    private void setButtonCalendar() { // 캘린더에 이전달 다음달 버튼 처리
        Button btn_pre = (Button) findViewById(R.id.btn_pre);
        btn_pre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mThisMonthCalendar.add(Calendar.MONTH, -1);
                costMoney="0";
                new getWaterTask().execute();
            }
        });

        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mThisMonthCalendar.add(Calendar.MONTH, 1);
                costMoney="0";
                new getWaterTask().execute();

            }
        });
    }

    private void setCalendar() { // 이번 달 여행의 년 월 출력
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        nowDate.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        getCalendar(mThisMonthCalendar);
    }

    private void getCalendar(Calendar calendar) {// 이번 달 여행의의 달력 값들을 세팅해준다
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번 달 여행의 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);

        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;

        DayInfo day = null;

        for (int i = 0; i < dayOfMonth - 1; i++) { // 지날달의 남은 일자들을 구해서 mDayList
            // add 시켜준다
            int date = lastMonthStartDay + i;
            day = new DayInfo(Integer.toString(date),
                    String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1),
                    String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)),
                    false, "");

            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) { // 이번 달 여행의 날짜들을 mDayList add
            // 시켜준다
            String days = "";
            if (i < 10) {
                days = "0" + Integer.toString(i);
            } else {
                days = Integer.toString(i);
            }
            String mon = "";
            int tempMon = mThisMonthCalendar.get(Calendar.MONTH) + 1;
            if (tempMon < 10) {
                mon = "0" + tempMon;
            } else {
                mon = Integer.toString(tempMon);
            }

            // 이번 달 여행의 날짜중에  값이 있는 날짜가 있는지 체크해서 내용을 가져온다
            String count = checkDate(String.valueOf(mThisMonthCalendar
                    .get(Calendar.YEAR)) + "-" + mon + "-" + days);

            day = new DayInfo(Integer.toString(i),
                    String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1),
                    String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)),
                    true, count); //내용을 DayInfo클래스에 생성해준다

            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) { // 다음달의
            // 시작
            // 날짜들을
            // 구해서
            // mDayList
            // add
            // 시켜준다
            day = new DayInfo(Integer.toString(i),
                    String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1),
                    String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)),
                    false, "");
            mDayList.add(day);
        }

        mCalendarAdapter = new CalendarAdapterMoney(getApplicationContext(),
                R.layout.schedule_cost, mDayList, "");
        mGvCalendar.setAdapter(mCalendarAdapter); // 캘린더 어댑터를 생성해서 mGvCalendar
        // 그리드뷰에 연결해준다

        int spec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        mGvCalendar.measure(0, spec);
        mGvCalendar.getLayoutParams().height = mGvCalendar.getMeasuredHeight();

    }

    private String checkDate(String date) {
        String text="";
        for (int i = 0; i < incomeData.size(); i++) {
            if (date.equals(incomeData.get(i).getDay())) { // 오늘날짜와 일치하면 예상 경비 값 리턴
                // 리턴 없으면 "" 리턴
                text+= "예상 경비" +"("+incomeData.get(i).getCate()+")" + incomeData.get(i).getMoney()+"\n";
            }
        }

        for (int i = 0; i < excomeData.size(); i++) {
            if (date.equals(excomeData.get(i).getDay())) { // 오늘날짜와 일치하면 지출 금액 값
                // 리턴 없으면 "" 리턴
                text+= "지출" +"("+excomeData.get(i).getCate()+")" + excomeData.get(i).getMoney()+"\n";;
            }
        }

        return text;

    }

    private class getWaterTask extends AsyncTask<Void, Void, Void> {// 이번 달 여행의의 데이터를
        // 가져온다

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 값을 가져온다
        }

        @Override
        protected Void doInBackground(Void... params) {
            incomeData.clear();
            excomeData.clear();
            int mon = mThisMonthCalendar.get(Calendar.MONTH) + 1;
            String month = "";
            if (mon < 10) {
                month = "0" + mon;
            } else {
                month = Integer.toString(mon);
            }
            getMon(month);
            getcost(month);

            return null;
        }

        protected void onPostExecute(Void result) {
            totalIn =0;
            totalEx=0;

            for(int i =0 ; i < incomeData.size(); i++){
                totalIn+=  Integer.parseInt(incomeData.get(i).getMoney());

            }

            for(int i =0 ; i < excomeData.size(); i++){
                totalEx+=  Integer.parseInt(excomeData.get(i).getMoney());
            }

            TextView mm = (TextView)findViewById(R.id.mm);//예상 경비 지출 뿌려준다
            mm.setText("예상 경비  : " +totalIn +"원 / "  + "지출  : " +totalEx +"원 "     );


            cost = (TextView)findViewById(R.id.cost);

            if(costMoney.equals("0")){ //예산 보여준다
                cost.setText("예산입력");
            }else{
                cost.setText(costMoney+"원");
            }

            cost.setOnClickListener(new View.OnClickListener() {//예산터치시

                @Override
                public void onClick(View v) {

                    if(costMoney.equals("0")){//예산이 없으면 예산을 입력한다
                        costInput();
                    }else{
                        Toast.makeText(CostActivity.this, "이번 달 여행의 예산을 이미 입력하셨습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            setButtonCalendar();//다음달 이전달 버튼 처리
            setCalendar();//캘린더 세팅
/*
            int cost = Integer.parseInt(costMoney);//지출이 예산의 70%초과인지 체크
            cost = (cost*70)/100;
            if(cost<totalEx){
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();//소리 울려줌


                AlertDialog.Builder adialog = new AlertDialog.Builder(CostActivity.this);
                adialog.setMessage("이번 달 여행의 예산 70% 초과 하셨습니다!")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {//경고창 띄워줌
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                ;
                AlertDialog alert = adialog.create();
                alert.show();
            }*/
        }
    }

    TextView cost ;


    private void costInput() {//예산입력
        AlertDialog.Builder adialog = new AlertDialog.Builder(
                CostActivity.this);

        // Set an EditText view to get user input
        final EditText input = new EditText(CostActivity.this);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        adialog.setView(input);

        adialog.setMessage("이번 달 여행의 예산을 입력하세요!")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(input.getText().toString())) {
                            Toast.makeText(CostActivity.this,
                                    "예산을 입력하지 않으셨습니다!", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            dialog.dismiss();
                            String monNew;

                            int mm = mThisMonthCalendar.get(Calendar.MONTH) + 1;
                            if (mm < 10) {
                                monNew = "0" + mm;
                            } else {
                                monNew = Integer.toString(mm);
                            }

                            String selectDate = mThisMonthCalendar.get(Calendar.YEAR)
                                    + "-" + monNew + "-" + "01";

                            insertDate(input.getText().toString(), selectDate);
                        }

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = adialog.create();
        alert.setTitle("예산 입력");
        alert.show();

    }

    private void insertDate(String money, String date) { // 예상 경비/지출 인서트
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase(); // DB를 오픈한다

        try {
            ContentValues row;
            row = new ContentValues();
            row.put("date", date);
            row.put("money", money);

            db.insert("cost", null, row);

        } catch (Exception e) {
            Log.e("Thread", "Insert Error", e);

        } finally {
            helper.close();
            db.close();
        }

        costMoney=money;
        cost.setText(costMoney+"원");


    }


    private void getMon(String mon) {//이번 달 여행의의 예상 경비/지출/데이터를 가져온다
        DBHelper helper = new DBHelper(CostActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase(); // DB를 오픈한다

        String upSql = "SELECT   date ,  money,cate  FROM income where strftime('%m' ,date) = '"
                + mon + "'"; // 이번 달 여행의의 값들을 select해주는 쿼리

        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);

            while (monthCursor.moveToNext()) {
                String date = monthCursor.getString(0);
                String money = monthCursor.getString(1);
                String cate = monthCursor.getString(2);

                incomeData.add(new MoneyData(date, money, cate));

            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        }

        String upSql1= "SELECT   date ,  money, cate  FROM outcome where strftime('%m' ,date) = '"
                + mon + "'"; // 이번 달 여행의의 값들을 select해주는 쿼리

        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql1, null);

            while (monthCursor.moveToNext()) {
                String date = monthCursor.getString(0);
                String money = monthCursor.getString(1);
                String cate = monthCursor.getString(2);

                excomeData.add(new MoneyData(date, money, cate));

            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        }
    }

    private void getcost(String mon) {//이번 달 여행의의 예산 데이터를 가져온다
        DBHelper helper = new DBHelper(CostActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase(); // DB를 오픈한다

        String upSql = "SELECT   money  FROM cost where strftime('%m' ,date) = '"
                + mon + "'"; // 이번 달 여행의의 값들을 select해주는 쿼리

        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);

            while (monthCursor.moveToNext()) {
                costMoney= monthCursor.getString(0);


            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        }

    }


    int 	totalIn =0;
    int 	totalEx =0;
    String costMoney="0";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//MoneyInputActivity에서 입력 후 호출된다
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            new getWaterTask().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(actionbar);

        /*//액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);
*/
        return true;
    }
}
