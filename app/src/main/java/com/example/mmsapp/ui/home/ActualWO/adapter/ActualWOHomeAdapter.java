package com.example.mmsapp.ui.home.ActualWO.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.home.ActualWO.model.ActualWOHomeMaster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ActualWOHomeAdapter extends RecyclerView.Adapter<ActualWOHomeAdapter.NoteVH> {
    private List<ActualWOHomeMaster> mNoteList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ActualWOHomeAdapter(List<ActualWOHomeMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actual_wo,
                parent, false);
        return new NoteVH(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNoteList != null ? mNoteList.size() : 0;
    }

    class NoteVH extends RecyclerView.ViewHolder {
        public CardView cardwo;
        public TextView target, atno, model_wo, no, description, code_wo, name_wo;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            no = itemView.findViewById(R.id.no);
            target = itemView.findViewById(R.id.target);
            atno = itemView.findViewById(R.id.atno);
            model_wo = itemView.findViewById(R.id.model_wo);

            description = itemView.findViewById(R.id.description);
            model_wo = itemView.findViewById(R.id.model_wo);
            name_wo = itemView.findViewById(R.id.name_wo);
            code_wo = itemView.findViewById(R.id.code_wo);
            cardwo = itemView.findViewById(R.id.cardwo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, position);
                        }
                    }
                }
            });
        }

        public void bindData(ActualWOHomeMaster note) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            target.setText(formatter.format(note.getTarget()));
            atno.setText(note.getAtNo());
            model_wo.setText(note.getMdCd());
            no.setText(getAdapterPosition() + 1 + "");
            description.setText(note.getRemark());
            code_wo.setText(note.getProduct());
            name_wo.setText(note.getStyleNm());
            if(note.getProcessCount()==note.getPoRun() && note.getProcessCount()>0){
                cardwo.setCardBackgroundColor(Color.GREEN);
            }else if (note.getProcessCount()>note.getPoRun() && note.getPoRun()>0){
                cardwo.setCardBackgroundColor(Color.YELLOW);
            }else {
                cardwo.setCardBackgroundColor(Color.WHITE);
            }

        }

        public void filterList(ArrayList<ActualWOHomeMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
