package pogadanka.mvvmlib.viewmodel;

import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import pogadanka.mvvmlib.BR;
import rx.Observable;
import rx.subjects.PublishSubject;

public class RecycleViewModel extends BaseViewModel {

    public RecycleViewModel() {
        super();
    }

    public RecycleViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    @Bindable
    public boolean fixedSize;

    public void setFixedSize(boolean fixedSize) {
        this.fixedSize = fixedSize;
        notifyPropertyChanged(BR.fixedSize);
    }

    @Bindable
    public RecyclerView.ItemAnimator itemAnimator;

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        this.itemAnimator = itemAnimator;
        notifyPropertyChanged(BR.itemAnimator);
    }

    @Bindable
    public RecyclerView.LayoutManager layoutManager;

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        notifyPropertyChanged(BR.layoutManager);
    }

    @Bindable
    public RecyclerView.Adapter adapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }
}
