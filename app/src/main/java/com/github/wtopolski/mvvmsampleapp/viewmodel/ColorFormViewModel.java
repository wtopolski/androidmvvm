package com.github.wtopolski.mvvmsampleapp.viewmodel;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.github.wtopolski.mvvmsampleapp.R;
import com.github.wtopolski.mvvmsampleapp.controller.ColorListActivity;
import com.github.wtopolski.mvvmsampleapp.model.ColorForm;
import com.github.wtopolski.mvvmsampleapp.utils.ActionDelegate;
import com.github.wtopolski.mvvmsampleapp.utils.ActionViewModel;
import com.github.wtopolski.mvvmsampleapp.utils.ColorUtils;
import com.github.wtopolski.libmvvm.view.ColorationSeekBar;
import com.github.wtopolski.libmvvm.viewmodel.ButtonViewModel;
import com.github.wtopolski.libmvvm.viewmodel.ColorationSeekBarViewModel;
import com.github.wtopolski.libmvvm.viewmodel.ProgressViewModel;
import com.github.wtopolski.libmvvm.viewmodel.SeekBarViewModel;
import com.github.wtopolski.libmvvm.viewmodel.TextInputLayoutViewModel;
import com.github.wtopolski.libmvvm.viewmodel.TextViewModel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by wojciechtopolski on 05/05/16.
 */
public class ColorFormViewModel extends ActionViewModel {
    private static final String SAVED_OBJECT = "SAVED_OBJECT";

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
    public ColorationSeekBarViewModel coloration;

    public final ObservableField<String> color = new ObservableField<>();

    public ColorFormViewModel(ActionDelegate actionDelegate) {
        super(actionDelegate);

        progress = new ProgressViewModel(false, false);

        confirm = new ButtonViewModel(false, true);
        confirm.setText("Ok");

        next = new ButtonViewModel(true, false);
        next.setText("Next");

        name = new TextInputLayoutViewModel(true, true);
        name.setHint("Name");

        description = new TextInputLayoutViewModel(true, true);
        description.setHint("Description");

        red = new SeekBarViewModel(true, true);
        red.setMax(255);
        red.setProgress(0);

        green = new SeekBarViewModel(true, true);
        green.setMax(255);
        green.setProgress(0);

        blue = new SeekBarViewModel(true, true);
        blue.setMax(255);
        blue.setProgress(0);

        redDesc = new TextViewModel(true, true);
        redDesc.setText("Red");
        redDesc.setTextAppearance(R.style.MVVMTextAppearance_Caption);

        greenDesc = new TextViewModel(true, true);
        greenDesc.setText("Green");
        greenDesc.setTextAppearance(R.style.MVVMTextAppearance_Caption);

        blueDesc = new TextViewModel(true, true);
        blueDesc.setText("Blue");
        blueDesc.setTextAppearance(R.style.MVVMTextAppearance_Caption);

        coloration = new ColorationSeekBarViewModel(true, true);
        coloration.setLabel("Coloration");
        coloration.setPosition(ColorationSeekBar.ColorationType.COLOR);
    }

    @Override
    public void bind() {
        // Validation of name
        compositeSubscription.add(name.rxValue().subscribe(value -> {
            name.setValueError(TextUtils.isEmpty(value) ? "Empty value" : null);
        }));

        // Validation of description
        compositeSubscription.add(description.rxValue().subscribe(value -> {
            description.setValueError(TextUtils.isEmpty(value) ? "Empty value" : null);
        }));

        // Enable / disable button
        compositeSubscription.add(Observable.combineLatest(name.rxValue(), description.rxValue(),
                (nameValue, descriptionValue) -> !TextUtils.isEmpty(nameValue) && !TextUtils.isEmpty(descriptionValue)).
                subscribe(enabled -> {
                    confirm.setEnabled(enabled);
                }));

        // Join color
        compositeSubscription.add(Observable.
                combineLatest(red.rxProgress(), green.rxProgress(), blue.rxProgress(), coloration.rxValue(), ColorUtils::generateColor).
                subscribe(this.color::set));

        // Setup first state for 'combineLatest'
        if (!TextUtils.isEmpty(name.getValue())) {
            name.setValue(name.getValue());
        }

        if (!TextUtils.isEmpty(description.getValue())) {
            description.setValue(description.getValue());
        }

        compositeSubscription.add(confirm.rxTap().subscribe(o -> {
            checkEnabledStateOnView();
            checkEnabledStateOnViewModel();
            actionDelegate.showToastMessage("" + getData());
            doSomeAction();
        }));

        compositeSubscription.add(next.rxTap().subscribe(o -> {
            actionDelegate.openWindow(ColorListActivity.class);
        }));

        setColor(color.get());
    }

    private void checkEnabledStateOnView() {
        confirm.setEditableObserver(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isEnabled) {
                actionDelegate.showToastMessage("Enabled State On View: " + isEnabled);
            }
        });

        // Immediate Binding
        // When a variable or observable changes, the binding will be scheduled to change before
        // the next frame. There are times, however, when binding must be executed immediately.
        // To force execution, use the executePendingBindings() method.
        // Without it toast sequence would be: B -> A not A -> B
        actionDelegate.executePendingBindings();
    }

    private void doSomeAction() {
        setProgress(true);
        Observable.just(true).
                delay(2000, TimeUnit.MILLISECONDS).
                subscribe(value -> {
                    setProgress(false);
                    next.setVisibility(true);
                });
    }

    private void checkEnabledStateOnViewModel() {
        boolean isEnabled = confirm.getEnabled();
        actionDelegate.showToastMessage("Enabled State On View Model: " + isEnabled);
    }

    public void setProgress(boolean inProgress) {
        progress.setVisibility(inProgress);
        name.setEnabled(!inProgress);
        description.setEnabled(!inProgress);
        red.setEnabled(!inProgress);
        green.setEnabled(!inProgress);
        blue.setEnabled(!inProgress);
        confirm.setEnabled(!inProgress);
        coloration.setEnabled(!inProgress);
    }

    public void setData(ColorForm form) {
        name.setValue(form.getName());
        description.setValue(form.getDescription());
        setColor(form.getColor());
    }

    public ColorForm getData() {
        ColorForm form = new ColorForm();
        form.setName(name.getValue());
        form.setDescription(description.getValue());
        form.setColor(color.get());
        form.setColoration(coloration.getPosition());
        return form;
    }

    private void setColor(String color) {
        if (TextUtils.isEmpty(color)) {
            color = "#000000";
        }
        this.color.set(color);
        int tmpColor = Color.parseColor(color);
        red.setProgress(Color.red(tmpColor));
        green.setProgress(Color.green(tmpColor));
        blue.setProgress(Color.blue(tmpColor));
    }

    @Override
    public void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ColorForm colorForm = savedInstanceState.getParcelable(SAVED_OBJECT);
            if (colorForm != null) {
                setData(colorForm);
            }
        }
    }

    @Override
    public void saveState(Bundle outState) {
        ColorForm colorForm = getData();
        if (colorForm != null) {
            outState.putParcelable(SAVED_OBJECT, colorForm);
        }
    }
}
