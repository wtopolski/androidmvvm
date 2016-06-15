package com.github.wtopolski.libmvvm.viewmodel;

import android.databinding.Bindable;

import com.github.wtopolski.libmvvm.BR;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by wojciechtopolski on 21/03/16.
 */
public class CheckBoxViewModel extends BaseViewModel {

    public CheckBoxViewModel() {
        super();
    }

    public CheckBoxViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    private boolean checked;

    private PublishSubject<Boolean> checkBoxObservable = PublishSubject.create();

    public void setChecked(boolean checked) {
        this.checked = checked;
        checkBoxObservable.onNext(checked);
        notifyPropertyChanged(BR.checked);
    }

    @Bindable
    public boolean getChecked() {
        return checked;
    }

    public Observable<Boolean> rxCheck() {
        return checkBoxObservable.asObservable();
    }
}
