package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DaetaCalendarFragment extends Fragment {

    private static String TAG = "CalendarFragment";

    private String[] storageCalendar = new String[42];
    private GregorianCalendar cal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daeta_calendar, container, false);
        init(rootView);
        return rootView;
    }

    protected void init(View rootView) {
        // ImageButton 등록
        ImageButton prevBtn, nextBtn;

        prevBtn = rootView.findViewById(R.id.daeta_prevBtn);
        nextBtn = rootView.findViewById(R.id.daeta_nextBtn);

        prevBtn.setOnClickListener(imageBtnClickEvent);
        nextBtn.setOnClickListener(imageBtnClickEvent);

        // Calendar 데이터 세팅
        cal = new GregorianCalendar();
        calendarSetting(cal, rootView);

        recyclerViewCreate(rootView);
    }

    // 버튼 이벤트 등록
    View.OnClickListener imageBtnClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.daeta_prevBtn) {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) - 1, 1);
            } else if (v.getId() == R.id.daeta_nextBtn) {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
            }

            calendarSetting(cal, getView());

            TextView timeTextView = getView().findViewById(R.id.daeta_date_text_view);
            timeTextView.setText(cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH) + 1) + "월");
        }
    };

    // RecyclerView 생성
    protected void recyclerViewCreate(View view) {
        // Recycler Calendar 생성
        RecyclerView calendarView = view.findViewById(R.id.daeta_calendar_recyclerView);
        Calendar_Recycler_Adapter calendarAdapter = new Calendar_Recycler_Adapter(requireContext(), storageCalendar);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        calendarView.setLayoutManager(layoutManager);
        calendarView.setAdapter(calendarAdapter);
    }

    // 캘린더 날짜 데이터 세팅
    protected void calendarSetting(GregorianCalendar cal, View view) {
        // 현재 날짜의 첫번째 1일
        GregorianCalendar calendar = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                1,0,0,0);

        // 저번달의 첫번째 1일
        GregorianCalendar prevCalendar = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) - 1,
                1,0,0,0 );

        // 만약 3일이 수요일이다 하면 값이 3이 반환되는데 여기서 -1를 해야 빈공간을 셀수있다.
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // 한달의 최대일 그 이후의 빈공간을 만들기 위해서 사용한다.
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;

        for (int i = 0; i < storageCalendar.length; i++) {
            if (i < week) { // 저번달의 끝의 일수을 설정
                storageCalendar[i] = Integer.toString(prevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - week + i + 1);
            } else if (i > (max + week)) { // 이번달 끝이후의 일수를 설정
                storageCalendar[i] = Integer.toString(i - (max + week));
            } else { // 이번달 일수
                storageCalendar[i] = " " + (i - week + 1) + " ";
            }
        }

        recyclerViewCreate(view);
    }
}