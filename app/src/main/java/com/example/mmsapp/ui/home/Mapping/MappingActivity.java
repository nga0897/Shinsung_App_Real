package com.example.mmsapp.ui.home.Mapping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.R;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Mapping.adapter.MappingAdapter;
import com.example.mmsapp.ui.home.Mapping.apiInterface.MappingAPI;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.CheckUpdateGrtyRes;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.GetMtDateWebRes;
import com.example.mmsapp.ui.home.Mapping.model.MappingMaster;
import com.example.mmsapp.ui.home.QCcheck.QCCheckActivity;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.InsertwMaterialRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MappingActivity extends AppCompatActivity {

    String webUrl;
    String idActual;
    int page = 1;
    private ProgressDialog progressDialog;
    TextView nodata;
    List<MappingMaster> mappingMasterArrayList;
    MappingAdapter mappingAdapter;
    int total = -1;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    FloatingActionButton scan;
    FloatingActionButton input;
    MappingAPI apiInterface;
    private String product;
    private String styleName;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        setTitle("Mapping");
        apiInterface = APIClient.getClient().create(MappingAPI.class);
        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        product = SharedPref.getInstance().get(Constants.PRODUCT,String.class);
        idActual = SharedPref.getInstance().get(Constants.ID_ACTUAL,String.class);
        styleName = SharedPref.getInstance().get(Constants.STYLE_NAME,String.class);
        recyclerView = findViewById(R.id.recyclerView);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(MappingActivity.this, R.style.AlertDialogCustom);

        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getVisibility()==View.VISIBLE){
                    input.setVisibility(View.GONE);
                    scan.setVisibility(View.GONE);
                }else {
                    input.setVisibility(View.VISIBLE);
                    scan.setVisibility(View.VISIBLE);
                }

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBobbin();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }
    private void inputData() {

        final Dialog dialog = new Dialog(MappingActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(MappingActivity.this).inflate(R.layout.popup_input, null);
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
                    creatingMLno(Containercode.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void getData(int pages,final boolean isLoadMore) {
//        new getData().execute(webUrl + "ActualWO/getmt_date_web?id_actual=" + id_actual +
//        "&page=" + page + "&rows=50&sidx=&sord=asc&_search=false&mc_type=&mc_no=&mc_nm=");
       showProgressDialog();
        Call<GetMtDateWebRes> call = apiInterface.getMtDateWeb(idActual,pages,50,null,"asc",false,null,null,null);
       call.enqueue(new Callback<GetMtDateWebRes>() {
           @Override
           public void onResponse(Call<GetMtDateWebRes> call, Response<GetMtDateWebRes> response) {
               if(response.isSuccessful()){
                GetMtDateWebRes res = response.body();
                List<MappingMaster> resultList = res.getMappingMasterList();
                   if (resultList.size() == 0) {
                       progressDialog.dismiss();
                       nodata.setVisibility(View.VISIBLE);
                       return;
                   }
                   total = res.getTotal();
                   page = res.getPage();

                   if(isLoadMore){
                       mappingMasterArrayList.addAll(resultList);
                       mappingAdapter.notifyDataSetChanged();
                       progressDialog.dismiss();
                   }else {
                       mappingMasterArrayList = new ArrayList<>();
                       mappingMasterArrayList = resultList;
                       progressDialog.dismiss();
                       setRecyc();
                   }


               }else {
                   nodata.setVisibility(View.VISIBLE);
                   AlertError.alertError("The server response error", MappingActivity.this);
                   progressDialog.dismiss();
               }
           }

           @Override
           public void onFailure(Call<GetMtDateWebRes> call, Throwable t) {
                call.cancel();
               nodata.setVisibility(View.VISIBLE);
               AlertError.alertError(t.getMessage(), MappingActivity.this);
               progressDialog.dismiss();
           }
       });
        Log.e("mapping", webUrl + "ActualWO/getmt_date_web?id_actual=" + idActual + "&page=" +
                page + "&rows=50&sidx=&sord=asc&_search=false&mc_type=&mc_no=&mc_nm=");
    }

    private void showProgressDialog(){
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MappingActivity.this);
        mappingAdapter = new MappingAdapter(mappingMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mappingAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == mappingMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getaddData(page + 1);
                    }
                }
            }
        });

        mappingAdapter.setOnItemClickListener(new MappingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedPref.getInstance().put(Constants.ML_NO,mappingMasterArrayList.get(position).getMtCd());
                Intent intent = new Intent(MappingActivity.this, MappingDetailActivity.class);
                intent.putExtra("isEndShift",mappingMasterArrayList.get(position).getEndShift() ==0);
                startActivity(intent);
            }

            @Override
            public void onQuantityChange(int position, TextView edittext) {
                if(mappingMasterArrayList.get(position).getEndShift() ==0){
                    Toast.makeText(MappingActivity.this, "Hết Ca !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                inputnum(position);
            }

            @Override
            public void onQCCheck(int position, TextView edittext) {
                if(mappingMasterArrayList.get(position).getEndShift() ==0){
                    Toast.makeText(MappingActivity.this, "Hết Ca !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mappingMasterArrayList.get(position).getGrQty() == 0){
                    AlertError.alertError("Number gr_qty = 0, please check again", MappingActivity.this);
                }else {
                    SharedPref.getInstance().put(Constants.ML_NO,mappingMasterArrayList.get(position).getMtCd());
                    SharedPref.getInstance().put(Constants.NUM_GR_QTY,mappingMasterArrayList.get(position).getGrQty());
                    Intent intent = new Intent(MappingActivity.this, QCCheckActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onDelete(final int position, ImageView imageView) {
                if(mappingMasterArrayList.get(position).getEndShift() ==0){
                    Toast.makeText(MappingActivity.this, "Hết Ca !!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MappingActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Are you sure Delete: " + mappingMasterArrayList.get(position).getMtCd());
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("onDelete", webUrl + "ActualWO/Xoa_mt_pp_composite?id=" + mappingMasterArrayList.get(position).getWmtId());
//                        new onDelete().execute(webUrl + "ActualWO/Xoa_mt_pp_composite?id=" + mappingMasterArrayList.get(position).getWmtId());
                        Call<BaseMessageResponse> call = apiInterface.deleteMtPpComposite(mappingMasterArrayList.get(position).getWmtId());
                        call.enqueue(new Callback<BaseMessageResponse>() {
                            @Override
                            public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                                if(response.isSuccessful()){
                                    BaseMessageResponse res = response.body();
                                    if (res.isResult()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(MappingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                        startActivity(getIntent());
                                    } else {
                                        progressDialog.dismiss();
                                        AlertError.alertError("Delete error. Please check again.", MappingActivity.this);
                                    }
                                }else {
                                    AlertError.alertError("The server response error", MappingActivity.this);
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                                call.cancel();
                                AlertError.alertError(t.getMessage(), MappingActivity.this);
                                progressDialog.dismiss();
                            }
                        });
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
            public void onClickDesc(int pos) {
                updateDescriptionWMaterialInfo(pos);
            }
        });


    }

    private void inputnum(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MappingActivity.this);
        View viewInflated = LayoutInflater.from(MappingActivity.this).inflate(R.layout.number_input_layout, null);
        builder.setTitle("Input Quantity Reality");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (input.getText().toString() == null) {
                    dialog.dismiss();
                } else if (input.getText().toString() == "0") {
                    dialog.dismiss();
                } else if (input.getText().toString().length() == 0) {
                    dialog.dismiss();
                } else {
                    showProgressDialog();
                    Call<CheckUpdateGrtyRes> call = apiInterface.checkUpdateGrty(mappingMasterArrayList.get(pos).getMtCd(),input.getText().toString(),mappingMasterArrayList.get(pos).getWmtId());
                    call.enqueue(new Callback<CheckUpdateGrtyRes>() {
                        @Override
                        public void onResponse(Call<CheckUpdateGrtyRes> call, Response<CheckUpdateGrtyRes> response) {
                            if(response.isSuccessful()){
                                CheckUpdateGrtyRes res = response.body();
                                if (res.isResult()) {
                                    Toast.makeText(MappingActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
//                                    startActivity(getIntent());
                                    /*Cheat by PhayTran-210603-accepted by Ms.Nga*/
                                    mappingMasterArrayList.get(pos).setGrQty(Integer.parseInt(input.getText().toString()));
                                    setRecyc();
                                } else {
                                    AlertError.alertError(res.getMessage(), MappingActivity.this);
                                }

                            }else {
                                AlertError.alertError("The server response error", MappingActivity.this);
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<CheckUpdateGrtyRes> call, Throwable t) {
                            call.cancel();
                            AlertError.alertError(t.getMessage(), MappingActivity.this);
                            progressDialog.dismiss();
                        }
                    });

                    Log.e("ChangeNumber",webUrl + "ActualWO/check_update_grty?mt_cd=" +
                            mappingMasterArrayList.get(pos).getMtCd() +
                            "&value=" +
                            input.getText().toString() +
                            "&wmtid=" +
                            mappingMasterArrayList.get(pos).getWmtId());

                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void getaddData(int page) {
        getData(page,true);
    }

    // open scan qr code
    private void scanBobbin() {
        new IntentIntegrator(this).initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MappingActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                creatingMLno(result.getContents());
            }
        }
    }

    private  void updateDescriptionWMaterialInfo(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(MappingActivity.this);
        View viewInflated = LayoutInflater.from(MappingActivity.this).inflate(R.layout.description_input_layout, null);
        builder.setTitle("Enter description (Nhập chú thích)");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                if (input.getText().toString() != null) {
                    showProgressDialog();
                    Call<BaseMessageResponse> call = apiInterface.updateDescriptionWMaterialInfo(mappingMasterArrayList.get(pos).getWmtId(), input.getText().toString());
                    call.enqueue(new Callback<BaseMessageResponse>() {
                        @Override
                        public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                            if (response.isSuccessful()) {
                                BaseMessageResponse res = response.body();
                                if (res.isResult()) {
                                    progressDialog.dismiss();
                                    mappingMasterArrayList.get(pos).setDescription(input.getText().toString());
                                    setRecyc();
                                } else {
                                    progressDialog.dismiss();
                                    AlertError.alertError(res.getMessage(), MappingActivity.this);
                                }
                            } else {
                                AlertError.alertError("The server response error", MappingActivity.this);
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                            call.cancel();
//                nodata.setVisibility(View.VISIBLE);
                            AlertError.alertError(t.getMessage(), MappingActivity.this);
                            progressDialog.dismiss();
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void creatingMLno(String contents) {
        showProgressDialog();
        Call<InsertwMaterialRes> call = apiInterface.insertwMaterial(product,idActual,styleName,contents);
        call.enqueue(new Callback<InsertwMaterialRes>() {
            @Override
            public void onResponse(Call<InsertwMaterialRes> call, Response<InsertwMaterialRes> response) {
                if(response.isSuccessful()){
                    InsertwMaterialRes res = response.body();
                    if(res.isResult()){
                        Toast.makeText(MappingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }else if(!res.isResult()&&res.getMessage()!=null){
                        AlertError.alertError(res.getMessage(), MappingActivity.this);
                    }else {
                        Toast.makeText(MappingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }
                }else {
                    AlertError.alertError("The server response error", MappingActivity.this);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<InsertwMaterialRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), MappingActivity.this);
                progressDialog.dismiss();
            }
        });
        Log.e("creatingMLno", webUrl + "ActualWO/insertw_material?style_no=" +
                product +
                "&id_actual=" +
                idActual +
                "&ROT=" +
                styleName +
                "&bb_no=" +
                contents);

    }

    @Override
    protected void onResume() {
        getData(page,false);
        super.onResume();
    }
}