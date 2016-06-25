package pogadanka.mvvmlib.viewmodel;

import android.databinding.Bindable;

import pogadanka.mvvmlib.BR;

/**
 * Created by wojciechtopolski on 22/03/16.
 */
public class TextViewModel extends BaseViewModel {

    public TextViewModel() {
        super();
    }

    public TextViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    @Bindable
    public String text;

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }
}
