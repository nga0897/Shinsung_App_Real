package com.example.mmsapp;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.example.mmsapp.AlertError.AlertError;
import com.example.mmsapp.checkVerRes.CheckVerRes;
import com.example.mmsapp.service.APIClient;
import com.example.mmsapp.service.BaseMessageResponse;
import com.example.mmsapp.service.SharedPref;
import com.example.mmsapp.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.thlsoft.downloader.Error;
import com.thlsoft.downloader.OnCancelListener;
import com.thlsoft.downloader.OnDownloadListener;
import com.thlsoft.downloader.OnPauseListener;
import com.thlsoft.downloader.OnProgressListener;
import com.thlsoft.downloader.OnStartOrResumeListener;
import com.thlsoft.downloader.PRDownloader;
import com.thlsoft.downloader.Progress;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private final String [] permissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_PHONE_STATE",
            "android.permission.SYSTEM_ALERT_WINDOW",
            "android.permission.CAMERA"};

    private EditText edtPassword;
    private EditText edtUsername;
    Button btnLogin;
    private ProgressDialog dialog;
    TextView tvVersion,tvUrlWeb;
    TextInputLayout h2;
    TextInputLayout h1;
    private String verName;
    private int verCode;
    private LoginAPI apiInterface;
    private LoginAPI apiCheckVer;
    private long downloadID;
    private String downloadedFileName;

    int downloadId;
    private Dialog dialogDownload;
    private ProgressBar downloadBar;
    private TextView textViewProgressOne;
    private static String dirPath;
    private boolean isInstalledNewVer = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        edtPassword = findViewById(R.id.user_login);
        edtUsername = findViewById(R.id.user_full_name);
        tvVersion= findViewById(R.id.version);
        String id = SharedPref.getInstance().get(Constants.ID,String.class);
        String pw = SharedPref.getInstance().get(Constants.PW,String.class);
        SharedPref.getInstance().put(Constants.BASE_URL, Constants.BASE_URL_OFFLINE);
        String baseURL = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
        apiInterface = APIClient.getClient().create(LoginAPI.class);
        apiCheckVer = APIClient.getClientForCheckVersion().create(LoginAPI.class);
        dirPath = Utils.getRootDirPath(getApplicationContext());
        checkIsAccessPermission();
        h1 = findViewById(R.id.H1);
        h2 = findViewById(R.id.H2);
        dialog = new ProgressDialog(this);
        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, SharedPref.getInstance().get(Constants.BASE_URL,String.class), Toast.LENGTH_SHORT).show(); }
        });



        tvUrlWeb = findViewById(R.id.tv_urlweb);
        if (baseURL.equals(Constants.BASE_URL_ONLINE)){
            tvUrlWeb.setText("Online");
        } else {
            tvUrlWeb.setText("Offline");
        }

        tvUrlWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeUrl();
            }
        });

        edtPassword.setText(pw);
        edtUsername.setText(id);

        edtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                h2.setError(null);
            }
        });
        edtUsername.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                h1.setError(null);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseURL = SharedPref.getInstance().get(Constants.BASE_URL,String.class);
                if(baseURL.equals("")||baseURL==null){
                    baseURL = Constants.BASE_URL_ONLINE;
                }
                if (edtUsername.getText().toString() == null || edtUsername.getText().toString().length() == 0) {
                    h1.setErrorEnabled(true);
                    h2.setError(null);
                    h1.setError("Please, Input ID");
                }else if (edtPassword.getText().toString() == null || edtPassword.getText().toString().length() == 0) {

                    h1.setError(null);
                    h2.setErrorEnabled(true);
                    h2.setError("Please, Input Password");
                   // userLoginEditText.setError("Input PW");
                } else {
                    h1.setError(null);
                    h2.setError(null);
                    Log.d("Login",baseURL +"home/API_Login?" + "user=" + edtUsername.getText().toString() + "&password=" + edtPassword.getText().toString()+"&type=MMS");
                    if(!isInstalledNewVer){
                        checkVersion(verCode);
                        Toast.makeText(LoginActivity.this, "Vui lòng cài đặt phiên bản mới nhất để tiếp tục!!!", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LoginActivity.this,baseURL,Toast.LENGTH_SHORT).show();
                        login(edtUsername.getText().toString(),edtPassword.getText().toString());
                    }
                }
            }
        });

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            verName = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersion.setText("Version: " + verName);
        String urCheck = baseURL  + "Home/API_APP_Version?phienban=" + verCode
                + "&name_app=MMS";
        Log.d("url check ver", urCheck);
        checkVersion(verCode);
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void checkIsAccessPermission() {
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), 1234);
            } else {
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void checkVersion(int verCode) {
        showDialog("Checking version...");
        Call<CheckVerRes> call = apiCheckVer.checkVersion(verCode,"MMS");
        call.enqueue(new Callback<CheckVerRes>() {
            @Override
            public void onResponse(Call<CheckVerRes> call, Response<CheckVerRes> response) {
                if(response.isSuccessful()){
                    final CheckVerRes res = response.body();
                    if(res.isResult()&&res.isVersionNew()){ //Have new version on server
                        new AlertDialog.Builder(LoginActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Cập nhật phiên bản")
                                .setCancelable(false)
                                // .setMessage("Is there a new version of the app you downloaded?")
                                .setMessage(res.getMessage())
                                .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startDownloadNewVersion(SharedPref.getInstance().get(Constants.BASE_URL,String.class) + "Images/app_shinsung/" + res.getDataRes().getFileName());
                                    }
                                })
                                .show();

                    }else if(!res.isResult()&&!res.isVersionNew()){ //Version does not exist
                        Toast.makeText(LoginActivity.this,"This version does not exist",Toast.LENGTH_SHORT).show();
                    }else if(res.isResult()&&!res.isVersionNew()){ //Is latest version
                        Toast.makeText(LoginActivity.this, "This is latest version", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "The server response error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<CheckVerRes> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startDownloadNewVersion(String url) {
        showDialogDownload();
        downloadBar.setIndeterminate(true);
        downloadBar.getIndeterminateDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        final String fileName = "MMS-"+System.currentTimeMillis()+".apk";
        downloadId = PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        downloadBar.setIndeterminate(false);

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        downloadBar.setProgress(0);
                        downloadId = 0;
                        downloadBar.setIndeterminate(false);
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        downloadBar.setProgress((int) progressPercent);
                        textViewProgressOne.setText(Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                        downloadBar.setIndeterminate(false);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        dialogDownload.dismiss();
                        installAPK(dirPath+"/"+fileName);
                    }

                    @Override
                    public void onError(Error error) {
                        AlertError.alertError(error.getServerErrorMessage(),LoginActivity.this);
                        dialogDownload.dismiss();
                        checkVersion(verCode);
                    }
                });
    }

    private void showDialog(String message){
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void login(String userName, String password) {
        showDialog("Loading...");
        Call<BaseMessageResponse> call = apiInterface.login(userName,password,"MMS");
        call.enqueue(new Callback<BaseMessageResponse>() {
            @Override
            public void onResponse(Call<BaseMessageResponse> call, Response<BaseMessageResponse> response) {
                if(response.isSuccessful()){
                    BaseMessageResponse res = response.body();
                    if(res.isResult()){
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        SharedPref.getInstance().put(Constants.ID, edtUsername.getText().toString());
                        SharedPref.getInstance().put(Constants.PW, edtPassword.getText().toString());
                        finish();
                    } else {
                        alertError("The User name or Password you entered were invalid.");
                        dialog.dismiss();
                    }
                }else {
                    AlertError.alertError("The server response error", LoginActivity.this);
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<BaseMessageResponse> call, Throwable t) {
                call.cancel();
                AlertError.alertError(t.getMessage(), LoginActivity.this);
                dialog.dismiss();
            }
        });

    }

    private void showChangeUrl() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.select_dialog_singlechoice);
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(LoginActivity.this);

        builderSingle.setTitle("Select One Line:");
        arrayAdapter.add("Offline");
        arrayAdapter.add("Online");

        final ArrayList<String> arrayAdapterUrl = new ArrayList<>();
        arrayAdapterUrl.add(Constants.BASE_URL_OFFLINE);
        arrayAdapterUrl.add(Constants.BASE_URL_ONLINE);

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        int position;

        switch (SharedPref.getInstance().get(Constants.BASE_URL,String.class)) {
            case Constants.BASE_URL_OFFLINE:
                position = 0;
                break;
            case Constants.BASE_URL_ONLINE:
                position = 1;
                break;
            default:
                position = -1;
                break;
        }
        builderSingle.setSingleChoiceItems(arrayAdapter, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvUrlWeb.setText(arrayAdapter.getItem(i));
                SharedPref.getInstance().put(Constants.BASE_URL, arrayAdapterUrl.get(i));
                apiInterface = APIClient.getClient().create(LoginAPI.class);
                apiCheckVer = APIClient.getClientForCheckVersion().create(LoginAPI.class);
                Log.e("Phaydev:: ","Save value"+arrayAdapterUrl.get(i));
                checkVersion(verCode);
                dialogInterface.dismiss();
            }
        });
        builderSingle.show();
    }

    private void alertError(String text) {
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

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(LoginActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                installAPK("");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    void installAPK(String urlDownload){
        isInstalledNewVer = false;
        File file = new File(urlDownload);
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(getApplicationContext(), new File(urlDownload)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in opening the file!");
            }
        }else{
            Toast.makeText(getApplicationContext(),"File does not exists",Toast.LENGTH_LONG).show();
        }
    }

    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }


    private void showDialogDownload(){
        dialogDownload = new Dialog(this);
        dialogDownload.setContentView(R.layout.dialog_progress_download_new_version);
        dialogDownload.setCancelable(false);
        dialogDownload.show();
        downloadBar = dialogDownload.findViewById(R.id.progressBarDownload);
        textViewProgressOne = dialogDownload.findViewById(R.id.textViewProgressOne);

    }


}