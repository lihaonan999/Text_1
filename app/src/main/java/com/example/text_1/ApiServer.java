package com.example.text_1;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by ${李昊男} on 2019/11/12.
 */

public interface ApiServer {

    String url="https://www.wanandroid.com/";
    @GET("project/list/1/json?cid=294")
    Observable<Bean> getJson();

}
