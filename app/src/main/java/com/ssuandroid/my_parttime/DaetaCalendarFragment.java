package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DaetaCalendarFragment extends Fragment {

    private String[] storageCalendar = new String[42];
    private GregorianCalendar cal;
    private String branchName;

    FirebaseFirestore db;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daeta_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            branchName = getArguments().getString("branchName");
        }
        TextView storeNameView = view.findViewById(R.id.daeta_store_name);
        storeNameView.setText(branchName);
        initializeCloudFirestore();
        init(view);
        recyclerView = view.findViewById(R.id.daeta_calendar_recyclerView);
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
        Date[] result = calendarSetting(cal, rootView);

        recyclerViewCreate(rootView, result[0], result[1]);
    }

    // 버튼 이벤트 등록
    View.OnClickListener imageBtnClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.daeta_prevBtn) {
                cal.add(Calendar.MONTH, -1);
            } else if (v.getId() == R.id.daeta_nextBtn) {
                cal.add(Calendar.MONTH, 1);
            }

            calendarSetting(cal, getView());

            TextView timeTextView = getView().findViewById(R.id.daeta_date_text_view);
            timeTextView.setText(new SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(cal.getTime()));
        }
    };

    // RecyclerView 생성
    protected void recyclerViewCreate(View view, Date start, Date end) {
        // Recycler Calendar 생성
        RecyclerView calendarView = view.findViewById(R.id.daeta_calendar_recyclerView);
        Calendar_Recycler_Adapter calendarAdapter = new Calendar_Recycler_Adapter(requireContext(), storageCalendar);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        calendarView.setLayoutManager(layoutManager);
        calendarView.setAdapter(calendarAdapter);
        
        // 여기에 daetaSetting을 놔야 계속 디비에서 새롭게 불러옴
        daetaSetting(start, end);
    }

    // firestore 불러오는 함수
    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    // 캘린더 날짜 데이터 세팅
    protected Date[] calendarSetting(GregorianCalendar cal, View view) {
        // 해당 월의 1일로 설정
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // 한달의 최대일
        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 해당 월의 시작 요일 (1일이 무슨 요일인지, 일요일: 1, 월요일: 2, ..., 토요일: 7)
        int startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // 이전 달의 마지막 주의 시작 요일 계산
        int startDayOfPrevMonth = (startDayOfWeek - Calendar.SUNDAY + 7) % 7;

        // db에서 날짜별로 split하기 위한 이전 달 마지막 날, 다음 달 첫 날
        Date[] result = new Date[2];

        // 이전 달의 마지막 날짜 구하기
        GregorianCalendar start = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                1
        );
        start.add(Calendar.DAY_OF_MONTH, -1);

        // 다음 달의 첫 날 구하기
        Date end = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                1
        ).getTime();

        result[0] = start.getTime();
        result[1] = end;

        // 날짜 초기화
        storageCalendar = new String[42];

        // 이번 달의 날짜 표시
        for (int i = 0; i < max; i++) {
            storageCalendar[startDayOfPrevMonth + i] = " " + (i + 1) + " ";
        }

        recyclerViewCreate(view, result[0], result[1]);
        return result;
    }

    // daeta정보 디비에서 불러와 세팅하는 함수
    protected void daetaSetting(Date start, Date end) {

        HashMap<String, Boolean> datas = new HashMap<>();

        db.collection("Daeta")
                .whereEqualTo("branchName", branchName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Timestamp timestamp = (Timestamp) document.get("date");
                                Date timestampToDate = timestamp.toDate();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                                String day = sdf.format(timestampToDate);
                                if (timestampToDate.after(start) && timestampToDate.before(end)) {
                                    Boolean covered = (Boolean) document.get("covered");
                                    // 구인 중이 하나라도 있으면 그 날짜는 구인 중 라벨로 표시
                                    if (datas.containsKey(day)) {
                                        if (covered == false) {
                                            datas.put(day, false);
                                        }
                                    } else {
                                        datas.put(day, covered);
                                    }
                                }
                            }
                            addDaetaLabelsOnCalendar(datas);
                        } else {
                            Log.d("Surin", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    protected void addDaetaLabelsOnCalendar(HashMap<String, Boolean> datas) {
            for (Map.Entry<String, Boolean> entry : datas.entrySet()) {
                // 날짜 받아 오기
                int day = Integer.parseInt(entry.getKey());
                // 구인 중, 구인 완료 여부 받아 오기
                Boolean value = entry.getValue();

                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View recyclerItem = recyclerView.getChildAt(i);

                    if (recyclerItem != null) {
                        TextView tvItemGridView = recyclerItem.findViewById(R.id.day_recycler_calendar);
                        int itemDay = 0;

                        try {
                            itemDay = Integer.parseInt(tvItemGridView.getText().toString().trim());
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        if (itemDay == day) {
                            ImageView imageView = recyclerItem.findViewById(R.id.image_daeta_recycler_calendar);
                            imageView.setVisibility(View.VISIBLE);

                            // 구인 중일 때
                            if (value == false) {
                                imageView.setImageResource(R.drawable.oval_style1);
                            }
                            // 구인 완료 일 때
                            else {
                                imageView.setImageResource(R.drawable.oval_style5);
                            }
                        }
                    }
                }
            }
    }
}
