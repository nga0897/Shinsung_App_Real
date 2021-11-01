package com.example.mmsapp.ui.home.Divide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.R;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.Divide.adapter.DivideAdapter;
import com.example.mmsapp.ui.home.Divide.api.DivideAPI;
import com.example.mmsapp.ui.home.Divide.api.response.DeceviceStaRes;
import com.example.mmsapp.ui.home.Divide.api.response.GetMtDateWebRes;
import com.example.mmsapp.ui.home.Divide.model.DivideMaster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivideActivity extends AppCompatActivity {

    private String idActual;
    private int page = 1;
    private ProgressDialog dialog;
    private TextView nodata;
    private List<DivideMaster> mappingMasterArrayList;
    private DivideAdapter divideAdapter;
    private int total = -1;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private EditText Containercode;
    private DivideAPI apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divide);
        setTitle("Divide");
        apiInterface = APIClient.getClient().create(DivideAPI.class);
        idActual = SharedPref.getInstance().get(Constants.ID_ACTUAL,String.class);
        recyclerView = findViewById(R.id.recyclerView);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(this, R.style.AlertDialogCustom);

    }

    private void showDialog(){
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }


    private void getData(int pages, final boolean isLoadMore) {
        showDialog();
        Call<GetMtDateWebRes> call = apiInterface.getMtDateWeb(idActual,pages,50,null,"asc",false,null,null,null);
        call.enqueue(new Callback<GetMtDateWebRes>() {
            @Override
            public void onResponse(Call<GetMtDateWebRes> call, Response<GetMtDateWebRes> response) {
                if(response.isSuccessful()){
                    GetMtDateWebRes res = response.body();
                    total = res.getTotal();
                    page = res.getPage();
                    List<DivideMaster> resultList = res.getDivideMasterList();
                    if (resultList.size() == 0) {
                        dialog.dismiss();
                        nodata.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(isLoadMore){
                        mappingMasterArrayList.addAll(resultList);
                        divideAdapter.notifyDataSetChanged();
                    }else {
                        mappingMasterArrayList = new ArrayList<>();
                        mappingMasterArrayList = resultList;
                        setRecyc();
                    }
                }else {
                    nodata.setVisibility(View.VISIBLE);
                    AlertError.alertError("The server response error", DivideActivity.this);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<GetMtDateWebRes> call, Throwable t) {
                call.cancel();
                nodata.setVisibility(View.VISIBLE);
                AlertError.alertError(t.getMessage(), DivideActivity.this);
                dialog.dismiss();
            }
        });
    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        divideAdapter = new DivideAdapter(mappingMasterArrayList,SharedPref.getInstance().get(Constants.DIVIDE_SELECTED_POS,Integer.class));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(divideAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == mappingMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getAddData(page + 1);

                    }
                }
            }
        });

        divideAdapter.setOnItemClickListener(new DivideAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedPref.getInstance().put(Constants.DIVIDE_SELECTED_POS,position);
                view.setBackgroundColor(Color.parseColor("#594691"));
                openDetailDiv(mappingMasterArrayList.get(position).getMtCd());
            }

            @Override
            public void onDiv(int position) {
                SharedPref.getInstance().put(Constants.DIVIDE_SELECTED_POS,position);
                popupDiv(position,mappingMasterArrayList.get(position).getGrQty());

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popupDiv(final int position,final int realQuantity) {

        final Dialog dialog = new Dialog(DivideActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(DivideActivity.this).inflate(R.layout.popup_input_div, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        Containercode = dialog.findViewById(R.id.Containercode);
        Containercode.setVisibility(View.GONE);
        Containercode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Containercode.getRight() - Containercode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        openScan();
                        return true;
                    }
                }
                return false;
            }
        });

        final EditText num_div = dialog.findViewById(R.id.num_div);
        final Button confirm = dialog.findViewById(R.id.confirm);
        final TextInputLayout h2;
        final TextInputLayout h1;

        h1 = dialog.findViewById(R.id.H1);
        h2 = dialog.findViewById(R.id.H2);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = 0;
                int naturalPart = realQuantity/Integer.parseInt(num_div.getText().toString());
                int remainder = realQuantity%Integer.parseInt(num_div.getText().toString());
                if(remainder>0){
                    result = naturalPart+1;
                }else {
                    result = naturalPart;
                }
                if (num_div.getText().toString() == null || num_div.getText().toString().length() == 0) {
                    h1.setErrorEnabled(true);
                    h2.setError(null);
                    h1.setError("Vui lòng nhập giá trị để chia");
                }else if(naturalPart<1){
                    h1.setError("Vui lòng kiểm tra lại sản lượng có trong đồ đựng, nó phải lớn hơn hoặc bằng 50");
                } else if (Integer.parseInt(num_div.getText().toString())<50) {
                    h1.setError("Vui lòng không nhập nhỏ hơn 50");
                } else if(result>100){
                    Toast.makeText(DivideActivity.this, "Số lượng hộp nhỏ được chia ra không được vượt quá 100", Toast.LENGTH_SHORT).show();
                } else {
                    h1.setError(null);
                    h2.setError(null);
                    confirmDiv(mappingMasterArrayList.get(position).getMtCd(),num_div.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void confirmDiv(final String mtCd, String numberDiv){
        showDialog();
        Call<DeceviceStaRes> call = apiInterface.divideSta(mtCd,numberDiv);
        call.enqueue(new Callback<DeceviceStaRes>() {
            @Override
            public void onResponse(Call<DeceviceStaRes> call, Response<DeceviceStaRes> response) {
                if(response.isSuccessful()){
                    DeceviceStaRes res = response.body();
                    if (!res.isResult()){
                        dialog.dismiss();
                        AlertError.alertError(res.getMessage(), DivideActivity.this);
                        return;
                    }
                    openDetailDiv(mtCd);
                }else {
                    AlertError.alertError("The server response error", DivideActivity.this);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DeceviceStaRes> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), DivideActivity.this);
                dialog.dismiss();
            }
        });
    }

    private void openDetailDiv(String mlNo) {
        Intent intent =new Intent(DivideActivity.this, DivDetailActivity.class);
        intent.putExtra("mlNo", mlNo);
        startActivity(intent);
    }

    private void openScan() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(DivideActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Containercode.setText(result.getContents());
            }
        }
    }

    private void getAddData(int page) {
        getData(page,true);
    }


    @Override
    protected void onResume() {
        getData(page,false);
        super.onResume();
    }
}