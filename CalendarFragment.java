package org.radiokit.almanac.calendar;

import android.app.Fragment;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.radiokit.almanac.AlmanacApplication;
import org.radiokit.almanac.R;
import org.radiokit.almanac.model.CustomWeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import static org.radiokit.almanac.R.id.calendar;

/**
 * Created by mateuszziomek on 26.09.16.
 */

public class CalendarFragment extends Fragment implements CalendarContract.View {

    @Inject
    CalendarPresenter presenter;

    private WeekView calendarView;
    private List<CustomWeekViewEvent> allEvents = new ArrayList<>();
    private Calendar calendarInstance;

    // newInstance constructor for creating fragment with arguments
    public static CalendarFragment newInstance(int page) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public WeekView getCalendarView() {
        return this.calendarView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.calendar_view, container, false);
        calendarView = (WeekView) viewGroup.findViewById(calendar);

        calendarInstance = Calendar.getInstance();

        // Access components of Dagger 2
        ((AlmanacApplication) getActivity().getApplication()).getAppComponent().inject(this);

        // Set an action when any event is clicked.
        calendarView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Toast.makeText(getActivity().getApplicationContext(), event.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        calendarView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                if (allEvents.size() == 0) {
                    return setEventsToDisplay(getMockEvents(), newYear, newMonth);
                } return setEventsToDisplay(allEvents, newYear, newMonth);
            }
        });

        // Set long press listener for events.
        calendarView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        });

        calendarView.goToHour(presenter.getCurrentHour() - 2);
        return viewGroup;
    }

    public List<CustomWeekViewEvent> getMockEvents() {
        int day = calendarInstance.get(Calendar.DAY_OF_MONTH);
        int nextDay = day + 1;
        int month = calendarInstance.get(Calendar.MONTH);
        month = month + 1;

        CustomWeekViewEvent event1 =
                new CustomWeekViewEvent(1, "Program poranny \n\nDziennikarz 1", 2016, month, day, 8, 30, 2016, month, day, 10, 45, null, null, null, "Studio 1");
        CustomWeekViewEvent event2 =
                new CustomWeekViewEvent(1, "Informacje \n\nDziennikarz 2", 2016, month, day, 11, 15, 2016, month, day, 12, 30, null, null, null, "Studio 1");
        event2.setColor(R.color.colorRed);
        CustomWeekViewEvent event3 =
                new CustomWeekViewEvent(1, "Gość dnia \n\nPaweł Michalski", 2016, month, day, 12, 30, 2016, month, day, 14, 0, null, null, null, "Studio 1");
        CustomWeekViewEvent event4 =
                new CustomWeekViewEvent(1, "Program popołudniowy \n\nDziennikarz 3", 2016, month, nextDay, 14, 30, 2016, month, nextDay, 16, 15, null, null, null, "Studio 1");
        CustomWeekViewEvent event5 =
                new CustomWeekViewEvent(1, "Program poranny \n\nDziennikarz 1", 2016, month, nextDay, 8, 30, 2016, month, nextDay, 10, 45, null, null, null, "Studio 1");
        CustomWeekViewEvent event6 =
                new CustomWeekViewEvent(1, "Audycja wieczorna \n\nDziennikarz 2", 2016, month, day, 18, 0, 2016, month, day, 20, 0, null, null, null, "Studio 1");
        CustomWeekViewEvent event7 =
                new CustomWeekViewEvent(1, "Audycja wieczorna \n\nDziennikarz 2", 2016, month, nextDay, 18, 0, 2016, month, nextDay, 20, 0, null, null, null, "Studio 1");

        List<CustomWeekViewEvent> mockEvents = new ArrayList<>();
        mockEvents.add(event1);
        mockEvents.add(event2);
        mockEvents.add(event3);
        mockEvents.add(event4);
        mockEvents.add(event5);
        mockEvents.add(event6);
        mockEvents.add(event7);

        allEvents = mockEvents;
        return mockEvents;
    }

    public void addNewEvent(CustomWeekViewEvent event) {
        allEvents.add(event);
        calendarView.notifyDatasetChanged();
    }

    public List<CustomWeekViewEvent> setEventsToDisplay(List<CustomWeekViewEvent> list, int newYear, int newMonth) {
        List<CustomWeekViewEvent> matchedEvents = new ArrayList<>();
        for (CustomWeekViewEvent weekViewEvent : list) {
            if (((newYear == weekViewEvent.getStartTime().get(Calendar.YEAR)) &&
                    (newMonth == weekViewEvent.getStartTime().get(Calendar.MONTH) + 1))
                    ||
                    ((newYear == weekViewEvent.getEndTime().get(Calendar.YEAR)) &&
                            (newMonth == weekViewEvent.getEndTime().get(Calendar.MONTH) + 1))) {
                matchedEvents.add(weekViewEvent);
            }
        }
        return matchedEvents;
    }
}
