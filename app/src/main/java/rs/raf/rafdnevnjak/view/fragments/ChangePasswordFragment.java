package rs.raf.rafdnevnjak.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.database.DBHelper;
import rs.raf.rafdnevnjak.model.User;
import rs.raf.rafdnevnjak.view.activities.MainActivity;

public class ChangePasswordFragment extends Fragment {

    Gson gson = new Gson();
    private EditText newPswrdET;
    private EditText confirmPswrdET;
    private Button confirmChangesBtn;
    public ChangePasswordFragment() {
        super(R.layout.fragment_change_password);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        newPswrdET = view.findViewById(R.id.newPasswordET);
        confirmPswrdET = view.findViewById(R.id.confirmPasswordET);
        confirmChangesBtn = view.findViewById(R.id.confitmChangesBtn);
        initListeners();
    }

    private void initListeners() {
        confirmChangesBtn.setOnClickListener(v -> {
            if(!newPswrdET.getText().toString().equals(confirmPswrdET.getText().toString())) {
                Toast.makeText(this.getActivity().getApplicationContext(), "PASSWORDS MUST MATCH", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_KEY, Context.MODE_PRIVATE);
            String userString = sharedPreferences.getString(LoginFragment.USER_LOGIN_PREF_KEY, null);
            User user = gson.fromJson(userString, User.class);
            if(newPswrdET.getText().toString().equals(user.getPassword())) {
                Toast.makeText(this.getActivity().getApplicationContext(), "PASSWORDS MUST BE DIFFERENT THAN CURRENT", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setPassword(newPswrdET.getText().toString());
            DBHelper dbHelper = new DBHelper(getActivity());
            dbHelper.updatePasswordForUser(user.getUsername(), newPswrdET.getText().toString());
            sharedPreferences.edit().putString(LoginFragment.USER_LOGIN_PREF_KEY,gson.toJson(user)).apply();
            Toast.makeText(this.getActivity().getApplicationContext(), "PASSWORDS SUCCESSFULLY CHANGED", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });
    }
}
