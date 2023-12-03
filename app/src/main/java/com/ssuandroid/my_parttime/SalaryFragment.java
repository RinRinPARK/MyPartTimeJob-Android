package com.ssuandroid.my_parttime;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SalaryFragment extends Fragment {
    Map<String, Integer> yearMonthWageMap = new HashMap<>();
    ArrayList<String> keySet = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseUser user;
    String uid;
    public RecyclerView recyclerViewMySalary;
    public RecyclerView.Adapter adapter_mySalary;

    public SalaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        // Calendar에서 현재 연도 정보를 얻어옴
        int currentYear = calendar.get(Calendar.YEAR);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_salary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeCloudFirestore();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        getSalaryObject();

        recyclerViewMySalary = view.findViewById(R.id.mySalaryList);

        ImageButton btn = view.findViewById(R.id.back_to_home_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_container, homeFragment)
                        .commit();
            }
        });
    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
    }

    public void getSalaryObject() {
        db.collection("Work")
                .whereEqualTo("userId", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot workDocument : task.getResult()) {
                                Work work = workDocument.toObject(Work.class);

                                // 날짜에서 연도와 월을 추출
                                String yearMonthKey = getYearMonthKey(work.getDate());

                                // yearMonthWageMap에 yearMonthKey가 있는지 확인
                                if (!yearMonthWageMap.containsKey(yearMonthKey)) {
                                    yearMonthWageMap.put(yearMonthKey, 0); // 초기화
                                    keySet.add(yearMonthKey);
                                }

                                // dayWage를 해당 연도와 월의 총 dayWage에 더함
                                int totalDayWage = (int) (yearMonthWageMap.get(yearMonthKey) + work.getDayWage());
                                yearMonthWageMap.put(yearMonthKey, totalDayWage);
                                Log.d("ymj", totalDayWage+" ");
                            }
                        } else {
                            Log.e("Firebase", "Error getting Work documents: ", task.getException());
                        }
                        ///어댑터 셋팅 : 2개의 ArrayList를 전해준다
                        adapter_mySalary = new Salary_Recycler_Adapter(keySet, yearMonthWageMap);
                        recyclerViewMySalary.setLayoutManager(new LinearLayoutManager((getActivity())));
                        recyclerViewMySalary.setAdapter(adapter_mySalary);
                    }
                });

    }
    // Date에서 연도와 월을 추출하는 메서드
    private String getYearMonthKey(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH는 0부터 시작하므로 1을 더함
        Log.d("ymj", year + " " + month);
        return year + " " + month;
    }

}