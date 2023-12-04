package com.ssuandroid.my_parttime;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlbaHomeFragment extends Fragment implements View.OnClickListener, WorkedTimeDialogFragment.WorkTimeFragmentInterfacer {
    //근무 목록을 띄울 recyclerView
    public RecyclerView recyclerViewWorkLog;
    //recyclerView가 필요로 하는 adapter
    public RecyclerView.Adapter adapter_workLog;
    //근무 목록을 가져올 리스트
    ArrayList<Work> workArrayList = new ArrayList<>();
    FirebaseFirestore db;
    Button workAddBtn;
    Button moveToDaetaCalendar;
    ImageButton backToHome;
    TextView albaTitle;
    TextView albaWage;
    TextView albaWorkedTime;
    TextView albaSalary;
    TextView thisMonthWorkHour;
    Alba selectedAlba;
    String selectedAlbaName;
    double totalWorkedTime = 0;

    //새로운 Work 객체 만들기 위해 사용
    String workedDate;
    double workedTime;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alba_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        initializeCloudFirestore(); //db에 firestore instance 얻어옴
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
        }

        Bundle bundle = getArguments();
        selectedAlba = (Alba) bundle.getSerializable("selectedAlba");
        selectedAlbaName = selectedAlba.getBranchName();

        getWorkObject(); //db로부터 데이터를 얻어와 albaArrayList에 세팅

        //현재 몇 월인지를 기준으로 월급으로 가져오고자 함
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 1을 더합니다.

        Date firstDayOfMonth = new Date(currentYear - 1900, currentMonth - 1, 1);
        Date lastDayOfMonth = new Date(currentYear - 1900, currentMonth, 0);

        //recyclerView 사용을 위한 초기 작업
        recyclerViewWorkLog = (RecyclerView) view.findViewById(R.id.workLogRecyclerView);
        recyclerViewWorkLog.setHasFixedSize(true);

        //버튼에 리스너 달아주기
        workAddBtn = (Button) view.findViewById(R.id.inputWorkedTimeBtn);
        moveToDaetaCalendar = (Button) view.findViewById(R.id.moveToDaetaCalendarBtn);
        backToHome = (ImageButton) view.findViewById(R.id.back_to_home_btn);
        workAddBtn.setOnClickListener(this);
        moveToDaetaCalendar.setOnClickListener(this);
        backToHome.setOnClickListener(this);

        //값을 바꿔줘야 하는 텍스트뷰 받기
        albaTitle = (TextView) view.findViewById(R.id.AlbaHome_title);
        albaWage = (TextView) view.findViewById(R.id.albaHome_HourlyRate);
        albaWorkedTime = (TextView) view.findViewById(R.id.albaHome_workedTime);
        albaSalary = (TextView) view.findViewById(R.id.albaHome_salary);
        thisMonthWorkHour = (TextView) view.findViewById(R.id.this_month_workHour);

        //값을 바로 받아 와서 바꿔줌
        albaTitle.setText(selectedAlba.getBranchName());
        albaWage.setText(Long.toString(selectedAlba.getWage()));
        thisMonthWorkHour.setText(currentMonth+"월 근무 시간:");


        // branchName을 이용하여 Work 컬렉션에서 총 일한 시간과 총 예상 월급을 계산
        db.collection("Work")
                .whereEqualTo("branchName", selectedAlba.getBranchName())
                .whereEqualTo("userId", user.getUid())
                .whereGreaterThanOrEqualTo("date", firstDayOfMonth)
                .whereLessThanOrEqualTo("date", lastDayOfMonth)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                totalWorkedTime += ((Number)document.get("workTime")).doubleValue();
                            }
                            // 계산이 완료된 후에 albaWorkedTime을 설정
                            albaWorkedTime.setText(Double.toString(totalWorkedTime));
                            albaSalary.setText (( Integer.toString ((int)(selectedAlba.getWage() * totalWorkedTime)) ));
                        } else {
                            Log.e("ymj", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void initializeCloudFirestore(){
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
    }


    //알맞은 Alba에서 workLog 가져오도록 수정 예정
    private void getWorkObject(){
        db.collection("Work")
                .whereEqualTo("branchName", selectedAlbaName)
                .whereEqualTo("userId", user.getUid() )
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                workArrayList.add(document.toObject(Work.class));
                            }

                            adapter_workLog = new WorkLogAdapter(workArrayList);
                            recyclerViewWorkLog.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerViewWorkLog.setAdapter(adapter_workLog);

                        }
                        else {
                            Log.d("ymj", "Error getting documents: ", task.getException());
                        }
                    }
                });}

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.inputWorkedTimeBtn){
            WorkedTimeDialogFragment workedTimeDialogFragment = new WorkedTimeDialogFragment();
            workedTimeDialogFragment.setFragmentInterfacer(this);
            workedTimeDialogFragment.show(getActivity().getSupportFragmentManager(), "WORKED_TIME_TAG");
        }
        else if (v.getId()==R.id.moveToDaetaCalendarBtn) {

            // daeta calendar fragment로 브랜치 이름 전달
            Bundle result = new Bundle();
            // 번들 키 값과 전달 할 데이터 입력
            result.putString("branchName", selectedAlba.getBranchName());
            result.putString("participationCode", selectedAlba.getParticipationCode());
            result.putString("wage", Long.toString(selectedAlba.getWage()));
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            // 대타캘린더로 이동 + branchName 데이터 전달
            DaetaCalendarFragment daetaCalendarFragment = new DaetaCalendarFragment();
            daetaCalendarFragment.setArguments(result);
            transaction.replace(R.id.alba_home_fragment_view, daetaCalendarFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (v.getId()==R.id.back_to_home_btn){
            //homefragment로 돌아가기
            HomeFragment homeFragment = new HomeFragment();
            getParentFragmentManager().beginTransaction()
                            .replace(R.id.main_container, homeFragment)
                                    .commit();
        }
    }

    public void newWorkBtn(String date, String workedTime) {
        this.workedDate= date;
        this.workedTime = 0.5* Double.parseDouble(workedTime);
        newWorkObject();
    }

    public void newWorkObject(){
        //임시 userId
        String userId= user.getUid();


        //workedDate를 Date 객체로 만들어주어야함.
        Date date= new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            date = dateFormat.parse(workedDate);
        } catch (ParseException e) {e.printStackTrace(); }

        int dayWage= (int) (selectedAlba.getWage() * workedTime);

        Work work = new Work(userId, selectedAlba.getParticipationCode(),date,  workedTime, selectedAlba.getBranchName(), selectedAlba.getWage(), dayWage);
        db.collection("Work").document(work.getUserId()+" "+work.getParticipationCode()+" "+work.getDate()).set(work);
    }

}