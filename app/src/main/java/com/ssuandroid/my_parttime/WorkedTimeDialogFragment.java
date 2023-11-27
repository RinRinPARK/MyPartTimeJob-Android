package com.ssuandroid.my_parttime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.awt.font.NumericShaper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkedTimeDialogFragment extends DialogFragment implements View.OnClickListener{

    Date curDate= new Date();
    ImageButton inputCalendar;
    NumberPicker numberPicker;
    TextView inputDate;
    String[] workedTime= {"0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5", "5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5", "10.0", "10.5", "11.0", "11.5", "12.0", "12.5", "13.0", "13.5", "14.0", "14.5", "15.0", "15.5", "16.0", "16.5", "17.0", "17.5", "18.0", "18.5", "19.0", "19.5", "20.0", "20.5", "21.0", "21.5", "22.0", "22.5", "23.0", "23.5", "24.0"};
    final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd");
    String result = dataFormat.format(curDate);

    public WorkedTimeDialogFragment() {}//생성자
    public static WorkedTimeDialogFragment getInstance() {
        WorkedTimeDialogFragment e = new WorkedTimeDialogFragment();
        return e;
    }

    public interface WorkTimeFragmentInterfacer {
        void newWorkBtn(String date, String workedTime);
    }

    private WorkTimeFragmentInterfacer workTimeFragmentInterfacer;

    public void setFragmentInterfacer(WorkTimeFragmentInterfacer workTimeFragmentInterfacer){
        this.workTimeFragmentInterfacer = workTimeFragmentInterfacer;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_worked_time, container);
        Button closeButton = (Button) v.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
        Button inputButton = (Button) v.findViewById(R.id.inputButton);
        inputButton.setOnClickListener(this);

        inputCalendar = (ImageButton) v.findViewById(R.id.dateInputCalendarBtn);
        inputDate = v.findViewById(R.id.dateInputText);
        numberPicker = v.findViewById(R.id.workedTimeNumberPicker);

        inputDate.setText(result);
        inputCalendar.setOnClickListener(this);
        inputDate.setOnClickListener(this);

        numberPicker.setValue(0);
        numberPicker.setMaxValue(48);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDisplayedValues(workedTime);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        return v;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("WORKED_TIME_TAG");
        if (v.getId()==R.id.closeButton){
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.inputButton){
            if (fragment!=null){
                String workedDate = inputDate.getText().toString();
                String workedTime = Integer.toString(numberPicker.getValue());
                Log.d("ymj", inputDate+"받음"+workedTime);
                workTimeFragmentInterfacer.newWorkBtn(workedDate, workedTime);

                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.dateInputCalendarBtn || v.getId()==R.id.dateInputText)
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

        DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.DatePickerTheme, workedDateSetListener,  curYear, curMonth, curDay);
        dialog.show();
    }


    private DatePickerDialog.OnDateSetListener workedDateSetListener = new DatePickerDialog.OnDateSetListener() {
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