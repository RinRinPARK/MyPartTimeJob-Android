package com.ssuandroid.my_parttime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DaetaDialogFragment extends DialogFragment implements View.OnClickListener {
    Date curDate= new Date();
    ImageButton inputCalendar;
    NumberPicker numberPicker, numberPicker1;
    String sTime;
    TextView inputDate;
    long wage;
    String participationCode;
    EditText reasonEditText;
    String[] dTime= {"01:00","01:30", "02:00","02:30", "03:00", "03:30", "04:00", "04:30", "05:00","05:30","06:00","06:30", "07:00","07:30", "08:00","08:30", "09:00","09:30", "10:00","10:30", "11:00","11:30", "12:00","12:30", "13:00","13:30", "14:00","14:30", "15:00","15:30", "16:00", "16:30", "17:00","17:30", "18:00","18:30", "19:00","19:30", "20:00","20:30", "21:00", "21:30","22:00", "22:30", "23:00","23:30", "24:00", "00:30"};
    final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd");
    String result = dataFormat.format(curDate);
    FirebaseFirestore db;
    FirebaseUser user;

    public DaetaDialogFragment() {}//생성자
    public static DaetaDialogFragment getInstance() {
        DaetaDialogFragment d = new DaetaDialogFragment();
        return d;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_daetareg, container);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
        }

        db = FirebaseFirestore.getInstance();
        Button closeButton = (Button) v.findViewById(R.id.deatacloseButton);
        closeButton.setOnClickListener(this);
        Button inputButton = (Button) v.findViewById(R.id.deatainputButton);
        inputButton.setOnClickListener(this);

        inputCalendar = (ImageButton) v.findViewById(R.id.InputCalendarBtn);
        inputDate = v.findViewById(R.id.InputText);
        numberPicker = v.findViewById(R.id.TimeNumberPicker);
        numberPicker1= v.findViewById(R.id.TimeNumberPicker1);
        reasonEditText=v.findViewById(R.id.editTextReason);


        inputDate.setText(result);
        inputCalendar.setOnClickListener(this);
        inputDate.setOnClickListener(this);

        numberPicker.setValue(0);
        numberPicker.setMaxValue(48);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDisplayedValues(dTime);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker1.setValue(0);
        numberPicker1.setMaxValue(48);
        numberPicker1.setMinValue(0);
        numberPicker1.setWrapSelectorWheel(true);
        numberPicker1.setDisplayedValues(dTime);
        numberPicker1.setWrapSelectorWheel(false);
        numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        return v;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("DAETA_TIME_TAG");
        if (v.getId()==R.id.deatacloseButton){
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.deatainputButton){
            if (fragment!=null){
                String daetaDate = inputDate.getText().toString();
                sTime=numberPicker.getValue() + "-" + numberPicker1.getValue();
                String daetaTime =sTime;
                String reason = reasonEditText.getText().toString();
                String userId =user.getUid();
                Date date =new Date();
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy.MM.dd");
                try{
                    date=dateFormat.parse(daetaDate);
                }catch (ParseException d){d.printStackTrace();}

                participationCode="PARME456";
                Daeta daeta= new Daeta(participationCode, wage, date, daetaTime,reason, userId, null, true );
                wage=daeta.getWage();
                db.collection("Daeta").document(daeta.getParticipationCode()+" "+daeta.getDate()+" "+daeta.getTime()).set(daeta);
                Log.d("saxa", inputDate+"받음"+daetaTime);


                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.InputCalendarBtn || v.getId()==R.id.InputText)
            showDateDialog();
    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();
        try {
            curDate = dataFormat.parse(inputDate.getText().toString());
            // 문자열로 된 생년월일을 Date로 파싱
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        calendar.setTime(curDate);

        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        // 년,월,일 넘겨줄 변수

        DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.DatePickerTheme, DateSetListener,  curYear, curMonth, curDay);
        dialog.show();
    }


    private DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, month);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            // 달력의 년월일을 버튼에서 넘겨받은 년월일로 설정

            Date curDate = selectedCalendar.getTime(); // 현재를 넘겨줌
            setSelectedDate(curDate);
        }
    };

    private void setSelectedDate(Date curDate) {
        String selectedDateStr = dataFormat.format(curDate);
        inputDate.setText(selectedDateStr);
    }




}