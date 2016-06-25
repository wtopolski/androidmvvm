# Android MVVM

~~~
MODEL <--- (set/get) ---> VIEWMODEL <--- (notifications, callbacks) ---> VIEW
~~~

## Introduction

Traditional method for control UI bases on _findViewById(resId) -> View_ method.
Then we can get View object (for example Button widget), manipulate its state and use some callbacks.

_**VIEW**_
~~~
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/activity_vertical_margin"
    tools:context=".controller.SimpleFormActivity">

    <Button
        android:id="@+id/my_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</LinearLayout>
~~~

_**CONTROLLER**_
~~~
...
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_form);

    Button myButton = (Button) findViewById(R.id.my_button);

    myButton.setText("Tap Me");
    myButton.setVisibility(View.VISIBLE);

    myButton.setOnClickListener(v -> {
        Toast.makeText(SimpleFormActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
        myButton.setVisibility(View.GONE);
    });
}
...
~~~

MVVM which I used in project bases on two libraries:
- Data Binding Library (http://developer.android.com/tools/data-binding/guide.html)
- RxJava (https://github.com/ReactiveX/RxJava)

Thanks to them, we can simply split connection between controller and view, and put view model in the middle.

Let's change previous example.

_**MODEL**_
~~~
public class SimpleForm {
    private String buttonName;

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
}
~~~

_**VIEW MODEL**_
~~~
public class SimpleFormViewModel extends BaseObservable {

    @Bindable
    public boolean visibility;

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
        notifyPropertyChanged(BR.visibility);
    }
    
    @Bindable
    public String text;

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    private PublishSubject<Object> buttonObservable = PublishSubject.create();

    public Observable<Object> rxTap() {
        return buttonObservable.asObservable();
    }

    public void onButtonClick(View view) {
        buttonObservable.onNext(null);
    }

    public void setSimpleForm(SimpleForm form) {
        setText(form.getButtonName());
    }

    public void enableInterface(boolean enabled) {
        setVisibility(enabled);
    }
}
~~~

_**VIEW**_
~~~
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <import type="android.view.View" />

        <variable
            name="viewModel"
            type="pogadanka.androidmvvm.viewmodel.SimpleFormViewModel" />
    </data>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="@dimen/activity_vertical_margin"
        tools:context=".controller.SimpleFormActivity">

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{viewModel.text}"
            android:onClick="@{viewModel::onButtonClick}"
            android:visibility="@{viewModel.visibility ? View.VISIBLE : View.GONE}"/>

    </RelativeLayout>

</layout>
~~~

_**CONTROLLER**_
~~~
public class SimpleFormActivity extends AppCompatActivity {
    private SimpleFormViewModel viewModel;
    private SimpleForm form;
    private Subscription myButtonSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new SimpleFormViewModel();
        form = new SimpleForm();
        ActivitySimpleFormBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_form);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();

        form.setButtonName("Tap Me");
        viewModel.enableInterface(true);
        viewModel.setSimpleForm(form);

        myButtonSubscription = viewModel.rxTap().subscribe(o -> {
            Toast.makeText(SimpleFormActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
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
~~~

At the beginning traditional method is much shorter, but during but during develop, every new widget dramatically increases number of code lines. Additionally we must care about every widget separately so complexity of controller bothers in maintenance.

Benefits of MVVM:
- Controller doesn't care about single widget, it rather operates on higher abstraction level like business model (ex. _setProgress(true)_).
- Divide responsibility among layers. **Controller** operates on business model. **View model** do conversion between displayed data and stored data (model), validation of forms, etc.
- Changing UI (view) is easy. Thanks to prepared basic view models for single widgets we can combine complex layouts. (Next example)
- It's probably much easier to write unit tests. (Especially for view model and model)

## Form screen

In mobile applications we can distinguish several types of screens. One of them is **form screen**. It usually contains input widget like edit text and buttons and output widgets with content.

So let's create some more complicated example.

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/color_form.png)

To build above view I used a few view model components, which where prepared by me and extracted into library.

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/mvvm_viewmodel.png)

And now let's see SeekBarViewModel as an example of view model component. It's worth to notice, that ordinary callback object like _SeekBar.OnSeekBarChangeListener_ can be passed to **View** as a property. Another interesting thing is _progressObservable_ property (_PublishSubject<Integer>_). It collects all feedback from widget and forwards it to all subscribers.

_**VIEW**_
~~~
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/apk/res-auto">
    <data>
        <import type="android.view.View" />
        <variable name="viewModel" type="pogadanka.mvvmlib.viewmodel.SeekBarViewModel"/>
    </data>

    <SeekBar
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="@{viewModel.max}"
        android:progress="@{viewModel.progress}"
        android:onSeekBarChangeListener="@{viewModel.onSeekBarChangeListener}"
        android:visibility="@{viewModel.visibility ? View.VISIBLE : View.GONE}"
        bind:editable="@{viewModel.enabled}" />

</layout>
~~~

_**VIEW MODEL**_
~~~
...
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

    @Bindable
    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

    private PublishSubject<Integer> progressObservable = PublishSubject.create();

    public Observable<Integer> rxProgress() {
        return progressObservable.asObservable();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progressObservable.onNext(progress);
    }
...
}
~~~

Combining more elements like this into one, it's possible to create complex screen with minimal lines of code.

See:

- [VIEW] (https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/res/layout/activity_color_form.xml)
~~~
...
<include
    layout="@layout/seekbar_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    bind:viewModel="@{viewModel.red}" />
...
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="50dp"
    bind:backgroundColor="@{viewModel.color}">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:textSize="15dp"
        android:textColor="@android:color/white"
        android:text="@{viewModel.color, default=fffffff}" />

</LinearLayout>
...
~~~
- [VIEW MODEL] (https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/java/pogadanka/androidmvvm/viewmodel/ColorFormViewModel.java)
~~~
...
colorSubscription = Observable.combineLatest(red.rxProgress(), green.rxProgress(), blue.rxProgress(),
        (red1, green1, blue1) -> String.format("#%02X%02X%02X", red1, green1, blue1)).
        subscribe(color -> {
            this.color = color;
            notifyPropertyChanged(BR.color);
        });
...
~~~
_What is important in this snippet is that three values are converted into one. **And** the result is immediately pushed to View using notifyPropertyChanged method, without bothering Controller._
- [MODEL] (https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/java/pogadanka/androidmvvm/model/ColorForm.java)
- [CONTROLLER] (https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/java/pogadanka/androidmvvm/controller/ColorFormActivity.java)

## List screen

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/list_screen.png)

Presenting list screen won't be so easy. We have to remember that this kind of screen has two sets of MVVM elements. One for list screen itself. And one for every item on the list. The first one is very similar to form screen. Essence of all we can see in *Controller*:

~~~
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    viewModel = new ColorListViewModel();
    ActivityColorListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_color_list);
    binding.setViewModel(viewModel);

    viewModel.configure(true, new DefaultItemAnimator(), new LinearLayoutManager(this));

    List<ColorForm> colors = new ArrayList<>(50);
    ...

    customAdapter = new CustomAdapter(colors);
    viewModel.setAdapter(customAdapter);
}
~~~

More interesting is MVVM for list elements. First of all, our adapter is much more simple. 

No more custom *ViewHolders* for *RecycleView*. All we need is *BindingViewHolder*:
~~~
public class BindingViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    public BindingViewHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}
~~~

*onCreateViewHolder* has only two lines in body:
~~~
public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ActivityColorListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_color_list_item, parent, false);
    return new BindingViewHolder(binding);
}
~~~

And *onBindViewHolder* is also much simpler:
~~~
public void onBindViewHolder(BindingViewHolder holder, int position) {
    ActivityColorListItemBinding binding = (ActivityColorListItemBinding) holder.getBinding();
    ColorListItemViewModel viewModel = binding.getViewModel();
    if (viewModel == null) {
        viewModel = new ColorListItemViewModel();
        binding.setViewModel(viewModel);
        ...

        viewModel.setListener(new ColorListItemViewModel.ViewModelListener() {
            @Override
            public void onItemClick() {
                CustomAdapter.this.onItemClick(holder.getAdapterPosition());
            }

            @Override
            public void onItemSelected(boolean value) {
                CustomAdapter.this.onItemSelected(holder.getAdapterPosition(), value);
            }
        });
    }

    ColorForm form = colors.get(position);
    viewModel.setData(form);
}

private void onItemSelected(int position, boolean value) {
    colors.get(position).setFavorite(value);
}

private void onItemClick(int position) {
    Toast.makeText(getApplicationContext(), "Click: " + position, Toast.LENGTH_SHORT).show();
}
~~~

And one more thing about *ViewModel*
~~~
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
        ...
    }
~~~

## Bonus

- Data Binding doesn't use reflection. Binding classes are generated almost in real time by Android Studio, and they are part of application. In case of androidmvvm app we can find those files in directory: AndroidMVVM/app/build/intermediates/classes/debug/pogadanka/androidmvvm/databinding/.
- Data Binding allow us to modify UI state from background thread.
- Sometimes there is not XML attribute that we can use to bind value. It's not a problem at all. We can write it using BindingAdapter annotation. Additionally there is no limit for number of attributes:

_**recycle_view.xml**_
~~~
bind:fixedSize="@{viewModel.fixedSize}"
bind:itemAnimator="@{viewModel.itemAnimator}"
bind:layoutManager="@{viewModel.layoutManager}"
~~~

_**CustomBindingAdapter.java**_
~~~
@BindingAdapter({"fixedSize", "itemAnimator", "layoutManager"})
public static void configure(RecyclerView view, boolean fixedSize, RecyclerView.ItemAnimator itemAnimator, RecyclerView.LayoutManager layoutManager) {
    view.setHasFixedSize(fixedSize);
    view.setItemAnimator(itemAnimator);
    view.setLayoutManager(layoutManager);
}
~~~

- The same solution help us write attributes for custom widgets. No more *attrs.xml* files!
- Show / hide password use case
~~~
@BindingAdapter({"passwordVisible"})
public static void passwordVisible(EditText view, boolean passwordVisible) {
    view.setTransformationMethod(passwordVisible ? new SingleLineTransformationMethod() : new PasswordTransformationMethod());
}
~~~

- Getting value from *View* which is not available in *ViewModel*.
~~~
...
private void checkIfConfirmIsEnabled() {
    viewModel.confirm.getEditableState(new Subscriber<Boolean>() {
        ...
        @Override
        public void onNext(Boolean isEnabled) {
            Toast.makeText(ColorFormActivity.this, "Is enabled: " + isEnabled, Toast.LENGTH_SHORT).show();
        }
    });
}
...
public void getEditableState(Subscriber<Boolean> editableState) {
    this.editableState = editableState;
    notifyPropertyChanged(BR.editableState);
}
...
<Button
    ...
    bind:editableState="@{viewModel.editableState}" />
...

@BindingAdapter({"editableState"})
public static void editableState(View view, Subscriber<Boolean> subscriber) {
    if (subscriber != null) {
        Observable.just(view.isEnabled()).subscribe(subscriber);
    }
}
...
~~~

*Keep in mind that due to async operation (notifyPropertyChanged), editable state of widget could be different than expected.*

- The order of attributes is important.

*BAD:* progress: 200, max: 255 -> progress: 100, max: 255 (WTF?!?!?)
~~~
android:progress="@{viewModel.progress}"
android:max="@{viewModel.max}"
~~~

*GOOD:* max: 255, progress: 200 -> max: 255, progress: 200 
~~~
android:max="@{viewModel.max}"
android:progress="@{viewModel.progress}"
~~~
