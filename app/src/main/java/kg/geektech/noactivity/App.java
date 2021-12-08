package kg.geektech.noactivity;

import android.app.Application;

import androidx.room.Room;

import kg.geektech.noactivity.room.AppDatabase;

public class App extends Application {
    private AppDatabase database;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries().build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
