package pogadanka.androidmvvm.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import pogadanka.androidmvvm.BR;

import pogadanka.androidmvvm.R;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by wojciechtopolski on 22/03/16.
 */
public class SpinnerViewModel extends BaseViewModel {
    public ArrayAdapter<CharSequence> adapter;

    public SpinnerViewModel(Context ctx, int arrayRes) {
        adapter = ArrayAdapter.createFromResource(ctx, arrayRes, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Bindable
    public int position;

    private PublishSubject<String> countryObservable = PublishSubject.create();

    public AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String value = String.valueOf(adapter.getItem(position));
            SpinnerViewModel.this.position = position;
            countryObservable.onNext(value);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public String getValue() {
        return String.valueOf(adapter.getItem(position));
    }

    public void setValue(String value) {
        int position = adapter.getPosition(value);
        if (position < 0) {
            position = 0;
        }
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    public Observable<String> rxSpinner() {
        return countryObservable.asObservable();
    }
}
