package pogadanka.mvvmlib.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import pogadanka.mvvmlib.BR;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;

public class ButtonViewModel extends BaseViewModel {

    public ButtonViewModel() {
        super();
    }

    public ButtonViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

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

    public Observable<Object> rxTap() {
        return buttonObservable.asObservable();
    }

    public void onButtonClick(View view) {
        buttonObservable.onNext(null);
    }

    @Bindable
    public Subscriber<Boolean> editableState;

    public void getEditableState(Subscriber<Boolean> editableState) {
        this.editableState = editableState;
        notifyPropertyChanged(BR.editableState);
    }
}
