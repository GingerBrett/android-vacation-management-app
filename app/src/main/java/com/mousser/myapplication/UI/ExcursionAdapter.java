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

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> excursionList;
    private final Context context;
    private final LayoutInflater inflater;
    public ExcursionAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;
    }
    public class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;


        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionItemView= itemView.findViewById(R.id.list_item_View);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Excursion current= excursionList.get(position);
                    Intent intent = new Intent(context, ExcursionDetailActivity.class);
                    intent.putExtra("id", current.getId());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("date", current.getDate());
                    intent.putExtra("vacationId", current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }

    }


    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.list_item,parent,false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(excursionList!=null){
            Excursion current = excursionList.get(position);
            String name = current.getTitle();
            holder.excursionItemView.setText(name);
        }
        else {
            holder.excursionItemView.setText("No Vacation name");
        }

    }

    @Override
    public int getItemCount() {
        if(excursionList!=null){
            return excursionList.size();
        }
        else return 0;
    }

    public void setExcursions(List<Excursion> excursions){
        excursionList = excursions;
        notifyDataSetChanged();
    }


}
