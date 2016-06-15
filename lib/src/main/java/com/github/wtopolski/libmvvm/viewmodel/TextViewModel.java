package com.github.wtopolski.libmvvm.viewmodel;

import android.databinding.Bindable;
import com.github.wtopolski.libmvvm.BR;

/**
 * Created by wojciechtopolski on 22/03/16.
 */
public class TextViewModel extends BaseViewModel {

    private String text;
    private Integer textAppearance;
    private Integer textColor;

    public TextViewModel() {
        super();
    }

    public TextViewModel(boolean enabled, boolean visibility) {
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

    public void setTextAppearance(Integer textAppearance) {
        this.textAppearance = textAppearance;
        notifyPropertyChanged(BR.textAppearance);
    }

    @Bindable
    public Integer getTextAppearance() {
        return textAppearance;
    }

    public void setTextColor(Integer textColor) {
        this.textColor = textColor;
        notifyPropertyChanged(BR.textColor);
    }

    @Bindable
    public Integer getTextColor() {
        return textColor;
    }
}
