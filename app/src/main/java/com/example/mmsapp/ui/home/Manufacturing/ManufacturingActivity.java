package com.example.mmsapp.ui.home.Manufacturing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.R;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.ActualWO.api.ActualWOAPI;
import com.example.mmsapp.ui.home.ActualWO.api.response.GetDatawActualRes;
import com.example.mmsapp.ui.home.Composite.CompositeActivity;
import com.example.mmsapp.ui.home.Manufacturing.adapter.ActualWOAdapter;
import com.example.mmsapp.ui.home.Manufacturing.adapter.ActualdetailAdapter;
import com.example.mmsapp.ui.home.Manufacturing.model.ActualWOMaster;
import com.example.mmsapp.ui.home.Manufacturing.model.ActualWOdetailMaster;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class ManufacturingActivity extends AppCompatActivity {
    String webUrl;
    List<ActualWOMaster> actualWOMasterArrayList;
    List<ActualWOdetailMaster> actualWODetailMasterArrayList;
    ActualdetailAdapter actualdetailAdapter;
    ActualWOAdapter adapter;
    ListView theListView;
    RecyclerView recyclerView;
    View viewDetail;
    int page =1;
    TextView totalDetail,contentRequestBtn;
    private ProgressDialog dialog;
    private int pressPosition = -1;
    int total=-1;
    private String atNo;
    private String product;
    ImageView im_delete;
    ActualWOAPI apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturing);
        apiInterface = APIClient.getClient().create(ActualWOAPI.class);
        setTitle("Manufacturing");
        atNo = SharedPref.getInstance().get(Constants.AT_NO,String.class);
        product = SharedPref.getInstance().get(Constants.PRODUCT,String.class);
        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        theListView = findViewById(R.id.mainListView);
        dialog = new ProgressDialog(this,R.style.AlertDialogCustom);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(page,false);
    }

    private void getData(int pages, final boolean isLoadMore) {
//        new getData().execute(webUrl+ "ActualWO/Getdataw_actual?rows=50&page="+ page+"&sidx=&sord=asc&at_no="+atNo);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        Call<GetDatawActualRes> call = apiInterface.getListDatawActual(20,pages,null,"asc",atNo);
        call.enqueue(new Callback<GetDatawActualRes>() {
            @Override
            public void onResponse(Call<GetDatawActualRes> call, Response<GetDatawActualRes> response) {
                if(response.isSuccessful()){
                    GetDatawActualRes res = response.body();
                    total = res.getTotal();
                    page = res.getPage();
                    if (res.getListActualWOMaster().size() == 0) {
                        dialog.dismiss();
                        AlertError.alertError("No data", ManufacturingActivity.this);
                        return;
                    }
                    if(isLoadMore){
                        actualWOMasterArrayList.addAll(res.getListActualWOMaster());
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                        return;

                    }else {
                        actualWOMasterArrayList = res.getListActualWOMaster();
                    }

                    dialog.dismiss();
                    setListView();
                }else {
                    AlertError.alertError("The server response error", ManufacturingActivity.this);
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<GetDatawActualRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), ManufacturingActivity.this);
                dialog.dismiss();
            }
        });
        Log.e("getData",webUrl+ "ActualWO/Getdataw_actual?rows=50&page="+ page+"&sidx=&sord=asc&at_no="+atNo);
    }

    private void getAddData(int page) {
        getData(page,true);
        Log.e("getaddData",webUrl+ "ActualWO/Getdataw_actual?rows=50&page="+ page+"&sidx=&sord=asc&at_no="+atNo);
    }

    private void setListView() {
        adapter = new ActualWOAdapter(ManufacturingActivity.this, actualWOMasterArrayList);
        theListView.setAdapter(adapter);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                // toggle clicked cell state

                recyclerView =  view.findViewById(R.id.recycview);
                totalDetail =  view.findViewById(R.id.totaldetail);
                im_delete =  view.findViewById(R.id.im_delete);
                viewDetail = view;
                pressPosition = pos;
                contentRequestBtn =  view.findViewById(R.id.content_request_btn);
                contentRequestBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ManufacturingActivity.this, CompositeActivity.class);
                        intent.putExtra("product",product);
                        SharedPref.getInstance().put(Constants.ID_ACTUAL,actualWOMasterArrayList.get(pos).getId_actual());
                        SharedPref.getInstance().put(Constants.QC_CODE,actualWOMasterArrayList.get(pos).getItem_vcd());
                        SharedPref.getInstance().put(Constants.STYLE_NO,actualWOMasterArrayList.get(pos).getProduct());
                        SharedPref.getInstance().put(Constants.STYLE_NAME,actualWOMasterArrayList.get(pos).getName());
                        if (pos == actualWOMasterArrayList.size()-1){
                            SharedPref.getInstance().put(Constants.CHECK_LAST,"last");
                        }else {
                            SharedPref.getInstance().put(Constants.CHECK_LAST,"");
                        }
                        startActivity(intent);
                    }
                });
                loadDataDetail(pos);

                im_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ManufacturingActivity.this, R.style.AlertDialogCustom);
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("Warning!!!");
                        alertDialog.setMessage("Are you sure Delete?");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new onDelete().execute(webUrl + "ActualWO/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).getId_actual());
                                Log.e("onDelete", webUrl + "ActualWO/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).getId_actual());
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
        });



        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view.getLastVisiblePosition()== totalItemCount-1)
                {
                    if (page < total){
                        total = -1;
                        getAddData(page+1);

                    }
                }
            }
        });
    }

    private class onDelete extends AsyncTask<String, Void, String> {
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
                if (jsonObject.getBoolean("result")){
                    Toast.makeText(ManufacturingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }else {
                    AlertError.alertError(jsonObject.getString("message"), ManufacturingActivity.this);
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlertError.alertError("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void loadDataDetail(int pos) {
//        new loaddatadetail().execute(webUrl+ "ActualWO/getdetail_actual?id=" +
//                actualWOMasterArrayList.get(pos).id_actual +
//                "&_search=false&nd=1602733203479&rows=10&page=1&sidx=&sord=asc");
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        Call<List<ActualWOdetailMaster>> call = apiInterface.getListDetailActual(actualWOMasterArrayList.get(pos).getId_actual(),false,"1602733203479",10,1,null,"asc");
        call.enqueue(new Callback<List<ActualWOdetailMaster>>() {
            @Override
            public void onResponse(Call<List<ActualWOdetailMaster>> call, Response<List<ActualWOdetailMaster>> response) {
                if(response.isSuccessful()){
                    actualWODetailMasterArrayList = response.body();
                    setDetail();
                }else {
                    dialog.dismiss();
                    AlertError.alertError("The server response error", ManufacturingActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<ActualWOdetailMaster>> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                AlertError.alertError(t.getMessage(), ManufacturingActivity.this);
            }
        });

        Log.e("loaddatadetail",webUrl+ "ActualWO/getdetail_actual?id=" +
                actualWOMasterArrayList.get(pos).getId_actual() +
                "&_search=false&nd=1602733203479&rows=10&page=1&sidx=&sord=asc");
    }

    private void setDetail() {
        dialog.dismiss();
        RecyclerView.LayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ManufacturingActivity.this);
        actualdetailAdapter = new ActualdetailAdapter(actualWODetailMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(actualdetailAdapter);
        totalDetail.setText(actualWODetailMasterArrayList.size()+" ML No");
        ((FoldingCell) viewDetail).toggle(true);
        for (int i = 0;i<actualWOMasterArrayList.size();i++) {
            if (i!=pressPosition) {
                adapter.registerFold(i);
            }else {
                adapter.registerToggle(pressPosition);
            }
        }

    }
}