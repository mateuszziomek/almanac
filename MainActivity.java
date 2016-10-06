package org.radiokit.almanac;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.radiokit.almanac.calendar.CalendarFragment;
import org.radiokit.almanac.calendar.CalendarPresenter;
import org.radiokit.almanac.calendar.CustomViewPager;
import org.radiokit.almanac.calendar.adapter.SmartPagerAdapter;
import org.radiokit.almanac.event.AddEventActivity;
import org.radiokit.almanac.model.CustomWeekViewEvent;
import org.radiokit.almanac.model.Event;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_EVENT_REQUEST = 1;
    private static final String EVENT_TAG = "Event";
    private SmartPagerAdapter adapter;
    private CalendarFragment fragment;

    @Inject
    CalendarPresenter presenter;

    @BindView(R.id.pager)
    CustomViewPager pager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.actionButton)
    FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((AlmanacApplication)getApplication()).getAppComponent().inject(this);

        adapter = new SmartPagerAdapter(getFragmentManager(), this);
        pager.setAdapter(adapter);
        pager.setSwappable(false);
        pager.setOffscreenPageLimit(50);
        tabs.setupWithViewPager(pager);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivityForResult(intent,ADD_EVENT_REQUEST);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exitMessage)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ADD_EVENT_REQUEST) {
            Event event = (Event) data.getSerializableExtra(EVENT_TAG);
            CustomWeekViewEvent customWeekViewEvent = new CustomWeekViewEvent(
                    1, event.getReason(), event.getFromYear(), event.getFromMonth(),
                    event.getFromDay(), event.getFromHour(), event.getFromMinute(),
                    event.getToYear(), event.getToMonth(), event.getToDay(),
                    event.getToHour(),event.getToMinute(), "mock-Stock-Id",
                    "mock-Event-Id", "Paweł Michalski", event.getStockName());
            // return current Fragment item displayed within pager
            switch (event.getStockName()) {
                case "Studio 1":
                    fragment = (CalendarFragment) adapter.getRegisteredFragment(0);
                    break;
                case "Studio 2":
                    fragment = (CalendarFragment) adapter.getRegisteredFragment(1);
                    break;
                case "Samochód":
                    fragment = (CalendarFragment) adapter.getRegisteredFragment(2);
                    break;
            }
            fragment.addNewEvent(customWeekViewEvent);
        }
    }
}
