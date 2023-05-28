package rs.raf.rafdnevnjak.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.List;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.Day;
import rs.raf.rafdnevnjak.view.activities.MainActivity;
import rs.raf.rafdnevnjak.view.recycler.adapter.DayAdapter;
import rs.raf.rafdnevnjak.view.recycler.differ.DayDifferItemCallback;
import rs.raf.rafdnevnjak.view.viewpager.NonScrollableViewPager;
import rs.raf.rafdnevnjak.view.viewpager.PagerAdapter;
import rs.raf.rafdnevnjak.viewmodels.DayViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerCalendarViewModel;
import rs.raf.rafdnevnjak.viewmodels.RecyclerTodayEventViewModel;

public class CalendarFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView dayTextView;
    private TextView monthTextView;
    private RecyclerCalendarViewModel recyclerCalendarViewModel;
    private DayViewModel dayViewModel;
    private DayAdapter dayAdapter;
    private ConstraintLayout viewParent;
    private RecyclerTodayEventViewModel recyclerTodayEventViewModel;
    private BottomNavigationView bottomNavigationView;
    private boolean first = false;
    private boolean last = false;

    public CalendarFragment() {
        super(R.layout.fragment_calendar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerCalendarViewModel = new ViewModelProvider(requireActivity()).get(RecyclerCalendarViewModel.class);
        dayViewModel = new ViewModelProvider(requireActivity()).get(DayViewModel.class);
        init(view);
        initObservers();
        initRecycler();
    }

    private void init(View view) {
        initView(view);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerCalendar);
        dayTextView = view.findViewById(R.id.dayItem);
        monthTextView = view.findViewById(R.id.monthTV);
//        System.out.println("AAAJAJAJAJAJAJJA" + view.getParent().getParent());
        viewParent = (ConstraintLayout) view.getParent().getParent();
        bottomNavigationView = viewParent.findViewById(R.id.bottomNavigation);
    }

    private void initObservers() {
        recyclerCalendarViewModel.getDays().observe(this, days -> {
            dayAdapter.submitList(days);
            dayAdapter.notifyDataSetChanged();
        });
    }

    private void initRecycler() {
        // Ovde se nalazi pritisnut dan
        // Kada se pritisne na dan onda se u dayViewModel setuje taj pritisnut dan
        dayAdapter = new DayAdapter(new DayDifferItemCallback(), day -> {
            dayViewModel.setDay(day);
//            bottomNavigationView.setSelectedItemId(R.id.navigation_2);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainFragmentFcv, new DailyPlanFragment()).addToBackStack(null);
            transaction.commit();
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 7));
        recyclerView.setAdapter(dayAdapter);
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        LocalDate now = LocalDate.now();
        int dayInMonth = now.getDayOfMonth();
        // Namestam da recycler view pocne od trenutnog meseca
        // I da odmah u dayviewmodel setuje trenutan dan dan
        for(int i = 0; i < recyclerCalendarViewModel.getDays().getValue().size(); i++) {
            if(recyclerCalendarViewModel.getDays().getValue().get(i).getDate().equals(now)) {
                // Skrolujem na poziciju trenutna - dan u mesecu plus jedan da bi bio prvi
                layoutManager.scrollToPosition(i-dayInMonth+1);
                Day today = recyclerCalendarViewModel.getDays().getValue().get(i);
                dayViewModel.setDay(today);
            }
        }
        // On scroll adapter za recyclerview
        // Okine se i pri prvom ucitavanju aplikacije
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(firstVisiblePosition != -1) {
                    for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {
                        if (!first) {
                            if (recyclerCalendarViewModel.getDays().getValue().get(i).getDate().getDayOfMonth() == 1) {
                                last = false;
                                first = true;
                            }
                        } else {
                            if (recyclerCalendarViewModel.getDays().getValue().get(i).getDate().getDayOfMonth() == 1) {
                                last = true;
                                first = false;
                                // Uzimam dan iz liste koji je bio pre prvog da bih mogao da mu izvucem mesec
                                monthTextView.setText(recyclerCalendarViewModel.getDays().getValue().get(i - 1).getDate().getMonth().toString() + " "
                                        + recyclerCalendarViewModel.getDays().getValue().get(i - 1).getDate().getYear() + ".");
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dayAdapter.notifyDataSetChanged();
    }
}
