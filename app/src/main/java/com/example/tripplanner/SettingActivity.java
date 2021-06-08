package com.example.tripplanner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SettingActivity extends AppCompatActivity {
    ImageView btn_schedule;
    ImageView btn_cost;
    ImageView btn_maps;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btn_schedule = findViewById(R.id.btn_schedule);

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

        btn_cost = findViewById(R.id.btn_cost);

        btn_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });

        btn_maps = findViewById(R.id.btn_maps);

        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        TextView textView = (TextView) findViewById(R.id.share);
        textView.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View view) {
                Intent msg = new Intent(Intent.ACTION_SEND);

                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=tripplanner");
                msg.putExtra(Intent.EXTRA_TITLE, "여행 일정 어플, TripPlanner!");
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "앱을 선택해 주세요"));

            }
        });

        /*Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);*/

    }

    public void shareKakao(View v) {
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoBuilder.addText("카카오링크 테스트입니다.");

            String url = "https://blog.kakaocdn.net/dn/c5LQqg/btqF2RW4Azu/QAsNSztWLHsjlnX54nj4v1/img.png";
            kakaoBuilder.addImage(url, 160, 160);

            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");

            kakaoLink.sendMessage(kakaoBuilder, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareLine(View v) {
        try {

            String text = "line://msg/text/" + "메시지를 입력하세요";
            text = text.replaceAll("\n", "");
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.share_not_install_line), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public void shareTwitter (View v){

            try {
                String sharedText = String.format("http://twitter.com/intent/tweet?text=%s",
                        URLEncoder.encode("공유한다", "utf-8"));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharedText));
                startActivity(intent);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        public void logout(View v) {
        auth.signOut();
        finish();
            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
