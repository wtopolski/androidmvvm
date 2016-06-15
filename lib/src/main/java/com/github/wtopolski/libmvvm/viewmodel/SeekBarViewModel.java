package com.github.wtopolski.libmvvm.viewmodel;

import android.databinding.Bindable;

import com.github.wtopolski.libmvvm.BR;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * View model for Button
 * <p>
 * Created by wojciechtopolski on 11/03/16.
 */
public class SeekBarViewModel extends BaseViewModel {

    private int progress;
    private int max;
    private PublishSubject<Integer> progressObservable = PublishSubject.create();

    public SeekBarViewModel() {
        super();
    }

    public SeekBarViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        progressObservable.onNext(progress);
        notifyPropertyChanged(BR.progress);
    }

    @Bindable
    public int getProgress() {
        return progress;
    }

    public void setMax(int max) {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }

    @Bindable
    public int getMax() {
        return max;
    }

    public Observable<Integer> rxProgress() {
        return progressObservable.asObservable();
    }
}
