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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CodeInputDialogFragment extends DialogFragment implements View.OnClickListener{

    private EditText editText;
    public CodeInputDialogFragment() {}//생성자
    public static CodeInputDialogFragment getInstance() {
        CodeInputDialogFragment e = new CodeInputDialogFragment();
        return e;
    }

    //원래 fragment에 데이터를 넘겨주기 위한 인터페이스
    public interface MyFragmentInterfacer{
        void onButtonClick(String input);
    }

    private MyFragmentInterfacer fragmentInterfacer;

    public void setFragmentInterfacer(MyFragmentInterfacer fragmentInterfacer){
        this.fragmentInterfacer = fragmentInterfacer;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialogfragment_codeinput, container);

        Button closeButton = (Button) v.findViewById(R.id.codecloseButton);
        closeButton.setOnClickListener(this);

        Button inputButton = (Button) v.findViewById(R.id.codeinputButton);
        inputButton.setOnClickListener(this);

        editText = (EditText)v.findViewById(R.id.input_code);

        setCancelable(false); //화면 터치 시 꺼짐을 막음

        return v;
    }

    public void onClick(View v) {
        if (v.getId()==R.id.codecloseButton){
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("CODE_TAG");
            if (fragment!=null){
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
        else if (v.getId()==R.id.codeinputButton){
            Fragment fragment = getParentFragmentManager().findFragmentByTag("CODE_TAG");
            if (fragment!=null){
                String input = editText.getText().toString();
                fragmentInterfacer.onButtonClick(input);

                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismiss();
            }
        }
    }


}