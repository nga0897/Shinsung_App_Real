package com.example.mmsapp.ui.home.Composite.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.home.Composite.model.ItemCompositeMaster;

import java.util.ArrayList;
import java.util.List;

public class ItemCompositeAdapter extends RecyclerView.Adapter<ItemCompositeAdapter.NoteVH> {
    private List<ItemCompositeMaster> mNoteList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(ItemCompositeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }



    public ItemCompositeAdapter(List<ItemCompositeMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mold,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position),position+1);
    }

    public void addNewNote(ItemCompositeMaster note) {
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

        TextView type,code,name,purpose,use,remark,no;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);
            no= itemView.findViewById(R.id.no);

            //title = (TextView) itemView.findViewById(R.id.title);
            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            purpose = itemView.findViewById(R.id.purpose);
            type = itemView.findViewById(R.id.type);
            use= itemView.findViewById(R.id.use);
            remark= itemView.findViewById(R.id.remark);
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
        }


        public void bindData(ItemCompositeMaster note,int rowNo) {
            type.setText(note.getMcType());
            code.setText(note.getBarcode());
            name.setText(note.getMcNm());
            purpose.setText(note.getPurpose());
            use.setText(note.getUsing());
            remark.setText(note.getRemark());
            no.setText(String.valueOf(rowNo));

        }

        public void filterList(ArrayList<ItemCompositeMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
