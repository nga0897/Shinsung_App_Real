package com.example.mmsapp.ui.home.Composite.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.home.Composite.model.CompositeMaster;

import java.util.ArrayList;
import java.util.List;

public class CompositeAdapter extends RecyclerView.Adapter<CompositeAdapter.NoteVH> {
    private List<CompositeMaster> mNoteList;


    private CompositeAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void ondelete(int position);
    }

    public void setOnItemClickListener(CompositeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public CompositeAdapter(List<CompositeMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_composite,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position));
    }

    public void addNewNote(CompositeMaster note) {
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

        TextView code, start_dt, end_dt, type, no, name;
        ImageView im_delete;
    RelativeLayout nen_item;
        public NoteVH(View itemView, final CompositeAdapter.OnItemClickListener listener) {
            super(itemView);
            nen_item = itemView.findViewById(R.id.nen_item);

            im_delete = itemView.findViewById(R.id.im_delete);
            code = itemView.findViewById(R.id.code);
            start_dt = itemView.findViewById(R.id.start_dt);
            end_dt = itemView.findViewById(R.id.end_dt);
            type = itemView.findViewById(R.id.type);
            no = itemView.findViewById(R.id.no);
            name = itemView.findViewById(R.id.name);

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
            im_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.ondelete(position);
                        }
                    }
                }
            });
        }

        public void bindData(CompositeMaster note) {
            code.setText(note.getCode());
            start_dt.setText(note.getStartDt());
            end_dt.setText(note.getEndDt());
            type.setText(note.getType());
            no.setText(note.getNo());
            name.setText(note.getName());

            if (note.getEndShift().equals("QK")) {
                nen_item.setBackgroundColor(Color.GRAY);

            } else {
                nen_item.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        public void filterList(ArrayList<CompositeMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
