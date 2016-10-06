package org.radiokit.almanac.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;
import org.radiokit.almanac.R;
import org.radiokit.almanac.event.picker.FromDatePickerFragment;
import org.radiokit.almanac.event.picker.FromTimePickerFragment;
import org.radiokit.almanac.event.picker.ToDatePickerFragment;
import org.radiokit.almanac.event.picker.ToTimePickerFragment;
import org.radiokit.almanac.model.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mateuszziomek on 29.09.16.
 */

public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddEventContract.View {

    @BindView(R.id.fromDate)
    TextView fromDate;
    @BindView(R.id.fromTime)
    TextView fromTime;
    @BindView(R.id.toDate)
    TextView toDate;
    @BindView(R.id.toTime)
    TextView toTime;
    @BindView(R.id.addingUser)
    TextView addingUser;
    @BindView(R.id.saveButton)
    Button saveButton;
    @BindView(R.id.stock)
    Spinner spinner;
    @BindView(R.id.reason)
    EditText reason;

    private AddEventPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        presenter = new AddEventPresenter();
        presenter.setView(this);

        // Create ArrayAdapter to supply the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.stocks_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Set views
        presenter.setCurrentDatetime();

        // Set default-current date and time for a new event
        presenter.setEventWithCurrentTime();

        fromDate.setText(presenter.getCurrentDate());
        fromTime.setText(presenter.getCurrentTime());
        toDate.setText(presenter.getCurrentDate());
        toTime.setText(presenter.getNextTime());

        // Just for the mock purposes
        addingUser.setText(R.string.mockUser);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        presenter.setSpinnerItemName(parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @OnClick(R.id.fromTime)
    @Override
    public void showFromTimePickerDialog(View v) {
        FromTimePickerFragment timePicker = new FromTimePickerFragment();
        timePicker.setPresenter(presenter);
        timePicker.show(getFragmentManager(), "FromTimePicker");
    }

    @OnClick(R.id.toTime)
    @Override
    public void showToTimePickerDialog(View view) {
        ToTimePickerFragment timePicker = new ToTimePickerFragment();
        timePicker.setPresenter(presenter);
        timePicker.show(getFragmentManager(), "ToTimePicker");
    }

    @OnClick(R.id.fromDate)
    @Override
    public void showFromDatePickerDialog(View view) {
        FromDatePickerFragment picker = new FromDatePickerFragment();
        picker.setPresenter(presenter);
        picker.show(getFragmentManager(), "FromDatePicker");
    }

    @OnClick(R.id.toDate)
    @Override
    public void showToDatePickerDialog(View view) {
        ToDatePickerFragment picker = new ToDatePickerFragment();
        picker.setPresenter(presenter);
        picker.show(getFragmentManager(), "ToDatePicker");
    }

    @Override
    public void updateFromTime(String hour, String minute) {
        fromTime.setText(String.format("%s:%s", hour, minute));
    }

    @Override
    public void updateFromDate(String year, String month, String day) {
        fromDate.setText(String.format("%s/%s/%s", year, month, day));
    }

    @Override
    public void updateToDate(String year, String month, String day) {
        toDate.setText(String.format("%s/%s/%s", year, month, day));
    }

    @Override
    public void updateToTime(String hour, String minute) {
        toTime.setText(String.format("%s:%s", hour, minute));
    }

    @Override
    public String getReason() {
        return reason.getText().toString();
    }

    @OnClick(R.id.saveButton)
    @Override
    public void saveEvent() {
        Event newEvent = presenter.getEvent();

        Chronology chronology = GregorianChronology.getInstance();
        DateTime startTime = new DateTime(
                newEvent.getFromYear(), newEvent.getFromMonth(), newEvent.getFromDay(),
                newEvent.getFromHour(), newEvent.getFromMinute(), 0, 0, chronology);
        DateTime endTime = new DateTime(
                newEvent.getToYear(), newEvent.getToMonth(), newEvent.getToDay(),
                newEvent.getToHour(), newEvent.getToMinute(), 0, 0, chronology);

        if (startTime.isBefore(endTime)) {
            Intent intent = new Intent();
            intent.putExtra("Event", newEvent);
            setResult(RESULT_OK, intent);
            Toast.makeText(this, getResources().getString(R.string.add_message), Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.wrongPeriodMessage), Toast.LENGTH_LONG).show();
        }
    }
}
