package com.sam.termtracker;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import com.sam.termtracker.UI.TermRecyclerAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Helper {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    public static MaterialDatePicker<Long> buildDatePicker(TextInputEditText inputText) {
        MaterialDatePicker <Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                java.time.Instant instant = java.time.Instant.ofEpochMilli(selection);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy").withLocale(Locale.US).withZone(ZoneId.from(ZoneOffset.UTC));
                String test = formatter.format(instant);
                inputText.setText(test);
            }
        });
        return datePicker;
    }

    public static int dateTextToEpoch(String dateText) {
        LocalDate date = LocalDate.parse(dateText, formatter);
        int test = (int) date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        return test;
    }

    public static String epochToString(int timeStamp) {
       LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timeStamp, 0, ZoneOffset.UTC);
       return formatter.format(localDateTime);
    }


}
