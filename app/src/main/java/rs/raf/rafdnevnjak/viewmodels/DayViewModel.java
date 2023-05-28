package rs.raf.rafdnevnjak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import rs.raf.rafdnevnjak.model.Day;

public class DayViewModel extends ViewModel {
    private final MutableLiveData<Day> day = new MutableLiveData<>();

    public LiveData<Day> getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day.setValue(day);
    }

}
