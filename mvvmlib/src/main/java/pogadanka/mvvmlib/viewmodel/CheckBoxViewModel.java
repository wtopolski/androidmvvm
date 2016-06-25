package pogadanka.mvvmlib.viewmodel;

import android.databinding.Bindable;
import android.widget.CompoundButton;

import pogadanka.mvvmlib.BR;
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

    @Bindable
    public boolean checked;

    private PublishSubject<Boolean> checkBoxObservable = PublishSubject.create();

    public void setBoxChecked(boolean checked) {
        this.checked = checked;
        checkBoxObservable.onNext(checked);
        notifyPropertyChanged(BR.checked);
    }

    public void onCheckBoxChange(CompoundButton view, boolean isChecked) {
        checked = view.isChecked();
        checkBoxObservable.onNext(checked);
        notifyPropertyChanged(BR.checked);
    }

    public Observable<Boolean> rxCheckBox() {
        return checkBoxObservable.asObservable();
    }
}
