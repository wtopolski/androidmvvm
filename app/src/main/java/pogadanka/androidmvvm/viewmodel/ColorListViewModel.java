package pogadanka.androidmvvm.viewmodel;

import android.support.v7.widget.RecyclerView;

import pogadanka.mvvmlib.viewmodel.BaseViewModel;
import pogadanka.mvvmlib.viewmodel.RecycleViewModel;

public class ColorListViewModel extends BaseViewModel {
    public RecycleViewModel list;

    public ColorListViewModel() {
        list = new RecycleViewModel(true, true);
    }

    public void configure(boolean fixedSize, RecyclerView.ItemAnimator itemAnimator, RecyclerView.LayoutManager layoutManager) {
        list.fixedSize = fixedSize;
        list.itemAnimator = itemAnimator;
        list.layoutManager = layoutManager;
        notifyChange();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        list.setAdapter(adapter);
    }
}
