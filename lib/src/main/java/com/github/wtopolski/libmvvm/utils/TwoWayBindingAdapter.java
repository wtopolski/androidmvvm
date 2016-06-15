package com.github.wtopolski.libmvvm.utils;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.util.Log;

import com.github.wtopolski.libmvvm.view.ColorationSeekBar;

@InverseBindingMethods({@InverseBindingMethod(
        type = com.github.wtopolski.libmvvm.view.ColorationSeekBar.class,
        attribute = "position",
        method = "getPosition",
        event = "positionAttrChanged"
)})
public class TwoWayBindingAdapter {

    @BindingAdapter({"colorationLabel"})
    public static void colorationLabel(com.github.wtopolski.libmvvm.view.ColorationSeekBar view, String label) {
        view.colorationLabel(label);
    }

    @BindingAdapter({"position"})
    public static void setPosition(com.github.wtopolski.libmvvm.view.ColorationSeekBar view, ColorationSeekBar.ColorationType position) {
        Log.d("wtopolski", "set " + position);
        if (!view.getPosition().equals(position)) {
            view.setPosition(position);
        }
    }

    @InverseBindingAdapter(attribute = "position")
    public static ColorationSeekBar.ColorationType getPosition(com.github.wtopolski.libmvvm.view.ColorationSeekBar view) {
        Log.d("wtopolski", "get " + view.getPosition());
        return view.getPosition();
    }

    @BindingAdapter({"positionAttrChanged"})
    public static void positionAttrChanged(com.github.wtopolski.libmvvm.view.ColorationSeekBar view, final InverseBindingListener positionChange) {
        if (positionChange == null) {
            view.setOnPositionChangeListener(null);
        } else {
            view.setOnPositionChangeListener(new ColorationSeekBar.OnPositionChangeListener() {
                @Override
                public void onColorChange(ColorationSeekBar view, int position) {
                    Log.d("wtopolski", "onColorChange");
                    positionChange.onChange();
                }
            });
        }
        positionChange.onChange();
    }
}
