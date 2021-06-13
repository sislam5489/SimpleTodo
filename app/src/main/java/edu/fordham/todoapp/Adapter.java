package edu.fordham.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public interface  OnClickListener{
        void onItemClicked(int adapterPosition);
    }
    public interface  OnLongClickListener{
        void onItemLongClicked(int adapterPosition);
    }
    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener onClickListener;

    public Adapter(List<String>items, OnLongClickListener longClickListener, OnClickListener onClickListener){
        this.items = items;
        this.longClickListener = longClickListener;
        this.onClickListener = onClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvitem;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           tvitem = itemView.findViewById(android.R.id.text1);
       }

        public void bind(String item) {
           tvitem.setText(item);
           tvitem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   onClickListener.onItemClicked(getAdapterPosition());
               }
           });
           tvitem.setOnLongClickListener(new View.OnLongClickListener(){
               @Override
               public boolean onLongClick(View v) {
                   longClickListener.onItemLongClicked(getAdapterPosition());
                   return true;
               }
           });
        }
    }
}
