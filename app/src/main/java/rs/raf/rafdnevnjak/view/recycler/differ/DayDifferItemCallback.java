package rs.raf.rafdnevnjak.view.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import rs.raf.rafdnevnjak.model.Day;

public class DayDifferItemCallback extends DiffUtil.ItemCallback<Day> {
    @Override
    public boolean areItemsTheSame(@NonNull Day oldItem, @NonNull Day newItem) {
        return oldItem.getDate().compareTo(newItem.getDate()) == 0;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Day oldItem, @NonNull Day newItem) {
        return oldItem.getEvents().equals(newItem.getEvents())
                && oldItem.getDayPriority().equals(newItem.getDayPriority());
    }
}
