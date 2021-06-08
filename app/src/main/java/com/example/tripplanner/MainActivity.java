package com.example.tripplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ImageView btn_cost;
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
    ArrayList<ScheduleData> mWaterList = new ArrayList<ScheduleData>(); // 스케쥴
    // 데이터

    RbPreference pref;

    float[] graph1;
    private CalendarAdapter mCalendarAdapter;
    Calendar mThisMonthCalendar;

    Button btnClick;
    TextView time, place;


    /*private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cost = findViewById(R.id.btn_cost);

        btn_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CostActivity.class);
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
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

        /*databaseReference.child("category").orderByKey().equalTo("yourvalue").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                info = dataSnapshot.getValue(LoginActivity.class);
                InfoValue = info.getName();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        mThisMonthCalendar = Calendar.getInstance();// 캘린더 객체 얻어옮

        pref = new RbPreference(this); // 내장DB관리 클래스불러온다

        calendarView = (ViewGroup) findViewById(R.id.calendar);// 캘린더 뷰
        nowDate = (TextView) findViewById(R.id.date);// 현재 년월
        mGvCalendar = (GridView) findViewById(R.id.calendarView);// 캘린더
        // 그리드
        // 뷰
        mGvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (mDayList.get(position).isInMonth()) { // 이번달 날짜이면

                    if (mDayList.get(position).getSc_type().equals("0")) { // 메모가
                        // 없으면
                        // 메모
                        // 인서트
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
                                + "-" + monNew + "-" + daysNew;

                        memoInput();

                    } else {// 메모가 있으면 메모 보여준다

                        showmemo(mDayList.get(position).getSc_type());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "이번달 일정만 등록 가능합니다.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        new getWaterTask().execute();

        /*// 커스텀 다이얼로그에서 입력한 메시지를 출력할 TextView 를 준비한다.
        final TextView time = (TextView) findViewById(R.id.time);

        // 커스텀 다이얼로그를 호출할 버튼을 정의한다.
        Button button = (Button) findViewById(R.id.okButton);

        // 커스텀 다이얼로그 호출할 클릭 이벤트 리스너 정의
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                ScheduleDialog scheduleDialog = new ScheduleDialog(MainActivity.this);

                // 커스텀 다이얼로그를 호출한다.
                // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                scheduleDialog.callFunction(time);
            }
        });*/

    }

    String selectDate = "";

    private void memoInput() {
        AlertDialog.Builder adialog = new AlertDialog.Builder(
                MainActivity.this);

        // Set an EditText view to get user input
        final EditText input = new EditText(MainActivity.this);
        adialog.setView(input);

        adialog.setMessage("일정을 입력하세요!")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(input.getText().toString())) {
                            Toast.makeText(MainActivity.this,
                                    "일정을 입력하지 않으셨습니다!", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            dialog.dismiss();
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
        alert.setTitle("일정 입력");
        alert.show();

    }

    private void showmemo(String memo) {
        AlertDialog.Builder adialog = new AlertDialog.Builder(
                MainActivity.this);

        adialog.setMessage(memo).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        AlertDialog alert = adialog.create();
        alert.setTitle("메모 확인");
        alert.show();

    }

    private void deletememo(String memo) {
        AlertDialog.Builder adialog = new AlertDialog.Builder(
                MainActivity.this);
        adialog.setTitle("삭제 확인");
        adialog.setMessage("삭제 하시겠습니까?");

        adialog.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        adialog.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        adialog.show();
    }

    private void insertDate(String memo, String date) { // 메모 인서트
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase(); // DB를 오픈한다

        try {
            ContentValues row;
            row = new ContentValues();
            row.put("date", date);
            row.put("memo", memo);

            db.insert("sc", null, row);

        } catch (Exception e) {
            Log.e("Thread", "Insert Error", e);

        } finally {
            helper.close();
            db.close();
        }

        new getWaterTask().execute();

    }

    private void setButtonCalendar() { // 캘린더에 이전달 다음달 버튼 처리
        Button btn_pre = (Button) findViewById(R.id.btn_pre);
        btn_pre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mThisMonthCalendar.add(Calendar.MONTH, -1);
                new getWaterTask().execute();
            }
        });

        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mThisMonthCalendar.add(Calendar.MONTH, 1);
                new getWaterTask().execute();

            }
        });
    }

    private void setCalendar() { // 이번달 년 월 출력
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        nowDate.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        getCalendar(mThisMonthCalendar);
    }

    private void getCalendar(Calendar calendar) {// 이번달의 달력 값들을 세팅해준다
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH) + "");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH) + "");

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
                    false, "0");

            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) { // 이번달 날짜들을 mDayList add
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

            // 이번달 날짜중에 sc 테이블에 값이 있는 날짜가 있는지 체크한다(메모 표시용 태그)
            String count = checkDate(String.valueOf(mThisMonthCalendar
                    .get(Calendar.YEAR)) + "-" + mon + "-" + days);

            day = new DayInfo(Integer.toString(i),
                    String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1),
                    String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)),
                    true, count);

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
                    false, "0");
            mDayList.add(day);
        }

        mCalendarAdapter = new CalendarAdapter(getApplicationContext(),
                R.layout.schedule_days, mDayList, "");
        mGvCalendar.setAdapter(mCalendarAdapter); // 캘린더 어댑터를 생성해서 mGvCalendar
        // 그리드뷰에 연결해준다

        int spec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        mGvCalendar.measure(0, spec);
        mGvCalendar.getLayoutParams().height = mGvCalendar.getMeasuredHeight();

    }

    private String checkDate(String string) { // mWaterList 에는 내장DB에서 가져온 메모값이
        // 저장 되어있다(
        for (int i = 0; i < mWaterList.size(); i++) {
            if (string.equals(mWaterList.get(i).getDay())) { // 오늘날짜와 일치하면 메모 값
                // 리턴 없으면 "0" 리턴
                return mWaterList.get(i).getMemo();
            }
        }
        return "0";

    }

    private class getWaterTask extends AsyncTask<Void, Void, Void> {// 이번달의 데이터를
        // 가져온다
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(MainActivity.this, "",
                    "데이터 가져오는 중....", true);
            // 값을 가져온다
        }

        @Override
        protected Void doInBackground(Void... params) {
            mWaterList.clear();
            int mon = mThisMonthCalendar.get(Calendar.MONTH) + 1;
            String month = "";
            if (mon < 10) {
                month = "0" + mon;
            } else {
                month = Integer.toString(mon);
            }
            getMonWater(month);

            return null;
        }

        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            setButtonCalendar();
            setCalendar();
        }
    }

    private void getMonWater(String mon) {
        DBHelper helper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase(); // DB를 오픈한다

        String upSql = "SELECT  _id , date ,  memo FROM sc where strftime('%m' ,date) = '"
                + mon + "'"; // 이번달의 값들을 select해주는 쿼리

        try {
            Cursor monthCursor;
            monthCursor = db.rawQuery(upSql, null);

            while (monthCursor.moveToNext()) {
                String data = monthCursor.getString(1);
                String count = monthCursor.getString(2);

                mWaterList.add(new ScheduleData(data, count)); // 이번달의 날짜값과 메모값을
                // WaterList에
                // add해준다

            }
        } catch (Exception e) {
            Log.e("Thread", "select Error", e);

        } finally {
            helper.close();
            db.close();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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