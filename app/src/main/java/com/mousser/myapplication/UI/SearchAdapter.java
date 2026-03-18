package com.mousser.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mousser.myapplication.Entites.Excursion;
import com.mousser.myapplication.Entites.Vacation;
import com.mousser.myapplication.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Vacation> vacationList;
    private final Context context;
    private final LayoutInflater inflater;
    public SearchAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;
    }
    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView searchItemView;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            searchItemView = itemView.findViewById(R.id.list_item_View);

        }

    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.list_item,parent,false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        if(vacationList!=null){
            Vacation current = vacationList.get(position);
            String name = current.getTitle();
            holder.searchItemView.setText(name);
        }
        else {
            holder.searchItemView.setText("No Vacation name");
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
