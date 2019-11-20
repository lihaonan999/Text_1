package com.example.text_1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private Toolbar mToolbar;
    private FrameLayout mFram;
    private TabLayout mTab;
    private HomeFragment fragment;
    private XiaZaiFragment zaiFragment;
    private FragmentManager manager;
    private File downloadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        myCheckPermission();//动态申请权限
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFram = (FrameLayout) findViewById(R.id.fram);
        mTab = (TabLayout) findViewById(R.id.tab);

        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);

        fragment = new HomeFragment();
        zaiFragment = new XiaZaiFragment();

        mTab.addTab(mTab.newTab().setText("首页"));
        mTab.addTab(mTab.newTab().setText("下载"));

        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fram, fragment)
                .add(R.id.fram, zaiFragment)
                .show(fragment)
                .hide(zaiFragment)
                .commit();

        mTab.addOnTabSelectedListener(this);
    }

    private void myCheckPermission() {
        int i = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (i != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (position == 0) {
            mToolbar.setTitle("首页");
            setSupportActionBar(mToolbar);
            manager.beginTransaction().show(fragment).hide(zaiFragment).commit();
        }
        if (position == 1) {
            mToolbar.setTitle("下载");
            setSupportActionBar(mToolbar);
            manager.beginTransaction().show(zaiFragment).hide(fragment).commit();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void uploadOk() {
        //文件下载路径
        String downloadUrl = "https://alissl.ucdl.pp.uc.cn/fs08/2019/07/05/1/110_17e4089aa3a4b819b08069681a9de74b.apk";
        //2.创建本地保存的路径//
        downloadFile = new File("/storage/emulated/0/a.apk");
        if (!downloadFile.exists()) {//创建文件
            try {
                downloadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Okhttp读取
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                String s = e.getMessage();
                Log.i("111", "onFailure: " + s);
                EventBus.getDefault().post(new DownloadMsg(0, 0, 0));//设置下载失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功
                long max = response.body().contentLength();//得到文件的总大小
                InputStream is = response.body().byteStream();
                downloadFile(is, downloadFile, max);//调用专门下载的方法进行读取保存
            }
        });
    }

    private void downloadFile(InputStream is, final File downloadFile, long max) {
        //先设置进度条的总大小和文件保持一致，1比1
        EventBus.getDefault().post(new DownloadMsg(1, 0, (int) max));
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(downloadFile);//创建本地文件的输出流，接收下载文件的信息，保存到本地
            int len = 0;
            byte[] buff = new byte[1024];
            int count = 0;//表示下载了多少
            while ((len = is.read(buff)) != -1) {
                fileOutputStream.write(buff, 0, len);//写到本地文件中
                count += len;
                //设置进度
                EventBus.getDefault().post(new DownloadMsg(2, count, (int) max));
                Log.i("111", "downloadFile: 总大小：" + max + ",下载大小：" + count);
            }
            //下载完成
            EventBus.getDefault().post(new DownloadMsg(3, count, (int) max));
            //此处安装，下载完成，需要dialog对话框，所以需要 在主线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //ApkInstallUtil.installApk(MainActivity.this, downloadFile.getPath());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
