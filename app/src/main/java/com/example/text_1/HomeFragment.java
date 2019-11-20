package com.example.text_1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Recy_view{

    private RecyclerView recy;
    private ArrayList <Bean.DataBean.DatasBean> beans;
    private Recy_Presenter presenter;
    private RecyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new Recy_Presenter(this);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recy = view.findViewById(R.id.recy);
        recy.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        recy.setLayoutManager(new LinearLayoutManager(getContext()));

        beans = new ArrayList<>();
        adapter = new RecyAdapter(getContext(), beans);
        recy.setAdapter(adapter);

        presenter.getData();
    }

    @Override
    public void onSuccess(List<Bean.DataBean.DatasBean> list) {
        beans.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFail(String str) {
        Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
    }
}
