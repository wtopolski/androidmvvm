package com.github.wtopolski.libmvvm.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import rx.Subscriber;

public class CustomBindingAdapter {

    @BindingAdapter({"errorEnabled"})
    public static void errorEnabled(TextInputLayout view, String value) {
        view.setError(value);
        view.setErrorEnabled(!TextUtils.isEmpty(value));
    }

    @BindingAdapter({"editableObserver"})
    public static void editableObserver(View view, Subscriber<Boolean> subscriber) {
        if (subscriber != null) {
            subscriber.onNext(view.isEnabled());
            subscriber.onCompleted();
        }
    }

    @BindingAdapter({"textColor"})
    public static void textColor(TextView view, Integer colorRes) {
        if (colorRes != null) {
            view.setTextColor(colorRes);
        }
    }

    @BindingAdapter({"textAppearance"})
    public static void textAppearance(TextView view, Integer textAppearanceRes) {
        if (textAppearanceRes != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextAppearance(textAppearanceRes);
            } else {
                view.setTextAppearance(view.getContext(), textAppearanceRes);
            }
        }
    }

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(String color) {
        return new ColorDrawable(Color.parseColor(color));
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
