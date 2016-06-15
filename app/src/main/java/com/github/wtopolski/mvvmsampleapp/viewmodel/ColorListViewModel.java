package com.github.wtopolski.mvvmsampleapp.viewmodel;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.github.wtopolski.libmvvm.viewmodel.RecycleViewModel;
import com.github.wtopolski.mvvmsampleapp.controller.ColorListAdapter;
import com.github.wtopolski.mvvmsampleapp.model.ColorForm;
import com.github.wtopolski.mvvmsampleapp.utils.ActionDelegate;
import com.github.wtopolski.mvvmsampleapp.utils.ActionViewModel;

import java.util.ArrayList;
import java.util.List;

public class ColorListViewModel extends ActionViewModel implements ColorListItemViewModel.ViewModelListener {
    public RecycleViewModel list;
    private ColorListAdapter adapter;
    private List<ColorForm> colors;

    public ColorListViewModel(ActionDelegate actionDelegate) {
        super(actionDelegate);

        colors = generateColors();

        adapter = new ColorListAdapter(colors, this);

        list = new RecycleViewModel(true, true);
        list.setFixedSize(true);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(actionDelegate.getAppContext()));
        list.setAdapter(adapter);
    }

    @Override
    public void bind() {
        super.bind();
    }

    @Override
    public void onItemSelected(int position, boolean value) {
        colors.get(position).setFavorite(value);
    }

    @Override
    public void onItemClick(int position) {
        actionDelegate.showToastMessage("Click: " + position);
    }

    @Override
    public void releaseDelegate() {
        super.releaseDelegate();
        adapter.clearData();
    }

    private List<ColorForm> generateColors() {
        List<ColorForm> colors = new ArrayList<>(50);

        for (int color = 1; color <= 50; color++) {
            int grayColor = color * 4;
            String colorValue = String.format("#%02X%02X%02X", grayColor, grayColor, grayColor);
            colors.add(new ColorForm("Gray " + color, "Some description", colorValue, null, false));
        }

        return colors;
    }
}
