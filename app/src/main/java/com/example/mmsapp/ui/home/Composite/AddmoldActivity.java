package com.example.mmsapp.ui.home.Composite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.mmsapp.ui.home.Composite.apiInterface.AddMoldAPI;
import com.example.mmsapp.ui.home.Composite.apiInterface.response.ScanMachineRes;
import com.example.mmsapp.ui.home.Composite.model.ItemCompositeMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class AddmoldActivity extends AppCompatActivity {
    String webUrl;
    private String idActual;
    private ProgressDialog dialog;
    List<ItemCompositeMaster> itemCompositeMasterArrayList;
    ItemCompositeAdapter itemCompositeAdapter;
    int page = 1;
    int total = -1;
    RecyclerView recyclerView;
    String mc_no ="",use_unuse="";
    FloatingActionButton scan;
    FloatingActionButton input;
    AddMoldAPI apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmold);
        setTitle("Mold");

        apiInterface = APIClient.getClient().create(AddMoldAPI.class);

        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        idActual = SharedPref.getInstance().get(Constants.ID_ACTUAL,String.class);
        dialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);


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
                scanMold();
            }
        });
        input.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               inputData();
           }
       });
        getData(page,null,false);
    }

    private void inputData() {

        final Dialog dialog = new Dialog(AddmoldActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(AddmoldActivity.this).inflate(R.layout.popup_input, null);
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
                    findScanMold(Containercode.getText().toString());
                    dialog.dismiss();
                }
            }
        });


        dialog.show();

    }

    private void scanMold() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(AddmoldActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                findScanMold(result.getContents());
            }
        }
    }

    private void findScanMold(String contents) {
//        new getData().execute(webUrl + "ActualWO/MoldMgtData?page=" + 1 + "&rows=50&sidx=&sord=asc&md_no=" + contents + "&md_nm=&_search=false");
        getData(1,contents,false);
        Log.e("Link",webUrl + "ActualWO/MoldMgtData?page=" + 1 + "&rows=50&sidx=&sord=asc&md_no=" + contents + "&md_nm=&_search=false");
        Log.e("Link",webUrl + "ActualWO/MoldMgtData?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");

    }

    private void getData(int pages, String contents, final boolean isLoadMore) {
        showDialog();
        //new getData().execute(webUrl + "ActualWO/MoldMgtData?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
        Call<ScanMachineRes> call = apiInterface.findMold(pages,50,null,"asc",contents,null,false);
        call.enqueue(new Callback<ScanMachineRes>() {
            @Override
            public void onResponse(Call<ScanMachineRes> call, Response<ScanMachineRes> response) {
                if(response.isSuccessful()){
                    ScanMachineRes res = response.body();
                    List<ItemCompositeMaster> listRes = res.getItemCompositeMasterList();
                    if(!isLoadMore){
                        itemCompositeMasterArrayList = new ArrayList<>();
                    }
                    if (listRes.size() == 0) {
                        dialog.dismiss();
                        AlertError.alertError("No data", AddmoldActivity.this);
                        return;
                    }
                    total = res.getTotal();
                    page = res.getPage();
                    if(isLoadMore){
                        itemCompositeMasterArrayList.addAll(listRes);
                        itemCompositeAdapter.notifyDataSetChanged();
                    }else {
                        itemCompositeMasterArrayList = listRes;
                        buildRecycleView();
                    }
                }else {
                    AlertError.alertError("The server response error", AddmoldActivity.this);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ScanMachineRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), AddmoldActivity.this);
                dialog.dismiss();
            }
        });

        Log.e("Link",webUrl + "ActualWO/MoldMgtData?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");

    }

    private void showDialog(){
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void buildRecycleView() {

        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(AddmoldActivity.this);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddmoldActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add mold");
        alertDialog.setMessage("You want: "+itemCompositeMasterArrayList.get(position).getMcNo()); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mc_no = itemCompositeMasterArrayList.get(position).getMcNo();
                use_unuse = "Y";
                Log.e("comfirmData",webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).getMcNo() +
                        "&use_yn=Y&id_actual="+
                        idActual +"&remark=");
                new comfirmData().execute(webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).getMcNo() +
                        "&use_yn=Y&id_actual="+
                        idActual +"&remark=");

            }
        });
        alertDialog.show();
    }

    private class comfirmData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
           dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getString("result").equals("0")){
                    thanhcong();
                }else if(jsonObject.getString("result").equals("1")){
                    AlertError.alertError("The Process Mold Unit was setting duplicate date", AddmoldActivity.this);
                }else if (jsonObject.getString("result").equals("2")){
                    AlertError.alertError("Start day was bigger End day. That is wrong", AddmoldActivity.this);
                } else if (jsonObject.getString("result").equals("3")){
                    xacnhan_datontai(jsonObject.getString("update"),jsonObject.getString("start"),jsonObject.getString("end"));
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlertError.alertError("Could not connect to server", AddmoldActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void thanhcong() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddmoldActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add mold");
        alertDialog.setMessage("Add mold finnish."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void xacnhan_datontai(final String update, final String start, final String end) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddmoldActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add mold");
        alertDialog.setMessage("This mold has already selected. If you confirm it, this mold will finish the task at the previous stage."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               new huyketquatruoc().execute(webUrl+"ActualWO/Createprocessmachine_duplicate?mc_no="+
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

    private class huyketquatruoc extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                dialog.dismiss();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                AlertError.alertError("Could not connect to server", AddmoldActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void getaddData(int page) {
        getData(page,null,true);
        Log.e("Link",webUrl + "ActualWO/MoldMgtData?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
    }
}