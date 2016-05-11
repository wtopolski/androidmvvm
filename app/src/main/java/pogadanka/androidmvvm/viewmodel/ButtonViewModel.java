package pogadanka.androidmvvm.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import pogadanka.androidmvvm.BR;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * View model for Button
 * <p>
 * Created by wojciechtopolski on 11/03/16.
 */
public class ButtonViewModel extends BaseViewModel {
    @Bindable
    public String text;

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public int textColor;

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyPropertyChanged(BR.textColor);
    }

    private PublishSubject<Object> buttonObservable = PublishSubject.create();

    public Observable<Object> rxButton() {
        return buttonObservable.asObservable();
    }

    public void onButtonClick(View view) {
        buttonObservable.onNext(null);
    }
}
