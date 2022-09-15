package ices.project.siakapmy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    // creating a new variable for course repository.
    private UserRepository repository;

    // below line is to create a variable for live
    // data where all the courses are present.
    private LiveData<List<UserModel>> allUsers;

    // constructor for our view modal.
    public AppViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllCourses();
    }

    // below method is use to insert the data to our repository.
    public void insert(UserModel model) {
        repository.insert(model);
    }

    // below line is to update data in our repository.
    public void update(UserModel model) {
        repository.update(model);
    }

    // below line is to delete the data in our repository.
    public void delete(UserModel model) {
        repository.delete(model);
    }

    // below method is to delete all the courses in our list.
    public void deleteAllUsers() {
        repository.deleteAllUsers();
    }

    // below method is to get all the courses in our list.
    public LiveData<List<UserModel>> getAllUsers() {
        return allUsers;
    }

}
