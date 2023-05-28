package rs.raf.rafdnevnjak.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "Login.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {
        myDB.execSQL("create TABLE Users(username TEXT primary key, password TEXT, email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int i, int i1) {
        myDB.execSQL("drop table if exists users");
    }

    public Boolean insertData(String username, String password, String email){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        long result = myDB.insert("users", null, contentValues);
        if (result == -1) return false;
        return true;
    }
    public Boolean checkUsername(String username){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * from users where username = ?", new String[] {username});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public Boolean checkUsernamePassword(String username, String password, String email){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * from users where username = ? and password = ? and email = ?", new String[] {username, password, email});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public void updatePasswordForUser(String username, String newPassword){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        myDB.update("users", contentValues, "username = '" + username +"'", null);
    }

}
