package com.sam.termtracker;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Helper {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");


    public static MaterialDatePicker<Long> changeDate(MaterialDatePicker<Long> inputDatePicker, TextInputEditText dateInput, FragmentManager supportedFragmentManager) {
        // create the datepicker object if one has not already been created/used in the view
        if (inputDatePicker == null) {
            inputDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            inputDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {

                    java.time.Instant instant = java.time.Instant.ofEpochMilli(selection);

                    // the date picker returns the unix time at 00:00 UTC of the day, so we have to make sure we use utc otherwise the date will be off
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy").withLocale(Locale.US).withZone(ZoneId.from(ZoneOffset.UTC));
                    String test = formatter.format(instant);
                    dateInput.setText(test);

                }
            });
        }
        inputDatePicker.show(supportedFragmentManager, "tag");
        return inputDatePicker;
    }

    public static int dateTextToEpoch(String dateText) {
        LocalDate date = LocalDate.parse(dateText, formatter);
        return (int) date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    }
}
