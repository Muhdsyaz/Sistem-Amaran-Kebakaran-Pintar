package ices.project.siakapmy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    public void insert(UserModel userModel);

    @Update
    public void update(UserModel users);

    @Delete
    public void delete(UserModel users);

    @Query("SELECT * FROM UserModel")
    LiveData<List<UserModel>> getAllUsers();

    @Query("DELETE FROM UserModel")
    void deleteAllUsers();
}

