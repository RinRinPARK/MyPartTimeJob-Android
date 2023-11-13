package com.ssuandroid.my_parttime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button newAlbaBtn = (Button) view.findViewById(R.id.PlusButton);
        newAlbaBtn.setOnClickListener(this);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.PlusButton){
            Log.d("ymj", "here");
            CodeInputDialogFragment codeInputDialogFragment = new CodeInputDialogFragment();
            codeInputDialogFragment.show(getActivity().getSupportFragmentManager(),"HOME_TAG");
        }
    }
}