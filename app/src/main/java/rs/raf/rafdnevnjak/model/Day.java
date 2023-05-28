package rs.raf.rafdnevnjak.model;

import java.time.LocalDate;
import java.util.List;

public class Day {
    private Priority dayPriority;
    private LocalDate date;
    private List<DayEvent> events;

    public Day(LocalDate date, List<DayEvent> events) {
        this.date = date;
        this.events = events;
        updatePriority();
    }

    public Priority getDayPriority() {
        return dayPriority;
    }

    public void setDayPriority(Priority dayPriority) {
        this.dayPriority = dayPriority;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<DayEvent> getEvents() {
        return events;
    }

    public void setEvents(List<DayEvent> events) {
        this.events = events;
        updatePriority();
    }

    public void addEvent(DayEvent dayEvent) {
        events.add(dayEvent);
        updatePriority();
    }
    public void updatePriority() {
        int maxPriority = 0;
        int currPriority = 0;
        if(events != null) {
            for (DayEvent dayEvent : events) {
                switch (dayEvent.getEventPriority()) {
                    case NO:
                        currPriority = 0;
                        break;
                    case LOW:
                        currPriority = 1;
                        break;
                    case MID:
                        currPriority = 2;
                        break;
                    case HIGH:
                        currPriority = 3;
                        break;
                }
                if (currPriority > maxPriority) {
                    maxPriority = currPriority;
                }
            }
        } else {
            maxPriority = 0;
        }
        switch (maxPriority) {
            case 0:
                this.dayPriority = Priority.NO;
                break;
            case 1:
                this.dayPriority = Priority.LOW;
                break;
            case 2:
                this.dayPriority = Priority.MID;
                break;
            case 3:
                this.dayPriority = Priority.HIGH;
                break;
        }
    }
}
