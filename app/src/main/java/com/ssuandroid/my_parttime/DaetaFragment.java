package com.ssuandroid.my_parttime;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DaetaFragment extends Fragment implements View.OnClickListener, DaetaDescriptionDialogFragment.DaetaInterfacer1, DaetaApplicationDialogFragment.DaetaInterfacer2 {
    Context mContext;
    public RecyclerView recyclerViewDaetaList;
    public RecyclerView.Adapter adapter_daetaList;
    FirebaseFirestore db;
    ArrayList<Daeta> daetaArrayList = new ArrayList<>();
    ArrayList<Branch> branchArrayList = new ArrayList<>(); //daeta branchname 띄우기 위해서 필요함
    FirebaseUser user;
    ImageButton daetaListBtn;
    Button dateOrderBtn;
    Button wageOrderBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_daeta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeCloudFirestore();

        getBranchObject();

        recyclerViewDaetaList = (RecyclerView) view.findViewById(R.id.daetaListRecyclerView);
        recyclerViewDaetaList.setHasFixedSize(true);
        daetaListBtn = (ImageButton) view.findViewById(R.id.daetaList_btn);
        daetaListBtn.setOnClickListener(this);

        dateOrderBtn = (Button) view.findViewById(R.id.daeta_order_date_btn);
        wageOrderBtn = (Button) view.findViewById(R.id.daeta_order_wage_btn);
        dateOrderBtn.setOnClickListener(this);
        wageOrderBtn.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
        }


    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance(); //firestore의 인스턴스를 db로 얻음
    }

    private void getBranchObject() {
        db.collection("Branch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                branchArrayList.add(document.toObject(Branch.class));
                            }
                        }
                        getDaetaObject(); //branch 다 채우면 대타 object 가져옴
                    }
                });
    }

    private void getDaetaObject() {
        db.collection("Daeta")
                .whereEqualTo("externalTF", true)
                .whereEqualTo("covered", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                daetaArrayList.add(document.toObject(Daeta.class));
                            }

                            ///어댑터 셋팅 : 2개의 ArrayList를 전해준다
                            adapter_daetaList = new DaetaListAdapter(branchArrayList, daetaArrayList, buttonClickListener);
                            recyclerViewDaetaList.setLayoutManager(new LinearLayoutManager((getActivity())));
                            recyclerViewDaetaList.setAdapter(adapter_daetaList);
                        } else {
                            Log.d("ymj", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getDateOrderedObject(){
        db.collection("Daeta")
                .whereEqualTo("externalTF", true)
                .whereEqualTo("covered", false)
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            daetaArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                daetaArrayList.add(document.toObject(Daeta.class));
                            }
                            ///어댑터 셋팅 : 2개의 ArrayList를 전해준다
                            adapter_daetaList = new DaetaListAdapter(branchArrayList, daetaArrayList, buttonClickListener);
                            recyclerViewDaetaList.setLayoutManager(new LinearLayoutManager((getActivity())));
                            recyclerViewDaetaList.setAdapter(adapter_daetaList);
                        } else {
                            Log.d("ymj", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getWageOrderedObject() {
        db.collection("Daeta")
                .whereEqualTo("externalTF", true)
                .whereEqualTo("covered", false)
                .orderBy("wage", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            daetaArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                daetaArrayList.add(document.toObject(Daeta.class));
                            }
                            ///어댑터 셋팅 : 2개의 ArrayList를 전해준다
                            adapter_daetaList = new DaetaListAdapter(branchArrayList, daetaArrayList, buttonClickListener);
                            recyclerViewDaetaList.setLayoutManager(new LinearLayoutManager((getActivity())));
                            recyclerViewDaetaList.setAdapter(adapter_daetaList);
                        } else {
                            Log.d("ymj", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.daetaList_btn){
            MyDaetaApplicationListFragment myDaetaApplicationListFragment = new MyDaetaApplicationListFragment();

            getParentFragmentManager()
                    .beginTransaction().replace(R.id.main_container, myDaetaApplicationListFragment).commit();
        }
        else if (v.getId()==R.id.daeta_order_date_btn){
            Log.d("ymj", "버튼 눌림");
            getDateOrderedObject();
            dateOrderBtn.setBackground(getResources().getDrawable(R.drawable.clicked_daetafilter_button_custom));
            wageOrderBtn.setBackground(getResources().getDrawable(R.drawable.daetafilter_button_custom));
        }
        else if (v.getId()==R.id.daeta_order_wage_btn){
            Log.d("ymj", "버튼 눌림");
            getWageOrderedObject();
            wageOrderBtn.setBackground(getResources().getDrawable(R.drawable.clicked_daetafilter_button_custom));
            dateOrderBtn.setBackground(getResources().getDrawable(R.drawable.daetafilter_button_custom));
        }
    }

    DaetaListAdapter.OnButtonClickListener buttonClickListener = new DaetaListAdapter.OnButtonClickListener() {
        @Override
        public void onButtonClick(int position, String buttonPos) {
            if (buttonPos.equals("descriptionBtn")) {
                callDescriptionDialog(position);
            } else if (buttonPos.equals("applicationBtn"))
                callApplicationDialog(position);
        }
    };

    public void callDescriptionDialog(int position){
        Daeta daeta = daetaArrayList.get(position); //대타 객체를 통째로 dialogfragment에게 보낸다
        DaetaDescriptionDialogFragment daetaDescriptionDialogFragment = new DaetaDescriptionDialogFragment(daeta, position);
        daetaDescriptionDialogFragment.setDaetaInterfacer1(this); //인터페이스로 나를 건넨다....
        daetaDescriptionDialogFragment.show(getActivity().getSupportFragmentManager(), "DAETA_DESCRIPTION_TAG");
    }

    public void callApplicationDialog(int position){
        Daeta daeta = daetaArrayList.get(position); //대타 객체를 통째로 dialogfragment에게 보낸다
        DaetaApplicationDialogFragment daetaApplicationDialogFragment = new DaetaApplicationDialogFragment(daeta, position);
        daetaApplicationDialogFragment.setDaetaInterfacer2(this);
        daetaApplicationDialogFragment.show(getActivity().getSupportFragmentManager(), "DAETA_APPLICATION_TAG");
    }

        @Override
        public void onDescriptionApplication(Daeta daeta, int position) {
            daeta.setCovered();
            daeta.setApplicantId(user.getUid());
            db.collection("Daeta").document(daeta.getParticipationCode()+" "+daeta.getDate()+" "+daeta.getTime()).set(daeta);

            makeWorkObject(daeta);

            //신청이 완료되면 토스트를 띄운다
            ToastCustom toastCustom = new ToastCustom(mContext);
            toastCustom.showToast("외부 대타 신청이 완료되었어요!");

        }

        @Override
        public void onApplication(Daeta daeta, int position) {
            daeta.setCovered(); //covered를 true로 바꾼다
            daeta.setApplicantId(user.getUid());
            db.collection("Daeta").document(daeta.getParticipationCode()+" "+daeta.getDate()+" "+daeta.getTime()).set(daeta);

            makeWorkObject(daeta);

            //신청이 완료되면 토스트를 띄운다
            ToastCustom toastCustom = new ToastCustom(mContext);
            toastCustom.showToast("외부 대타 신청이 완료되었어요!");
    };

    //대타 신청시 work에도 추가함
    public void makeWorkObject(Daeta daeta){
        //workTime (실제 일하는 시간, 예를 들어 3시간, 2.5시간 등.. ->이걸로 "일당"을 계산
        String timeRange = daeta.getTime();
        String[] parts = timeRange.split(" - ");

        // 첫 번째 시간대 13:30
        String startHour = parts[0].split(":")[0]; //13
        String startMin = parts[0].split(":")[1]; //30

        // 두 번째 시간대에서 15:00
        String endHour = parts[1].split(":")[0]; //15
        String endMin = parts[1].split(":")[1]; //00

        double workHour = Double.parseDouble(endHour)-Double.parseDouble(startHour); //2
        double workMin = Double.parseDouble(endMin)-Double.parseDouble(startMin); //-30 또는 0 또는 30
        double daetaWorkTotalTime = (workHour + workMin/60);
        if (daetaWorkTotalTime<0) daetaWorkTotalTime=-daetaWorkTotalTime; //음수 가능성 존재

        Work work = new Work(user.getUid(), daeta.getParticipationCode(), daeta.getDate(), daetaWorkTotalTime , daeta.getBranchName(), daeta.getWage(), (int)(daeta.getWage()*daetaWorkTotalTime));
        db.collection("Work").document(daeta.getApplicantId()+" "+daeta.getParticipationCode()+" "+daeta.getDate()).set(work);
    }


}


