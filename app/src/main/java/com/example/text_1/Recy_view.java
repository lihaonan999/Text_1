package com.example.text_1;

import java.util.List;

/**
 * Created by ${李昊男} on 2019/11/12.
 */

public interface Recy_view  {

    void onSuccess(List<Bean.DataBean.DatasBean> list);
    void onFail(String str);
}
