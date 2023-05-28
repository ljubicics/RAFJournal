package rs.raf.rafdnevnjak.view.recycler.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.function.Consumer;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.DayEvent;
import rs.raf.rafdnevnjak.model.Priority;

public class DayEventAdapter extends ListAdapter<DayEvent, DayEventAdapter.ViewHolder> {

    private final Consumer<DayEvent> onDayEventClicked;
    private final Consumer<DayEvent> deleteDayEventClicked;
    private final Consumer<Integer> dayEventPosition;
    private final Consumer<DayEvent> editDayEventClicked;
    private final Consumer<Integer> editDayEventPosition;


    public DayEventAdapter(@NonNull DiffUtil.ItemCallback<DayEvent> diffCallback, Consumer<DayEvent> onDayEventClicked,
                           Consumer<DayEvent> deleteDayEventClicked, Consumer<Integer> dayEventPosition,
                           Consumer<DayEvent> editDayEventClicked, Consumer<Integer> editDayEventPosition) {
        super(diffCallback);
        this.onDayEventClicked = onDayEventClicked;
        this.deleteDayEventClicked = deleteDayEventClicked;
        this.dayEventPosition = dayEventPosition;
        this.editDayEventClicked = editDayEventClicked;
        this.editDayEventPosition = editDayEventPosition;
    }

    @NonNull
    @Override
    public DayEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_event_item, parent, false);
        return new ViewHolder(itemView, parent.getContext(), position -> {
            DayEvent dayEvent = getItem(position);
            onDayEventClicked.accept(dayEvent);
        }, deleteClicked -> {
            DayEvent dayEvent = getItem(deleteClicked);
            deleteDayEventClicked.accept(dayEvent);
            dayEventPosition.accept(deleteClicked);
        }, editClicked -> {
            DayEvent dayEvent = getItem(editClicked);
            editDayEventClicked.accept(dayEvent);
            editDayEventPosition.accept(editClicked);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull DayEventAdapter.ViewHolder holder, int position) {
        if(position >= 0 && position < getItemCount()) {
            DayEvent dayEvent = getItem(position);
            holder.bind(dayEvent);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked, Consumer<Integer> onDeleteClicked,
                          Consumer<Integer> onEditClicked) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
            ImageButton delete = itemView.findViewById(R.id.deleteImageButton);
            delete.setOnClickListener(v -> {
                onDeleteClicked.accept(getBindingAdapterPosition());
            });
            ImageButton edit = itemView.findViewById(R.id.editImageButton);
            edit.setOnClickListener(v -> {
                onEditClicked.accept(getBindingAdapterPosition());
            });
        }

        public void bind(DayEvent dayEvent) {
            ImageView imageView = itemView.findViewById(R.id.dayEventPicture);
            TextView title = itemView.findViewById(R.id.eventNameTV);
            TextView time = itemView.findViewById(R.id.eventTimeTV);
            ImageButton edit = itemView.findViewById(R.id.editImageButton);
            ImageButton delete = itemView.findViewById(R.id.deleteImageButton);
            ConstraintLayout constraintLayout = itemView.findViewById(R.id.day_single_event_item);
            title.setText(dayEvent.getTitle());
            time.setText(dayEvent.getStart().toString() + "-" + dayEvent.getEnd().toString());
            if(LocalTime.now().compareTo(dayEvent.getEnd()) < 0) {
                constraintLayout.setBackground(context.getDrawable(R.drawable.day_event_present));
                edit.setBackgroundColor(context.getColor(R.color.backgroud_light_blue));
                delete.setBackgroundColor(context.getColor(R.color.backgroud_light_blue));
            } else {
                constraintLayout.setBackground(context.getDrawable(R.drawable.day_event_past));
                edit.setBackgroundColor(context.getColor(R.color.day_event_past));
                delete.setBackgroundColor(context.getColor(R.color.day_event_past));
            }
            Priority priority = dayEvent.getEventPriority();
            Drawable drawable = null;
            switch (priority) {
                case NO:
                    drawable = context.getDrawable(R.drawable.day_box_plain);
                    break;
                case LOW:
                    drawable = context.getDrawable(R.drawable.day_box_low);
                    break;
                case MID:
                    drawable = context.getDrawable(R.drawable.day_box_mid);
                    break;
                case HIGH:
                    drawable = context.getDrawable(R.drawable.day_box_high);
                    break;
            }
            imageView.setBackground(drawable);
        }
    }
}
