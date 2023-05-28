package rs.raf.rafdnevnjak.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.model.User;
import rs.raf.rafdnevnjak.view.activities.MainActivity;

public class ProfileFragment extends Fragment {
    Gson gson = new Gson();
    private TextView studentEmailTV;
    private Button logoutBtn;
    private Button changePswrdBtn;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        studentEmailTV = view.findViewById(R.id.emailTV);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        changePswrdBtn = view.findViewById(R.id.changePswrdBtn);
        initView();
        initListeners();
    }

    private void initView() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_KEY, Context.MODE_PRIVATE);
        String userString = sharedPreferences.getString(LoginFragment.USER_LOGIN_PREF_KEY, null);
        User user = gson.fromJson(userString, User.class);
        studentEmailTV.setText(user.getEmail());
    }
    private void initListeners() {
        logoutBtn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_KEY, Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(LoginFragment.USER_LOGIN_PREF_KEY).commit();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainFragmentFcv, new LoginFragment());
            transaction.commit();
        });
        changePswrdBtn.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainFragmentFcv, new ChangePasswordFragment()).addToBackStack(null);
            transaction.commit();
        });
    }
}
