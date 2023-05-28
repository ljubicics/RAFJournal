package rs.raf.rafdnevnjak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.model.DayEvent;
import rs.raf.rafdnevnjak.model.Priority;

public class RecyclerCalendarViewModel extends ViewModel {
    public static int counter = 10000;
    private final MutableLiveData<List<Day>> days = new MutableLiveData<>();
    private ArrayList<Day> dayArrayList = new ArrayList<>();

    public RecyclerCalendarViewModel() {
        LocalDate localDate = LocalDate.of(2022,8, 1);
        for(int i = 0; i < 10000; i++) {
            List<DayEvent> events = new ArrayList<>();
            events.add(new DayEvent(LocalTime.of(11,45), LocalTime.of(12,00), "Obaveza", "Opis", Priority.HIGH));
            events.add(new DayEvent(LocalTime.of(12,45), LocalTime.of(18,00), "Obaveza1", "Opis", Priority.LOW));
            Day day = new Day(localDate.plusDays(i), events);
//            day.setEvents(events);
            dayArrayList.add(day);
        }
        ArrayList<Day> listToSubmit = new ArrayList<>(dayArrayList);
        days.setValue(listToSubmit);
    }

    public LiveData<List<Day>> getDays() {
        return days;
    }

    public void updateCalendar(Day day) {
        for(Day d : dayArrayList) {
            if(d.getDate().compareTo(day.getDate()) == 0) {
                d.setEvents(day.getEvents());
            }
        }
        days.setValue(dayArrayList);
    }

    public void setDays(List<Day> dayss) {
        this.days.setValue(dayss);
    }
}
