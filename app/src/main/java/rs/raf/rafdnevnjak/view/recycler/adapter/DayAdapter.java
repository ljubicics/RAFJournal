package rs.raf.rafdnevnjak.view.recycler.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.model.Priority;

public class DayAdapter extends ListAdapter<Day, DayAdapter.ViewHolder> {
    private final Consumer<Day> onDayClicked;

    public DayAdapter(@NonNull DiffUtil.ItemCallback<Day> diffCallback, Consumer<Day> onDayClicked) {
        super(diffCallback);
        this.onDayClicked = onDayClicked;

    }

    @NonNull
    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        // Ovde podesavam dimenziju jednog pravougaonika u kalendaru
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.height = parent.getMeasuredHeight() / 6;
        layoutParams.width = parent.getMeasuredWidth() / 7;
        itemView.setLayoutParams(layoutParams);

        return new ViewHolder(itemView, parent.getContext(), position -> {
            Day day = getItem(position);
            onDayClicked.accept(day);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.ViewHolder holder, int position) {
        Day day = getItem(position);
        holder.bind(day);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if (getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(Day day) {
            // Ovde treba da se spoje stvari iz jednog layout-a koji ce da opisuje kako izgleda jedan dan u kalendaru
            TextView textView = itemView.findViewById(R.id.dayItem);
            textView.setText(String.valueOf(day.getDate().getDayOfMonth()));
            Priority priority = day.getDayPriority();
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
            textView.setBackground(drawable);

        }
    }
}
