package com.ssuandroid.my_parttime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class HourlyRateDialogFragment extends DialogFragment implements View.OnClickListener{
    private EditText editText;
    public HourlyRateDialogFragment() {}//생성자
    public static HourlyRateDialogFragment getInstance() {
            HourlyRateDialogFragment e = new HourlyRateDialogFragment();
            return e;
        }


        //homefragment에 데이터 넘겨주기 위한 인터페이스
    public interface FragmentInterfacer {
        void onWageButtonClick(long input);
      }

      private FragmentInterfacer fragmentInterfacer;

    public void setFragmentInterfacer(FragmentInterfacer fragmentInterfacer){
        this.fragmentInterfacer = fragmentInterfacer;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_hourlyrate, container);

        Button closeButton = (Button) v.findViewById(R.id.hourlyRateBtnClose);
        closeButton.setOnClickListener(this);

        Button inputButton = (Button) v.findViewById(R.id.hourlyRateBtnInput);
        inputButton.setOnClickListener(this);

        editText = (EditText) v.findViewById((R.id.hourly_rate));

        setCancelable(false); //화면 터치 시 꺼짐을 막음

        return v;
    }

//    "tag"를 통해 부모 fragment로 돌아갈 예정
    public void onClick(View v) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("WAGE_TAG");
        if (v.getId()== R.id.hourlyRateBtnClose){
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.hourlyRateBtnInput){
            if (fragment!=null){
                long input = Long.parseLong(editText.getText().toString());
                fragmentInterfacer.onWageButtonClick(input);

                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
    }
}