package com.ssuandroid.my_parttime;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlbaBranchAdapter extends RecyclerView.Adapter<AlbaBranchAdapter.ViewHolder>{

    private ArrayList<Alba> albaList;
    private MainActivity mainActivity; //어댑터에서 mainActivity의 함수 호출이 필요 -> 이 부분은 수정 예정

    public AlbaBranchAdapter(ArrayList<Alba> List, MainActivity activity){
        this.albaList = List; //HomeFragment로부터 건네받은 Alba 객체를 list 객체에 할당
        this.mainActivity = activity;
    }

    @NonNull
    @Override
    public AlbaBranchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.albaitem, parent, false); //어떤 것을 inflate해야 옳은지.. 다시 확인할 부분
        return new ViewHolder(v);
    }

    //recyclerView가 특정 행에 대한 ViewHolder을 요구
    @Override
    public void onBindViewHolder(@NonNull AlbaBranchAdapter.ViewHolder holder, int position) { //position: 몇번째 행인지?
        //매개변수로 위에서 만든 holder을 받는다. 이를 사용하여 setText 해주어야 한다.
        holder.albaName.setText(albaList.get(position).getBranchName().toString()); //db 사용시 알맞게 getter 사용할 것. 우선은 branchName만 변경
        holder.albaParticipationCode.setText(albaList.get(position).getParticipationCode().toString());
    }


    //행이 몇 개인가?
    @Override
    public int getItemCount() {
        return albaList.size();
    }



    //이름,등록일,개수 만든거 구성
    public class ViewHolder extends RecyclerView.ViewHolder { //viewHolder와 view는 1:1로 대응되며, recyclerview는 adapter이 가지고 있는
        //ViewHolder을 통해서 View를 얻을 수 있다.
        private TextView albaName;
        private TextView albaParticipationCode;

        public ViewHolder(@NonNull View itemView) { //생성자
            super(itemView);
            albaName = (TextView)itemView.findViewById(R.id.albaName);
            albaParticipationCode=(TextView)itemView.findViewById(R.id.albaParticipationCode);

            itemView.setOnClickListener(new View.OnClickListener()
                    //특정 알바를 선택하면 특정 알바의 albahomefragment로 이동함
            {
                @Override
                public void onClick(View v){
                    int pos = getAbsoluteAdapterPosition();
                    if (pos!= RecyclerView.NO_POSITION){
                        //특정 albahomefragment로 이동하도록 조절
                        mainActivity.fragmentChange(albaList.get(pos));
                    }
                }
            });
        }
    }

}
