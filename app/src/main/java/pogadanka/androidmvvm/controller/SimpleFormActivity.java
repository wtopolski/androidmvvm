package pogadanka.androidmvvm.controller;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import pogadanka.androidmvvm.R;
import pogadanka.androidmvvm.databinding.ActivitySimpleFormBinding;
import pogadanka.androidmvvm.model.SimpleForm;
import pogadanka.androidmvvm.viewmodel.SimpleFormViewModel;
import rx.Subscription;

public class SimpleFormActivity extends AppCompatActivity {
    private SimpleFormViewModel viewModel;
    private SimpleForm form;
    private Subscription myButtonSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new SimpleFormViewModel();
        form = new SimpleForm();
        ActivitySimpleFormBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_form);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        form.setButtonName("Tap Me");
        viewModel.enableInterface(true);
        viewModel.setSimpleForm(form);

        myButtonSubscription = viewModel.myButton.rxButton().subscribe(o -> {
            Toast.makeText(SimpleFormActivity.this, "Tank you", Toast.LENGTH_SHORT).show();
            viewModel.enableInterface(false);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myButtonSubscription != null && !myButtonSubscription.isUnsubscribed()) {
            myButtonSubscription.unsubscribe();
        }
    }
}
