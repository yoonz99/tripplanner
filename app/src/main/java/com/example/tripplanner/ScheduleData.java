//스케줄 데이터
package com.example.tripplanner;

public class ScheduleData {
	private String day;// 날짜
	private String memo;// 스케쥴 메모

	public ScheduleData(String day, String memo) {
		super();
		this.day = day;
		this.memo = memo;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
