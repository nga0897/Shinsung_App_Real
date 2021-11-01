package com.example.mmsapp.ui.home.Composite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Composite.adapter.CompositeAdapter;
import com.example.mmsapp.ui.home.Composite.model.CompositeMaster;
import com.example.mmsapp.ui.home.Composite.model.PositionOfController;
import com.example.mmsapp.ui.home.Divide.DivideActivity;

import com.example.mmsapp.ui.home.Composite.apiInterface.CompositeAPI;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.InfoMcWkMoldRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ModifyProcessMachineRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ModifyProcessMachineWKRes;
import com.example.mmsapp.ui.home.Mapping.MappingActivity;
import com.example.mmsapp.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompositeActivity extends AppCompatActivity {
    String webUrl;
    private ProgressDialog dialog;
    List<CompositeMaster> compositeMasterArrayList;
    TextView nodata;
    RecyclerView recyclerView;
    CompositeAdapter compositeAdapter;
    private String idActual;
    List<PositionOfController> positionOfControllerArrayList;
    EditText tvid;
    TextView StaffType;
    CompositeAPI apiInterface;
    BoomMenuButton mapping;
    private String product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite);
        setTitle("Composite");
        apiInterface = APIClient.getClient().create(CompositeAPI.class);
        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        product = getIntent().getStringExtra("product");
        dialog = new ProgressDialog(this);
        nodata = findViewById(R.id.nodata);
        recyclerView = findViewById(R.id.recyclerView);
        nodata.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        idActual = SharedPref.getInstance().get(Constants.ID_ACTUAL,String.class);
        getPositionOfController();
//        new getvitridungmay().execute(webUrl + "ActualWO/get_staff");
        //build bombutton
        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);
        bmb4.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.ic_mold)
                .normalTextRes(R.string.Mold)
                .subNormalTextRes(R.string.addModeinhere));
        bmb4.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.ic_staff)
                .normalTextRes(R.string.Worker)
                .subNormalTextRes(R.string.movingpositionofmachinery));
        bmb4.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.ic_machine)
                .normalTextRes(R.string.Machine)
                .subNormalTextRes(R.string.ConfirmMoving));
        bmb4.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(CompositeActivity.this, AddmoldActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(CompositeActivity.this, WorkerActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(CompositeActivity.this, MachineActivity.class);
                        startActivity(intent2);
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

            }

            @Override
            public void onBoomDidShow() {

            }
        });

        mapping = (BoomMenuButton) findViewById(R.id.mapping);

        if (SharedPref.getInstance().get(Constants.STYLE_NAME,String.class).substring(0,3).equals("STA")) {
            mapping.setButtonEnum(ButtonEnum.Ham);
            mapping.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
            mapping.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
            mapping.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_mapping)
                    .normalTextRes(R.string.Mappiing)
                    .subNormalTextRes(R.string.CompositeMaterialMapping));
            mapping.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_div)
                    .normalTextRes(R.string.Divide)
                    .subNormalTextRes(R.string.DivideMTNo));
        } else {
            mapping.setButtonEnum(ButtonEnum.Ham);
            mapping.setButtonPlaceEnum(ButtonPlaceEnum.HAM_1);
            mapping.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
            mapping.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_mapping)
                    .normalTextRes(R.string.Mappiing)
                    .subNormalTextRes(R.string.CompositeMaterialMapping));
        }

        mapping.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(CompositeActivity.this, MappingActivity.class);
                        intent.putExtra("product",product);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(CompositeActivity.this, DivideActivity.class);
                        startActivity(intent1);
                        break;
//                    case 2:
//                        Intent intent2 = new Intent(CompositeActivity.this,MachineActivity.class);
//                        startActivity(intent2);
//                        break;
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

            }

            @Override
            public void onBoomDidShow() {

            }
        });
    }

    private void getPositionOfController() {
        Call<List<PositionOfController>> call = apiInterface.getPositionOfController();
        call.enqueue(new Callback<List<PositionOfController>>() {
            @Override
            public void onResponse(Call<List<PositionOfController>> call, Response<List<PositionOfController>> response) {
                if(response.isSuccessful()){
                    positionOfControllerArrayList = new ArrayList<>();
                    positionOfControllerArrayList = response.body();

                }else{
                    AlertError.alertError("The server response error", CompositeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<PositionOfController>> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), CompositeActivity.this);
            }
        });
    }


    private void getData() {
//        new getData().execute(webUrl + "ActualWO/Getinfo_mc_wk_mold?id_actual=" + id_actual);
        showDialog();
        Call<InfoMcWkMoldRes> call = apiInterface.getCompositeMaster(idActual);
        call.enqueue(new Callback<InfoMcWkMoldRes>() {
            @Override
            public void onResponse(Call<InfoMcWkMoldRes> call, Response<InfoMcWkMoldRes> response) {
                if(response.isSuccessful()){
                    compositeMasterArrayList = new ArrayList<>();
                    InfoMcWkMoldRes res = response.body();
                    if (res!=null){
                        compositeMasterArrayList = res.getCompositeMasterList();
                        if (compositeMasterArrayList.isEmpty()) {
                            dialog.dismiss();
                            nodata.setVisibility(View.VISIBLE);
                            mapping.setVisibility(View.GONE);
                            return;
                        }
                        dialog.dismiss();
                        buildRecycleView();
                    }

                }else {
                    nodata.setVisibility(View.VISIBLE);
                    mapping.setVisibility(View.GONE);
                    AlertError.alertError("The server response error", CompositeActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<InfoMcWkMoldRes> call, Throwable t) {
                call.cancel();
                nodata.setVisibility(View.VISIBLE);
                mapping.setVisibility(View.GONE);
                AlertError.alertError(t.getMessage(), CompositeActivity.this);
                dialog.dismiss();

            }
        });

        Log.e("getData", webUrl + "ActualWO/Getinfo_mc_wk_mold?id_actual=" + idActual);
    }

    private void buildRecycleView() {
        nodata.setVisibility(View.GONE);

        int typemachine = 0;
        int typeworker = 0;
        for (int i=0;i<compositeMasterArrayList.size();i++){
            if(compositeMasterArrayList.get(i).getType()!=null){
                if (compositeMasterArrayList.get(i).getType().equals("machine")){
                    typemachine = 1;
                }
                if (compositeMasterArrayList.get(i).getType().equals("worker")){
                    typeworker = 1;
                }
            }
        }
        if (typemachine == 1 && typeworker==1){
            mapping.setVisibility(View.VISIBLE);
        }else {
            mapping.setVisibility(View.GONE);
        }


        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CompositeActivity.this);
        compositeAdapter = new CompositeAdapter(compositeMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(compositeAdapter);
        compositeAdapter.setOnItemClickListener(new CompositeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (compositeMasterArrayList.get(position).getType()) {
                    case Constants.MACHINE:
                        openPopupMold(position, compositeMasterArrayList.get(position).getStartDt(), compositeMasterArrayList.get(position).getEndDt(), "MC");
                        break;
                    case Constants.WORKER:
                        openPopupMold(position, compositeMasterArrayList.get(position).getStartDt(), compositeMasterArrayList.get(position).getEndDt(), "WK");
                        break;
                    case Constants.MOLD:
                        openPopupMold(position, compositeMasterArrayList.get(position).getStartDt(), compositeMasterArrayList.get(position).getEndDt(), "MD");
                        break;
                    default:
                        Toast.makeText(CompositeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void ondelete(final int position) {
                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(CompositeActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Are you sure Delete?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                 showDialog();
                 Call<BaseMessageResponse> call = null;
                 String id = compositeMasterArrayList.get(position).getPmId();
                        switch (compositeMasterArrayList.get(position).getType()) {
                            case Constants.MACHINE:
                                call = apiInterface.deleteMoldMcWkActual(id,Constants.MACHINE_TYPE_CODE);
                                //new onDelete().execute(webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).pmId + "&sts=mc");
                                Log.e("onDelete", webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).getPmId() + "&sts=mc");
                                break;
                            case Constants.WORKER:
                                call = apiInterface.deleteMoldMcWkActual(id,Constants.WORKER_TYPE_CODE);
                                //new onDelete().execute(webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).pmId + "&sts=wk");
                                Log.e("onDelete", webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).getPmId() + "&sts=wk");
                                break;
                            case Constants.MOLD:
                                call = apiInterface.deleteMoldMcWkActual(id,Constants.MOLD);
                                //new onDelete().execute(webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).pmId + "&sts=mold");
                                Log.e("onDelete", webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).getPmId() + "&sts=mold");
                                break;
                            default:
                                Toast.makeText(CompositeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        if(call!=null){
                            call.enqueue(new Callback<BaseMessageResponse>() {
                                @Override
                                public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                                    if(response.isSuccessful()){
                                        BaseMessageResponse res = response.body();
                                            if(res.isResult()){
                                                Toast.makeText(CompositeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                                startActivity(getIntent());
                                            }else {
                                                Toast.makeText(CompositeActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                    }else {
                                        Toast.makeText(CompositeActivity.this, "The server response error", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                                    call.cancel();
                                    dialog.dismiss();
                                    Toast.makeText(CompositeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
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

    //modify mc mold worker
    private void openPopupMold(final int position, String start_dt, String end_dt, final String KEY) {
        final Dialog dialog = new Dialog(CompositeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView;

        if (KEY.equals("WK")) {
            dialogView = LayoutInflater.from(CompositeActivity.this).inflate(R.layout.change_worker, null);
            dialog.setCancelable(false);
            dialog.setContentView(dialogView);
            StaffType = dialog.findViewById(R.id.StaffType);
            StaffType.setText(compositeMasterArrayList.get(position).getStaffTp());
            dialog.findViewById(R.id.rll2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWorker(StaffType);
                }
            });


        } else {
            dialogView = LayoutInflater.from(CompositeActivity.this).inflate(R.layout.change_mold, null);
            dialog.setCancelable(false);
            dialog.setContentView(dialogView);
        }

        final TextView ngaystart, giostart, Used;
        ngaystart = dialog.findViewById(R.id.ngaystart);
        giostart = dialog.findViewById(R.id.giostart);

        final TextView ngayend, gioend;
        ngayend = dialog.findViewById(R.id.ngayend);
        gioend = dialog.findViewById(R.id.gioend);
        Used = dialog.findViewById(R.id.Used);
        tvid = dialog.findViewById(R.id.tvid);
        tvid.setText(compositeMasterArrayList.get(position).getCode());

        if (compositeMasterArrayList.get(position).getUseYn().equals("Y")) {
            Used.setText("USE");
        } else {
            Used.setText("UNUSE");
        }
        dialog.findViewById(R.id.im1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraScan();
            }
        });

        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvid.getText().toString().length() != 0) {
                    //so sanh 2 ngay duoc chon
                    Date dsend = new Date();
                    Date dstart = new Date();

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dstart = sdf.parse(ngaystart.getText().toString() + " " + giostart.getText().toString());
                        dsend = sdf.parse(ngayend.getText().toString() + " " + gioend.getText().toString());

                    } catch (ParseException ex) {
                        Log.e("rrr", ex.getMessage());
                    }

                    if (dsend.after(dstart)) {

                        String us = Used.getText().toString();
                        String keyus = "";
                        if (us.equals("USE")) {
                            keyus = "Y";
                        } else {
                            keyus = "N";
                        }
                        if (KEY.equals("WK")) {
                            showDialog();
                            modifyProcessMachineWK(KEY,compositeMasterArrayList.get(position).getPmId(),keyus,ngaystart.getText().toString() + " " + giostart.getText().toString(),ngayend.getText().toString() + " " + gioend.getText().toString());
                        } else {
                            showDialog();
                            modifyProcessMachine(KEY,compositeMasterArrayList.get(position).getPmId(),keyus,ngaystart.getText().toString() + " " + giostart.getText().toString(),ngayend.getText().toString() + " " + gioend.getText().toString());
                        }

                    } else {
                        AlertError.alertError("Start day was bigger than end day. That is wrong", CompositeActivity.this);
                    }

                } else {
                    tvid.setError("Please input here!");
                }

            }
        });

        dialog.findViewById(R.id.rl2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPpUse(Used);
            }
        });

        final String yy, MM, dd, hh, mm, ss, yye, MMe, dde, hhe, mme, sse;
        if (start_dt.length() == 19) {
            yy = start_dt.substring(0, 4);
            MM = start_dt.substring(5, 7);
            dd = start_dt.substring(8, 10);
            hh = start_dt.substring(11, 13);
            mm = start_dt.substring(14, 16);
            ss = start_dt.substring(17, 19);
        } else {
            AlertError.alertError("Format date incorrect.", CompositeActivity.this);
            return;
        }
        if (end_dt.length() == 19) {
            yye = end_dt.substring(0, 4);
            MMe = end_dt.substring(5, 7);
            dde = end_dt.substring(8, 10);
            hhe = end_dt.substring(11, 13);
            mme = end_dt.substring(14, 16);
            sse = end_dt.substring(17, 19);
        } else {
            AlertError.alertError("Format date incorrect.", CompositeActivity.this);
            return;
        }
        ngaystart.setText(yy + "-" + MM + "-" + dd);
        ngayend.setText(yye + "-" + MMe + "-" + dde);
        giostart.setText(hh + ":" + mm + ":" + ss);
        gioend.setText(hhe + ":" + mme + ":" + sse);

        ngaystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDay(ngaystart);
            }
        });
        giostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHour(giostart, hh, mm);
            }
        });
        ngayend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDay(ngayend);
            }
        });
        gioend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHour(gioend, hhe, mme);
            }
        });

        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void modifyProcessMachine(final String KEY, String pmId, String keyus, String startDate, String endDate) {
        Call<ModifyProcessMachineRes> call = apiInterface.modifyProcessMachine(tvid.getText().toString(),pmId,keyus,startDate,idActual,endDate,null);
        call.enqueue(new Callback<ModifyProcessMachineRes>() {
            @Override
            public void onResponse(Call<ModifyProcessMachineRes> call, Response<ModifyProcessMachineRes> response) {
                if(response.isSuccessful()){
                   if(response.body()!=null){
                       if (response.body().getResult()==0) {
                           Toast.makeText(CompositeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                           startActivity(getIntent());
                       } else if (response.body().getResult()==1) {
                           if (KEY.equals("WK")) {
                               AlertError.alertError("The Worker was setting duplicate date", CompositeActivity.this);
                           } else if (KEY.equals("MD")) {
                               AlertError.alertError("The Mold was setting duplicate date", CompositeActivity.this);
                           } else {
                               AlertError.alertError("The Machine was setting duplicate date", CompositeActivity.this);
                           }
                       } else if (response.body().getResult()==2) {
                           AlertError.alertError("Start day was bigger End day. That is wrong", CompositeActivity.this);
                       } else if (response.body().getResult()==3) {
                           //   xacnhan_datontai(jsonObject.getString("update"),jsonObject.getString("start"),jsonObject.getString("end"));
                       }else {
                           AlertError.alertError(response.body().getMessage(), CompositeActivity.this);
                       }
                   }
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                    AlertError.alertError("The server response error", CompositeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ModifyProcessMachineRes> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                AlertError.alertError(t.getMessage(), CompositeActivity.this);
            }
        });

    }

    private void modifyProcessMachineWK(final String KEY, String psId, String keyUs, String start, String end) {
        Call<ModifyProcessMachineWKRes> call = apiInterface.modifyProcessMachineWK(tvid.getText().toString(),StaffType.getText().toString(),psId,keyUs,start,idActual,end,null);
        call.enqueue(new Callback<ModifyProcessMachineWKRes>() {
            @Override
            public void onResponse(Call<ModifyProcessMachineWKRes> call, Response<ModifyProcessMachineWKRes> response) {
                if(response.isSuccessful()){
                    ModifyProcessMachineWKRes res = response.body();
                    if(res!=null){
                        if (res.getResult()==0) {
                            Toast.makeText(CompositeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            startActivity(getIntent());
                        } else if (res.getResult()==1) {
                            if (KEY.equals("WK")) {
                                AlertError.alertError("The Worker was setting duplicate date", CompositeActivity.this);
                            } else if (KEY.equals("MD")) {
                                AlertError.alertError("The Mold was setting duplicate date", CompositeActivity.this);
                            } else {
                                AlertError.alertError("The Machine was setting duplicate date", CompositeActivity.this);
                            }
                        } else if (res.getResult()==2) {
                            AlertError.alertError("Start day was bigger End day. That is wrong", CompositeActivity.this);
                        } else if (res.getResult()==3) {
                            AlertError.alertError(res.getMessage(), CompositeActivity.this);
                            //   xacnhan_datontai(jsonObject.getString("update"),jsonObject.getString("start"),jsonObject.getString("end"));
                        }else if(res.getResult()==4){
                            AlertError.alertError(res.getMessage(), CompositeActivity.this);
                        }else {
                            AlertError.alertError(res.getMessage(), CompositeActivity.this);
                        }
                    }
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                    AlertError.alertError("The server response error", CompositeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ModifyProcessMachineWKRes> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                AlertError.alertError(t.getMessage(), CompositeActivity.this);
            }
        });
    }

    private void popupWorker(final TextView staffType) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CompositeActivity.this, android.R.layout.select_dialog_singlechoice);
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(CompositeActivity.this);
        builderSingle.setTitle("Select One Line:");
        for (int i = 0; i < positionOfControllerArrayList.size(); i++) {
            arrayAdapter.add(positionOfControllerArrayList.get(i).getDtNm());
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                staffType.setText(positionOfControllerArrayList.get(i).getDtCd());
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void openCameraScan() {
        new IntentIntegrator(CompositeActivity.this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(CompositeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                tvid.setText(result.getContents());
            }
        }
    }

    private void openPpUse(final TextView used) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CompositeActivity.this, android.R.layout.select_dialog_singlechoice);
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(CompositeActivity.this);
        builderSingle.setTitle("Select One Line:");
        arrayAdapter.add("USE");
        arrayAdapter.add("UNUSE");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                used.setText(arrayAdapter.getItem(i));
                dialog.dismiss();
            }
        });
        builderSingle.show();

    }

    private void dialogDay(final TextView ngaystart) {
        Calendar c = Calendar.getInstance();
        int selectedYear = c.get(Calendar.YEAR);
        int selectedMonth = c.get(Calendar.MONTH);
        int selectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                ngaystart.setText((year + "-" + String.format("%02d", monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void dialogHour(final TextView ngaystart, final String hh, final String mm) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ngaystart.setText(String.format("%02d", hourOfDay) + ":" + (String.format("%02d", minute)) + ":00");
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener,
                Integer.parseInt(hh), Integer.parseInt(mm), true);
        timePickerDialog.show();
    }

    //modify mc mold worker

    @Override
    protected void onResume() {
        getData();
        SharedPref.getInstance().put(Constants.DIVIDE_SELECTED_POS,-1);
        super.onResume();
    }

    private void showDialog() {
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

}