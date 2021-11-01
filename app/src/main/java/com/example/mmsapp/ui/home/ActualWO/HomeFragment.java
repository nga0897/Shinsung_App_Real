package com.example.mmsapp.ui.home.ActualWO;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.R;

import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.ActualWO.adapter.ActualWOHomeAdapter;
import com.example.mmsapp.ui.home.ActualWO.api.ActualWOAPI;
import com.example.mmsapp.ui.home.ActualWO.api.response.ActualWOHomeMasterRes;
import com.example.mmsapp.ui.home.ActualWO.model.ActualWOHomeMaster;
import com.example.mmsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private String webUrl;
    private RecyclerView recyclerView;
    private int total = -1;
    private int page = 1;
    private ProgressDialog dialog;
    private List<ActualWOHomeMaster> actualWOMasterArrayList;
    private ActualWOHomeAdapter actualWOHomeAdapter;
    private LinearLayout layoutSearchInput;
    private ImageView imvExpand,imvReloadPO;
    private Animation rotate;
    private ActualWOAPI apiInterface;
    private EditText edtPONo,edtProduct,edtProductName,edtModel,edtStartDate,edtEndDate;
    private Button btnSearch;
    private boolean isSearching = false;
    private Calendar startCalendar,endCalendar;

    private EditText containerCode;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        apiInterface = APIClient.getClient().create(ActualWOAPI.class);
        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutSearchInput = root.findViewById(R.id.layoutSearchInput);
        imvExpand = root.findViewById(R.id.imvDropDown);
        imvReloadPO = root.findViewById(R.id.reloadPO);
        imvReloadPO.setOnClickListener(this);
        connectSearchForm(root);
        imvExpand.setOnClickListener(this);

        dialog = new ProgressDialog(getContext(),R.style.AlertDialogCustom);

        root.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_pp_create();
            }
        });

        getData(page,false);
        return root;
    }

    private void rotateView(boolean isHidden) {
        if(isHidden){
            imvExpand.clearAnimation();
        }else {
            rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
            imvExpand.startAnimation(rotate);
        }
    }

    private void connectSearchForm(View view){
        edtPONo = view.findViewById(R.id.edtPONo);
        edtProduct = view.findViewById(R.id.edtProduct);
        edtProductName = view.findViewById(R.id.edtProductName);
        edtModel = view.findViewById(R.id.edtModel);
        edtStartDate = view.findViewById(R.id.edtStartDate);
        edtEndDate = view.findViewById(R.id.edtEndDate);
        btnSearch = view.findViewById(R.id.btnSearch);
        edtStartDate.setOnClickListener(this);
        edtEndDate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    private void searchPO() {
        String poNo = edtPONo.getText().toString();
        String product = edtProduct.getText().toString();
        String productName = edtProductName.getText().toString();
        String model = edtModel.getText().toString();
        String startDate = edtStartDate.getText().toString();
        if(startDate.equals(Constants.START_DATE)){
            startDate=null;
        }
        String endDate = edtEndDate.getText().toString();
        if(endDate.equals(Constants.END_DATE)){
            endDate = null;
        }
        searchPO(1,false,poNo,product,productName,model,startDate,endDate);

    }

    private void searchPO(int pages,final boolean isLoadMore,String poNo,String product,String productName,String model,String startDate,String endDate){
        Call<ActualWOHomeMasterRes> call = apiInterface.searchPo(20,pages,null,"asc",poNo,product,productName,model,startDate,endDate);
        call.enqueue(new Callback<ActualWOHomeMasterRes>() {
            @Override
            public void onResponse(Call<ActualWOHomeMasterRes> call, Response<ActualWOHomeMasterRes> response) {
                if(response.isSuccessful()){
                    isSearching = true;
                    ActualWOHomeMasterRes result = response.body();
                    imvReloadPO.setVisibility(View.VISIBLE);
                    if(result.getActualWOHomeMasterList().size()==0){
                        dialog.dismiss();
                        AlertError.alertError("No data", getContext());
                        actualWOMasterArrayList = result.getActualWOHomeMasterList();
                        setListView();
                        return;
                    }
                    total = result.getTotal();
                    page = result.getPage();
                    if(isLoadMore){
                        actualWOMasterArrayList.addAll(result.getActualWOHomeMasterList());
                        actualWOHomeAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        return;
                    }else {
                        actualWOMasterArrayList = result.getActualWOHomeMasterList();
                    }
                    dialog.dismiss();
                    setListView();
                }else {
                    dialog.dismiss();
                    AlertError.alertError("The server response error", getContext());
                }
            }

            @Override
            public void onFailure(Call<ActualWOHomeMasterRes> call, Throwable t) {
                dialog.dismiss();
                call.cancel();
                AlertError.alertError(t.getMessage(), getContext());
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void open_pp_create() {

        final Dialog dialog = new Dialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_create_first, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
// click drawable in TextInputEditText
        containerCode = dialog.findViewById(R.id.Containercode);
        containerCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (containerCode.getRight() - containerCode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        openScan();
                        return true;
                    }
                }
                return false;
            }
        });
        final EditText num_div = dialog.findViewById(R.id.num_div);
        final Button confirm = dialog.findViewById(R.id.confirm);
        final EditText remark = dialog.findViewById(R.id.remark);
        final TextInputLayout h2;
        final TextInputLayout h1;
        final TextInputLayout h3;
        h1 = dialog.findViewById(R.id.H1);
        h2 = dialog.findViewById(R.id.H2);
        h3 = dialog.findViewById(R.id.H3);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num_div.getText().toString() == null || num_div.getText().toString().length() == 0) {
                    h2.setErrorEnabled(true);
                    h1.setError(null);
                    h3.setError(null);
                    h2.setError("Please, Input here.");
                    return;
                } else if (containerCode.getText().toString() == null || containerCode.getText().toString().length() == 0) {

                    h2.setError(null);
                    h1.setErrorEnabled(true);
                    h3.setError(null);
                    h1.setError("Please, Input here.");
                    return;
                } else {
                    h1.setError(null);
                    h2.setError(null);
                    h3.setError(null);

                    new create().execute(webUrl+"ActualWO/Add_w_actual_primary?product=" + containerCode.getText().toString() +"&target="+num_div.getText().toString()
                    +"&remark="+remark.getText().toString());
                    Log.e("create",webUrl+"ActualWO/Add_w_actual_primary?product=" + containerCode.getText().toString() +"&target="+num_div.getText().toString()
                            +"&remark="+remark.getText().toString());
                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSearch:
                searchPO();
                break;
            case R.id.imvDropDown:
                if(layoutSearchInput.getVisibility() == View.VISIBLE){
                    layoutSearchInput.setVisibility(View.GONE);
                    rotateView(true);
                }else {
                    layoutSearchInput.setVisibility(View.VISIBLE);
                    rotateView(false);
                }
                break;
            case R.id.reloadPO:
                rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_360);
                imvReloadPO.startAnimation(rotate);
                resetSearchInput();
                getData(1,false);
                imvReloadPO.clearAnimation();
                break;
            case R.id.edtStartDate:
                showDatePickerDialog(true);
                break;
            case R.id.edtEndDate:
                showDatePickerDialog(false);
                break;
        }
    }

    private void showDatePickerDialog(final boolean isStartDate){
        Log.e("Phaydev:: ","Show date picker");
            Calendar c = Calendar.getInstance();
            int selectedYear = c.get(Calendar.YEAR);
            int selectedMonth = c.get(Calendar.MONTH);
            int selectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String result = (year + "-" + String.format("%02d", monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    if(isStartDate){
                        startCalendar = Calendar.getInstance();
                        startCalendar.set(Calendar.YEAR,year);
                        startCalendar.set(Calendar.MONTH,monthOfYear);
                        startCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        startCalendar.clear(Calendar.HOUR_OF_DAY);
                        startCalendar.clear(Calendar.HOUR);
                        startCalendar.clear(Calendar.MINUTE);
                        startCalendar.clear(Calendar.SECOND);
                        startCalendar.clear(Calendar.MILLISECOND);
                        boolean isValid = compareDate(true);
                        if(isValid){
                            edtStartDate.setText(result);
                        }
                    }else {
                        endCalendar = Calendar.getInstance();
                        endCalendar.set(Calendar.YEAR,year);
                        endCalendar.set(Calendar.MONTH,monthOfYear);
                        endCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        endCalendar.clear(Calendar.HOUR_OF_DAY);
                        endCalendar.clear(Calendar.HOUR);
                        endCalendar.clear(Calendar.MINUTE);
                        endCalendar.clear(Calendar.SECOND);
                        endCalendar.clear(Calendar.MILLISECOND);
                        boolean isValid = compareDate(false);
                        if(isValid){
                            edtEndDate.setText(result);
                        }
                    }
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();

    }

    private boolean compareDate(boolean isStartCalendar) {
        if(isStartCalendar){
            if (endCalendar!=null){
                if(startCalendar.compareTo(endCalendar)>0){
                    Toast.makeText(getContext(),"Ngày bắt đầu không được lớn hơn ngày kết thúc",Toast.LENGTH_SHORT).show();
                    System.out.println(startCalendar.getTime());
                    System.out.println(endCalendar.getTime());
                    startCalendar = null;
                    return false;
                }else {
                    return true;
                }
            }

        }else {
            if (startCalendar!=null){
                if(endCalendar.compareTo(startCalendar)<0){
                    Toast.makeText(getContext(),"Ngày kết thúc không được nhỏ hơn ngày bắt đầu",Toast.LENGTH_SHORT).show();
                    System.out.println(startCalendar.getTime());
                    System.out.println(endCalendar.getTime());
                    endCalendar = null;
                    return false;
                }else {
                    return true;
                }
            }
        }
        return true;
    }

    private void resetSearchInput() {
        edtPONo.setText("");
        edtProduct.setText("");
        edtProductName.setText("");
        edtModel.setText("");
        edtStartDate.setText("");
        edtEndDate.setText("");
    }

    private class create extends AsyncTask<String, Void, String> {
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
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    getData(1,false);
                }else {
                    AlertError.alertError(jsonObject.getString("kq"), getContext());
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlertError.alertError("Could not connect to server", getContext());
                dialog.dismiss();
            }
        }

    }

    private void openScan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                containerCode.setText(result.getContents());
            }
        }
    }

    private void showDialog(){
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void getData(int pages, final boolean isLoadMore) {
        showDialog();
        Call<ActualWOHomeMasterRes> call = apiInterface.getListActualWOHomeMaster(20,pages,null,"asc");
        call.enqueue(new Callback<ActualWOHomeMasterRes>() {
            @Override
            public void onResponse(Call<ActualWOHomeMasterRes> call, Response<ActualWOHomeMasterRes> response) {
                if(response.isSuccessful()){
                    isSearching = false;
                    imvReloadPO.setVisibility(View.INVISIBLE);
                    ActualWOHomeMasterRes result = response.body();
                    if(result.getActualWOHomeMasterList().size()==0){
                        dialog.dismiss();
                        AlertError.alertError("No data", getContext());
                        actualWOMasterArrayList = result.getActualWOHomeMasterList();
                        setListView();
                        return;
                    }
                    total = result.getTotal();
                    page = result.getPage();
                    if(isLoadMore){
                        actualWOMasterArrayList.addAll(result.getActualWOHomeMasterList());
                        actualWOHomeAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        return;
                    }else {
                        actualWOMasterArrayList = result.getActualWOHomeMasterList();
                    }
                    dialog.dismiss();
                    setListView();
                }else {
                    dialog.dismiss();
                    AlertError.alertError("The server response error", getContext());
                }
            }

            @Override
            public void onFailure(Call<ActualWOHomeMasterRes> call, Throwable t) {
                dialog.dismiss();
                call.cancel();
                AlertError.alertError(t.getMessage(), getContext());

            }
        });
    }

    private void setListView() {
        dialog.dismiss();
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        actualWOHomeAdapter = new ActualWOHomeAdapter(actualWOMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(actualWOHomeAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == actualWOMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getAddData(page + 1);
                    }
                }
            }
        });
        actualWOHomeAdapter.setOnItemClickListener(new ActualWOHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getContext(), ManufacturingActivity.class);
//                intent.putExtra("atNo",actualWOMasterArrayList.get(position).getAtNo());
//                intent.putExtra("product",actualWOMasterArrayList.get(position).getProduct());
                SharedPref.getInstance().put(Constants.AT_NO,actualWOMasterArrayList.get(position).getAtNo());
                SharedPref.getInstance().put(Constants.PRODUCT,actualWOMasterArrayList.get(position).getProduct());
                startActivity(intent);
            }
        });
    }

    private void getAddData(int pages) {
        if(isSearching){
            String startDate = edtStartDate.getText().toString();
            if(startDate.equals(Constants.START_DATE)){
                startDate = null;
            }
            String endDate = edtEndDate.getText().toString();
            if(endDate.equals(Constants.END_DATE)){
                endDate = null;
            }
            searchPO(pages,true,edtPONo.getText().toString(),edtProduct.getText().toString(),edtProductName.getText().toString(),edtModel.getText().toString(),startDate,endDate);
        }else {
            getData(pages,true);
        }
    }

}