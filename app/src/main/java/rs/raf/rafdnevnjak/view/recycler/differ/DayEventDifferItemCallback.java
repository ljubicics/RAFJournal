package rs.raf.rafdnevnjak.view.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.model.DayEvent;

public class DayEventDifferItemCallback extends DiffUtil.ItemCallback<DayEvent> {
    @Override
    public boolean areItemsTheSame(@NonNull DayEvent oldItem, @NonNull DayEvent newItem) {
        return oldItem.getTitle().equals(newItem.getTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull DayEvent oldItem, @NonNull DayEvent newItem) {
        return oldItem.getEventPriority().equals(newItem.getEventPriority())
                && oldItem.getDescription().equals(newItem.getDescription());
    }
}
