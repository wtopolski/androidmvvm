package com.github.wtopolski.libmvvm.viewmodel;

import android.databinding.Bindable;

import com.github.wtopolski.libmvvm.BR;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * View model for Text Input Layout
 * <p>
 * Created by wojciechtopolski on 11/03/16.
 */
public class TextInputLayoutViewModel extends BaseViewModel {

    private String hint;
    private String value;
    private String valueError;
    private PublishSubject<String> valueObservable = PublishSubject.create();

    public TextInputLayoutViewModel() {
        super();
    }

    public TextInputLayoutViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    public void setHint(String hint) {
        this.hint = hint;
        notifyPropertyChanged(BR.hint);
    }

    @Bindable
    public String getHint() {
        return hint;
    }

    public void setValue(String value) {
        this.value = value;
        valueObservable.onNext(value);
        notifyPropertyChanged(BR.value);
    }

    @Bindable
    public String getValue() {
        return value;
    }

    public Observable<String> rxValue() {
        return valueObservable.asObservable();
    }

    public void setValueError(String valueError) {
        this.valueError = valueError;
        notifyPropertyChanged(BR.valueError);
    }

    @Bindable
    public String getValueError() {
        return valueError;
    }
}
