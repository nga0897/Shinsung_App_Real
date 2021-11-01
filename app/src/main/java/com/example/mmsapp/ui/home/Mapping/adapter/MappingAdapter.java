package com.example.mmsapp.ui.home.Mapping.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.Constants;
import com.example.mmsapp.R;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Mapping.model.MappingMaster;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MappingAdapter extends RecyclerView.Adapter<MappingAdapter.NoteVH> {
    private List<MappingMaster> mNoteList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onQuantityChange(int position, TextView edittext);

        void onQCCheck(int position, TextView edittext);

        void onDelete(int position, ImageView imageView);

        void onClickDesc(int wmid);
    }

    public void setOnItemClickListener(MappingAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public MappingAdapter(List<MappingMaster> noteList) {
        mNoteList = noteList;
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mapping,
                parent, false);
        NoteVH evh = new NoteVH(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        holder.bindData(mNoteList.get(position),position+1);
    }

    public void addNewNote(MappingMaster note) {
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

        TextView mt_cd, num_gr_qty, num_gr_qtyreal, pqc, container, no, mtcnt, tgtao,tvDesc;
        ImageView im_delete;
        RelativeLayout nenItem2, nenItem;

        public NoteVH(View itemView, final OnItemClickListener listener) {
            super(itemView);

            //title = (TextView) itemView.findViewById(R.id.title);
            tgtao = itemView.findViewById(R.id.tgtao);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            nenItem2 = itemView.findViewById(R.id.nenItem2);
            nenItem = itemView.findViewById(R.id.nenItem);

            im_delete = itemView.findViewById(R.id.im_delete);
            no = itemView.findViewById(R.id.no);
            mt_cd = itemView.findViewById(R.id.mt_cd);
            num_gr_qty = itemView.findViewById(R.id.num_gr_qty);
            num_gr_qtyreal = itemView.findViewById(R.id.num_gr_qtyreal);
            pqc = itemView.findViewById(R.id.pqc);
            container = itemView.findViewById(R.id.container);
            mtcnt = itemView.findViewById(R.id.mtcnt);
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
            num_gr_qtyreal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onQuantityChange(position, num_gr_qtyreal);
                        }
                    }
                }
            });
            pqc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onQCCheck(position, num_gr_qtyreal);
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
                            listener.onDelete(position, im_delete);
                        }
                    }
                }
            });

            tvDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onClickDesc(position);
                        }
                    }
                }
            });
        }


        @SuppressLint("ResourceAsColor")
        public void bindData(MappingMaster note,int rowNum) {
            tgtao.setText(note.getRegDt());
            if (note.getGrQty() > 0) {
                im_delete.setVisibility(View.GONE);
            } else {
                im_delete.setVisibility(View.VISIBLE);
            }

            if (!SharedPref.getInstance().get(Constants.CHECK_LAST,String.class).equals("last")) {
                pqc.setEnabled(false);
                pqc.setBackgroundColor(Color.parseColor("#a9a9a9"));
            } else {
                pqc.setEnabled(true);
                pqc.setBackgroundColor(Color.parseColor("#009688"));
            }

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            no.setText(String.valueOf(rowNum) /*+"/"+note.het_ca*/);
            mt_cd.setText(note.getMtCd());
            num_gr_qty.setText(formatter.format(note.getSlTruNg() /*+ note.gr_qty*/));
            num_gr_qtyreal.setText(formatter.format(note.getGrQty()));
            pqc.setText(SharedPref.getInstance().get(Constants.QC_CODE,String.class));
            container.setText(note.getBbNo());
            mtcnt.setText(formatter.format(note.getCount_table2()));
            tvDesc.setText(note.getDescription());

            if (note.getEndShift() == 0) {
                nenItem.setBackgroundColor(Color.GRAY);
                nenItem2.setBackgroundColor(Color.GRAY);
                mt_cd.setBackgroundColor(Color.GRAY);
            } else {
                mt_cd.setBackgroundColor(R.color.bgTitleLeft1);
                nenItem.setBackgroundColor(Color.TRANSPARENT);
                nenItem2.setBackgroundColor(R.color.bgTitleLeft1);
            }
        }

        public void filterList(ArrayList<MappingMaster> filteredList) {
            mNoteList = filteredList;
            notifyDataSetChanged();
        }

    }

}
