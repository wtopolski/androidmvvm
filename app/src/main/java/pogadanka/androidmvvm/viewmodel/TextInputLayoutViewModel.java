package pogadanka.androidmvvm.viewmodel;

import android.databinding.Bindable;

import pogadanka.androidmvvm.BR;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * View model for Text Input Layout
 * <p>
 * Created by wojciechtopolski on 11/03/16.
 */
public class TextInputLayoutViewModel extends BaseViewModel {

    @Bindable
    public String value;

    @Bindable
    public String valueError;

    private PublishSubject<String> valueObservable = PublishSubject.create();

    public void setValue(String value) {
        this.value = value;
        valueObservable.onNext(value);
        notifyPropertyChanged(BR.value);
    }

    public void setValueError(String valueError) {
        this.valueError = valueError;
        notifyPropertyChanged(BR.valueError);
    }

    public Observable<String> rxValue() {
        return valueObservable.asObservable();
    }

    public void onValueTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && before == 0 && count == 0) {
            return;
        }
        value = String.valueOf(s);
        valueObservable.onNext(value);
    }

}
