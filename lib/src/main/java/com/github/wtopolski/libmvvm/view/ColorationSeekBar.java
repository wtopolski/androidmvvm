package com.github.wtopolski.libmvvm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.github.wtopolski.libmvvm.databinding.ColorationSeekbarInnerBinding;

/**
 * Created by wojciechtopolski on 01/07/16.
 */
public class ColorationSeekBar extends LinearLayout implements SeekBar.OnSeekBarChangeListener {
    private ColorationSeekbarInnerBinding binding;
    private OnPositionChangeListener listener;
    private int max;
    private ColorationType position;

    public enum ColorationType {
        COLOR, SEPIA, BLACK_WHITE
    }

    public ColorationSeekBar(Context context) {
        super(context);
        init();
    }

    public ColorationSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorationSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d("wtopolski", "init");
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = ColorationSeekbarInnerBinding.inflate(inflater, this, true);

        max = ColorationType.values().length - 1;
        position = ColorationType.COLOR;

        updateValue();
        binding.colorationSeekbarData.setMax(max);
        binding.colorationSeekbarData.setProgress(position.ordinal());
        binding.colorationSeekbarData.setOnSeekBarChangeListener(this);
    }

    public void colorationLabel(String label) {
        if (binding == null) {
            return;
        }

        binding.colorationSeekbarLabel.setText(label);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (binding == null) {
            return;
        }

        binding.colorationSeekbarLabel.setEnabled(enabled);
        binding.colorationSeekbarValue.setEnabled(enabled);
        binding.colorationSeekbarData.setEnabled(enabled);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (binding == null) {
            return;
        }

        binding.colorationSeekbarLabel.setVisibility(visibility);
        binding.colorationSeekbarValue.setVisibility(visibility);
        binding.colorationSeekbarData.setVisibility(visibility);
    }

    public ColorationType getPosition() {
        Log.d("wtopolski", "CSB getPosition: " + position);
        return position;
    }

    public void setPosition(ColorationType position) {
        Log.d("wtopolski", "CSB setPosition: " + position);
        this.position = position;
        binding.colorationSeekbarData.setProgress(position.ordinal());
        updateValue();
    }

    public void setOnPositionChangeListener(OnPositionChangeListener listener) {
        this.listener = listener;
    }

    public interface OnPositionChangeListener {
        void onColorChange(ColorationSeekBar view, int position);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress >= 0 && progress <= max) {
            position = ColorationType.values()[progress];
            Log.d("wtopolski", "onProgressChanged: " + position);
            updateValue();
            if (listener != null) {
                listener.onColorChange(this, progress);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void updateValue() {
        binding.colorationSeekbarValue.setText(position.name().replace('_', ' '));
    }
}
