package com.github.wtopolski.libmvvm.viewmodel;

import android.databinding.Bindable;
import android.util.Log;

import com.github.wtopolski.libmvvm.BR;
import com.github.wtopolski.libmvvm.view.ColorationSeekBar;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * View model for ColorationSeekBar
 * <p>
 * Created by wojciechtopolski on 11/03/16.
 */
public class ColorationSeekBarViewModel extends BaseViewModel {
    private String label;
    private ColorationSeekBar.ColorationType position;
    private PublishSubject<ColorationSeekBar.ColorationType> valueObservable = PublishSubject.create();

    public ColorationSeekBarViewModel() {
        super();
    }

    public ColorationSeekBarViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    @Bindable
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        notifyPropertyChanged(BR.label);
    }

    @Bindable
    public ColorationSeekBar.ColorationType getPosition() {
        Log.d("wtopolski", "VM getPosition " + position);
        return position;
    }

    public void setPosition(ColorationSeekBar.ColorationType position) {
        Log.d("wtopolski", "VM setPosition " + position);
        this.position = position;
        valueObservable.onNext(position);
        notifyPropertyChanged(BR.position);
    }

    public Observable<ColorationSeekBar.ColorationType> rxValue() {
        return valueObservable.asObservable();
    }
}
