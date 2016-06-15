package com.github.wtopolski.libmvvm.utils;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wojciechtopolski on 14/05/16.
 */
public class BindingViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    public BindingViewHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}
