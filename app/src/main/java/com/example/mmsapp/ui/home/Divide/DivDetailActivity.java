package com.example.mmsapp.ui.home.Divide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.R;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Divide.adapter.DivideDetailAdapter;
import com.example.mmsapp.ui.home.Divide.api.DivideAPI;
import com.example.mmsapp.ui.home.Divide.model.DivDetailMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivDetailActivity extends AppCompatActivity {
    private String webUrl;
    private String mlNo ="";
    private ProgressDialog dialog;
    private TextView nodata;
    private RecyclerView recyclerView;
    private List<DivDetailMaster> divDetailMasterArrayList;
    private DivideDetailAdapter divideDetailAdapter;
    private String wmtidclick = "";
    private FloatingActionButton scan;
    private FloatingActionButton input;
    private DivideAPI apiInterface;
    private Button btnRedo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_div_detail);
        setTitle("Divide Detail");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mlNo= null;
            } else {
                mlNo= extras.getString("mlNo");
            }
        } else {
            mlNo= (String) savedInstanceState.getSerializable("mlNo");
        }

        apiInterface = APIClient.getClient().create(DivideAPI.class);

        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        recyclerView = findViewById(R.id.recyclerView);
        btnRedo = findViewById(R.id.btnRedo);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(this, R.style.AlertDialogCustom);

        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);
        getData();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(DivDetailActivity.this).initiateScan();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(DivDetailActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Alert Dialog !")
                        .setMessage("Bạn chắc chắn muốn Redo?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redoNow();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void redoNow() {
        dialog.show();
        Call<BaseMessageResponse> call = apiInterface.destroyDivide(mlNo);
        call.enqueue(new Callback<BaseMessageResponse>() {
            @Override
            public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                if(response.isSuccessful()){
                    BaseMessageResponse res = response.body();
                    if(!res.isResult()&&res.getMessage()!=null){
                        AlertError.alertError(res.getMessage(), DivDetailActivity.this);
                    }else {
                        Toast.makeText(DivDetailActivity.this,res.getMessage(),Toast.LENGTH_SHORT).show();
                        DivDetailActivity.this.finish();
                    }

                }else {
                    AlertError.alertError("The server response error", DivDetailActivity.this);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), DivDetailActivity.this);
                dialog.dismiss();
            }
        });
    }

    private void inputData() {
        final Dialog dialog = new Dialog(DivDetailActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(DivDetailActivity.this).inflate(R.layout.popup_input, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final EditText Containercode = dialog.findViewById(R.id.Containercode);
        final Button confirm = dialog.findViewById(R.id.confirm);
        final TextInputLayout h2;

        h2 = dialog.findViewById(R.id.H2);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Containercode.getText().toString() == null || Containercode.getText().toString().length() == 0) {
                    h2.setError("Please, Input here.");
                    return;
                }  else {
                    h2.setError(null);
                    saveChangeDetail(Containercode.getText().toString(),wmtidclick);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    private void getData() {
        Log.e("Divide detail", webUrl + "ActualWO/ds_mapping_sta?mt_cd=" + mlNo + "&_search=false&nd=1603168138884&rows=50&page=1&sidx=&sord=asc");
        dialog.show();
        Call<List<DivDetailMaster>> call = apiInterface.dsMappingSta(mlNo,false,"1603168138884",50,1,null,"asc");
        call.enqueue(new Callback<List<DivDetailMaster>>() {
            @Override
            public void onResponse(Call<List<DivDetailMaster>> call, Response<List<DivDetailMaster>> response) {
                if(response.isSuccessful()){
                    List<DivDetailMaster> res = response.body();
                    divDetailMasterArrayList = new ArrayList<>();
                    if (res.size() == 0) {
                        dialog.dismiss();
                        nodata.setVisibility(View.VISIBLE);
                        btnRedo.setVisibility(View.GONE);
                        return;
                    }
                    divDetailMasterArrayList = res;
                    dialog.dismiss();
                    setRecyc();

                }else {
                    nodata.setVisibility(View.VISIBLE);
                    btnRedo.setVisibility(View.GONE);
                    AlertError.alertError("The server response error", DivDetailActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<DivDetailMaster>> call, Throwable t) {
                call.cancel();
                nodata.setVisibility(View.VISIBLE);
                btnRedo.setVisibility(View.GONE);
                AlertError.alertError(t.getMessage(), DivDetailActivity.this);
                dialog.dismiss();
            }
        });
    }


    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        divideDetailAdapter = new DivideDetailAdapter(divDetailMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(divideDetailAdapter);
        divideDetailAdapter.setOnItemClickListener(new DivideDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                wmtidclick = String.valueOf(divDetailMasterArrayList.get(position).getWmtId());
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(DivDetailActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Do you want to change Bobbin Code"); //"The data you entered does not exist on the server !!!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        starScan();
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.show();
            }

            @Override
            public void changeQuanlity(int position) {
            }

            @Override
            public void changebb(int position) {


            }
        });
    }

    private void starScan() {

        if (input.getVisibility()==View.VISIBLE){
            input.setVisibility(View.GONE);
            scan.setVisibility(View.GONE);
        }else {
            input.setVisibility(View.VISIBLE);
            scan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(DivDetailActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                saveChangeDetail(result.getContents(),wmtidclick);
            }
        }
    }

    private void saveChangeDetail(String bbNo,String wmtId){
        showProgressDialog();
        Call<BaseMessageResponse> call = apiInterface.changebbDv(bbNo, wmtId);
        call.enqueue(new Callback<BaseMessageResponse>() {
            @Override
            public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                if(response.isSuccessful()){
                    BaseMessageResponse res = response.body();

                    if (!res.isResult()&&res.getMessage()!=null){
                        dialog.dismiss();
                        AlertError.alertError(res.getMessage(), DivDetailActivity.this);
                        return;
                    }
                    Toast.makeText(DivDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    getData();
                }else {
                    AlertError.alertError("The server response error", DivDetailActivity.this);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), DivDetailActivity.this);
                dialog.dismiss();
            }
        });

    }

    private void showProgressDialog(){
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

}