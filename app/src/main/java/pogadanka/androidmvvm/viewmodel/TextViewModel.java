package pogadanka.androidmvvm.viewmodel;

import android.databinding.Bindable;

import pogadanka.androidmvvm.BR;

/**
 * Created by wojciechtopolski on 22/03/16.
 */
public class TextViewModel extends BaseViewModel {

    @Bindable
    public String value;

    public void setValue(String value) {
        this.value = value;
        notifyPropertyChanged(BR.value);
    }
}
