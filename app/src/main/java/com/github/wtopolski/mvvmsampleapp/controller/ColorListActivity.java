package com.github.wtopolski.mvvmsampleapp.controller;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.github.wtopolski.mvvmsampleapp.R;
import com.github.wtopolski.mvvmsampleapp.BR;
import com.github.wtopolski.mvvmsampleapp.databinding.ActivityColorListBinding;
import com.github.wtopolski.mvvmsampleapp.model.ColorForm;
import com.github.wtopolski.mvvmsampleapp.utils.ActionDelegate;
import com.github.wtopolski.mvvmsampleapp.viewmodel.ColorListItemViewModel;
import com.github.wtopolski.mvvmsampleapp.viewmodel.ColorListViewModel;

/**
 * Created by wojciechtopolski on 13/05/16.
 */
public class ColorListActivity extends AppCompatActivity implements ActionDelegate {
    private ActivityColorListBinding binding;
    private ColorListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ColorListViewModel(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_color_list);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.bind();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.unbind();
    }

    @Override
    protected void onDestroy() {
        viewModel.releaseDelegate();
        super.onDestroy();
    }

    @Override
    public void executePendingBindings() {
        binding.executePendingBindings();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openWindow(Class className) {

    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }
}
