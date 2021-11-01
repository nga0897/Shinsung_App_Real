package com.example.mmsapp.ui.status.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.status.model.ListStatus;

import java.util.List;

public class ListStatusAdapter extends RecyclerView.Adapter<ListStatusAdapter.ListStatusViewHolder> {
    private List<ListStatus> items;
    private String bbNo;
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public ListStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fm_st,
                viewGroup, false);
        ListStatusViewHolder evh = new ListStatusViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListStatusViewHolder vh, int i) {
        ListStatus currentItem = items.get(i);
        vh.v_rec_dt.setText(currentItem.getReceviceDtTims());
        vh.v_stt.setText(String.valueOf(i + 1));
        vh.v_bobbin_no.setText(bbNo);
        vh.v_qty.setText(String.valueOf(currentItem.getGrQty()));
        vh.v_qty_bf.setText(String.valueOf(currentItem.getGrQtyBf()));
        vh.v_mt_cd.setText(currentItem.getMtCd());
        vh.v_product.setText(currentItem.getProduct());
        vh.v_po_no.setText(currentItem.getPo());
        vh.v_process.setText(currentItem.getProcess());
        vh.v_type.setText(currentItem.getMtType());
        vh.v_lct_nm.setText(currentItem.getLctNm());
        vh.v_rec_dt.setText(currentItem.getLctNm());
        vh.v_staff_id.setText(currentItem.getStaffId());
        vh.v_staff_nm.setText(currentItem.getStaffNm());
        vh.v_status_nm.setText(currentItem.getMtStsNm());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ListStatusViewHolder extends RecyclerView.ViewHolder {
        public TextView v_stt, v_bobbin_no;
        public TextView v_qty, v_qty_bf;
        public TextView v_mt_cd, v_product, v_po_no, v_process, v_type, v_lct_nm, v_status_nm;
        public TextView v_rec_dt, v_staff_id, v_staff_nm;


        public ListStatusViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            v_stt = itemView.findViewById(R.id.v_stt);
            v_bobbin_no = itemView.findViewById(R.id.v_bobbin_no);
            v_qty = itemView.findViewById(R.id.v_qty);
            v_qty_bf = itemView.findViewById(R.id.v_qty_bf);
            v_mt_cd = itemView.findViewById(R.id.v_mt_cd);
            v_product = itemView.findViewById(R.id.v_product);
            v_po_no = itemView.findViewById(R.id.v_po_no);
            v_process = itemView.findViewById(R.id.v_process);
            v_type = itemView.findViewById(R.id.v_type);
            v_lct_nm = itemView.findViewById(R.id.v_lct_nm);
            v_rec_dt = itemView.findViewById(R.id.v_rec_dt);
            v_staff_id = itemView.findViewById(R.id.v_staff_id);
            v_staff_nm = itemView.findViewById(R.id.v_staff_nm);
            v_status_nm = itemView.findViewById(R.id.v_status_nm);
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
        }
    }

    public ListStatusAdapter(List<ListStatus> waitItem,String bbNo) {
        items = waitItem;
        this.bbNo = bbNo;
    }
}