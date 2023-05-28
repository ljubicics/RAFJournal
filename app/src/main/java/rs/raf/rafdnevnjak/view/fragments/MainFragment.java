package rs.raf.rafdnevnjak.view.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.view.viewpager.PagerAdapter;
import rs.raf.rafdnevnjak.viewmodels.DayViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerCalendarViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerDayEventViewModel;

public class MainFragment extends Fragment {
    private ViewPager viewPager;
    private DayViewModel dayViewModel;
    private RecyclerCalendarViewModel recyclerCalendarViewModel;
    private Day day;

    public MainFragment() {
        super(R.layout.bottom_navigation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayViewModel = new ViewModelProvider(requireActivity()).get(DayViewModel.class);
        recyclerCalendarViewModel = new ViewModelProvider(requireActivity()).get(RecyclerCalendarViewModel.class);
        init(view);
    }

    private void init(View view) {
        initViewPager(view);
        initNavigation(view);
    }

    private void initViewPager(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getActivity().getSupportFragmentManager()));
    }

    private void initNavigation(View view) {
        ((BottomNavigationView)view.findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            for(int i = 0; i <  recyclerCalendarViewModel.getDays().getValue().size(); i++) {
                if(recyclerCalendarViewModel.getDays().getValue().get(i).getDate().compareTo(LocalDate.now()) == 0) {
                    day = recyclerCalendarViewModel.getDays().getValue().get(i);
                }
            }
            dayViewModel.setDay(day);
            switch (item.getItemId()) {
                // setCurrentItem metoda viewPager samo obavesti koji je Item trenutno aktivan i onda metoda getItem u adapteru setuje odredjeni fragment za tu poziciju
                case R.id.navigation_1: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_1, false); break;
                case R.id.navigation_2: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_2, false); break;
                case R.id.navigation_3: viewPager.setCurrentItem(PagerAdapter.FRAGMENT_3, false); break;
            }
            return true;
        });
    }
}
