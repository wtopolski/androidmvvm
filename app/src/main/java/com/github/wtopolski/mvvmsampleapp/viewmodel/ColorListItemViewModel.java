package com.github.wtopolski.mvvmsampleapp.viewmodel;

import android.databinding.Bindable;
import android.graphics.Color;
import android.util.Log;

import java.lang.ref.WeakReference;

import com.github.wtopolski.mvvmsampleapp.BR;
import com.github.wtopolski.mvvmsampleapp.R;
import com.github.wtopolski.mvvmsampleapp.model.ColorForm;
import com.github.wtopolski.libmvvm.viewmodel.BaseViewModel;
import com.github.wtopolski.libmvvm.viewmodel.CheckBoxViewModel;
import com.github.wtopolski.libmvvm.viewmodel.TextViewModel;
import rx.Subscription;

public class ColorListItemViewModel extends BaseViewModel {
    public static int HELP_SUB_COUNT = 0;

    private WeakReference<ViewModelListener> listenerRef;
    private Subscription subscription;
    private int adapterPosition;

    public TextViewModel name;
    public TextViewModel description;
    public CheckBoxViewModel favorite;

    @Bindable
    public String color;

    public ColorListItemViewModel() {
        name = new TextViewModel(true, true);
        name.setTextAppearance(R.style.MVVMTextAppearance_Body1);
        name.setTextColor(Color.BLACK);

        description = new TextViewModel(true, true);
        description.setTextAppearance(R.style.MVVMTextAppearance_Caption);
        description.setTextColor(Color.DKGRAY);

        favorite = new CheckBoxViewModel(true, true);
        
        color = "#ffffff";
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public void setListener(ViewModelListener listener) {
        this.listenerRef = new WeakReference<>(listener);
    }

    public void setData(ColorForm form) {
        name.setText(form.getName());
        description.setText(form.getDescription());
        color = form.getColor();
        favorite.setChecked(form.isFavorite());
        notifyChange();

        unsubscribe();
        subscription = favorite.rxCheck().subscribe(value -> {
            if (listenerRef != null) {
                ViewModelListener listener = listenerRef.get();
                if (listener != null) {
                    listener.onItemSelected(adapterPosition, value);
                }
            }
        });
        HELP_SUB_COUNT++;
        Log.d("wtopolski", "setData::HELP_SUB_COUNT: " + HELP_SUB_COUNT);
    }

    public void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
            HELP_SUB_COUNT--;
            Log.d("wtopolski", "unsubscribe::HELP_SUB_COUNT: " + HELP_SUB_COUNT);
        }
    }

    public void onItemClick() {
        if (listenerRef != null) {
            ViewModelListener listener = listenerRef.get();
            if (listener != null) {
                listener.onItemClick(adapterPosition);
            }
        }
    }

    public interface ViewModelListener {
        void onItemClick(int position);
        void onItemSelected(int position, boolean value);
    }
}
