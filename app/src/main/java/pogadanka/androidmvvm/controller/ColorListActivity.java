package pogadanka.androidmvvm.controller;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pogadanka.androidmvvm.R;
import pogadanka.androidmvvm.databinding.ActivityColorListBinding;
import pogadanka.androidmvvm.databinding.ActivityColorListItemBinding;
import pogadanka.androidmvvm.model.ColorForm;
import pogadanka.androidmvvm.viewmodel.ColorListItemViewModel;
import pogadanka.androidmvvm.viewmodel.ColorListViewModel;
import pogadanka.mvvmlib.utils.BindingViewHolder;

/**
 * Created by wojciechtopolski on 13/05/16.
 */
public class ColorListActivity extends AppCompatActivity {
    private ColorListViewModel viewModel;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ColorListViewModel();
        ActivityColorListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_color_list);
        binding.setViewModel(viewModel);

        viewModel.configure(true, new DefaultItemAnimator(), new LinearLayoutManager(this));

        List<ColorForm> colors = new ArrayList<>(50);

        for (int color = 1; color <= 50; color++) {
            int grayColor = color * 4;
            String colorValue = String.format("#%02X%02X%02X", grayColor, grayColor, grayColor);
            colors.add(new ColorForm("Gray " + color, "Some description", colorValue, false));
        }

        customAdapter = new CustomAdapter(colors);
        viewModel.setAdapter(customAdapter);
    }

    @Override
    protected void onDestroy() {
        customAdapter.clearData();
        Log.d("wtopolski", "onDestroy::HELP_SUB_COUNT: " + ColorListItemViewModel.HELP_SUB_COUNT);
        super.onDestroy();
    }

    public class CustomAdapter extends RecyclerView.Adapter<BindingViewHolder> {
        private List<ColorForm> colors = new LinkedList<>();

        public CustomAdapter(List<ColorForm> input) {
            colors.addAll(input);
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
            if (viewModel == null) {
                viewModel = new ColorListItemViewModel();
                binding.setViewModel(viewModel);
                binding.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

                viewModel.setListener(new ColorListItemViewModel.ViewModelListener() {
                    @Override
                    public void onItemClick() {
                        CustomAdapter.this.onItemClick(holder.getAdapterPosition());
                    }

                    @Override
                    public void onItemSelected(boolean value) {
                        CustomAdapter.this.onItemSelected(holder.getAdapterPosition(), value);
                    }
                });
            }

            ColorForm form = colors.get(position);
            viewModel.setData(form);
        }

        private void onItemSelected(int position, boolean value) {
            colors.get(position).setFavorite(value);
        }

        private void onItemClick(int position) {
            Toast.makeText(getApplicationContext(), "Click: " + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public int getItemCount() {
            return colors.size();
        }

        @Override
        public void onViewRecycled(BindingViewHolder holder) {
            ActivityColorListItemBinding binding = (ActivityColorListItemBinding) holder.getBinding();
            binding.getViewModel().unsubscribe();
            super.onViewRecycled(holder);
        }

        public void clearData() {
            for (int position = colors.size(); position > 0; position--) {
                // This will invoke onViewRecycled with unsubscribe method.
                notifyItemRemoved(position - 1);
            }
            colors.clear();
        }
    }
}
