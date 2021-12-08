package kg.geektech.noactivity.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kg.geektech.noactivity.models.FirstModel;

@Database(entities = {FirstModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FirstModelDao firstModelDao();
}
