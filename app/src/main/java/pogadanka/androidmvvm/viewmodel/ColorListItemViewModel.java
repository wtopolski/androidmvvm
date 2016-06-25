package pogadanka.androidmvvm.viewmodel;

import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

import pogadanka.androidmvvm.model.ColorForm;
import pogadanka.mvvmlib.viewmodel.BaseViewModel;
import pogadanka.mvvmlib.viewmodel.CheckBoxViewModel;
import pogadanka.mvvmlib.viewmodel.TextViewModel;
import rx.Subscription;

public class ColorListItemViewModel extends BaseViewModel {

    public static int HELP_SUB_COUNT = 0;

    private WeakReference<ViewModelListener> listenerRef;
    private Subscription subscription;

    public TextViewModel name;
    public TextViewModel description;
    public CheckBoxViewModel favorite;

    @Bindable
    public String color;

    public ColorListItemViewModel() {
        name = new TextViewModel(true, true);
        description = new TextViewModel(true, true);
        favorite = new CheckBoxViewModel(true, true);
        color = "#ffffff";
    }

    public void setListener(ViewModelListener listener) {
        this.listenerRef = new WeakReference<>(listener);
    }

    public void setData(ColorForm form) {
        name.text = form.getName();
        description.text  = form.getDescription();
        color = form.getColor();
        favorite.checked = form.isFavorite();
        notifyChange();

        unsubscribe();
        subscription = favorite.rxCheckBox().subscribe(value -> {
            if (listenerRef != null) {
                ViewModelListener listener = listenerRef.get();
                if (listener != null) {
                    listener.onItemSelected(value);
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

    public void onItemClick(View view) {
        if (listenerRef != null) {
            ViewModelListener listener = listenerRef.get();
            if (listener != null) {
                listener.onItemClick();
            }
        }
    }

    public interface ViewModelListener {
        void onItemClick();
        void onItemSelected(boolean value);
    }
}
