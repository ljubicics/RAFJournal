package rs.raf.rafdnevnjak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import rs.raf.rafdnevnjak.model.DayEvent;

public class RecyclerTodayEventViewModel extends ViewModel {
    private MutableLiveData<List<DayEvent>> events = new MutableLiveData<>();
    private List<DayEvent> eventList = new ArrayList<>();

    public LiveData<List<DayEvent>> getEvents() {
        return events;
    }

    public void setEvents(List<DayEvent> dayEventsList) {
        this.events.setValue(dayEventsList);
    }
}
