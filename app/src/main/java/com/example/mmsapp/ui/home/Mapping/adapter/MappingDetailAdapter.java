package com.example.mmsapp.ui.home.Mapping.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.home.Mapping.model.MappingDetailMaster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MappingDetailAdapter extends RecyclerView.Adapter<MappingDetailAdapter.NoteVH> {
    private List<MappingDetailMaster> mNoteList;
    private boolean isEndShift;
    
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onR(int position, TextView edittext);
        void onF(int position, TextView edittext);
        void ondelete(int position, TextView edittext);


    }

    public void setOnItemClickListener(MappingDetailAdapter.OnItemClickListener listener) {
        mListener = listener;
    }



    public MappingDetailAdapter(List<MappingDetailMaster> noteList,boolean isEndShift) {
        mNoteList = noteList;
        this.isEndShift = isEndShift;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mapping_detail,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position),position+1);
    }

    public void addNewNote(MappingDetailMaster note) {
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

        TextView mt_cd,gr_qty,use_yn,Used,Remain,no,mt_no,bb_no,desc;
        TextView r,f,delete;
        TextView mapping_dt;
        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            mapping_dt= itemView.findViewById(R.id.mapping_dt);

            no= itemView.findViewById(R.id.no);
            mt_cd = itemView.findViewById(R.id.mt_cd);
            gr_qty = itemView.findViewById(R.id.gr_qty);
            use_yn = itemView.findViewById(R.id.use_yn);
            Used = itemView.findViewById(R.id.Used);
            Remain= itemView.findViewById(R.id.Remain);
            mt_no= itemView.findViewById(R.id.mt_no);
            bb_no= itemView.findViewById(R.id.bb_no);
            desc= itemView.findViewById(R.id.tvDesc);
            r= itemView.findViewById(R.id.r);
            f= itemView.findViewById(R.id.f);
            delete= itemView.findViewById(R.id.delete);

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
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(mNoteList.get(position).getUseYn().equals("Y")){
                        if (listener != null) {
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onR(position,r);
                            }
                        }
                    }
                }
            });
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEndShift){
                        return;
                    }
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onF(position,f);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEndShift){
                        return;
                    }
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.ondelete(position,delete);
                        }
                    }
                }
            });
        }


        public void bindData(MappingDetailMaster note,int rowNum) {

            mapping_dt.setText(note.getMappingDt());
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            no.setText(String.valueOf(rowNum));
            mt_cd.setText(note.getMtCd());
            use_yn.setText(note.getUseYn());
            //gr_qty.setText(formatter.format(note.gr_qty));
            Used.setText(formatter.format(note.getUsed()));
            if (String.valueOf(note.getRemain()) == null){
                Remain.setText("0");
                gr_qty.setText(formatter.format(note.getGrQty()));
            }else {
                Remain.setText(formatter.format(note.getRemain()));
                gr_qty.setText(formatter.format(note.getGrQty() + note.getRemain()));
            }
            mt_no.setText(note.getMtNo());
            desc.setText(note.getDesc());
            bb_no.setText(note.getBbNo());
            if (note.getUseYn().equals("Y")){
                delete.setEnabled(true);
                delete.setBackgroundColor(Color.parseColor("#ff1b1b"));
                r.setEnabled(true);
                r.setBackgroundColor(Color.parseColor("#009688"));
            } else {
                delete.setEnabled(false);
                delete.setBackgroundColor(Color.parseColor("#A39F9F"));
                r.setEnabled(false);
                r.setBackgroundColor(Color.parseColor("#C6BABA"));
            }
            /*if(isEndShift){
                delete.setEnabled(false);
                delete.setBackgroundColor(Color.parseColor("#C6BABA"));
                r.setEnabled(false);
                r.setBackgroundColor(Color.parseColor("#C6BABA"));
                if(note.getUseYn().equals("Y")){
                    f.setEnabled(false);
                    f.setBackgroundColor(Color.parseColor("#ff1b1b"));
                }else {
                    f.setEnabled(false);
                    f.setBackgroundColor(Color.parseColor("#C6BABA"));
                }
            }*/
        }

        public void filterList(ArrayList<MappingDetailMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
