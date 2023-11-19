package com.ssuandroid.my_parttime;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class CalendarFragment extends Fragment {
    //연,월 텍스트뷰
    private TextView tvDate;
    // 그리드뷰 어댑터
    private GridAdapter gridAdapter;

    // 일 저장할 리스트
    private ArrayList<String> dayList;

    // 그리드 뷰
    private GridView gridView;
    // 캘린더 변수
    private Calendar mCal;

    FirebaseFirestore db;

    List<Map<String, Object>> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("Surin", "calendar view");
        super.onViewCreated(view, savedInstanceState);
        initializeCloudFirestore();

        tvDate = (TextView)view.findViewById(R.id.month_wage);
        gridView = (GridView)view.findViewById(R.id.gridview);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);


        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curMonthFormat.format(date)+"월 월급 달력");

        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();


        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getActivity().getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(curYearFormat.format(date)));
        cal.set(Calendar.MONTH, Integer.parseInt(curMonthFormat.format(date)));
        cal.set(Calendar.DAY_OF_MONTH, 1); // 1일로 설정
        Date startDate = cal.getTime();

        cal.add(Calendar.MONTH, 1); // 한 달 증가 (다음 달의 1일)
        cal.add(Calendar.DAY_OF_MONTH, -1); // 하루 감소 (현재 달의 마지막 날)
        Date endDate = cal.getTime();

        // 데이터 받아 오기
        List<String> branchNames = new ArrayList<>();
        List<List<String>> datesAndBranches = new ArrayList<>();

        // 종료일 (년월의 마지막 날 23:59:59)
        Calendar startDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+9"));
        startDateCalendar.set(Calendar.YEAR, Integer.parseInt(curYearFormat.format(date)));
        startDateCalendar.set(Calendar.MONTH, Integer.parseInt(curMonthFormat.format(date)));
        startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDateCalendar.set(Calendar.MINUTE, 0);
        startDateCalendar.set(Calendar.SECOND, 0);
        Date start = startDateCalendar.getTime();

        // 시작일 (년월의 1일 00:00:00)
        Calendar endDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+9"));;
        endDateCalendar.set(Calendar.YEAR, Integer.parseInt(curYearFormat.format(date)));
        endDateCalendar.set(Calendar.MONTH, Integer.parseInt(curMonthFormat.format(date))-1);
        endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
        endDateCalendar.add(Calendar.DAY_OF_MONTH, -1); // 이전 달의 마지막 날로 설정
        endDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endDateCalendar.set(Calendar.MINUTE, 59);
        endDateCalendar.set(Calendar.SECOND, 59);
        Date end = endDateCalendar.getTime();

        db.collection("Work")
                .whereEqualTo("userId", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int totalWage = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> data = new ArrayList<>();
                                Timestamp timestamp = (Timestamp) document.get("date");
                                Date timestampToDate = timestamp.toDate();
                                String dateString = timestampToDate.toString();
                                if (timestampToDate.after(end) && timestampToDate.before(start)) {
                                    String branchName = (String) document.get("branchName");
                                    totalWage += (((Number) document.get("wage")).intValue() * ((Number) document.get("workTime")).intValue());
                                    String day = null;
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                    try {
                                        Date date = dateFormat.parse(dateString);
                                        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);
                                        day = dayFormat.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (!(branchNames.contains(branchName)))  {
                                        branchNames.add(branchName);
                                    }
                                    data.add(branchName);
                                    data.add(day);
                                    datesAndBranches.add(data);
                                }
                            }

                            // 오른쪽 상단 알바 지점 라벨 추가
                            RelativeLayout storeListView = view.findViewById(R.id.store_list);
                            initializeCalendarLabel(storeListView, branchNames);

                            // 캘린더 뷰 내부에 알바 지점 라벨 추가
                            addLabelsOnCalendar(datesAndBranches, branchNames);

                            // 예상 월급 뷰에 뿌리기
                            TextView expWageNum = (TextView) view.findViewById(R.id.exp_wage_num);
                            expWageNum.setText(totalWage + "원");
                        } else {
                            Log.d("Surin", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    // 오른쪽 상단 가게 이름 라벨링 뷰
    private void initializeCalendarLabel(RelativeLayout storeListView, List<String> branchNames) {
        for (int i = 0; i <branchNames.size(); i++) {
            TextView branchTextView = null;
            if (i == 0) {
                storeListView.findViewById(R.id.store_circle1).setVisibility(View.VISIBLE);
                branchTextView = (TextView) storeListView.findViewById(R.id.store_name1);
            }
            if (i == 1) {
                storeListView.findViewById(R.id.store_circle2).setVisibility(View.VISIBLE);
                branchTextView = (TextView) storeListView.findViewById(R.id.store_name2);
            }
            if (i == 2) {
                storeListView.findViewById(R.id.store_circle3).setVisibility(View.VISIBLE);
                branchTextView = (TextView) storeListView.findViewById(R.id.store_name3);
            }
            if (i == 3) {
                storeListView.findViewById(R.id.store_circle4).setVisibility(View.VISIBLE);
                branchTextView = (TextView) storeListView.findViewById(R.id.store_name4);
            }
            if (i == 4) {
                storeListView.findViewById(R.id.store_circle5).setVisibility(View.VISIBLE);
                branchTextView = (TextView) storeListView.findViewById(R.id.store_name5);
            }
            branchTextView.setVisibility(View.VISIBLE);
            branchTextView.setText(branchNames.get(i).toString().split(" ")[0]);
        }
    }

    private void addLabelsOnCalendar(List<List<String>> datesAndBranches, List<String> branchNames) {
        for (List<String> lst: datesAndBranches) {
            String day = String.valueOf(Integer.parseInt(lst.get(1)));
            String branchName = lst.get(0);

            for (int i = 0; i < gridView.getChildCount(); i++) {
                View gridItem = gridView.getChildAt(i);

                if (gridItem != null) {
                    TextView tvItemGridView = gridItem.findViewById(R.id.tv_item_gridview);
                    if (tvItemGridView.getText().toString().equals(day)) {

                        LinearLayout iconLayout = gridItem.findViewById(R.id.iconLayout);

                        ImageView imageView = new ImageView(requireContext());
                        int idx = branchNames.indexOf(branchName);
                        if (idx == 0) {
                            imageView.setImageResource(R.drawable.oval_style1);
                        } else if (idx == 1) {
                            imageView.setImageResource(R.drawable.oval_style2);
                        } else if (idx == 2) {
                            imageView.setImageResource(R.drawable.oval_style3);
                        } else if (idx == 3) {
                            imageView.setImageResource(R.drawable.oval_style4);
                        } else if (idx == 4) {
                            imageView.setImageResource(R.drawable.oval_style5);
                        }
                        iconLayout.addView(imageView);
                    }
                }
            }
        }
    }

    // firestore 불러오는 함수
    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }

    }

    private class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.lightblue));
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

}