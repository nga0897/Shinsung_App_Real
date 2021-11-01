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
import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Composite.adapter.ItemStaffAdapter;
import com.example.mmsapp.ui.home.Composite.apiInterface.CompositeAPI;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ConfirmAddWorkerRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.DestroyOldResultRes;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ListWorkerRes;
import com.example.mmsapp.ui.home.Composite.model.ItemStaffMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerActivity extends AppCompatActivity {
    String webUrl;
    private String idActual;
    private ProgressDialog dialog;
    List<ItemStaffMaster> itemStaffMasterArrayList = new ArrayList<>();
    ItemStaffAdapter itemStaffAdapter;
    int page = 1;
    int total = -1;
    RecyclerView recyclerView;
    CompositeAPI apiInterface;
    String mcNo = "", useYN = "";
    private String positionOfControllerCode = "";
    FloatingActionButton scan;
    FloatingActionButton input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        setTitle("Worker");
        apiInterface = APIClient.getClient().create(CompositeAPI.class);
        webUrl = SharedPref.getInstance().get(Constants.BASE_URL, String.class);
        idActual = SharedPref.getInstance().get(Constants.ID_ACTUAL,String.class);
        dialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        getData(page, Constants.GET_ALL, null); // barcode auto = null

        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getVisibility() == View.VISIBLE) {
                    input.setVisibility(View.GONE);
                    scan.setVisibility(View.GONE);
                } else {
                    input.setVisibility(View.VISIBLE);
                    scan.setVisibility(View.VISIBLE);
                }

            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWorker();
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

        final Dialog dialog = new Dialog(WorkerActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(WorkerActivity.this).inflate(R.layout.popup_input, null);
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
                } else {
                    h2.setError(null);
                    findScanWorker(Containercode.getText().toString());
                    dialog.dismiss();
                }


            }
        });


        dialog.show();

    }

    private void scanWorker() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(WorkerActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                findScanWorker(result.getContents());
            }
        }
    }

    private void findScanWorker(String contents) {
        getData(1, Constants.SCAN, contents);
        //queryPage = 1 is default, because contents (result of scan) may be null, so we use isScan variable to make sure not mistakenly requested
    }

    private void getData(int queryPage, final int type, String barcode) {
        showDialog();
        Call<ListWorkerRes> call;
        if (type==Constants.SCAN) {
            itemStaffMasterArrayList = new ArrayList<>();
            call = apiInterface.scanWorker(queryPage, 50, null, "asc", barcode, null, false);
        } else {
            call = apiInterface.getListWorker(queryPage, 50, null, "asc", null, null, false);
        }
        call.enqueue(new Callback<ListWorkerRes>() {
            @Override
            public void onResponse(Call<ListWorkerRes> call, Response<ListWorkerRes> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", response.code() + "");
                    ListWorkerRes result = response.body();
                    List<ItemStaffMaster> resultList = result.getListItem();
                    if (result == null) {
                        AlertError.alertError("No data", WorkerActivity.this);
                        dialog.dismiss();
                        return;
                    }
                    total = response.body().getTotal();
                    page = response.body().getPage();
                    itemStaffMasterArrayList.addAll(resultList);
                    dialog.dismiss();
                    if(type==Constants.GET_ALL||type==Constants.SCAN){
                        buildRecycleView();
                    }else if(type==Constants.LOAD_MORE){
                        itemStaffAdapter.notifyDataSetChanged();
                    }
                } else {
                    AlertError.alertError("The server response error", WorkerActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ListWorkerRes> call, Throwable t) {
                dialog.dismiss();
                call.cancel();
                AlertError.alertError(t.getMessage(), WorkerActivity.this);
            }
        });
    }

    private void showDialog() {
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void buildRecycleView() {
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(WorkerActivity.this);
        itemStaffAdapter = new ItemStaffAdapter(itemStaffMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemStaffAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == itemStaffMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getData(page + 1,Constants.LOAD_MORE,null); //barcode auto = null

                    }
                }
            }
        });

        itemStaffAdapter.setOnItemClickListener(new ItemStaffAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showConfirmAddDialog(position);
            }
        });

    }

    private void showConfirmAddDialog(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("You want: "); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mcNo = itemStaffMasterArrayList.get(position).getUserId();
                useYN = "Y";
                handleConfirmAddWorker(itemStaffMasterArrayList.get(position).getUserId(), idActual, useYN);

            }
        });

        alertDialog.show();
    }

    private void handleConfirmAddWorker(String userId, String idActual, String useYN) {
        Call<BaseMessageResponse> call = apiInterface.confirmAddWorker(userId, idActual, useYN);
        call.enqueue(new Callback<BaseMessageResponse>() {
            @Override
            public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                if (response.isSuccessful()) {
                    BaseMessageResponse responseData = response.body();
                    if (responseData.isResult()){
                        showSuccessDialog();
                    }else {
                        AlertError.alertError(responseData.getMessage(),WorkerActivity.this);
                    }
                } else {
                    AlertError.alertError("The server response error", WorkerActivity.this);
                }
            }

            @Override
            public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), WorkerActivity.this);
            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("Add Worker finnish."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void showDialogWorkerIsExist(final String update, final String start, final String end) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("This Worker has already selected. If you confirm it, this Worker will finish the task at the previous stage."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDialog();
                handleDestroyOldResult(positionOfControllerCode,mcNo,idActual,useYN,update,start,end);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void handleDestroyOldResult(String positionOfControllerCode, String mcNo, String idActual, String useYN, String update, String start, String end) {
        Call<DestroyOldResultRes> call = apiInterface.destroyOldResult(positionOfControllerCode,mcNo,idActual,useYN,update,start,end,null);
        call.enqueue(new Callback<DestroyOldResultRes>() {
            @Override
            public void onResponse(Call<DestroyOldResultRes> call, Response<DestroyOldResultRes> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    Log.e("Phaydev:: ","Destroy old result success");
                    finish();
                }else {
                    AlertError.alertError("The server response error", WorkerActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DestroyOldResultRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), WorkerActivity.this);
                dialog.dismiss();
            }
        });
    }

}