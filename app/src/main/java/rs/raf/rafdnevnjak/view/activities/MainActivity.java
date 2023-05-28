package rs.raf.rafdnevnjak.view.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import rs.raf.rafdnevnjak.R;
import rs.raf.rafdnevnjak.database.DBHelper;
import rs.raf.rafdnevnjak.model.User;
import rs.raf.rafdnevnjak.view.fragments.LoginFragment;
import rs.raf.rafdnevnjak.view.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {
    Gson gson = new Gson();
    private DBHelper dbHelper;
    private User user;
    public static final String PREF_KEY = "key";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        if(!dbHelper.checkUsername("strahinja")) {
            dbHelper.insertData("strahinja", "Strahinja123", "strahinja.ljubicic@gmail.com");
        }

        splashScreen();
        setContentView(R.layout.activity_main_fragment);

    }

    public void splashScreen() {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> {
            try {
                // Pod paketom u kom se nalazimo cuvamo podatke koje cemo da citamo da li je korisnik ulogovan
                // Context mode private oznacava da cemo moci da pristupimo samo iz ove aplikacije
                SharedPreferences sharedPreferences = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
                // SharedPreferences cuva user string pod kljucem koji se nalazi u ovom activity-ju
                String userString = sharedPreferences.getString(LoginFragment.USER_LOGIN_PREF_KEY, null); //uzima se key iz login fragmenta
                if(userString != null) {
                    // Ukoliko user postoji zelimo da stavimo fragment koji ce sluziti za rad sa aplikacijom a usera da sacuvamo
                    user = gson.fromJson(userString, User.class);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainFragmentFcv, new MainFragment());
                    transaction.commit();
                } else {
                    // Ukoliko user ne postoji u preferences zelimo da stavimo login fragment na izvrsavanje
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainFragmentFcv, new LoginFragment());
                    transaction.commit();
                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStackImmediate();

        System.out.println(getSupportFragmentManager().getBackStackEntryCount());
    }
}
