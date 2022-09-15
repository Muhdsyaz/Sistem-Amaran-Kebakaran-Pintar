package ices.project.siakapmy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<UserModel>> allUsers;

    UserRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
    }

    // creating a method to insert the data to our database.
    public void insert(UserModel model) {
        new InsertUserAsyncTask(userDao).execute(model);
    }

    // creating a method to update data in database.
    public void update(UserModel model) {
        new UpdateUsersAsyncTask(userDao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(UserModel model) {
        new DeleteUsersAsyncTask(userDao).execute(model);
    }

    // below is the method to delete all the users.
    public void deleteAllUsers() {
        new DeleteAllUsersAsyncTask(userDao).execute();
    }

    // below method is to read all the users.
    public LiveData<List<UserModel>> getAllCourses() {
        return allUsers;
    }

    // we are creating a async task method to insert new user.
    private static class InsertUserAsyncTask extends AsyncTask<UserModel, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserModel... model) {
            // below line is use to insert our modal in dao.
            userDao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our user.
    private static class UpdateUsersAsyncTask extends AsyncTask<UserModel, Void, Void> {
        private UserDao userDao;

        private UpdateUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserModel... models) {
            // below line is use to update
            // our modal in dao.
            userDao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete user.
    private static class DeleteUsersAsyncTask extends AsyncTask<UserModel, Void, Void> {
        private UserDao userDao;

        private DeleteUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserModel... models) {
            // below line is use to delete
            // our user model in dao.
            userDao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all users.
    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;
        private DeleteAllUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all users.
            userDao.deleteAllUsers();
            return null;
        }
    }
}
