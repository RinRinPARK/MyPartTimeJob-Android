package com.ssuandroid.my_parttime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DaetaCalendarFragment extends Fragment {


    // 저장된 날짜
    private String[] StorageCalendar = new String[42];
    // 현재날짜
    GregorianCalendar cal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_daeta_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton prevBtn, nextBtn;

        prevBtn = (ImageButton) view.findViewById(R.id.daeta_prevBtn);
        nextBtn = (ImageButton) view.findViewById(R.id.daeta_nextBtn);

        prevBtn.setOnClickListener(imageBtnClickEvent);
        nextBtn.setOnClickListener(imageBtnClickEvent);

        // Calendar 데이터 세팅
        cal = new GregorianCalendar();
        CalendarSetting(cal, view);

        RecyclerViewCreate(view);
    }

    // 버튼 이벤트 등록
    View.OnClickListener imageBtnClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.prevBtn:
//                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, 1);
//                    Log.e(TAG, "이전달");
//                    break;
//                case R.id.nextBtn:
//                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
//                    Log.e(TAG, "다음달");
//                    break;
//            }

            CalendarSetting(cal, v);

            TextView timeTextView = (TextView) v.findViewById(R.id.daeta_date_text_view);
            timeTextView.setText(cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH) + 1) + "월");
        }
    };

    protected void RecyclerViewCreate(View view) {
        // Recycler Calendar 생성
        RecyclerView calendarView = view.findViewById(R.id.daeta_calendar_recyclerView);
        Calendar_Recycler_Adapter calendarAdapter = new Calendar_Recycler_Adapter(view.getContext(), StorageCalendar);

        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 7);
        calendarView.setLayoutManager(layoutManager);
        calendarView.setAdapter(calendarAdapter);
    }

    // 캘린더 날짜 데이터 세팅
    protected void CalendarSetting(GregorianCalendar cal, View view) {
        // 현재 날짜의 첫번째 1일
        GregorianCalendar calendar = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                1, 0, 0, 0);

        // 저번달의 첫번째 1일
        GregorianCalendar prevCalendar = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) - 1,
                1, 0, 0, 0);

        // 만약 3일이 수요일이다 하면 값이 3이 반환되는데 여기서 -1를 해야 빈공간을 셀수있다.
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // 한달의 최대일 그 이후의 빈공간을 만들기 위해서 사용한다.
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;

        for (int i = 0; i < StorageCalendar.length; i++) {
            if (i < week) { // 저번달의 끝의 일수을 설정
                StorageCalendar[i] = Integer.toString(prevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - week + i + 1);
            } else if (i > (max + week)) { // 이번달 끝이후의 일수를 설정
                StorageCalendar[i] = Integer.toString(i - (max + week));
            } else { // 이번달 일수
                StorageCalendar[i] = " " + (i - week + 1) + " ";
            }
        }

        RecyclerViewCreate(view);
    }
}