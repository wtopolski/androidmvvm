package com.github.wtopolski.libmvvm.viewmodel;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.github.wtopolski.libmvvm.BR;
import rx.Subscriber;

/**
 * Created by wojciechtopolski on 06/04/16.
 */
public class BaseViewModel extends BaseObservable {
    private boolean enabled;
    private boolean visibility;
    private Subscriber<Boolean> editableObserver;

    public BaseViewModel() {
        this(false, false);
    }

    public BaseViewModel(boolean enabled, boolean visibility) {
        this.enabled = enabled;
        this.visibility = visibility;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyPropertyChanged(BR.enabled);
    }

    @Bindable
    public boolean getEnabled() {
        return enabled;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
        notifyPropertyChanged(BR.visibility);
    }

    @Bindable
    public boolean getVisibility() {
        return visibility;
    }

    public void setEditableObserver(Subscriber<Boolean> editableObserver) {
        this.editableObserver = editableObserver;
        notifyPropertyChanged(BR.editableObserver);
    }

    @Bindable
    public Subscriber<Boolean> getEditableObserver() {
        return editableObserver;
    }
}
