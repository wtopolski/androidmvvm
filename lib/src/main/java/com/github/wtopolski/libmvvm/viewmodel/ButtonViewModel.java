package com.github.wtopolski.libmvvm.viewmodel;

import android.databinding.Bindable;

import com.github.wtopolski.libmvvm.BR;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ButtonViewModel extends BaseViewModel {

    private String text;
    private int textColor;
    private PublishSubject<Object> buttonObservable = PublishSubject.create();

    public ButtonViewModel() {
        super();
    }

    public ButtonViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyPropertyChanged(BR.textColor);
    }

    @Bindable
    public int getTextColor() {
        return textColor;
    }

    public Observable<Object> rxTap() {
        return buttonObservable.asObservable();
    }

    // Reference method for click action.
    //public void onButtonClick(View view) {
    //    buttonObservable.onNext(null);
    //}

    // Lambda method for click action.
    public void onButtonClick() {
        buttonObservable.onNext(null);
    }
}
