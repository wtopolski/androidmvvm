package com.github.wtopolski.mvvmsampleapp.utils;

import android.os.Bundle;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by wojciechtopolski on 12/07/16.
 */
public class ActionViewModel {

    protected ActionDelegate actionDelegate;
    protected CompositeSubscription compositeSubscription;

    public ActionViewModel(ActionDelegate actionDelegate) {
        this.actionDelegate = actionDelegate;
        compositeSubscription = new CompositeSubscription();
    }

    public void restoreState(Bundle savedInstanceState) {

    }

    public void saveState(Bundle outState) {

    }

    public void bind() {

    }

    public void unbind() {
        compositeSubscription.clear();
    }

    public void releaseDelegate() {
        actionDelegate = null;
    }
}
