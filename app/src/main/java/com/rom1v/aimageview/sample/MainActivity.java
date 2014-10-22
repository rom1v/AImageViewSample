/*
 * Copyright (C) 2014 Romain Vimont <rom@rom1v.com>
 *
 * This file is part of AImageViewSample.
 *
 * AImageViewSample is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * AImageViewSample is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AImageView.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.rom1v.aimageview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.rom1v.aimageview.AImageView;

public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private AImageView aImageView;
    private CheckBox downscaleCheckbox;
    private CheckBox upscaleCheckbox;
    private RadioGroup fitRadioGroup;
    private RadioButton fitInsideRadioButton;
    private RadioButton fitOutsideRadioButton;
    private RadioButton fitHorizontalRadioButton;
    private RadioButton fitVerticalRadioButton;
    private SeekBar xWeightSeekBar;
    private SeekBar yWeightSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aImageView = (AImageView) findViewById(R.id.image);

        xWeightSeekBar = (SeekBar) findViewById(R.id.x_weight_seekbar);
        yWeightSeekBar = (SeekBar) findViewById(R.id.y_weight_seekbar);
        downscaleCheckbox = (CheckBox) findViewById(R.id.downscale_checkbox);
        upscaleCheckbox = (CheckBox) findViewById(R.id.upscale_checkbox);
        fitRadioGroup = (RadioGroup) findViewById(R.id.fit_radio_group);
        fitInsideRadioButton = (RadioButton) findViewById(R.id.fit_inside_radio_button);
        fitOutsideRadioButton = (RadioButton) findViewById(R.id.fit_outside_radio_button);
        fitHorizontalRadioButton = (RadioButton) findViewById(R.id.fit_horizontal_radio_button);
        fitVerticalRadioButton = (RadioButton) findViewById(R.id.fit_vertical_radio_button);

        xWeightSeekBar.setProgress((int) (aImageView.getXWeight() * 100));
        yWeightSeekBar.setProgress((int) (aImageView.getYWeight() * 100));
        downscaleCheckbox.setChecked(aImageView.getDownscale());
        upscaleCheckbox.setChecked(aImageView.getUpscale());
        switch (aImageView.getFit()) {
            case INSIDE:
                fitInsideRadioButton.setChecked(true);
                break;
            case OUTSIDE:
                fitOutsideRadioButton.setChecked(true);
                break;
            case HORIZONTAL:
                fitHorizontalRadioButton.setChecked(true);
                break;
            case VERTICAL:
                fitVerticalRadioButton.setChecked(true);
                break;
        }
        refreshFitEnabled();

        xWeightSeekBar.setOnSeekBarChangeListener(this);
        yWeightSeekBar.setOnSeekBarChangeListener(this);
        downscaleCheckbox.setOnCheckedChangeListener(this);
        upscaleCheckbox.setOnCheckedChangeListener(this);
        fitRadioGroup.setOnCheckedChangeListener(this);
    }

    private void refreshFitEnabled() {
        // fit has no meaning when scale is disabled
        boolean scalable = aImageView.getScaleFlags() != 0;
        fitInsideRadioButton.setEnabled(scalable);
        fitOutsideRadioButton.setEnabled(scalable);
        fitHorizontalRadioButton.setEnabled(scalable);
        fitVerticalRadioButton.setEnabled(scalable);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == xWeightSeekBar) {
            // we move the image horizontally on the vertical aciv
            aImageView.setXWeight(progress / 100f);
        } else {
            // we move the image vertically on the horizontal aciv
            aImageView.setYWeight(progress / 100f);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == downscaleCheckbox) {
            aImageView.setDownscale(isChecked);
        } else {
            aImageView.setUpscale(isChecked);
        }
        refreshFitEnabled();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        AImageView.Fit fit;
        switch (checkedId) {
            case R.id.fit_inside_radio_button:
                fit = AImageView.Fit.INSIDE;
                break;
            case R.id.fit_outside_radio_button:
                fit = AImageView.Fit.OUTSIDE;
                break;
            case R.id.fit_horizontal_radio_button:
                fit = AImageView.Fit.HORIZONTAL;
                break;
            case R.id.fit_vertical_radio_button:
                fit = AImageView.Fit.VERTICAL;
                break;
            default:
                throw new IllegalArgumentException();
        }
        aImageView.setFit(fit);
    }
}
