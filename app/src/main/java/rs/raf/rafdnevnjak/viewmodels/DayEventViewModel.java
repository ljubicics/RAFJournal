package rs.raf.rafdnevnjak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import rs.raf.rafdnevnjak.model.DayEvent;

public class DayEventViewModel extends ViewModel {
    private final MutableLiveData<DayEvent> dayEventMutable = new MutableLiveData<>();

    public void setDayEvent(DayEvent dayEvent) {
        dayEventMutable.setValue(dayEvent);
    }

    public LiveData<DayEvent> getDayEvent() {
        return dayEventMutable;
    }
}
