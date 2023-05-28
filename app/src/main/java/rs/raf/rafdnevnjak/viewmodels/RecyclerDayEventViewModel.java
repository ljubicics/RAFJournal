package rs.raf.rafdnevnjak.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.model.DayEvent;
import rs.raf.rafdnevnjak.model.Priority;

public class RecyclerDayEventViewModel extends ViewModel {

    private final MutableLiveData<List<DayEvent>> dayEvents = new MutableLiveData<>();

    private List<DayEvent> dayEventsList = new ArrayList<>();

    public RecyclerDayEventViewModel() {

    }

    public LiveData<List<DayEvent>> getDayEvents() {
        return dayEvents;
    }

    // Namesteno filtriranje liste po onome sto je prosledjeno
    public void filterEvents(String filter, boolean isChecked) {
        List<DayEvent> filteredList = dayEventsList.stream().filter(dayEvent -> dayEvent.getTitle().toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
        //setDayEvents(filteredList, isChecked);
        dayEvents.setValue(filteredList);
    }

    public void sortElementsByPriority(Priority priority) {
        // TODO: Implementirati sortiranje elemenata po prioritetu
        if(priority == Priority.LOW) {
            Collections.sort(dayEventsList, new Comparator<DayEvent>() {
                @Override
                public int compare(DayEvent dayEvent1, DayEvent dayEvent2) {
                    if (dayEvent1.getEventPriority() == dayEvent2.getEventPriority()) {
                        return dayEvent1.getStart().compareTo(dayEvent2.getStart());
                    } else {
                        return dayEvent1.getEventPriority().compareTo(dayEvent2.getEventPriority());
                    }
                }
            });
        } else if(priority == Priority.HIGH) {
            Collections.sort(dayEventsList, new Comparator<DayEvent>() {
                @Override
                public int compare(DayEvent dayEvent1, DayEvent dayEvent2) {
                    if (dayEvent1.getEventPriority() == dayEvent2.getEventPriority()) {
                        return dayEvent1.getStart().compareTo(dayEvent2.getStart());
                    } else {
                        return dayEvent2.getEventPriority().compareTo(dayEvent1.getEventPriority());
                    }
                }
            });
        }
        dayEvents.setValue(dayEventsList);
    }

    public void setDayEvents(List<DayEvent> dayEventsPassed, boolean isCheked) {
        // Setovanje elemenata u zavisnosti da li je checkbox cekiran
        System.out.println(dayEvents);
        if (!isCheked) {
            List<DayEvent> presentEvents = new ArrayList<>();
            for (DayEvent event : dayEventsPassed) {
                if (LocalTime.now().compareTo(event.getEnd()) < 0) {
                    presentEvents.add(event);
                }
            }
            this.dayEventsList = presentEvents;
            this.dayEvents.setValue(presentEvents);
        } else {
            dayEventsList = dayEventsPassed;
            this.dayEvents.setValue(dayEventsPassed);
        }
    }

}
