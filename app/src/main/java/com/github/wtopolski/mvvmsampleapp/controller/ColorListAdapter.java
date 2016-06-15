package com.github.wtopolski.mvvmsampleapp.controller;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.github.wtopolski.mvvmsampleapp.R;
import com.github.wtopolski.mvvmsampleapp.databinding.ActivityColorListItemBinding;
import com.github.wtopolski.mvvmsampleapp.model.ColorForm;
import com.github.wtopolski.mvvmsampleapp.viewmodel.ColorListItemViewModel;
import com.github.wtopolski.libmvvm.utils.BindingViewHolder;

/**
 * Created by wojciechtopolski on 26/06/16.
 */
public class ColorListAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private List<ColorForm> data;
    private ColorListItemViewModel.ViewModelListener listener;

    public ColorListAdapter(List<ColorForm> data, ColorListItemViewModel.ViewModelListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityColorListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_color_list_item, parent, false);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        ActivityColorListItemBinding binding = (ActivityColorListItemBinding) holder.getBinding();
        ColorListItemViewModel viewModel = binding.getViewModel();

        // Init new view model object
        if (viewModel == null) {
            viewModel = new ColorListItemViewModel();
            binding.setViewModel(viewModel);
            binding.setTextColor(R.color.colorAccent);
            viewModel.setListener(listener);
        }

        ColorForm form = data.get(position);
        viewModel.setAdapterPosition(holder.getAdapterPosition());
        viewModel.setData(form);

        // Immediate Binding
        // When a variable or observable changes, the binding will be scheduled to change before
        // the next frame. There are times, however, when binding must be executed immediately.
        // To force execution, use the executePendingBindings() method.
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewRecycled(BindingViewHolder holder) {
        ActivityColorListItemBinding binding = (ActivityColorListItemBinding) holder.getBinding();
        binding.getViewModel().unsubscribe();
        super.onViewRecycled(holder);
    }

    public void clearData() {
        for (int position = data.size(); position > 0; position--) {
            // This will invoke onViewRecycled with unsubscribe method.
            notifyItemRemoved(position - 1);
        }
    }
}
