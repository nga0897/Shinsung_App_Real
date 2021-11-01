package com.example.mmsapp.ui.status.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.R;
import com.example.mmsapp.ui.status.model.Material;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private List<Material> materialList;
    private String materialNo;

    public MaterialAdapter(List<Material> materialList,String materialNo) {
        this.materialList = materialList;
        this.materialNo = materialNo;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_material_status, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Material item = materialList.get(position);
        holder.tvMaterialNo.setText(materialNo);
        holder.tvNo.setText(String.valueOf(position+1));
        holder.tvSDNo.setText(item.getSdNo());
        holder.tvStatus.setText(item.getStatus());
        holder.tvLocation.setText(item.getLocation());
        holder.tvProcess.setText(item.getProcess());
        holder.tvProduct.setText(item.getProduct());
        holder.tvPO.setText(item.getPo());
        holder.tvConclude.setText(item.getConclude());
        holder.tvQuantity.setText(item.getQuantity());
        holder.tvTimeMapping.setText(item.getTimeMapping());
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMaterialNo,tvNo,tvSDNo,tvStatus,tvLocation,tvProcess,tvProduct,tvPO,tvConclude,tvQuantity,tvTimeMapping;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaterialNo = itemView.findViewById(R.id.tvMaterialNo);
            tvNo = itemView.findViewById(R.id.tvNo);
            tvSDNo = itemView.findViewById(R.id.tvSDNo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvProcess = itemView.findViewById(R.id.tvProcess);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvPO = itemView.findViewById(R.id.tvPO);
            tvConclude = itemView.findViewById(R.id.tvConclude);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTimeMapping = itemView.findViewById(R.id.tvTimeMapping);
        }
    }


}