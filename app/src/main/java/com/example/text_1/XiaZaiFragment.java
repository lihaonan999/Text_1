package com.example.text_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class XiaZaiFragment extends Fragment implements View.OnClickListener {

    private View view;
    /**
     * 开始下载
     */
    private Button mBtPlay;
    /**
     * 当前下载进度：34%
     */
    private TextView mTv;
    private ProgressBar mPb;
    private File downloadFile;
    private int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xia_zai, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mBtPlay = (Button) view.findViewById(R.id.bt_play);
        mBtPlay.setOnClickListener(this);
        mTv = (TextView) view.findViewById(R.id.tv);
        mPb = (ProgressBar) view.findViewById(R.id.pb);
        mTv.setText("总大小：100%"+"；已下载："+count%100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.bt_play://开始下载
                uploadOk();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(DownloadMsg downloadMsg){
        switch (downloadMsg.getCode()){
            case 0:
                Toast.makeText(getContext(), "下载失败", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                mPb.setMax(downloadMsg.getMax());
                break;
            case 2:
                mPb.setProgress(downloadMsg.getProgress());
                break;
            case 3:
                Toast.makeText(getContext(), "下载完成", Toast.LENGTH_SHORT).show();
                //表示下载完成，可以调用安装的代码
                break;
        }
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
            //表示下载了多少
            count = 0;
            while ((len = is.read(buff)) != -1) {
                fileOutputStream.write(buff, 0, len);//写到本地文件中
                count += len;
                //设置进度
                EventBus.getDefault().post(new DownloadMsg(2, count, (int) max));
                mTv.setText("总大小："+max+"；已下载："+ count);
                Log.i("111", "downloadFile: 总大小：" + max + ",下载大小：" + count);
            }
            //下载完成
            EventBus.getDefault().post(new DownloadMsg(3, count, (int) max));
            Toast.makeText(getContext(), "下载完成", Toast.LENGTH_SHORT).show();

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
