package rs.raf.rafdnevnjak.view.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalTime;
import java.util.List;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.model.DayEvent;
import rs.raf.rafdnevnjak.model.Priority;
import rs.raf.rafdnevnjak.view.recycler.adapter.DayAdapter;
import rs.raf.rafdnevnjak.view.recycler.adapter.DayEventAdapter;
import rs.raf.rafdnevnjak.viewmodels.DayEventViewModel;
import rs.raf.rafdnevnjak.viewmodels.DayViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerCalendarViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerDayEventViewModel;

public class AddNewEventFragment extends Fragment {
    private EditText title;
    private EditText timeStart;
    private EditText timeEnd;
    private EditText description;
    private Button add;
    private Button cancel;
    private LocalTime start;
    private LocalTime end;
    private TextView monthTV;
    private TextView lowTV;
    private TextView midTV;
    private TextView highTV;
    private TimePickerDialog timePickerDialog;
    private DayEventAdapter dayEventAdapter;
    private DayEventViewModel dayEventViewModel;
    private RecyclerDayEventViewModel recyclerDayEventViewModel;
    private RecyclerCalendarViewModel recyclerCalendarViewModel;
    private DayViewModel dayViewModel;
    private Day day;
    private Priority clickedTV = Priority.MID;

    public AddNewEventFragment(DayEventAdapter dayEventAdapter) {
        super(R.layout.fragment_add_new_event);
        this.dayEventAdapter = dayEventAdapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initView();
        initObservers();
    }

    public void init(View view) {
        title = view.findViewById(R.id.addTitleET);
        timeStart = view.findViewById(R.id.addStartTimeET);
        timeEnd = view.findViewById(R.id.addEndTimeET);
        description = view.findViewById(R.id.addDescriptionET);
        add = view.findViewById(R.id.addCreateButton);
        cancel = view.findViewById(R.id.addCancelButton);
        monthTV = view.findViewById(R.id.addMonthTV);
        lowTV = view.findViewById(R.id.lowAddTV);
        midTV = view.findViewById(R.id.midAddTV);
        highTV = view.findViewById(R.id.highAddTV);
        dayViewModel = new ViewModelProvider(requireActivity()).get(DayViewModel.class);
        dayEventViewModel = new ViewModelProvider(requireActivity()).get(DayEventViewModel.class);
        recyclerDayEventViewModel = new ViewModelProvider(requireActivity()).get(RecyclerDayEventViewModel.class);
        recyclerCalendarViewModel = new ViewModelProvider(requireActivity()).get(RecyclerCalendarViewModel.class);
        day = dayViewModel.getDay().getValue();
    }

    private void initView() {
        monthTV.setText(day.getDate().toString());
        lowTV.setBackground(getContext().getDrawable(R.drawable.box_low_light));
        midTV.setBackground(getContext().getDrawable(R.drawable.box_mid_light));
        highTV.setBackground(getContext().getDrawable(R.drawable.box_high_light));
    }
    private void initObservers() {
        cancel.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        add.setOnClickListener(v -> {
            String naziv = title.getText().toString();
            String opis = description.getText().toString();
            if(naziv != null && opis != null && start != null && end != null) {
                List<Day> list = recyclerCalendarViewModel.getDays().getValue();
                List<DayEvent> events = null;
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getDate().equals(day.getDate())) {
                        events = day.getEvents();
                    }
                }
                for(int i = 0; i < events.size(); i++) {
                    if(events.get(i).getEnd().compareTo(start) > 0 || end.compareTo(events.get(i).getStart()) > 0) {
                        Toast.makeText(getActivity().getApplicationContext(), "You cannot add event that is during another event", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                DayEvent dayEvent = new DayEvent(start, end, naziv, opis, clickedTV);
                day.getEvents().add(dayEvent);
                dayEventAdapter.notifyDataSetChanged();
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getDate().equals(day.getDate())) {
                        list.set(i, day);
                    }
                }
                // Vrsim update kalendara
                recyclerCalendarViewModel.setDays(list);
                recyclerCalendarViewModel.updateCalendar(day);
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            } else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),"FIELDS CANNOT BE EMPTY",Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            LocalTime now = LocalTime.now();
            int currentHour = now.getHour();
            int currentMinute = now.getMinute();
            @Override
            public void onClick(View view) {
//                System.out.println("ADKFBALKF DAKFB ADKFJBADF");

                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minutes) {
                        start = LocalTime.of(hourOfTheDay, minutes);
                        timeStart.setText(start.toString());
                    }
                },currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            LocalTime now = LocalTime.now();
            int currentHour = now.getHour();
            int currentMinute = now.getMinute();
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minutes) {
                        end = LocalTime.of(hourOfTheDay, minutes);
                        timeEnd.setText(end.toString());
                    }
                },currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
        lowTV.setOnClickListener(v -> {
            clickedTV = Priority.LOW;
            lowTV.setBackground(getContext().getDrawable(R.drawable.day_box_low));
            midTV.setBackground(getContext().getDrawable(R.drawable.box_mid_light));
            highTV.setBackground(getContext().getDrawable(R.drawable.box_high_light));
        });

        midTV.setOnClickListener(v -> {
            clickedTV = Priority.MID;
            lowTV.setBackground(getContext().getDrawable(R.drawable.box_low_light));
            midTV.setBackground(getContext().getDrawable(R.drawable.day_box_mid));
            highTV.setBackground(getContext().getDrawable(R.drawable.box_high_light));
        });

        highTV.setOnClickListener(v -> {
            clickedTV = Priority.HIGH;
            lowTV.setBackground(getContext().getDrawable(R.drawable.box_low_light));
            midTV.setBackground(getContext().getDrawable(R.drawable.box_mid_light));
            highTV.setBackground(getContext().getDrawable(R.drawable.day_box_high));
        });
    }
}
