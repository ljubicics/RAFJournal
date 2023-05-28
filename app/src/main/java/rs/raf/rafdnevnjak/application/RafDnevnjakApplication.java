package rs.raf.rafdnevnjak.application;

import android.app.Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import rs.raf.rafdnevnjak.database.DBHelper;
import timber.log.Timber;

public class RafDnevnjakApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
