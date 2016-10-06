package org.radiokit.almanac.event.picker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import org.radiokit.almanac.event.AddEventPresenter;

import java.util.Calendar;

/**
 * Created by mateuszziomek on 30.09.16.
 */

public class ToTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private AddEventPresenter presenter;

    public void setPresenter(AddEventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        hour = hour + 1;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, 0, true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        presenter.setToTime(hourOfDay, minute);
    }


}
