package pogadanka.androidmvvm.viewmodel;

import android.databinding.Bindable;
import android.graphics.Color;
import android.text.TextUtils;

import pogadanka.androidmvvm.BR;
import pogadanka.androidmvvm.model.ColorForm;
import pogadanka.mvvmlib.viewmodel.BaseViewModel;
import pogadanka.mvvmlib.viewmodel.ButtonViewModel;
import pogadanka.mvvmlib.viewmodel.ProgressViewModel;
import pogadanka.mvvmlib.viewmodel.SeekBarViewModel;
import pogadanka.mvvmlib.viewmodel.TextInputLayoutViewModel;
import pogadanka.mvvmlib.viewmodel.TextViewModel;
import rx.Observable;
import rx.Subscription;

/**
 * Created by wojciechtopolski on 05/05/16.
 */
public class ColorFormViewModel extends BaseViewModel {
    public ButtonViewModel confirm;
    public ButtonViewModel next;
    public ProgressViewModel progress;
    public TextInputLayoutViewModel name;
    public TextInputLayoutViewModel description;
    public SeekBarViewModel red;
    public SeekBarViewModel green;
    public SeekBarViewModel blue;
    public TextViewModel redDesc;
    public TextViewModel greenDesc;
    public TextViewModel blueDesc;

    private Subscription nameSubscription;
    private Subscription descriptionSubscription;
    private Subscription validSubscription;
    private Subscription colorSubscription;

    @Bindable
    public String color;

    public ColorFormViewModel() {
        progress = new ProgressViewModel(false, false);

        confirm = new ButtonViewModel(false, true);
        confirm.text = "Ok";

        next = new ButtonViewModel(true, false);
        next.text = "Next";

        name = new TextInputLayoutViewModel(true, true);
        name.hint = "Name";

        description = new TextInputLayoutViewModel(true, true);
        description.hint = "Description";

        red = new SeekBarViewModel(true, true);
        red.max = 255;
        red.progress = 0;

        green = new SeekBarViewModel(true, true);
        green.max = 255;
        green.progress = 0;

        blue = new SeekBarViewModel(true, true);
        blue.max = 255;
        blue.progress = 0;

        redDesc = new TextViewModel(true, true);
        redDesc.text = "Red";

        greenDesc = new TextViewModel(true, true);
        greenDesc.text = "Green";

        blueDesc = new TextViewModel(true, true);
        blueDesc.text = "Blue";
    }

    public void bind() {
        // Validation of name
        nameSubscription = name.rxValue().subscribe(value -> {
            name.setValueError(TextUtils.isEmpty(value) ? "Empty value" : null);
        });

        // Validation of description
        descriptionSubscription = description.rxValue().subscribe(value -> {
            description.setValueError(TextUtils.isEmpty(value) ? "Empty value" : null);
        });

        // Enable / disable button
        validSubscription = Observable.combineLatest(name.rxValue(), description.rxValue(),
                (nameValue, descriptionValue) -> !TextUtils.isEmpty(nameValue) && !TextUtils.isEmpty(descriptionValue)).
                subscribe(enabled -> {
                    confirm.setEnabled(enabled);
                });

        // Join color
        colorSubscription = Observable.combineLatest(red.rxProgress(), green.rxProgress(), blue.rxProgress(),
                (red1, green1, blue1) -> String.format("#%02X%02X%02X", red1, green1, blue1)).
                subscribe(color -> {
                    this.color = color;
                    notifyPropertyChanged(BR.color);
                });

        // Setup first state for 'combineLatest'
        if (!TextUtils.isEmpty(name.value)) {
            name.setValue(name.value);
        }
        if (!TextUtils.isEmpty(description.value)) {
            description.setValue(description.value);
        }
        setColor(color);
    }

    public void unbind() {
        if (colorSubscription != null && !colorSubscription.isUnsubscribed()) {
            colorSubscription.unsubscribe();
        }

        if (validSubscription != null && !validSubscription.isUnsubscribed()) {
            validSubscription.unsubscribe();
        }

        if (nameSubscription != null && !nameSubscription.isUnsubscribed()) {
            nameSubscription.unsubscribe();
        }

        if (descriptionSubscription != null && !descriptionSubscription.isUnsubscribed()) {
            descriptionSubscription.unsubscribe();
        }
    }

    public void setProgress(boolean inProgress) {
        progress.setVisibility(inProgress);
        name.setEnabled(!inProgress);
        description.setEnabled(!inProgress);
        red.setEnabled(!inProgress);
        green.setEnabled(!inProgress);
        blue.setEnabled(!inProgress);
        confirm.setEnabled(!inProgress);
    }

    public void setData(ColorForm form) {
        name.setValue(form.getName());
        description.setValue(form.getDescription());
        setColor(form.getColor());
    }

    public ColorForm getData() {
        ColorForm form = new ColorForm();
        form.setName(name.value);
        form.setDescription(description.value);
        form.setColor(color);
        return form;
    }

    public Observable<Object> rxConfirm() {
        return confirm.rxTap();
    }

    public Observable<Object> rxNext() {
        return next.rxTap();
    }

    private void setColor(String color) {
        if (TextUtils.isEmpty(color)) {
            color = "#000000";
        }
        this.color = color;
        int tmpColor = Color.parseColor(color);
        red.setProgress(Color.red(tmpColor));
        green.setProgress(Color.green(tmpColor));
        blue.setProgress(Color.blue(tmpColor));
    }
}
