package pogadanka.androidmvvm.controller;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import pogadanka.androidmvvm.R;
import pogadanka.androidmvvm.databinding.ActivityColorFormBinding;
import pogadanka.androidmvvm.model.ColorForm;
import pogadanka.androidmvvm.viewmodel.ColorFormViewModel;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class ColorFormActivity extends AppCompatActivity {
    private static final String SAVED_OBJECT = "SAVED_OBJECT";

    private ColorFormViewModel viewModel;
    private ColorForm colorForm;
    private Subscription confirmSubscription;
    private Subscription nextSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ColorFormViewModel();
        ActivityColorFormBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_color_form);
        binding.setViewModel(viewModel);

        if (savedInstanceState != null) {
            colorForm = savedInstanceState.getParcelable(SAVED_OBJECT);
        }

        if (colorForm != null) {
            viewModel.setData(colorForm);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.bind();

        checkIfConfirmIsEnabled();

        confirmSubscription = viewModel.rxConfirm().subscribe(o -> {
            checkIfConfirmIsEnabled();
            Toast.makeText(ColorFormActivity.this, "" + viewModel.getData(), Toast.LENGTH_SHORT).show();
            doSomeAction(); // TODO: comment during presentation
        });

        nextSubscription = viewModel.rxNext().subscribe(o -> {
            startActivity(new Intent(getApplicationContext(), ColorListActivity.class));
        });
    }

    private void checkIfConfirmIsEnabled() {
        viewModel.confirm.getEditableState(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isEnabled) {
                Toast.makeText(ColorFormActivity.this, "Is enabled: " + isEnabled, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        colorForm = viewModel.getData();
        if (colorForm != null) {
            outState.putParcelable(SAVED_OBJECT, colorForm);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.unbind();

        if (confirmSubscription != null && !confirmSubscription.isUnsubscribed()) {
            confirmSubscription.unsubscribe();
        }

        if (nextSubscription != null && !nextSubscription.isUnsubscribed()) {
            nextSubscription.unsubscribe();
        }
    }

    private void doSomeAction() {
        viewModel.setProgress(true);
        Observable.just(true).
                delay(2000, TimeUnit.MILLISECONDS).
                subscribe(value -> {
                    viewModel.setProgress(false);
                    viewModel.next.setVisibility(true);
                });
    }
}
