package com.example.text_1;

import java.util.List;

/**
 * Created by ${李昊男} on 2019/11/12.
 */

public class Recy_Presenter implements Recy_Model.ItemCallBack{
    private Recy_Model model;
    private Recy_view view;

    public Recy_Presenter(Recy_view view) {
        this.model = new Recy_Model();
        this.view = view;
    }

    public void getData(){
        if (model!=null){
            model.getData(this);
        }
    }


    @Override
    public void onSuccess(List<Bean.DataBean.DatasBean> list) {
        if (view!=null){
            view.onSuccess(list);
        }
    }

    @Override
    public void onFail(String str) {
        if (view!=null){
            view.onFail(str);
        }
    }
}
