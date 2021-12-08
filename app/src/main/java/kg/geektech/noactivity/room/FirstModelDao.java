package kg.geektech.noactivity.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kg.geektech.noactivity.models.FirstModel;

@Dao
public interface FirstModelDao {

    @Query("SELECT * FROM firstmodel order by id DESC")
    List<FirstModel> getAll();

    @Query("Select * from firstmodel order by name")
    List<FirstModel> getAllSortedByTitle();

    @Insert
    void insert(FirstModel firstModel);

    @Delete
    void delete(FirstModel firstModel);

    @Update
    void update(FirstModel firstModel);
}
