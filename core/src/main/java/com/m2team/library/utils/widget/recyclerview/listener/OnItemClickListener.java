package com.m2team.library.utils.widget.recyclerview.listener;

import android.view.View;

import com.m2team.library.utils.widget.recyclerview.BaseQuickAdapter;


public abstract   class OnItemClickListener extends SimpleClickListener {


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SimpleOnItemClick(adapter,view,position);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
    public abstract void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position);
}
