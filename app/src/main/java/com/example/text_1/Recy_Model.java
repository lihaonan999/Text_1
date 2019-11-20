package com.example.text_1;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${李昊男} on 2019/11/12.
 */

public class Recy_Model {

    public void getData(final ItemCallBack callBack){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServer.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiServer server = retrofit.create(ApiServer.class);
        Observable <Bean> json = server.getJson();
        json.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer <Bean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bean bean) {
                        if (bean!=null){
                            callBack.onSuccess(bean.getData().getDatas());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public interface ItemCallBack{
        void onSuccess(List<Bean.DataBean.DatasBean> list);
        void onFail(String str);
    }

}
