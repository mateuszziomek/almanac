package org.radiokit.almanac.event;

import org.radiokit.almanac.model.Event;
import org.radiokit.almanac.mvp.BasePresenter;
import org.radiokit.almanac.mvp.BaseView;

/**
 * Created by mateuszziomek on 30.09.16.
 */

public interface AddEventContract {

    interface View extends BaseView {
        void showFromTimePickerDialog(android.view.View view);
        void showToTimePickerDialog(android.view.View view);
        void showFromDatePickerDialog(android.view.View view);
        void showToDatePickerDialog(android.view.View view);
        void updateFromTime(String hour, String minute);
        void updateFromDate(String year, String month, String day);
        void updateToTime(String hour, String minute);
        void updateToDate(String year, String month, String day);
        String getReason();
        void saveEvent();
    }

    interface Presenter extends BasePresenter {
        void setView(AddEventContract.View view);
        void setFromTime(int hour, int minute);
        void setToTime(int hour, int minute);
        void setFromDate(int year, int month, int day);
        void setToDate(int year, int month, int day);
        void setCurrentDatetime();
        String getCurrentDate();
        String getCurrentTime();
        String getNextTime();
        void setSpinnerItemName(String spinnerItemName);
        Event getEvent();
        void setEventWithCurrentTime();
    }
}
