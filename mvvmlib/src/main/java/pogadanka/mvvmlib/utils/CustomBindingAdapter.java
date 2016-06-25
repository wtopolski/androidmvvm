package pogadanka.mvvmlib.utils;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;

public class CustomBindingAdapter {

    @BindingAdapter({"errorEnabled"})
    public static void errorEnabled(TextInputLayout view, String value) {
        view.setError(value);
        view.setErrorEnabled(!TextUtils.isEmpty(value));
    }

    @BindingAdapter({"editable"})
    public static void editable(View view, boolean enabled) {
        view.setEnabled(enabled);
    }

    @BindingAdapter({"editableState"})
    public static void editableState(View view, Subscriber<Boolean> subscriber) {
        if (subscriber != null) {
            Observable.just(view.isEnabled()).subscribe(subscriber);
        }
    }

    @BindingAdapter({"textColor"})
    public static void textColor(TextView view, Integer colorRes) {
        if (colorRes != null) {
            view.setTextColor(colorRes);
        }
    }

    @BindingAdapter({"backgroundColor"})
    public static void backgroundColor(ViewGroup view, String color) {
        if (!TextUtils.isEmpty(color)) {
            view.setBackgroundColor(Color.parseColor(color));
        }
    }

    @BindingAdapter({"passwordVisible"})
    public static void passwordVisible(EditText view, boolean passwordVisible) {
        view.setTransformationMethod(passwordVisible ? new SingleLineTransformationMethod() : new PasswordTransformationMethod());
    }

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
}
