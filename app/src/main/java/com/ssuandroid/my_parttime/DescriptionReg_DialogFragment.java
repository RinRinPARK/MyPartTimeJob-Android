package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DescriptionReg_DialogFragment extends DialogFragment implements View.OnClickListener {

    FirebaseFirestore db;
    String branchName;
    String itemDay;
    String participationCode;
    Timestamp date;
    double workTime;
    long wage;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public DescriptionReg_DialogFragment() {
    }

    public DescriptionReg_DialogFragment(String branchName, String itemDay) {
        this.branchName = branchName;
        this.itemDay = itemDay;
    }


    public static DescriptionReg_DialogFragment getInstance() {
        DescriptionReg_DialogFragment e = new DescriptionReg_DialogFragment();
        return e;
    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeCloudFirestore();
        return inflater.inflate(R.layout.dialogfragment_descriptionreg, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        TextView name = (TextView) v.findViewById(R.id.description_name);
        TextView description = (TextView) v.findViewById(R.id.description_text);

        Button closeButton = (Button) v.findViewById(R.id.description_closeBtn);
        closeButton.setOnClickListener(this);
        Button inputButton = (Button) v.findViewById(R.id.description_regBtn);
        inputButton.setOnClickListener(this);

        setCancelable(false); //화면 터치 시 꺼짐을 막음

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
                                String day = String.valueOf(Integer.parseInt(sdf.format(timestampToDate)));
                                if (day.equals(itemDay)) {
                                    String writerId = (String) document.get("writerId");
                                    final String[] writerName = new String[1];
                                    participationCode = (String) document.get("participationCode");
                                    date = (Timestamp) document.get("date");
                                    workTime = calculateHours((String) document.get("time"));
                                    wage = (long) document.get("wage");
                                    db.collection("User")
                                            .whereEqualTo("id", writerId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            writerName[0] = (String) document.get("name");
                                                        }
                                                    }  else {
                                                        Log.d("Surin", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });

                                    name.setText(writerName[0]);
                                    description.setText((String) document.get("description"));
                                }
                            }
                        } else {
                            Log.d("Surin", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        ///////calendar에서 이 dialogfragment 띄울 때 tag 지정해서 해당 tag로 바꾸어줘야함!!
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_CALENDAR_DESCRIPTION_TAG");
        if (v.getId()==R.id.description_closeBtn){
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==(R.id.description_regBtn)){
            Log.d("Surin", "대타신청");
            Work work = new Work(user.getUid(), participationCode, date.toDate(),  workTime, branchName, wage, (int) (wage*workTime));
            db.collection("Work").document(work.getUserId()+" "+work.getParticipationCode()+" "+work.getDate()).set(work);
            Log.d("Surin", "신청완료");
            DialogFragment dialogFragment = (DialogFragment) fragment;
            dialogFragment.dismiss();
        }
    }

    // "12:00 - 13:00과 같은 time을 1로 바꿔주는 함수
    private double calculateHours(String timeString) {
        // 시간 문자열을 "-"를 기준으로 나누기
        String[] timeParts = timeString.split(" - ");

        // 시작 시간과 종료 시간 문자열 추출
        String startTimeString = timeParts[0];
        String endTimeString = timeParts[1];

        // 시작 시간과 종료 시간을 계산하여 시간 간격을 얻음
        double hours = calculateTimeDifference(startTimeString, endTimeString);

        return hours;
    }

    private double calculateTimeDifference(String startTimeString, String endTimeString) {
        // 시간 문자열을 시간 단위로 변환
        double startTime = convertTimeStringToHours(startTimeString);
        double endTime = convertTimeStringToHours(endTimeString);

        // 시간 간격 계산
        double timeDifference = endTime - startTime;

        return timeDifference;
    }

    private double convertTimeStringToHours(String timeString) {
        // 시간 문자열을 시간 단위로 변환 (예: "05:00" -> 5.0)
        String[] timeParts = timeString.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        // 시간 단위로 변환
        double hoursInDouble = hours + (double) minutes / 60;

        return hoursInDouble;
    }
}