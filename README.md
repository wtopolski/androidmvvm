# Android MVVM

## Theory of MVVM

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/mvvm_diagram_black.png)

_"MVVM facilitates a separation of development of the graphical user interface from development of the business logic. The view model of MVVM is a value converter; meaning the view model is responsible for exposing (converting) the data objects from the model in such a way that objects are easily managed and presented. In this respect, the view model is more model than view, and handles most if not all of the view's display logic. The view model may implement a mediator pattern, organizing access to the back-end logic around the set of use cases supported by the view."_ [Source](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)

_"The essence of the Presentation Model is to take all behavior out of the View. The behavior and state is put in the Presentation Model. That means, the view itself will not keep any state (other than what it looks like.) The Presentation Model contains the state. This expresses itself best when a save button is clicked, at that moment, the state is saved from the Presentation Model to the Model. If any logic is needed to coordinate what the different controls on the form look like, then it happens in the Presentation Model, not in the view."_ [Source](http://www.albertzuurbier.com/index.php/programming/84-mvc-vs-mvp-vs-mvvm)

- **Model** - Business data layer, it could be set of POJO objects or abstraction layer with logic. Must be independent of user interface.
- **View** - User interface.
- **View Model** - Data converter model<->view, implement view logic, organize access to back-end logic.

## (Android) Data Binding Library

Allows manage and fill **View** layer in object oriented way. Instead of grabbing widgets one by one using `findViewById`, this library inverts the relationship. Now we build and fill object, which are passed into **View**. To use Data Binding with all latest described in this document, Android Plugin for Gradle **2.1+** is required. [Official page](https://developer.android.com/topic/libraries/data-binding/index.html)

Example by Google:

**User.java**
~~~
public class User {
   public final String firstName;
   public final String lastName;
   public User(String firstName, String lastName) {
       this.firstName = firstName;
       this.lastName = lastName;
   }
}
~~~

**MainActivity.java**
~~~
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
   User user = new User("Test", "User");
   binding.setUser(user);
}
~~~

**main_activity.xml**
~~~
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"/>
   </LinearLayout>
</layout>
~~~

Features:
- Variables and imports in `<data>...</data>` section bring logic into user interface.
- Widgets are available by `binding.id` expression, where `id` is equal to content of `android:id` attribute. However `android:id` is not obligatory at all.
- Setting any widget XML attribute is possible. Directly by setter paired with attribute like `android:enabled` and `view.setEnabled(boolean)`. Or indirectly using BindingAdapter or BindingMethods.
- Observable variables. Every data change could be reflected to View, if:
  - class extends `android.databinding.BaseObservable`
  - value is marked by `@Bindable` annotation
  - `notifyPropertyChanged(BR.value)` method executed
  (But there is more options)
- Two-way data binding allows synchronization widget's state between user interface and passed variable. Main restriction is XML attribute must have listener indicating change of value. Example: `<EditText android:text="@={user.firstName}" .../>` It works for [Source](https://halfthought.wordpress.com/2016/03/23/2-way-data-binding-on-android/):
  - AbsListView android:selectedItemPosition
  - CalendarView android:date
  - CompoundButton android:checked
  - DatePicker android:year, android:month, android:day (available listener)
  - NumberPicker android:value
  - RadioGroup android:checkedButton
  - RatingBar android:rating
  - SeekBar android:progress
  - TabHost android:currentTab (available listener)
  - EditText android:text
  - TimePicker android:hour, android:minute (available listener)
  We can implement two-way data binding for own custom views.
- Event Handling
  - Method References
    - _"listener implementation is created when the data is bound"_
    - _"expression is processed at compile time, so if the method does not exist or its signature is not correct, you receive a compile time error."_
    - _"method must match the parameters of the event listener"_
    - Example: `android:onClick="@{handlers::onClickFriend}"` `public void onClickFriend(View view) { ... }`
  - Listener Bindings
    - _"listener implementation is created when event is triggered"_
    - _"return value (of whole expression) must match the expected return value of the listener (unless it is expecting void)"_
    - Example: `android:onClick="@{() -> presenter.onSaveClick(task)}"` `public void onSaveClick(Task task){}`
- BindingAdapter allows to set any data using custom XML attribute. Example: `bind:errorEnabled="@{viewModel.valueError}"` where `valueError` is message to display. (The same solution helps us write attributes for custom widgets. No more *attrs.xml* files!)
~~~
@BindingAdapter({"errorEnabled"})
public static void errorEnabled(TextInputLayout view, String value) {
    view.setError(value);
    view.setErrorEnabled(!TextUtils.isEmpty(value));
}
~~~
  Use case "Show / hide password"
~~~
@BindingAdapter({"passwordVisible"})
public static void passwordVisible(EditText view, boolean passwordVisible) {
    view.setTransformationMethod(passwordVisible ? new SingleLineTransformationMethod() : new PasswordTransformationMethod());
}
~~~
- BindingMethods _"Some attributes have setters that don't match by name...For example, the android:tint attribute is really associated with setImageTintList(ColorStateList), not setTint."_
~~~
@BindingMethods({
  @BindingMethod(
    type = "android.widget.ImageView",
    attribute = "android:tint",
    method = "setImageTintList")
})
~~~
- Converter helps to translate given value format into format which is expected by specific XML tag. For example `android:background` expects `Drawable`. So, in order to translate color value (#000000) into `Drawable` object, we need below converter.
~~~
@BindingConversion
public static ColorDrawable convertColorToDrawable(String color) {
    return new ColorDrawable(Color.parseColor(color));
}
~~~
- In custom XML attributes we can use application resources by expression `bind:textColor="@{@color/blue}"`
- Immediate Binding. _"When a variable or observable changes, the binding will be scheduled to change before the next frame...To force execution, use the executePendingBindings() method."_
- Background Thread. _"You can change your data model in a background thread as long as it is not a collection."_
- Data Binding doesn't use reflection. Binding classes are generated almost in real time by Android Studio, and they are part of application. You can find them in `build` directory under location: `.../build/intermediates/classes/debug/package_name/databinding/*`.
- Including layout using `<include/>` tag
- Null Coalescing Operator `android:text="@{user.displayName ?? user.lastName}"`
- The order of attributes is important.

  DON'T: progress: 200, max: 255 -> progress: 100, max: 255 (WTF?!?!?)
~~~
android:progress="@{viewModel.progress}"
android:max="@{viewModel.max}"
~~~
  DO: max: 255, progress: 200 -> max: 255, progress: 200
~~~
android:max="@{viewModel.max}"
android:progress="@{viewModel.progress}"
~~~

### Traditional method

Traditional method for control UI bases on `findViewById()` method. Then we can get View object (for example Button widget), manipulate its state and use some callbacks.

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

At the beginning traditional method is simple and short, but during develop, every new widget dramatically increases number of code lines. Additionally we must care about every widget separately so complexity of controller bothers in maintenance.

### Custom MVVM implementation

This custom implementation of MVVM pattern bases on libraries:
- [Data Binding Library](http://developer.android.com/tools/data-binding/guide.html)
- [RxJava](https://github.com/ReactiveX/RxJava)

Responsibility among layers is:
- **Model** business data or interface to data
- **View** user interface (widgets, layouts)
- **View Model** conversion between displayed data and stored data (model), validation of forms

Ok, but where in this pattern is place for `Activity` and `Fragment`? There is no clear answer. In traditional Android application, without any doubt, they act as **Controller**. In MVVM pattern somebody could treat them as a **View**. However they contain too many complex logic as callbacks from system, saving and restoring state in bundles, initialization of layout, start new activity or fragment. So, in my opinion they don't belong to **View** layer for sure. As well as to **Model** layer. So the closest layer is **View Model**. There is plenty implementation in Internet which use `Activity` (or `Fragment`) as a trigger to create Binding object and **View Model** and next delegate all logic to that **View Model**. On the other hand we can add one more layer - **Controller**. Even if it breaks pattern, though it is better to split logic between **Controller** (framework logic) and **View Model** (logic related to view).

So in this MVVM(C) implementation we have additional layer:
- **Controller** (`Activity` or `Fragment`) which is responsible for initiation all layers, open Activities, display Toast messages operates.

So, let's change previous example.

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
   private SimpleForm form;
   private Subscription myButtonSubscription;
    
   public SimpleFormViewModel() {
      form = new SimpleForm();
      form.setButtonName("Tap Me");
   }

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

   public void onButtonClick(View view) {
      buttonObservable.onNext(null);
   }

   public void bind() {
      setText(form.getButtonName());
      setVisibility(true);

      myButtonSubscription = buttonObservable.asObservable().subscribe(o -> {
         form.setButtonName("Thank you");
         setText(form.getButtonName());
      });
   }
   
   public void unbind() {
      if (myButtonSubscription != null && !myButtonSubscription.isUnsubscribed()) {
         myButtonSubscription.unsubscribe();
      }
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
            type="package.SimpleFormViewModel" />
    </data>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="@dimen/activity_vertical_margin">

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

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      viewModel = new SimpleFormViewModel();
      ActivitySimpleFormBinding binding;
      binding = DataBindingUtil.setContentView(this, R.layout.activity_simple_form);
      binding.setViewModel(viewModel);
   }

   @Override
   protected void onStart() {
      super.onStart();
      viewModel.bind();
   }

   @Override
   protected void onStop() {
      super.onStop();
      viewModel.unbind();
   }
}
~~~

## View Model Composition

In real app, screens are definitely more complicated. They contain several input widgets like `EditText`, `Button`, etc.. and output widgets with content. Like this one:

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/color_form.png)

To build above view I used a few **View Model** components, which where prepared by me and extracted into separated project module.

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/mvvm_viewmodel.png)

Thanks them it is possible to build complex layouts.

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/color_form_view_model.png)

And now let's see `SeekBarViewModel` as an example of **View Model** component. It's worth to notice, that two-way binding is used.

_**VIEW**_
~~~
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/apk/res-auto">
    <data>
        <import type="android.view.View" />
        <variable name="viewModel" type="com.github.wtopolski.libmvvm.viewmodel.SeekBarViewModel"/>
    </data>

    <SeekBar
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:max="@{viewModel.max}"
        android:progress="@={viewModel.progress}"
        android:visibility="@{viewModel.visibility ? View.VISIBLE : View.GONE}"
        android:enabled="@{viewModel.enabled}" />

</layout>
~~~

_**VIEW MODEL**_
~~~
...
public class SeekBarViewModel extends BaseViewModel {

    private int progress;
    private int max;
    private PublishSubject<Integer> progressObservable = PublishSubject.create();

    public SeekBarViewModel() {
        super();
    }

    public SeekBarViewModel(boolean enabled, boolean visibility) {
        super(enabled, visibility);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        progressObservable.onNext(progress);
        notifyPropertyChanged(BR.progress);
    }

    @Bindable
    public int getProgress() {
        return progress;
    }

    public void setMax(int max) {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }

    @Bindable
    public int getMax() {
        return max;
    }

    public Observable<Integer> rxProgress() {
        return progressObservable.asObservable();
    }
  }
~~~

Combining more elements like above, allows us creating complex screen with minimal lines of code.

- [View](https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/res/layout/activity_color_form.xml)
  ~~~
  ...
  <include
    layout="@layout/seekbar_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    bind:viewModel="@{viewModel.red}" />

  <include
    layout="@layout/seekbar_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    bind:viewModel="@{viewModel.green}" />

  <include
    layout="@layout/seekbar_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    bind:viewModel="@{viewModel.blue}" />                        
  ...
  ~~~

- [View Model](https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/java/com/github/wtopolski/mvvmsampleapp/viewmodel/ColorFormViewModel.java)
  ~~~
  ...
   compositeSubscription.add(Observable.
      combineLatest(red.rxProgress(),
         green.rxProgress(),
         blue.rxProgress(),
         coloration.rxValue(),
         ColorUtils::generateColor).
      subscribe(this.color::set));
  ...
  ~~~

  What is important in this snippet is that three values are converted into one. And the result is immediately pushed to **View** using `notifyPropertyChanged` method, without bothering **Controller**.

- [Model](https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/java/com/github/wtopolski/mvvmsampleapp/model/ColorForm.java)
- [Controller](https://github.com/wtopolski/androidmvvm/blob/master/app/src/main/java/com/github/wtopolski/mvvmsampleapp/controller/ColorFormActivity.java)

## List

![alt tag](https://github.com/wtopolski/androidmvvm/blob/master/docs/list_screen.png)

Presenting list screen won't be so easy. We have to remember that this kind of screen has two sets of MVVM elements. One for list screen itself. And one for every item on the list. The first one is very similar to form screen. Essence of all we can see in **Controller**:

~~~
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   viewModel = new ColorListViewModel(this);
   binding = DataBindingUtil.setContentView(this, R.layout.activity_color_list);
   binding.setViewModel(viewModel);
}
~~~

And in **View Model**:
~~~
public ColorListViewModel(ActionDelegate actionDelegate) {
   super(actionDelegate);
   colors = generateColors();
   adapter = new ColorListAdapter(colors, this);
   
   list = new RecycleViewModel(true, true);
   list.setFixedSize(true);
   list.setItemAnimator(new DefaultItemAnimator());
   list.setLayoutManager(new LinearLayoutManager(actionDelegate.getAppContext()));
   list.setAdapter(adapter);
} 
~~~

More interesting is MVVM for list elements. First of all, our adapter is much more simple. No more custom `ViewHolders` for `RecycleView`. All we need is `BindingViewHolder`.
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

`onCreateViewHolder` has only two lines of code:
~~~
public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityColorListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_color_list_item, parent, false);
        return new BindingViewHolder(binding);
    }
~~~

And `onBindViewHolder` is also much simpler:
~~~
public void onBindViewHolder(BindingViewHolder holder, int position) {
  ActivityColorListItemBinding binding = (ActivityColorListItemBinding) holder.getBinding();
  ColorListItemViewModel viewModel = binding.getViewModel();

  // Init new view model object
  if (viewModel == null) {
      viewModel = new ColorListItemViewModel();
      binding.setViewModel(viewModel);
      binding.setTextColor(R.color.colorAccent);
      viewModel.setListener(listener);
  }

  ColorForm form = data.get(position);
  viewModel.setAdapterPosition(holder.getAdapterPosition());
  viewModel.setData(form);

  // Immediate Binding
  // When a variable or observable changes, the binding will be scheduled to change before
  // the next frame. There are times, however, when binding must be executed immediately.
  // To force execution, use the executePendingBindings() method.
  binding.executePendingBindings();
}
~~~

_**recycle_view.xml**_
~~~
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/apk/res-auto">
    <data>
        <import type="android.view.View" />
        <variable name="viewModel" type="com.github.wtopolski.libmvvm.viewmodel.RecycleViewModel"/>
    </data>

    <android.support.v7.widget.RecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical"
        bind:fixedSize="@{viewModel.fixedSize}"
        bind:itemAnimator="@{viewModel.itemAnimator}"
        bind:layoutManager="@{viewModel.layoutManager}"
        bind:adapter="@{viewModel.adapter}"/>

</layout>
~~~

_**CustomBindingAdapter.java**_
~~~
@BindingAdapter({"fixedSize", "itemAnimator", "layoutManager"})
public static void configure(RecyclerView view, boolean fixedSize, RecyclerView.ItemAnimator itemAnimator, RecyclerView.LayoutManager layoutManager) {
    view.setHasFixedSize(fixedSize);
    view.setItemAnimator(itemAnimator);
    view.setLayoutManager(layoutManager);
}

@BindingAdapter({"adapter"})
public static void adapter(RecyclerView view, RecyclerView.Adapter adapter) {
    view.setAdapter(adapter);
}
~~~

## Grabbing value directly from **View**

Getting value from *View* which is not available in *ViewModel*.
~~~
...
private void checkEnabledStateOnView() {
    viewModel.confirm.setEditableObserver(new Subscriber<Boolean>() {
        ...
        @Override
        public void onNext(Boolean isEnabled) {
            Toast.makeText(ColorFormActivity.this, "Enabled State On View: " + isEnabled, Toast.LENGTH_SHORT).show();
        }
    });

    // Immediate Binding
    // When a variable or observable changes, the binding will be scheduled to change before
    // the next frame. There are times, however, when binding must be executed immediately.
    // To force execution, use the executePendingBindings() method.
    // Without it toast sequence would be: B -> A not A -> B
    binding.executePendingBindings();
}
...
private Subscriber<Boolean> editableObserver;

public void setEditableObserver(Subscriber<Boolean> editableObserver) {
    this.editableObserver = editableObserver;
    notifyPropertyChanged(BR.editableObserver);
}

@Bindable
public Subscriber<Boolean> getEditableObserver() {
    return editableObserver;
}
...
<Button
    ...
    bind:editableObserver="@{viewModel.editableObserver}" />
...

@BindingAdapter({"editableObserver"})
    public static void editableObserver(View view, Subscriber<Boolean> subscriber) {
        if (subscriber != null) {
            subscriber.onNext(view.isEnabled());
            subscriber.onCompleted();
        }
    }
...
~~~

## Links
- [MVC VS. MVP VS. MVVM](http://www.albertzuurbier.com/index.php/programming/84-mvc-vs-mvp-vs-mvvm)
- [Model–view–viewmodel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)
- [Approaching Android with MVVM](https://labs.ribot.co.uk/approaching-android-with-mvvm-8ceec02d5442#.m19d0i4nq)
- [Going with MVVM on Android via Data Binding](http://cases.azoft.com/mvvm-android-data-binding/)
- [DataBinding: how to develop Android apps faster](https://stfalcon.com/en/blog/post/faster-android-apps-with-databinding)
- [Data Binding Library](https://developer.android.com/topic/libraries/data-binding/index.html)
- [Exploring the MVC, MVP, and MVVM design patterns](http://www.infoworld.com/article/2926003/microsoft-net/exploring-the-mvc-mvp-and-mvvm-design-patterns.html)
- [MVVMC thought experiment](http://kev-sharp.blogspot.com/2014/05/mvvmc-thought-experiment.html)
- [MVVM is dead, long live MVVMC!](http://skimp-blog.blogspot.com/2012/02/mvvm-is-dead-long-live-mvvmc.html)
- [Comparison of Architecture presentation patterns MVP(SC),MVP(PV),PM,MVVM and MVC](http://www.codeproject.com/Articles/66585/Comparison-of-Architecture-presentation-patterns-M)
- [MVMMC – MVVM Grows A Controller](http://trelford.com/blog/post/MVMMC-e28093-MVVM-grows-a-Controller.aspx)
