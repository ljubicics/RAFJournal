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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.database.DBHelper;
import rs.raf.rafdnevnjak.model.User;
import rs.raf.rafdnevnjak.view.activities.MainActivity;

public class LoginFragment extends Fragment {
    private EditText emailField;
    private EditText usernameField;
    private EditText passwordField;
    private Button loginBtn;
    Gson gson = new Gson();
    public static final String USER_LOGIN_PREF_KEY = "userKey";

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        loginBtn = view.findViewById(R.id.loginButton);
        emailField = ((EditText)view.findViewById(R.id.emailField));
        usernameField = ((EditText)view.findViewById(R.id.usernameField));
        passwordField = ((EditText)view.findViewById(R.id.passwordField));
        initListeners();
    }

    private void initListeners() {
        loginBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            if(email.length() == 0 || username.length() == 0 || password.length() == 0) {
//                System.out.println("USO");
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),"FIELD CANNOT BE EMPTY",Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                if(password.length() < 5) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"PASSWORD MUST BE LONGER THAN 5",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else {
                    boolean flagUpper = false;
                    boolean flagNumber = false;
                    boolean flagSpecialChar = false;
                    for(int i = 0; i < password.length(); i++) {
                        if(Character.isUpperCase(password.charAt(i))) {
                            flagUpper = true;
                        }
                        if(Character.isDigit(password.charAt(i))) {
                            flagNumber = true;
                        }
                        if(!Character.isDigit(password.charAt(i)) && !Character.isLetter(password.charAt(i))) {
                            flagSpecialChar = true;
                        }
                    }
                    if(!flagUpper) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"PASSWORD MUST CONTAIN UPPERCASE LETTER",Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    } else if(!flagNumber){
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"PASSWORD MUST CONTAIN AT LEAST ONE NUMBER",Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    } else if(flagSpecialChar) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"PASSWORD MUST NOT CONTAIN SPECIAL CHARACTERS",Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                }
            }
            DBHelper dbHelper = new DBHelper(getActivity());
            if(dbHelper.checkUsernamePassword(username, password, email)) {
                User u = new User(email, username, password);
                String userToString = gson.toJson(u);
                SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREF_KEY, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(USER_LOGIN_PREF_KEY, userToString).apply();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFragmentFcv, new MainFragment()).addToBackStack(null);
                transaction.commit();
            } else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),"INCORRECT PASSWORD",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}
