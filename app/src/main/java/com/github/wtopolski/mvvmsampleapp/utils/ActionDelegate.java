package com.github.wtopolski.mvvmsampleapp.utils;

import android.content.Context;

/**
 * Created by wojciechtopolski on 12/07/16.
 */
public interface ActionDelegate {
    void executePendingBindings();
    void showToastMessage(String message);
    void openWindow(Class className);
    Context getAppContext();
}
