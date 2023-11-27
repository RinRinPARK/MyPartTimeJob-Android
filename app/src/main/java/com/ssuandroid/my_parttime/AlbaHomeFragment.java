package com.ssuandroid.my_parttime;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    TextView albaTitle;
    TextView albaWage;
    TextView albaWorkedTime;
    TextView albaSalary;
    Alba selectedAlba;
    String selectedAlbaName;
    double totalWorkedTime = 0;

    //새로운 Work 객체 만들기 위해 사용
    String workedDate;
    double workedTime;

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

        Bundle bundle = getArguments();
        selectedAlba = (Alba) bundle.getSerializable("selectedAlba");
        selectedAlbaName = selectedAlba.getBranchName();

        getWorkObject(); //db로부터 데이터를 얻어와 albaArrayList에 세팅

        //recyclerView 사용을 위한 초기 작업
        recyclerViewWorkLog = (RecyclerView) view.findViewById(R.id.workLogRecyclerView);
        recyclerViewWorkLog.setHasFixedSize(true);

        //버튼에 리스너 달아주기
        workAddBtn = (Button) view.findViewById(R.id.inputWorkedTimeBtn);
        moveToDaetaCalendar = (Button) view.findViewById(R.id.moveToDaetaCalendarBtn);
        workAddBtn.setOnClickListener(this);
        moveToDaetaCalendar.setOnClickListener(this);

        //값을 바꿔줘야 하는 텍스트뷰 받기
        albaTitle = (TextView) view.findViewById(R.id.AlbaHome_title);
        albaWage = (TextView) view.findViewById(R.id.albaHome_HourlyRate);
        albaWorkedTime = (TextView) view.findViewById(R.id.albaHome_workedTime);
        albaSalary = (TextView) view.findViewById(R.id.albaHome_salary);

        //값을 바로 받아 와서 바꿔줌
        albaTitle.setText(selectedAlba.getBranchName());
        albaWage.setText(Long.toString(selectedAlba.getWage()));


        // branchName을 이용하여 Work 컬렉션에서 총 일한 시간과 총 예상 월급을 계산
        db.collection("Work")
                .whereEqualTo("branchName", selectedAlba.getBranchName())
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
        else if (v.getId()==R.id.moveToDaetaCalendarBtn){
            //대타 캘린더로 이동하기
        }
    }

    public void newWorkBtn(String date, String workedTime) {
        this.workedDate= date;
        this.workedTime = 0.5* Double.parseDouble(workedTime);
        newWorkObject();
    }

    public void newWorkObject(){
        //임시 userId
        long userId=1;


        //workedDate를 Date 객체로 만들어주어야함.
        Date date= new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            date = dateFormat.parse(workedDate);
        } catch (ParseException e) {e.printStackTrace(); }

        Work work = new Work(userId, selectedAlba.getParticipationCode(),date,  workedTime, selectedAlba.getBranchName(), selectedAlba.getWage());
        db.collection("Work").document().set(work);
    }

}