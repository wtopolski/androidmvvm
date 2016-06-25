package pogadanka.mvvmlib.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import pogadanka.mvvmlib.BR;

/**
 * Created by wojciechtopolski on 06/04/16.
 */
public class BaseViewModel extends BaseObservable {

    public BaseViewModel() {
        this(false, false);
    }

    public BaseViewModel(boolean enabled, boolean visibility) {
        this.enabled = enabled;
        this.visibility = visibility;
    }

    @Bindable
    public boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyPropertyChanged(BR.enabled);
    }

    @Bindable
    public boolean visibility;

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
        notifyPropertyChanged(BR.visibility);
    }
}
