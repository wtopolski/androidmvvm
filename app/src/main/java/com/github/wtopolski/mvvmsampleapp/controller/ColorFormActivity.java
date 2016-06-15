package com.github.wtopolski.mvvmsampleapp.controller;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.wtopolski.mvvmsampleapp.R;

import com.github.wtopolski.mvvmsampleapp.databinding.ActivityColorFormBinding;
import com.github.wtopolski.mvvmsampleapp.utils.ActionDelegate;
import com.github.wtopolski.mvvmsampleapp.viewmodel.ColorFormViewModel;

public class ColorFormActivity extends AppCompatActivity implements ActionDelegate {

    private ColorFormViewModel viewModel;
    private ActivityColorFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ColorFormViewModel(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_color_form);
        binding.setViewModel(viewModel);

        viewModel.restoreState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.bind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.saveState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.unbind();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.releaseDelegate();
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
        startActivity(new Intent(this, className));
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }
}
