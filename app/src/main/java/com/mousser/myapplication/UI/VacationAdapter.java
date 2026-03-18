package com.mousser.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mousser.myapplication.Entites.Vacation;
import com.mousser.myapplication.R;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacationList;
    private final Context context;
    private final LayoutInflater inflater;
    public VacationAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder{
        private final TextView vacationItemView;


        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView= itemView.findViewById(R.id.list_item_View);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Vacation current= vacationList.get(position);
                    Intent intent = new Intent(context, VacationDetailActivity.class);
                    intent.putExtra("id", current.getId());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.list_item,parent,false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if(vacationList!=null){
            Vacation current = vacationList.get(position);
            String name = current.getTitle();
            holder.vacationItemView.setText(name);
        }
        else {
            holder.vacationItemView.setText("No Vacation name");
        }
    }

    @Override
    public int getItemCount() {
        if(vacationList!=null){
            return vacationList.size();
        }
        else return 0;
    }

    public void setVacations(List<Vacation> vacations){
        vacationList = vacations;
        notifyDataSetChanged();
    }
}
