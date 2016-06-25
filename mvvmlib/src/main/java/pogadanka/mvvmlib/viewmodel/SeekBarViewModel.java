package pogadanka.mvvmlib.viewmodel;

import android.databinding.Bindable;
import android.util.Log;
import android.widget.SeekBar;

import pogadanka.mvvmlib.BR;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * View model for Button
 * <p>
 * Created by wojciechtopolski on 11/03/16.
 */
public class SeekBarViewModel extends BaseViewModel implements SeekBar.OnSeekBarChangeListener {

    public SeekBarViewModel() {
        super();
        onSeekBarChangeListener = this;
    }

    public SeekBarViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
        onSeekBarChangeListener = this;
    }

    @Bindable
    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

    @Bindable
    public int progress;

    public void setProgress(int progress) {
        this.progress = progress;
        progressObservable.onNext(progress);
        notifyPropertyChanged(BR.progress);
    }

    @Bindable
    public int max;

    public void setMax(int max) {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }

    private PublishSubject<Integer> progressObservable = PublishSubject.create();

    public Observable<Integer> rxProgress() {
        return progressObservable.asObservable();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progressObservable.onNext(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // ignore
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // ignore
    }
}
