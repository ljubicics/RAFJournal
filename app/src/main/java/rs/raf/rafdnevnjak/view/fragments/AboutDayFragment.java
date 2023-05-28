package rs.raf.rafdnevnjak.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.model.DayEvent;
import rs.raf.rafdnevnjak.view.recycler.adapter.DayAdapter;
import rs.raf.rafdnevnjak.view.recycler.adapter.DayEventAdapter;
import rs.raf.rafdnevnjak.viewmodels.DayEventViewModel;
import rs.raf.rafdnevnjak.viewmodels.DayViewModel;
import rs.raf.rafdnevnjak.viewmodels.PositionViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerCalendarViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerDayEventViewModel;

public class AboutDayFragment extends Fragment {
    private TextView monthTV;
    private TextView timeTV;
    private TextView titleTV;
    private TextView descriptionTV;
    private Button delete;
    private Button edit;
    private DayViewModel dayViewModel;
    private DayEventViewModel dayEventViewModel;
    private RecyclerCalendarViewModel recyclerCalendarViewModel;
    private RecyclerDayEventViewModel recyclerDayEventViewModel;
    private DayEventAdapter dayEventAdapter;
    private PositionViewModel positionViewModel;
    private ConstraintLayout mainLayout;
    private boolean flag;
    private Day day;

    public AboutDayFragment(DayEventAdapter dayEventAdapter) {
        super(R.layout.fragment_about_day);
        this.dayEventAdapter = dayEventAdapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initView();
        initObservers();
    }

    private void init(View view) {
        monthTV = view.findViewById(R.id.eventDescriptionMonthTV);
        timeTV = view.findViewById(R.id.eventDescriptionTimeTV);
        titleTV = view.findViewById(R.id.eventDescriptionTitleTV);
        descriptionTV = view.findViewById(R.id.eventDescriptionTV);
        edit = view.findViewById(R.id.descriptionEditButton);
        delete = view.findViewById(R.id.descriptionDeleteButton);
        mainLayout = view.findViewById(R.id.constraintAbout);
        recyclerDayEventViewModel = new ViewModelProvider(requireActivity()).get(RecyclerDayEventViewModel.class);
        recyclerCalendarViewModel = new ViewModelProvider(requireActivity()).get(RecyclerCalendarViewModel.class);
        dayViewModel = new ViewModelProvider(requireActivity()).get(DayViewModel.class);
        dayEventViewModel = new ViewModelProvider(requireActivity()).get(DayEventViewModel.class);
        positionViewModel = new ViewModelProvider(requireActivity()).get(PositionViewModel.class);
    }

    private void initView() {
        day = dayViewModel.getDay().getValue();
        DayEvent dayEvent = dayEventViewModel.getDayEvent().getValue();
        monthTV.setText(day.getDate().toString());
        timeTV.setText(dayEvent.getStart().toString() + "-" + dayEvent.getEnd().toString());
        titleTV.setText(dayEvent.getTitle());
        descriptionTV.setText(dayEvent.getDescription());
    }

    private void initObservers() {
        delete.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
            day.getEvents().remove(dayEventViewModel.getDayEvent().getValue());
            dayViewModel.setDay(day);
            recyclerDayEventViewModel.setDayEvents(day.getEvents(), false);
            recyclerCalendarViewModel.updateCalendar(day);
            dayEventAdapter.notifyItemRemoved(positionViewModel.getPosition().getValue());
        });

        edit.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainFragmentFcv, new EditEventFragment()).addToBackStack(null);
            transaction.commit();
        });
    }

}
