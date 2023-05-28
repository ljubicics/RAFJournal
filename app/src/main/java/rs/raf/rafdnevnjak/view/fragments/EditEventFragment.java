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
import rs.raf.rafdnevnjak.viewmodels.DayEventViewModel;
import rs.raf.rafdnevnjak.viewmodels.DayViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerCalendarViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerDayEventViewModel;

public class EditEventFragment extends Fragment {
    private RecyclerCalendarViewModel recyclerCalendarViewModel;
    private RecyclerDayEventViewModel recyclerDayEventViewModel;
    private DayEventViewModel dayEventViewModel;
    private DayViewModel dayViewModel;
    private TextView monthTV;
    private TextView lowTV;
    private TextView midTV;
    private TextView highTV;
    private EditText title;
    private EditText timeStart;
    private EditText timeEnd;
    private EditText description;
    private Button saveButton;
    private Button cancelButton;
    private LocalTime start;
    private LocalTime end;
    private Priority clickedTV;
    private Day day;
    private DayEvent dayEvent;
    public EditEventFragment() {
        super(R.layout.fragment_edit_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initView();
        initObservers();
    }

    private void init(View view) {
        monthTV = view.findViewById(R.id.editMonthTV);
        lowTV = view.findViewById(R.id.lowEditTV);
        midTV = view.findViewById(R.id.midEditTV);
        highTV = view.findViewById(R.id.highEditTV);
        title = view.findViewById(R.id.editTitleET);
        timeStart = view.findViewById(R.id.editStartTimeET);
        timeEnd = view.findViewById(R.id.editEndTimeET);
        description = view.findViewById(R.id.editDescriptionET);
        cancelButton = view.findViewById(R.id.editCancelButton);
        saveButton = view.findViewById(R.id.editSaveButton);
        recyclerCalendarViewModel = new ViewModelProvider(requireActivity()).get(RecyclerCalendarViewModel.class);
        recyclerDayEventViewModel = new ViewModelProvider(requireActivity()).get(RecyclerDayEventViewModel.class);
        dayEventViewModel = new ViewModelProvider(requireActivity()).get(DayEventViewModel.class);
        dayViewModel = new ViewModelProvider(requireActivity()).get(DayViewModel.class);
        this.day = dayViewModel.getDay().getValue();
        this.dayEvent = dayEventViewModel.getDayEvent().getValue();
    }

    private void initView() {
        monthTV.setText(day.getDate().toString());
        switch(dayEvent.getEventPriority()) {
            case LOW:
                lowTV.setBackground(getContext().getDrawable(R.drawable.day_box_low));
                midTV.setBackground(getContext().getDrawable(R.drawable.box_mid_light));
                highTV.setBackground(getContext().getDrawable(R.drawable.box_high_light));
                break;
            case MID:
                lowTV.setBackground(getContext().getDrawable(R.drawable.box_low_light));
                midTV.setBackground(getContext().getDrawable(R.drawable.day_box_mid));
                highTV.setBackground(getContext().getDrawable(R.drawable.box_high_light));
                break;
            case HIGH:
                lowTV.setBackground(getContext().getDrawable(R.drawable.box_low_light));
                midTV.setBackground(getContext().getDrawable(R.drawable.box_mid_light));
                highTV.setBackground(getContext().getDrawable(R.drawable.day_box_high));
                break;
        }
        title.setText(dayEvent.getTitle());
        start = dayEvent.getStart();
        end = dayEvent.getEnd();
        timeStart.setText(dayEvent.getStart().toString());
        timeEnd.setText(dayEvent.getEnd().toString());
        description.setText(dayEvent.getDescription());
    }

    private void initObservers() {
        cancelButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        saveButton.setOnClickListener(v -> {
            String naziv = title.getText().toString();
            String opis = description.getText().toString();
            if(naziv != null && opis != null && start != null && end != null) {
                // Ovo radim da bih proverio da se vreme ne poklapa slucajno
                List<Day> list = recyclerCalendarViewModel.getDays().getValue();
                List<DayEvent> events = null;
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getDate().equals(day.getDate())) {
                        events = day.getEvents();
                    }
                }
                if(dayEvent.getStart().compareTo(start) != 0 || dayEvent.getEnd().compareTo(end) != 0) {
                    for (int i = 0; i < events.size(); i++) {
                        if(!events.get(i).equals(dayEvent)) {
                            if (events.get(i).getEnd().compareTo(start) > 0 || end.compareTo(events.get(i).getStart()) > 0) {
                                Toast.makeText(getActivity().getApplicationContext(), "You cannot add event that is during another event", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }
                if(clickedTV == null) {
                    clickedTV = dayEvent.getEventPriority();
                }
                    DayEvent dayEvent1 = new DayEvent(start, end, naziv, opis, clickedTV);
                Day day1 = day;
                for(int i = 0; i < events.size(); i++) {
                    if(events.get(i).equals(dayEvent)) {
                        day1.getEvents().set(i, dayEvent1);
                    }
                }
                dayViewModel.setDay(day1);
                recyclerCalendarViewModel.setDays(list);
                recyclerDayEventViewModel.setDayEvents(day1.getEvents(), false);
                recyclerCalendarViewModel.updateCalendar(day1);
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "You can't leave any field empty", Toast.LENGTH_SHORT).show();
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

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("ADKFBALKF DAKFB ADKFJBADF");

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minutes) {
                        start = LocalTime.of(hourOfTheDay, minutes);
                        timeStart.setText(start.toString());
                    }
                }, dayEvent.getStart().getHour(), dayEvent.getStart().getMinute(), true);
                timePickerDialog.show();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minutes) {
                        end = LocalTime.of(hourOfTheDay, minutes);
                        timeEnd.setText(end.toString());
                    }
                }, dayEvent.getEnd().getHour(), dayEvent.getEnd().getMinute(), true);
                timePickerDialog.show();
            }
        });
        
    }
}
