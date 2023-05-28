package rs.raf.rafdnevnjak.model;

import java.time.LocalTime;

public class DayEvent {
    private LocalTime start;
    private LocalTime end;
    private String title;
    private String description;
    private Priority eventPriority;

    public DayEvent(LocalTime start, LocalTime end, String title, String description, Priority eventPriority) {
        this.start = start;
        this.end = end;
        this.title = title;
        this.description = description;
        this.eventPriority = eventPriority;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getEventPriority() {
        return  eventPriority;
    }

    public void setEventPriority(Priority eventPriority) {
        this.eventPriority = eventPriority;
    }
}
