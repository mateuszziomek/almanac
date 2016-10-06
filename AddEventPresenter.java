package org.radiokit.almanac.event;

import org.radiokit.almanac.model.Event;

import java.util.Calendar;

/**
 * Created by mateuszziomek on 29.09.16.
 */

public class AddEventPresenter implements AddEventContract.Presenter {

    private Calendar calendar = Calendar.getInstance();
    private String currentDate;
    private String currentTime;
    private String nextTime;
    private String spinnerItemName;
    private Event event = new Event();
    private AddEventContract.View view;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Integer nextHour;

    AddEventPresenter() {
    }

    private static String convertFormat(Integer date) {
        String textDate = date.toString();
        if (date < 10) {
            textDate = "0" + textDate;
        } return textDate;
    }

    @Override
    public void setView(AddEventContract.View view) {
        this.view = view;
    }

    @Override
    public void setSpinnerItemName(String spinnerItemName) {
        this.spinnerItemName = spinnerItemName;
    }

    @Override
    public String getCurrentDate() {
        return currentDate;
    }

    @Override
    public String getCurrentTime() {
        return currentTime;
    }

    @Override
    public String getNextTime() {
        return nextTime;
    }

    @Override
    public void setCurrentDatetime() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        month = month + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        nextHour = hour + 1;

        currentDate = year.toString() + "/" + convertFormat(month) + "/" + convertFormat(day);
        currentTime = convertFormat(hour) + ":00";
        nextTime = convertFormat(nextHour) + ":00";

    }

    @Override
    public void setFromTime(int hour, int minute) {
        String newHour = convertFormat(hour);
        String newMinute = convertFormat(minute);
        view.updateFromTime(newHour, newMinute);
        event.setFromHour(hour);
        event.setFromMinute(minute);
    }

    @Override
    public void setToTime(int hour, int minute) {
        String newHour = convertFormat(hour);
        String newMinute = convertFormat(minute);
        view.updateToTime(newHour, newMinute);
        event.setToHour(hour);
        event.setToMinute(minute);
    }

    @Override
    public void setFromDate(int year, int month, int day) {
        month = month + 1;
        String newYear = convertFormat(year);
        String newMonth = convertFormat(month);
        String newDay = convertFormat(day);
        view.updateFromDate(newYear, newMonth, newDay);
        event.setFromYear(year);
        event.setFromMonth(month);
        event.setFromDay(day);
    }

    @Override
    public void setToDate(int year, int month, int day) {
        month = month + 1;
        String newYear = convertFormat(year);
        String newMonth = convertFormat(month);
        String newDay = convertFormat(day);
        view.updateToDate(newYear, newMonth, newDay);
        event.setToYear(year);
        event.setToMonth(month);
        event.setToDay(day);
    }

    @Override
    public Event getEvent() {
        event.setReason(view.getReason());
        event.setStockName(spinnerItemName);
        return event;
    }

    @Override
    public void setEventWithCurrentTime() {
        event.setFromYear(year);
        event.setFromMonth(month);
        event.setFromDay(day);
        event.setFromHour(hour);
        event.setFromMinute(0);

        event.setToYear(year);
        event.setToMonth(month);
        event.setToDay(day);
        event.setToHour(nextHour);
        event.setToMinute(0);
    }
}
