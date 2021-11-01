package com.example.mmsapp.ui.status;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.R;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.ui.status.adapter.ListStatusAdapter;
import com.example.mmsapp.ui.status.adapter.MaterialAdapter;
import com.example.mmsapp.ui.status.api.DetailContainerCompositeRes;
import com.example.mmsapp.ui.status.api.GetStatusBobbinRes;
import com.example.mmsapp.ui.status.api.StatusAPI;
import com.example.mmsapp.ui.status.model.ListStatus;
import com.example.mmsapp.ui.status.model.Material;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {
    public static final String IDBOBBIN = "IDBOBBIN";

    private TextView tv_qe,tvMaterial;
    private ImageView imex,scanMaterial;
    private ProgressDialog progressDialog;
    private List<ListStatus> listStatus;
    private ListStatusAdapter adaptor;
    private RecyclerView rv_status;
    private StatusAPI apiInterface;
    private boolean isScanMaterial=false;


    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        getActivity().setTitle("Status");

        tv_qe = view.findViewById(R.id.tv_qe);
        tvMaterial = view.findViewById(R.id.tvMaterial);
        scanMaterial = view.findViewById(R.id.icScanMaterial);
        imex = view.findViewById(R.id.imex);
        rv_status = view.findViewById(R.id.rv_status);

        apiInterface = APIClient.getClient().create(StatusAPI.class);

        tvMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText(true);
            }
        });

        tv_qe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText(false);
            }
        });
        imex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScanMaterial = false;
                startQRScanner();
            }
        });

        scanMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScanMaterial = true;
                startQRScanner();
            }
        });

        return view;
    }

    private void loadJson(final String bbNo) {
        Log.d("LoadMaterialInformation", bbNo);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        Call<GetStatusBobbinRes> call = apiInterface.getStatusBobbin(bbNo);
        call.enqueue(new Callback<GetStatusBobbinRes>() {
            @Override
            public void onResponse(Call<GetStatusBobbinRes> call, retrofit2.Response<GetStatusBobbinRes> response) {
                if(response.isSuccessful()){
                    GetStatusBobbinRes res = response.body();
                    List<ListStatus>  resultList = res.getStatusList();
                    listStatus = new ArrayList<>();
                    if(resultList.isEmpty()){
                        AlertError.alertError(res.getMessage(), getActivity());
                    }else {
                        listStatus = resultList;
                        builRecyclerView(bbNo);
                    }
                    progressDialog.dismiss();

                }else {
                    progressDialog.dismiss();
                    AlertError.alertError("The server response error", getActivity());
                }
            }

            @Override
            public void onFailure(Call<GetStatusBobbinRes> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                AlertError.alertError(t.getMessage(), getActivity());
            }
        });
    }

    private void builRecyclerView(String bbNo) {
        adaptor = new ListStatusAdapter(listStatus,bbNo);
        rv_status.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_status.setHasFixedSize(true);
        rv_status.setAdapter(adaptor);
    }

    private void sendData(String conText) {
        loadJson(conText);
    }

    //
    private void inputText(final boolean isScanMaterial) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        builder.setTitle("Qr Code");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setCancelable(false);
        builder.setView(viewInflated);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String QrScan = input.getText().toString().trim();
                if (QrScan.length() > 0) {
                    if(isScanMaterial){
                        tvMaterial.setText(QrScan);
                        scanMaterialNow(QrScan);
                    }else {
                        tv_qe.setText(QrScan);
                        sendData(QrScan);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please insert QR code", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void scanMaterialNow(final String qrScan) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        Call<DetailContainerCompositeRes> call = apiInterface.detailContainerComposite(qrScan);
        call.enqueue(new Callback<DetailContainerCompositeRes>() {
            @Override
            public void onResponse(Call<DetailContainerCompositeRes> call, Response<DetailContainerCompositeRes> response) {
                if(response.isSuccessful()){
                    DetailContainerCompositeRes res = response.body();
                    if(res.isResult()){
                        Material item = res.getMaterial();
                        List<Material> materialList = new ArrayList<>();
                        materialList.add(item);
                        MaterialAdapter adaptor = new MaterialAdapter(materialList,qrScan);
                        rv_status.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_status.setHasFixedSize(true);
                        rv_status.setAdapter(adaptor);
                    }else {
                        AlertError.alertError(res.getMessage(),getContext());
                    }
                }else {
                    AlertError.alertError("The server response error",getContext());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DetailContainerCompositeRes> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                AlertError.alertError("The server response error",getContext());
            }
        });
    }

    public void startQRScanner() {
        IntentIntegrator.forSupportFragment(this).setOrientationLocked(false).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_LONG).show();
            } else {
                String QrScan = result.getContents().trim();

                if (QrScan.length() > 0) {
                    if(isScanMaterial){
                        tvMaterial.setText(QrScan);
                        scanMaterialNow(QrScan);
                    }else {
                        tv_qe.setText(QrScan);
                        sendData(QrScan);// scan
                    }
                } else {
                    Toast.makeText(getActivity(), "Please insert QR code", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
