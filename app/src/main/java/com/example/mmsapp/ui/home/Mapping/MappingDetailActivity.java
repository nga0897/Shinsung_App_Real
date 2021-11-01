package com.example.mmsapp.ui.home.Mapping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.LoginActivity;
import com.example.mmsapp.MainActivity;
import com.example.mmsapp.R;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Mapping.adapter.MappingDetailAdapter;
import com.example.mmsapp.ui.home.Mapping.apiInterface.MappingAPI;
import com.example.mmsapp.ui.home.Mapping.apiInterface.response.CancelMappingRes;
import com.example.mmsapp.ui.home.Mapping.model.MappingDetailMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class MappingDetailActivity extends AppCompatActivity {
    String webUrl;
    private String mtCd;
    private ProgressDialog dialog;
    List<MappingDetailMaster> mappingDetailMasterArrayList;
    MappingDetailAdapter mappingDetailAdapter;
    TextView nodata;
    RecyclerView recyclerView;
    BoomMenuButton bmb;
    String vt_scan = "";
    MappingAPI apiInterface;
    FloatingActionButton scan;
    FloatingActionButton input;
    private static final int FINISH_BACK = 24;
    private static final int CANCEL_MAPPING = 25;
    private static final int SAVE_RETURN = 99;
    private boolean isEndShift;
    private String idActual;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_detail);
        setTitle("Mapping Detail");
        apiInterface = APIClient.getClient().create(MappingAPI.class);
        idActual = SharedPref.getInstance().get(Constants.ID_ACTUAL,String.class);
        isEndShift = getIntent().getBooleanExtra("isEndShift",false);
        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        mtCd = SharedPref.getInstance().get(Constants.ML_NO,String.class);
        nodata = findViewById(R.id.nodata);
        recyclerView = findViewById(R.id.recyclerView);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(MappingDetailActivity.this, R.style.AlertDialogCustom);


        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MappingDetailActivity.this).initiateScan();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        bmb = findViewById(R.id.bmb);
        if(isEndShift){
            bmb.setVisibility(View.GONE);
        }
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_1);
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_mater)
                .normalTextRes(R.string.MaterialMapping));
        bmb.addBuilder(new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_container)
                .normalTextRes(R.string.ContainerMapping));


        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        vt_scan = "MT";
                        MaterialMapping();
                        break;
                    case 1:
                        vt_scan = "CT";
                        MaterialMapping();
                        break;

                }
            }

            @Override
            public void onBackgroundClick() {
            }

            @Override
            public void onBoomWillHide() {
            }

            @Override
            public void onBoomDidHide() {
            }

            @Override
            public void onBoomWillShow() {
                input.setVisibility(View.GONE);
                scan.setVisibility(View.GONE);
            }

            @Override
            public void onBoomDidShow() {
            }
        });

        getData();
    }

    private void inputData() {

        final Dialog dialog = new Dialog(MappingDetailActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(MappingDetailActivity.this).inflate(R.layout.popup_input, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final EditText containerCode = dialog.findViewById(R.id.Containercode);
        final Button confirm = dialog.findViewById(R.id.confirm);
        final TextInputLayout h2;

        h2 = dialog.findViewById(R.id.H2);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (containerCode.getText().toString() == null || containerCode.getText().toString().length() == 0) {
                    h2.setError("Please, Input here.");
                    return;
                }  else {
                    h2.setError(null);

                    if (vt_scan.equals("MT")) {
                        mappingMaterial(mtCd,containerCode.getText().toString(),idActual,"");
                        Log.e("Mappingmaterial", webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
                                mtCd +
                                "&mt_mapping=" +
                                containerCode.getText().toString() +
                                "&id_actual=" +
                                idActual +
                                "&bb_no=" +
                                "");
                    } else {

                        mappingMaterial(mtCd,"",idActual,containerCode.getText().toString());
                        Log.e("Mappingmaterial", webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
                                mtCd +
                                "&mt_mapping=" +
                                "" +
                                "&id_actual=" +
                                idActual +
                                "&bb_no=" +
                                containerCode.getText().toString());
                    }
                    dialog.dismiss();
                }


            }
        });


        dialog.show();

    }

    //scan
    @SuppressLint("RestrictedApi")
    private void MaterialMapping() {

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
                Toast.makeText(MappingDetailActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (vt_scan.equals("MT")) {
                    /*new Mappingmaterial().execute(webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
                            mtCd +
                            "&mt_mapping=" +
                            result.getContents() +
                            "&id_actual=" +
                            idActual +
                            "&bb_no=" +
                            "");*/
                    mappingMaterial(mtCd,result.getContents(),idActual,"");
                } else {
//                    new Mappingmaterial().execute(webUrl + "ActualWO/insertw_material_mping?mt_cd=" +
//                            mtCd +
//                            "&mt_mapping=" +
//                            "" +
//                            "&id_actual=" +
//                            idActual +
//                            "&bb_no=" +
//                            result.getContents());
                    mappingMaterial(mtCd,"",idActual,result.getContents());
                }
            }
        }
    }

    private void mappingMaterial(String mtCd,String mtMapping,String idActual,String bbNo){
        Call<BaseMessageResponse> call = apiInterface.insertWMaterialMapping(mtCd,mtMapping,idActual,bbNo);
        call.enqueue(new Callback<BaseMessageResponse>() {
            @Override
            public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                if(response.isSuccessful()){
                    BaseMessageResponse res = response.body();
                    if(res.isResult()){
                        dialog.dismiss();
                        Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    } else {
                        dialog.dismiss();
                        AlertError.alertError(res.getMessage(), MappingDetailActivity.this);
                    }
                }else {
                    AlertError.alertError("The server response error", MappingDetailActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                call.cancel();
                nodata.setVisibility(View.VISIBLE);
                AlertError.alertError(t.getMessage(), MappingDetailActivity.this);
                dialog.dismiss();
            }
        });
    }

    private void getData() {
        Call<List<MappingDetailMaster>> call = apiInterface.dsMappingW(mtCd);
        call.enqueue(new Callback<List<MappingDetailMaster>>() {
            @Override
            public void onResponse(Call<List<MappingDetailMaster>> call, Response<List<MappingDetailMaster>> response) {
                if(response.isSuccessful()){
                    List<MappingDetailMaster> res = response.body();
                    if (res.size() == 0) {
                        dialog.dismiss();
                        nodata.setVisibility(View.VISIBLE);
                        return;
                    }
                    mappingDetailMasterArrayList = new ArrayList<>();
                    mappingDetailMasterArrayList =res;
                    dialog.dismiss();
                    setRecyc();
                }else {
                    nodata.setVisibility(View.VISIBLE);
                    AlertError.alertError("The server response error", MappingDetailActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<MappingDetailMaster>> call, Throwable t) {
                call.cancel();
                nodata.setVisibility(View.VISIBLE);
                AlertError.alertError(t.getMessage(), MappingDetailActivity.this);
                dialog.dismiss();
            }
        });
        Log.e("mapping Detail", webUrl + "ActualWO/ds_mapping_w?mt_cd=" + mtCd);
    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MappingDetailActivity.this);
        mappingDetailAdapter = new MappingDetailAdapter(mappingDetailMasterArrayList,isEndShift);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mappingDetailAdapter);

        mappingDetailAdapter.setOnItemClickListener(new MappingDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onR(int position, TextView edittext) {
                inputnum(position);
            }

            @Override
            public void onF(final int position, TextView edittext) {

                if (mappingDetailMasterArrayList.get(position).getUseYn().equals("N")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MappingDetailActivity.this, R.style.AlertDialogCustom);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("Warning!!!");
                    alertDialog.setMessage("Is A sure you want to return to the used state"); //"The data you entered does not exist on the server !!!");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            new Cancel_mapping().execute(webUrl+"ActualWO/Finish_back?wmmid="+ mappingDetailMasterArrayList.get(position).getWmmId());
                            cancelMapping(FINISH_BACK,mappingDetailMasterArrayList.get(position).getWmmId(),null,null,null);
                            Log.e("Finish_back",webUrl+"ActualWO/Finish_back?wmmid="+ mappingDetailMasterArrayList.get(position).getWmmId());
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                }else {
//                    new Cancel_mapping().execute(webUrl+"ActualWO/Finish_back?wmmid="+ mappingDetailMasterArrayList.get(position).getWmmId());
                    cancelMapping(FINISH_BACK,mappingDetailMasterArrayList.get(position).getWmmId(),null,null,null);
                    Log.e("Finish_back",webUrl+"ActualWO/Finish_back?wmmid="+ mappingDetailMasterArrayList.get(position).getWmmId());
                }
            }

            @Override
            public void ondelete(final int position, TextView edittext) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MappingDetailActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Are you sure Delete: " + mappingDetailMasterArrayList.get(position).getMtCd()); //"The data you entered does not exist on the server !!!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // new Cancel_mapping().execute(webUrl+"ActualWO/Cancel_mapping?wmmid="+ mappingDetailMasterArrayList.get(position).getWmmId());
                       cancelMapping(CANCEL_MAPPING,mappingDetailMasterArrayList.get(position).getWmmId(),null,null,null);
                        Log.e("Cancel mapping",webUrl+"ActualWO/Cancel_mapping?wmmid="+ mappingDetailMasterArrayList.get(position).getWmmId());
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void cancelMapping(int type,int wmmId,String quantity,String mtCd,String mtLot) {
        showDialog();
        Call<CancelMappingRes> call = null;
        switch (type){
            case FINISH_BACK:
                call = apiInterface.finishBack(wmmId);
                break;
            case CANCEL_MAPPING:
                call = apiInterface.cancelMapping(wmmId);
                break;
            case SAVE_RETURN:
                call = apiInterface.saveReturnLot(quantity,mtCd,mtLot);
                break;
        }
        if(call!=null){
            call.enqueue(new Callback<CancelMappingRes>() {
                @Override
                public void onResponse(Call<CancelMappingRes> call, Response<CancelMappingRes> response) {
                    if(response.isSuccessful()){
                        CancelMappingRes res = response.body();
                        if(!res.isResult()&&res.getMessage()!=null){
                            dialog.dismiss();
                            AlertError.alertError(res.getMessage(), MappingDetailActivity.this);
                            return;
                        }else {
                            startActivity(getIntent());
                            Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }else {
                        AlertError.alertError("The server response error", MappingDetailActivity.this);
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<CancelMappingRes> call, Throwable t) {
                    call.cancel();
                    AlertError.alertError(t.getMessage(), MappingDetailActivity.this);
                    dialog.dismiss();
                }
            });
        }
    }

    private void inputnum(final int pos) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MappingDetailActivity.this);
        View viewInflated = LayoutInflater.from(MappingDetailActivity.this).inflate(R.layout.number_input_layout, null);
        builder.setTitle("Input Number Return");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input.getText().toString() == null) {
                    input.setText("0");
                } else if (input.getText().toString().length() == 0) {
                    input.setText("0");
                }else if(Integer.parseInt(input.getText().toString())>mappingDetailMasterArrayList.get(pos).getGrQty()){
                    input.setText(mappingDetailMasterArrayList.get(pos).getGrQty()+"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString() == null) {
                    dialog.dismiss();
                } else if (input.getText().toString() == "0") {
                    dialog.dismiss();
                } else if (input.getText().toString().length() == 0) {
                    dialog.dismiss();
                } if (Integer.parseInt(input.getText().toString())>0){

//                    new Cancel_mapping().execute(webUrl+"ActualWO/savereturn_lot?soluong=" +
//                            input.getText().toString() +
//                            "&mt_cd=" +
//                            mappingDetailMasterArrayList.get(pos).getMtCd() +
//                            "&mt_lot=" +
//                            Mt_cd);
                    cancelMapping(SAVE_RETURN,-1,input.getText().toString(),mappingDetailMasterArrayList.get(pos).getMtCd(),mtCd);
                    Log.e("savereturn_lot",webUrl+"ActualWO/savereturn_lot?soluong=" +
                            input.getText().toString() +
                            "&mt_cd=" +
                            mappingDetailMasterArrayList.get(pos).getMtCd() +
                            "&mt_lot=" +
                            mtCd);
                }
                else {
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

    private void showDialog(){
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }
}