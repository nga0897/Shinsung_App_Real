package com.example.mmsapp.ui.home.QCcheck.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.home.QCcheck.model.QcCheckerLessItem;

import java.util.ArrayList;

public class QcCheckerLessAdapter extends RecyclerView.Adapter<QcCheckerLessAdapter.CheckerLessViewHolder> {
    private ArrayList<QcCheckerLessItem> items;

    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemCheck(int position);
        void onItemEditText(int position);
        void onItemButtonUp(int position);
        void onItemButtonDown(int position);
    }

    public void setOnItemClickListener(QcCheckerLessAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public QcCheckerLessAdapter(ArrayList<QcCheckerLessItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public QcCheckerLessAdapter.CheckerLessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qc_check, parent, false);
        QcCheckerLessAdapter.CheckerLessViewHolder evh = new QcCheckerLessAdapter.CheckerLessViewHolder(v, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull QcCheckerLessAdapter.CheckerLessViewHolder holder, int position) {
        QcCheckerLessItem currentItem = items.get(position);

        //holder.textView_sub_1.setText(currentItem.getStt()+".");
        holder.textView_sub_2.setText(currentItem.getTextSub());
        holder.cbCheckQ.setChecked(currentItem.isCheck());
        holder.tvSub.setText(currentItem.getSub());
        holder.tvQty.setText(currentItem.getQty());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CheckerLessViewHolder extends RecyclerView.ViewHolder {
        TextView tvSub, tvQty;
        ImageButton btUp, btDown;
        CheckBox cbCheckQ;
        TextView textView_sub_1, textView_sub_2;

        public CheckerLessViewHolder(View itemView, final QcCheckerLessAdapter.OnItemClickListener listener) {
            super(itemView);

            textView_sub_1 = itemView.findViewById(R.id.textView_sub_1);
            textView_sub_2 = itemView.findViewById(R.id.textView_sub_2);

            cbCheckQ = itemView.findViewById(R.id.checkboxCheckQ);
            tvSub = itemView.findViewById(R.id.textView_food_1);
            tvQty = itemView.findViewById(R.id.textView_food_2);
            btUp = itemView.findViewById(R.id.imgButton_up);
            btDown = itemView.findViewById(R.id.imgButton_down);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // checker
            cbCheckQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemCheck(position);
                        }
                    }
                }
            });

            //Edittext
            tvQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemEditText(position);
                        }
                    }
                }
            });

            // +++++++++++
            btUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemButtonUp(position);
                        }
                    }
                }
            });

            // ----------
            btDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemButtonDown(position);
                        }
                    }
                }
            });

        }
    }
}
