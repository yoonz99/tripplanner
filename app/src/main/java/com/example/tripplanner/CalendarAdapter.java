//스케줄 보여주는 달력 BaseAdapter
package com.example.tripplanner;

import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {
	private ArrayList<DayInfo> mDayList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;
	Display display;

	private String mCommUserGroup;

	public CalendarAdapter(Context context, int textResource,
                           ArrayList<DayInfo> dayList, String commusergroup) { // 달력 하나하나마다 셀의 레이아웃및 값을 정해준다
		this.mContext = context;
		this.mDayList = dayList;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		display = ((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		mCommUserGroup = commusergroup;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DayInfo day = mDayList.get(position);

		DayViewHolde dayViewHolder;

		if (convertView == null) {
			convertView = mLiInflater.inflate(mResource, null); //schedule_days.xml 불러옮

			if (position % 7 == 6) {//하나의 셀 크기를 정한다
				convertView.setLayoutParams(new GridView.LayoutParams( 
						getCellWidthDP() + getRestCellWidthDP(),
						getCellHeightDP()));
			} else {
				convertView.setLayoutParams(new GridView.LayoutParams(
						getCellWidthDP(), getCellHeightDP()));
			}

			dayViewHolder = new DayViewHolde();

			dayViewHolder.llBackground = (FrameLayout) convertView
					.findViewById(R.id.day_cell_ll_background);
			dayViewHolder.tvDay = (TextView) convertView
					.findViewById(R.id.day_cell_tv_day);
			dayViewHolder.memo = (TextView) convertView
					.findViewById(R.id.memo);
			dayViewHolder.mDay_mark = (ImageView) convertView
					.findViewById(R.id.day_mark);

			convertView.setTag(dayViewHolder);
		} else {
			dayViewHolder = (DayViewHolde) convertView.getTag();
		}

		if (day != null) {

			dayViewHolder.tvDay.setText(day.getDay()); // 날짜 찍어준다
			if(day.getSc_type().equals("0")){
			}else{
				dayViewHolder.memo.setText(day.getSc_type());  //값이 있으면 스케쥴에 이미지를 표시해준다
			}
			

			if (day.isInMonth()) { // 날짜 글자색 지정
				if (position % 7 == 0) {
					dayViewHolder.tvDay.setTextColor(Color.RED);
				} else if (position % 7 == 6) {
					dayViewHolder.tvDay.setTextColor(Color.BLUE);
				} else {
					dayViewHolder.tvDay.setTextColor(Color.BLACK);
				}


			} else {
				dayViewHolder.tvDay.setTextColor(Color.GRAY);
			}

		}

		return convertView;
	}

	public class DayViewHolde {
		public FrameLayout llBackground;
		public TextView tvDay;
		public TextView day_txt;
		public ImageView mDay_mark;
		public TextView memo;
	}

	private int getCellWidthDP() {
		// int width = mContext.getResources().getDisplayMetrics().widthPixels;
		// int cellWidth = (GlobalValue.sharedStates().getDisplayWidth() - 12) /
		// 7;
		int cellWidth = (display.getWidth() - 12) / 7;

		return cellWidth;
	}

	private int getRestCellWidthDP() {
		// int width = mContext.getResources().getDisplayMetrics().widthPixels;
		// int cellWidth = (GlobalValue.sharedStates().getDisplayWidth() - 12) %
		// 7;
		int cellWidth = (display.getWidth() - 12) % 7;

		return cellWidth;
	}

	private int getCellHeightDP() {
		// int height = mContext.getResources().getDisplayMetrics().widthPixels;
		// int cellHeight = (GlobalValue.sharedStates().getDisplayWidth() - 12)
		// / 6;
		int cellHeight = (display.getWidth() - 12) / 6;

		return cellHeight;
	}

}
