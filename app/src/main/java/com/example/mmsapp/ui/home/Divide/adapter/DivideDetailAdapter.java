package com.example.mmsapp.ui.home.Divide.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.home.Divide.model.DivDetailMaster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DivideDetailAdapter extends RecyclerView.Adapter<DivideDetailAdapter.NoteVH> {
    private List<DivDetailMaster> mNoteList;
    
    
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void changeQuanlity(int position);
        void changebb(int position);
    }

    public void setOnItemClickListener(DivideDetailAdapter.OnItemClickListener listener) {
        mListener = listener;
    }



    public DivideDetailAdapter(List<DivDetailMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_divide_detail,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position),position+1);
    }

    public void addNewNote(DivDetailMaster note) {
        if (!mNoteList.contains(note)) {
            mNoteList.add(note);
            notifyItemInserted(mNoteList.size());
        }
    }
    @Override
    public int getItemCount() {
        return mNoteList != null ? mNoteList.size() : 0;
    }
    class NoteVH extends RecyclerView.ViewHolder {

        TextView mt_cd,quantity,container,pqc,no;
        RelativeLayout div;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);

            no= itemView.findViewById(R.id.no);
            mt_cd = itemView.findViewById(R.id.mt_cd);
            quantity = itemView.findViewById(R.id.quantity);
            pqc = itemView.findViewById(R.id.pqc);
            container= itemView.findViewById(R.id.container);
            div = itemView.findViewById(R.id.div);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v,position);
                        }
                    }
                }
            });
//            div.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.changeQuanlity(position);
//                        }
//                    }
//                }
//            });
//            container.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.changebb(position);
//                        }
//                    }
//                }
//            });

        }


        public void bindData(DivDetailMaster note,int rowNum) {
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            no.setText(String.valueOf(rowNum));
            mt_cd.setText(note.getMtCd());
            quantity.setText(formatter.format(note.getGrQty()));
            pqc.setText(note.getMtNo());
            container.setText(note.getBbNo());
        }

        public void filterList(ArrayList<DivDetailMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
