package com.example.mmsapp.ui.home.QCcheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.Constants;
import com.example.mmsapp.R;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.ui.home.QCcheck.adapter.QCcheckDetailAdapter;
import com.example.mmsapp.ui.home.QCcheck.adapter.QCcheckDetailChildAdapter;
import com.example.mmsapp.ui.home.QCcheck.adapter.QcCheckerLessAdapter;
import com.example.mmsapp.ui.home.QCcheck.model.QCCheckDetailChildMaster;
import com.example.mmsapp.ui.home.QCcheck.model.QCCheckdetailMaster;
import com.example.mmsapp.ui.home.QCcheck.model.QcCheckerLessItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.mmsapp.Url.NoiDung_Tu_URL;

public class QCCheckActivity extends AppCompatActivity {
    String webUrl;
    private RecyclerView recyclerViewCHECK;
    TextView tv_qcheck_mlno, tv_qcheck_date, tv_qcheck_defectqty;
    EditText tv_qcheck_checkqty, tv_qcheck_okcheck;
    private List<QcCheckerLessItem> rViewItemJSON;
    private QcCheckerLessAdapter adapterLess;
    private int numGrQty;
    private int valDefect = 0;
    private ProgressDialog dialog;
    Dialog filterDialog;
    RecyclerView rycviewdetail;
    ArrayList<QCCheckdetailMaster> qcCheckdetailMasters;
    QCcheckDetailAdapter qCcheckDetailAdapter;
    ArrayList<QCCheckDetailChildMaster> qcCheckDetailChildMasters;
    QCcheckDetailChildAdapter qCcheckDetailChildAdapter;
    RecyclerView recyclerViewchild;
    private String qcCode;
    private String mlNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc_check);
        setTitle("QC Check");
        numGrQty = SharedPref.getInstance().get(Constants.NUM_GR_QTY,Integer.class);
        webUrl = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        qcCode = SharedPref.getInstance().get(Constants.QC_CODE,String.class);
        mlNo = SharedPref.getInstance().get(Constants.ML_NO,String.class);
        recyclerViewCHECK = findViewById(R.id.recyclerViewContent);
        tv_qcheck_mlno = findViewById(R.id.tv_qcheck_mlno);
        tv_qcheck_date = findViewById(R.id.tv_qcheck_date);
        tv_qcheck_checkqty = findViewById(R.id.tv_qcheck_checkqty);
        tv_qcheck_okcheck = findViewById(R.id.tv_qcheck_okcheck);
        tv_qcheck_defectqty = findViewById(R.id.tv_qcheck_defectqty);
        dialog = new ProgressDialog(QCCheckActivity.this, R.style.AlertDialogCustom);
        tv_qcheck_mlno.setText(mlNo);
        tv_qcheck_date.setText(qcCode);
        tv_qcheck_checkqty.setText(numGrQty + "");

        new LoadCheckQc().execute(webUrl + "ActualWO/Popup_Qc_Check_API?item_vcd=" + qcCode);

        final int[] ck = {0};
        final int[] ok = {0};

        tv_qcheck_okcheck.setText("0");
        tv_qcheck_checkqty.setText("0");

        //show detail
        findViewById(R.id.header_p_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pp_detail();
            }
        });

        tv_qcheck_okcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                for (int i = 0; i < rViewItemJSON.size(); i++) {
//                    rViewItemJSON.get(i).setQty(0 + "");
//                    rViewItemJSON.get(i).setCheck(false);
//                }
//                adapterLess.notifyDataSetChanged();


                if (tv_qcheck_checkqty.getText().length() > 0) {
                    ck[0] = Integer.parseInt(tv_qcheck_checkqty.getText().toString().trim());
                } else {
                    ck[0] = 0;
                }

                if (tv_qcheck_okcheck.getText().length() > 0) {
                    ok[0] = Integer.parseInt(tv_qcheck_okcheck.getText().toString().trim());
                    if (ok[0] > ck[0]) {
                        tv_qcheck_okcheck.setError("less than or equal \"" + ck[0] + "\"");
                        tv_qcheck_okcheck.setText("" + ck[0]);
                    }
                } else {
                    ok[0] = 0;
                }

                if (ck[0] - ok[0] >= 0) {
                    valDefect = ck[0] - ok[0];
                    String itx = valDefect + "";
                    tv_qcheck_defectqty.setText(itx);
                } else {
                    valDefect = 0;
                    tv_qcheck_defectqty.setText("");
                    tv_qcheck_okcheck.setError("less than or equal \"" + ck[0] + "\"");
                    //tv_qcheck_okcheck.requestFocus();
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_qcheck_checkqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                for (int i = 0; i < rViewItemJSON.size(); i++) {
//                    rViewItemJSON.get(i).setQty(0 + "");
//                    rViewItemJSON.get(i).setCheck(false);
//                }
//                adapterLess.notifyDataSetChanged();


                if (tv_qcheck_checkqty.getText().length() > 0) {
                    ck[0] = Integer.parseInt(tv_qcheck_checkqty.getText().toString().trim());
                    if (Integer.parseInt(tv_qcheck_checkqty.getText().toString()) > numGrQty) {
                        tv_qcheck_checkqty.setError("less than or equal \"" + numGrQty + "\"");
                        tv_qcheck_checkqty.setText("" + numGrQty);
                    }
                } else {
                    ck[0] = 0;
                }

                if (tv_qcheck_okcheck.getText().length() > 0) {
                    ok[0] = Integer.parseInt(tv_qcheck_okcheck.getText().toString().trim());
                } else {
                    ok[0] = 0;
                }

                if (ck[0] - ok[0] >= 0) {
                    valDefect = ck[0] - ok[0];
                    String itx = valDefect + "";
                    tv_qcheck_defectqty.setText(itx);
                } else {
                    valDefect = 0;
                    tv_qcheck_defectqty.setText("");
                    tv_qcheck_okcheck.setError("less than or equal \"" + ck[0] + "\"");
                    //tv_qcheck_okcheck.requestFocus();
                    return;
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void pp_detail() {
        filterDialog = new Dialog(QCCheckActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        filterDialog.setContentView(R.layout.popup_detail_qc_check);
        filterDialog.setCancelable(false);
        filterDialog.getWindow().setLayout(getWidth(QCCheckActivity.this), ((getHight(QCCheckActivity.this) / 100) * 90));
        filterDialog.getWindow().setGravity(Gravity.BOTTOM);


        filterDialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.cancel();
            }
        });
        rycviewdetail = filterDialog.findViewById(R.id.rycview);

        getdatadetail();

        filterDialog.show();

    }

    private void getdatadetail() {
        new getdatadetail().execute(webUrl + "ActualWO/Getfacline_qc?mt_cd=" + mlNo +"&item_vcd="+qcCode);
    }

    class getdatadetail extends AsyncTask<String, Integer, String> {

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
            qcCheckdetailMasters = new ArrayList<>();
            String fqno,fq_no,work_dt,check_qty,ok_qty,defect_qty;

            try{
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length()==0){
                    dialog.dismiss();
                    AlertError.alertError("No data", QCCheckActivity.this);
                    return;
                }
                for (int i = 0 ;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    fqno = jsonObject.getString("fqno");
                    fq_no = jsonObject.getString("fq_no");
                    work_dt = jsonObject.getString("work_dt");
                    check_qty = jsonObject.getString("check_qty");
                    ok_qty = jsonObject.getString("ok_qty");
                    defect_qty = jsonObject.getString("defect_qty");
                    qcCheckdetailMasters.add(new QCCheckdetailMaster(fqno,fq_no,work_dt,check_qty,ok_qty,defect_qty));
                }
                builryc();

            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                AlertError.alertError("Could not connect to server", QCCheckActivity.this);
            }


        }
    }

    private void builryc() {
        dialog.dismiss();
        final LinearLayoutManager mLayoutManager;
        rycviewdetail.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(QCCheckActivity.this);
        qCcheckDetailAdapter = new QCcheckDetailAdapter(qcCheckdetailMasters);
        rycviewdetail.setLayoutManager(mLayoutManager);
        rycviewdetail.setAdapter(qCcheckDetailAdapter);
        qCcheckDetailAdapter.setOnItemClickListener(new QCcheckDetailAdapter.OnItemClickListener() {
            @Override
            public void onwarningClick(int position, RecyclerView recyclerView) {
                getDetailChild(position);
                recyclerViewchild = recyclerView;
            }
        });

    }

    private void getDetailChild(int position) {
        new getdatadetailChild().execute(webUrl + "ActualWO/Getfacline_qc_value?fq_no=" + qcCheckdetailMasters.get(position).getFq_no());
    }

    class getdatadetailChild extends AsyncTask<String, Integer, String> {

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
            qcCheckDetailChildMasters = new ArrayList<>();
            String fqhno,check_subject,check_value,check_qty;
            try{
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length()==0){
                    dialog.dismiss();
                    AlertError.alertError("No data", QCCheckActivity.this);
                    return;
                }
                for (int i = 0 ;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    fqhno = jsonObject.getString("fqhno");
                    check_subject = jsonObject.getString("check_subject");
                    check_value = jsonObject.getString("check_value");
                    check_qty = jsonObject.getString("check_qty");
                    qcCheckDetailChildMasters.add(new QCCheckDetailChildMaster(fqhno,check_subject,check_value,check_qty));
                }
                builrycchild();

            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                AlertError.alertError("Could not connect to server", QCCheckActivity.this);
            }


        }
    }

    private void builrycchild() {
        dialog.dismiss();
        final LinearLayoutManager mLayoutManager;
        recyclerViewchild.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(QCCheckActivity.this);
        qCcheckDetailChildAdapter = new QCcheckDetailChildAdapter(qcCheckDetailChildMasters);
        recyclerViewchild.setLayoutManager(mLayoutManager);
        recyclerViewchild.setAdapter(qCcheckDetailChildAdapter);

    }

    class LoadCheckQc extends AsyncTask<String, Integer, String> {

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
            rViewItemJSON = new ArrayList<>();

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("false") || object.getString("result").equals("[]")) {
                    dialog.dismiss();
                    Toast.makeText(QCCheckActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    rViewItemJSON.removeAll(rViewItemJSON);
                    tv_qcheck_checkqty.setEnabled(false);
                    tv_qcheck_checkqty.setInputType(InputType.TYPE_NULL);
                    tv_qcheck_checkqty.setFocusable(false);
                    tv_qcheck_okcheck.setEnabled(false);
                    tv_qcheck_okcheck.setInputType(InputType.TYPE_NULL);
                    tv_qcheck_okcheck.setFocusable(false);
                    return;
                }
                if (object.getString("result").equals("true") && object.getString("qc_itemcheck_mt").equals("[]")) {
                    Toast.makeText(QCCheckActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    tv_qcheck_checkqty.setEnabled(false);
                    tv_qcheck_checkqty.setInputType(InputType.TYPE_NULL);
                    tv_qcheck_checkqty.setFocusable(false);
                    tv_qcheck_okcheck.setEnabled(false);
                    tv_qcheck_okcheck.setInputType(InputType.TYPE_NULL);
                    tv_qcheck_okcheck.setFocusable(false);
                    dialog.dismiss();
                }

//                JSONArray jsonArray = object.getJSONArray("qc_itemcheck_mt");
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject object1 = jsonArray.getJSONObject(i);
//                    String qc_itemcheck_mt__icno = object1.getString("qc_itemcheck_mt__icno");
//                    String qc_itemcheck_mt__check_subject = object1.getString("qc_itemcheck_mt__check_subject");
//                    String qc_itemcheck_mt__check_id = object1.getString("qc_itemcheck_mt__check_id");
//
//                    JSONArray jsonArray_1 = object1.getJSONArray("view_qc_Model");
//                    for (int j = 0; j < jsonArray_1.length(); j++) {
//                        JSONObject object_2 = jsonArray_1.getJSONObject(j);
//                        String qc_itemcheck_dt__icdno = object_2.getString("qc_itemcheck_dt__icdno");
//                        String qc_itemcheck_dt__check_name = object_2.getString("qc_itemcheck_dt__check_name");
//
//                        rViewItemJSON.add(new QcCheckerLessItem(false,
//                                qc_itemcheck_dt__check_name,
//                                "0",
//                                qc_itemcheck_dt__icdno,
//                                i + 1 + "",
//                                qc_itemcheck_mt__check_subject,
//                                qc_itemcheck_mt__icno,
//                                qc_itemcheck_mt__check_id));
//                    }
//                }

                //buildQCMake();
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }


        }
    }


//    private void buildQCMake() {
//        dialog.dismiss();
//        adapterLess = new QcCheckerLessAdapter((ArrayList<QcCheckerLessItem>) rViewItemJSON);
//        recyclerViewCHECK.setLayoutManager(new LinearLayoutManager(QCCheckActivity.this));
//        recyclerViewCHECK.setAdapter(adapterLess);
//
//        adapterLess.setOnItemClickListener(new QcCheckerLessAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
//            }
//
//            @Override
//            public void onItemCheck(int position) {
//                if (rViewItemJSON.get(position).isCheck()) {
//                    rViewItemJSON.get(position).setCheck(false);
//                } else {
//                    rViewItemJSON.get(position).setCheck(true);
//                }
//                adapterLess.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onItemEditText(int position) {
//                inputNumberDialog(position);
//            }
//
//            @Override
//            public void onItemButtonUp(int position) {
//
//                int Tong = 0;
//
//                for (int i = 0; i < rViewItemJSON.size(); i++) {
//                    Tong = Tong + Integer.parseInt(rViewItemJSON.get(i).getQty().toString().trim());
//                }
//                if (valDefect <= Tong) {
//                    AlertNotExist("Defect Qty max value: \"" + valDefect + "\"");
//                    // Toast.makeText(QCCheckActivity.this, "Defect qty max value: \"" + valDefect + "\"", Toast.LENGTH_SHORT).show();
//                    return;
//
//                } else {
//                    rViewItemJSON.get(position).setCheck(true);
//                    rViewItemJSON.get(position).setQty(Integer.parseInt(rViewItemJSON.get(position).getQty().trim()) + 1 + "");
//                    adapterLess.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onItemButtonDown(int position) {
//
//                if (0 == Integer.parseInt(rViewItemJSON.get(position).getQty().toString().trim())) {
//                    rViewItemJSON.get(position).setCheck(false);
//                    adapterLess.notifyDataSetChanged();
//                    AlertNotExist("This min value !!!");
//                    return;
//
//                } else {
//                    rViewItemJSON.get(position).setCheck(true);
//                    rViewItemJSON.get(position).setQty(Integer.parseInt(rViewItemJSON.get(position).getQty().trim()) - 1 + "");
//                    if (0 == Integer.parseInt(rViewItemJSON.get(position).getQty().toString().trim())) {
//                        rViewItemJSON.get(position).setCheck(false);
//                    }
//
//                    adapterLess.notifyDataSetChanged();
//                }
//            }
//
//
//        });
//    }

//    private void inputNumberDialog(final int posi) {
//        Rect displayRectangle = new Rect();
//        Window window = QCCheckActivity.this.getWindow();
//        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(QCCheckActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        builder.setTitle("Qty Value");
//        View dialogView = LayoutInflater.from(QCCheckActivity.this).inflate(R.layout.number_input_layout_ok_cancel, null);
//        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
//        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));
//
//        builder.setView(dialogView);
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//
//        //anh xa
//
//        final EditText input = dialogView.findViewById(R.id.input);
//        Button bt_OK = dialogView.findViewById(R.id.in_btn_ok);
//        Button bt_Cal = dialogView.findViewById(R.id.in_btn_cancel);
//
//        input.setText(rViewItemJSON.get(posi).getQty());
//
//        //TODO
//
//        int Tong = 0;
//
//        for (int i = 0; i < rViewItemJSON.size(); i++) {
//            if (i != posi) {
//                Tong = Tong + Integer.parseInt(rViewItemJSON.get(i).getQty().toString().trim());
//            }
//
//        }
//
//        int maxdetail = 0;
//        maxdetail = valDefect - Tong;
//
//        final int finalMaxdetail = maxdetail;
//        bt_OK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txt = input.getText().toString().trim();
//
//                if (txt.length() > 0) {
//                    if (Integer.parseInt(txt) == 0) {
//                        rViewItemJSON.get(posi).setCheck(false);
//                        rViewItemJSON.get(posi).setQty("0");
//                        adapterLess.notifyDataSetChanged();
//                        alertDialog.cancel();
//                        return;
//                    }
//                    if (finalMaxdetail < Integer.parseInt(txt)) {
//
//                        input.setError("Defect qty = \"" + finalMaxdetail + "\"");
//                        input.requestFocus();
//                        return;
//
//                    } else {
//                        rViewItemJSON.get(posi).setCheck(true);
//                        rViewItemJSON.get(posi).setQty(Integer.parseInt(txt) + "");
//                        adapterLess.notifyDataSetChanged();
//                        alertDialog.cancel();
//                    }
//                } else {
//                    input.setError("Please enter value ");
//                    input.requestFocus();
//                    return;
//                }
//            }
//        });
//
//        bt_Cal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.cancel();
//            }
//        });
//
//        alertDialog.show();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.qc_check:

                savecheckQC();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savecheckQC() {
        if (tv_qcheck_checkqty.getText().toString().equals("0")) {
            finish();
        } else {
            String url = webUrl+"ActualWO/Insert_FaclineQc_API"
                    + "?check_qty="+ tv_qcheck_checkqty.getText().toString().trim()
                    + "&ok_qty="+ tv_qcheck_okcheck.getText().toString().trim()
                    + "&item_vcd="+ qcCode
                    + "&mt_cd="+mlNo
                    + "&check_qty_error=" + tv_qcheck_defectqty.getText().toString().trim() ;
            new saveQC().execute(url);
            Log.e("saveQC",url);
        }
    }

    class saveQC extends AsyncTask<String, Integer, String> {

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
            try{

                JSONObject jsonObject =new JSONObject(s);
                if (!jsonObject.getBoolean("result")){
                    dialog.dismiss();
                    AlertError.alertError(jsonObject.getString("message"), QCCheckActivity.this);
                }else {
                    dialog.dismiss();
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                AlertError.alertError("Could not connect to server", QCCheckActivity.this);

            }
        }
    }

    private void AlertNotExist(String text) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Warning!!!");
        alertDialog.setMessage(text); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }
    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getHight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}