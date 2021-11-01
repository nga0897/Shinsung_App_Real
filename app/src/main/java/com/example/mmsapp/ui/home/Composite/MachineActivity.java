package com.example.mmsapp.ui.home.Composite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.R;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Composite.adapter.ItemCompositeAdapter;
import com.example.mmsapp.ui.home.Composite.apiInterface.MachineAPI;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.CreateProcessMachineRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.DestroyPreviousResultRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ScanMachineRes;
import com.example.mmsapp.ui.home.Composite.model.ItemCompositeMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MachineActivity extends AppCompatActivity {
    String webUrl;
    private String idActual;
    private ProgressDialog dialog;
    List<ItemCompositeMaster> itemCompositeMasterArrayList;
    ItemCompositeAdapter itemCompositeAdapter;
    int page = 1;
    int total = -1;
    RecyclerView recyclerView;
    MachineAPI apiInterface;
    String mc_no ="",use_unuse="";

    FloatingActionButton scan;
    FloatingActionButton input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        setTitle("Machine");

        apiInterface = APIClient.getClient().create(MachineAPI.class);

        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        idActual = SharedPref.getInstance().get(Constants.ID_ACTUAL,String.class);
        dialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        getData(page,false);
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
                scanMachine();
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
        final Dialog dialog = new Dialog(MachineActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(MachineActivity.this).inflate(R.layout.popup_input, null);
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
                    findScanMachine(Containercode.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    private void scanMachine() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MachineActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                findScanMachine(result.getContents());
            }
        }
    }

    private void findScanMachine(String contents) {
        showDialog();
        Call<ScanMachineRes> call = apiInterface.scanMachine(1,50,null,"asc",contents,null,false);
        call.enqueue(new Callback<ScanMachineRes>() {
            @Override
            public void onResponse(Call<ScanMachineRes> call, Response<ScanMachineRes> response) {
                if(response.isSuccessful()){
                    ScanMachineRes res = response.body();
                    itemCompositeMasterArrayList = new ArrayList<>();
                    total = res.getTotal();
                    page = res.getPage();
                    List<ItemCompositeMaster> listRes = res.getItemCompositeMasterList();

                    if (listRes.size() == 0) {
                        dialog.dismiss();
                        AlertError.alertError("No data", MachineActivity.this);
                        return;
                    }
                    itemCompositeMasterArrayList = listRes;
                    dialog.dismiss();
                    buildrecyc();
                }else {
                    AlertError.alertError("The server response error", MachineActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ScanMachineRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), MachineActivity.this);
                dialog.dismiss();
            }
        });
        Log.e("Scan machine",webUrl + "ActualWO/searchpopupmachine?page=" + 1 + "&rows=50&sidx=&sord=asc&mc_no=" + contents +
                "&md_nm=&_search=false");

    }

    private void getData(int pages, final boolean isLoadMore) {
        showDialog();
        //new getData().execute(webUrl + "ActualWO/searchpopupmachine?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
        Call<ScanMachineRes> call = apiInterface.getAllMachine(pages,50,null,"asc",null,null,false);
        call.enqueue(new Callback<ScanMachineRes>() {
            @Override
            public void onResponse(Call<ScanMachineRes> call, Response<ScanMachineRes> response) {
                if(response.isSuccessful()){
                    ScanMachineRes res = response.body();
                    if(!isLoadMore){
                        itemCompositeMasterArrayList = new ArrayList<>();
                    }
                    total = res.getTotal();
                    page = res.getPage();
                    List<ItemCompositeMaster> listRes = res.getItemCompositeMasterList();

                    if (listRes.size() == 0) {
                        dialog.dismiss();
                        AlertError.alertError("No data", MachineActivity.this);
                        return;
                    }
                    if(isLoadMore){
                        itemCompositeMasterArrayList.addAll(listRes);
                        itemCompositeAdapter.notifyDataSetChanged();
                    }else {
                        itemCompositeMasterArrayList = listRes;
                        buildrecyc();
                    }

                }else {
                    AlertError.alertError("The server response error", MachineActivity.this);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ScanMachineRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), MachineActivity.this);
                dialog.dismiss();
            }
        });

        Log.e("getData Machine",webUrl + "ActualWO/searchpopupmachine?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
    }

    private void showDialog(){
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }


    private void buildrecyc() {

        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MachineActivity.this);
        itemCompositeAdapter = new ItemCompositeAdapter(itemCompositeMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemCompositeAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == itemCompositeMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getaddData(page + 1);

                    }
                }
            }
        });


        itemCompositeAdapter.setOnItemClickListener(new ItemCompositeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                xacnhanadd(position);

            }
        });


    }

    private void xacnhanadd(final int position) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MachineActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Machine");
        alertDialog.setMessage("You want: "+itemCompositeMasterArrayList.get(position).getMcNo()); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mc_no = itemCompositeMasterArrayList.get(position).getMcNo();
                use_unuse = "Y";
                /*new comfirmData().execute(webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).getMcNo() +
                        "&use_yn=Y&id_actual="+
                        id_actual +"&remark=");*/
                showDialog();
                Call<CreateProcessMachineRes> call = apiInterface.createProcessMachineUnit(itemCompositeMasterArrayList.get(position).getMcNo(),"Y",idActual,null);
                call.enqueue(new Callback<CreateProcessMachineRes>() {
                    @Override
                    public void onResponse(Call<CreateProcessMachineRes> call, Response<CreateProcessMachineRes> response) {
                        if(response.isSuccessful()){
                            CreateProcessMachineRes res = response.body();

                            if(res.getResult()==0){
                                thanhcong();
                            }else if(res.getResult()==1){
                                AlertError.alertError("The Machine was setting duplicate date", MachineActivity.this);
                            }else if (res.getResult()==2){
                                AlertError.alertError("Start day was bigger End day. That is wrong", MachineActivity.this);
                            } else if (res.getResult()==3){
                                xacnhan_datontai(res.getUpdate(),res.getStart(),res.getEnd());
                            }
                            dialog.dismiss();
                        }else {
                            AlertError.alertError("The server response error", MachineActivity.this);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateProcessMachineRes> call, Throwable t) {
                        call.cancel();
                        AlertError.alertError(t.getMessage(), MachineActivity.this);
                        dialog.dismiss();
                    }
                });

                Log.e("comfirmData",webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).getMcNo() +
                        "&use_yn=Y&id_actual="+
                        idActual +"&remark=");
            }
        });
//        alertDialog.setNegativeButton("UNUSE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mc_no = itemCompositeMasterArrayList.get(position).md_no;
//                use_unuse = "N";
//                new comfirmData().execute(webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).md_no +
//                        "&use_yn=N&id_actual="+
//                        id_actual +"&remark=");
//                Log.e("comfirmData",webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).md_no +
//                        "&use_yn=N&id_actual="+
//                        id_actual +"&remark=");
//            }
//        });
        alertDialog.show();
    }

    private void thanhcong() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MachineActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add machine");
        alertDialog.setMessage("Add machine finnish."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void xacnhan_datontai(final String update, final String start, final String end) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MachineActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Machine");
        alertDialog.setMessage("This Machine has already selected. If you confirm it, this Machine will finish the task at the previous stage."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

//                new huyketquatruoc().execute(webUrl+"ActualWO/Createprocessmachine_duplicate?mc_no="+ mc_no+"&id_actual="+id_actual+"&use_yn="+use_unuse+"&id_update="+update+"&start="+start+"&end="+end+"&remark=");
                destroyPreviousResult(update,start,end);
                Log.e("Destroy Previous",webUrl+"ActualWO/Createprocessmachine_duplicate?mc_no="+
                        mc_no+"&id_actual="+idActual+"&use_yn="+use_unuse+"&id_update="+update+"&start="+start+"&end="+end+"&remark=");
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void destroyPreviousResult(String update,String start,String end){
        showDialog();
        Call<DestroyPreviousResultRes> call = apiInterface.destroyPreviousResult(mc_no,idActual,use_unuse,update,start,end,null);
        call.enqueue(new Callback<DestroyPreviousResultRes>() {
            @Override
            public void onResponse(Call<DestroyPreviousResultRes> call, Response<DestroyPreviousResultRes> response) {
                if(response.isSuccessful()){
                    Log.e("Destroy previous",response.body().toString());
                    Toast.makeText(MachineActivity.this,"Successfully",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }else {
                    AlertError.alertError("The server response error", MachineActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DestroyPreviousResultRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), MachineActivity.this);
                dialog.dismiss();
            }
        });
    }

    private void getaddData(int page) { //Same get Data
        getData(page,true);
    }

}