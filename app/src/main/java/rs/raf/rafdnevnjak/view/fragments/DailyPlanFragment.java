package rs.raf.rafdnevnjak.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.model.DayEvent;
import rs.raf.rafdnevnjak.model.Priority;
import rs.raf.rafdnevnjak.view.recycler.adapter.DayAdapter;
import rs.raf.rafdnevnjak.view.recycler.adapter.DayEventAdapter;
import rs.raf.rafdnevnjak.view.recycler.differ.DayEventDifferItemCallback;
import rs.raf.rafdnevnjak.viewmodels.DayEventViewModel;
import rs.raf.rafdnevnjak.viewmodels.DayViewModel;
import rs.raf.rafdnevnjak.viewmodels.PositionViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerCalendarViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerDayEventViewModel;

public class DailyPlanFragment extends Fragment {

    private RecyclerDayEventViewModel recyclerDayEventViewModel;
    private RecyclerCalendarViewModel recyclerCalendarViewModel;
    private DayViewModel dayViewModel;
    private DayEventAdapter dayEventAdapter;
    private DayEventViewModel dayEventViewModel;
    private RecyclerView recyclerView;
    private ConstraintLayout mainLayout;
    private TextView dateTV;
    private TextView lowTV;
    private TextView midTV;
    private TextView highTV;
    private CheckBox checkBox;
    private EditText searchET;
    private ImageButton deleteButton;
    private FloatingActionButton addButton;
    private Day day;
    private PositionViewModel positionViewModel;
    private boolean flag = false;
    private DayAdapter dayAdapter;

    public DailyPlanFragment() {
        super(R.layout.fragment_daily_plan);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerDailyPlan);
        recyclerDayEventViewModel = new ViewModelProvider(requireActivity()).get(RecyclerDayEventViewModel.class);
        recyclerCalendarViewModel = new ViewModelProvider(requireActivity()).get(RecyclerCalendarViewModel.class);
        dayViewModel = new ViewModelProvider(requireActivity()).get(DayViewModel.class);
        dayEventViewModel = new ViewModelProvider(requireActivity()).get(DayEventViewModel.class);
        positionViewModel = new ViewModelProvider(requireActivity()).get(PositionViewModel.class);
        day = dayViewModel.getDay().getValue();
        init(view);
        initObservers();
        initRecycler();
    }

    private void init(View view) {
        initView(view);
    }

    private void initView(View view) {
        checkBox = view.findViewById(R.id.showPastCheckBox);
        searchET = view.findViewById(R.id.eventSearchET);
        lowTV = view.findViewById(R.id.lowTV);
        midTV = view.findViewById(R.id.midTV);
        highTV = view.findViewById(R.id.highTV);
        dateTV = view.findViewById(R.id.monthDailyPlanTV);
        mainLayout = view.findViewById(R.id.fragment_daily_plan);
        deleteButton = view.findViewById(R.id.deleteImageButton);
        addButton = view.findViewById(R.id.floatingActionButton);
        dateTV.setText(day.getDate().toString());
    }

    private void initObservers() {
        checkBox.setChecked(false);
        recyclerDayEventViewModel.setDayEvents(day.getEvents(), checkBox.isChecked());
        recyclerDayEventViewModel.getDayEvents().observe(this, dayEvents -> {
            dayEventAdapter.submitList(dayEvents);
            dayEventAdapter.notifyDataSetChanged();
        });
        // Menjanje prikaza obaveza na odnosu stanja checkboxa
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                recyclerDayEventViewModel.setDayEvents(day.getEvents(), isChecked);
            }
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                recyclerDayEventViewModel.filterEvents(editable.toString(), checkBox.isChecked());
            }
        });
        lowTV.setOnClickListener(v -> {
            recyclerDayEventViewModel.sortElementsByPriority(Priority.LOW);
        });
        highTV.setOnClickListener(v -> {
            recyclerDayEventViewModel.sortElementsByPriority(Priority.HIGH);
        });
        addButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainFragmentFcv, new AddNewEventFragment(dayEventAdapter)).addToBackStack(null);
            transaction.commit();
        });
    }

    private void initRecycler() {
        // DELETE CLICKED UZIMA U KOJOJ POZICIJI JE DELETE KLIKNUT I ONDA TAJ KOJI JE IZABRAN MOZE DA SE OBRISE
        dayEventAdapter = new DayEventAdapter(new DayEventDifferItemCallback(), dayEvent -> {
            dayEventViewModel.setDayEvent(dayEvent);
            for(int i = 0; i < day.getEvents().size(); i++) {
                if(day.getEvents().get(i) == dayEvent) {
                    positionViewModel.setPosition(i);
                    break;
                }
            }
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainFragmentFcv, new AboutDayFragment(dayEventAdapter)).addToBackStack(null);
            transaction.commit();
        }, deleteClicked -> {
//            day.getEvents().remove(deleteClicked);
//            dayViewModel.setDay(day);
//            recyclerDayEventViewModel.setDayEvents(day.getEvents(), checkBox.isChecked());
//            recyclerCalendarViewModel.updateCalendar(day);
            showDeleteSnackBar(deleteClicked);
        }, dayEventPosition -> {
//            dayEventAdapter.notifyItemRemoved(dayEventPosition);
            notifyItems(dayEventPosition);
        }, editClicked -> {
            dayEventViewModel.setDayEvent(editClicked);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainFragmentFcv, new EditEventFragment()).addToBackStack(null);
            transaction.commit();
        }, editDayEventPosition -> {

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(dayEventAdapter);
    }

    private void showDeleteSnackBar(DayEvent dayEvent) {
        Snackbar.make(mainLayout, "Are you sure you want to delete", Snackbar.LENGTH_SHORT)
                .setAction("Delete", view -> deleteEvent(dayEvent))
                .show();
    }

    private void deleteEvent(DayEvent dayEvent) {
        day.getEvents().remove(dayEvent);
        dayViewModel.setDay(day);
        recyclerDayEventViewModel.setDayEvents(day.getEvents(), checkBox.isChecked());
        recyclerCalendarViewModel.updateCalendar(day);
        flag = true;
    }

    private void notifyItems(int position) {
        if(flag) {
            dayEventAdapter.notifyItemRemoved(position);
            flag = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dayEventAdapter.notifyDataSetChanged();
    }
}
