package com.example.lab.lab3;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Future;

public class PhoneRepository {
    private PhoneDao phoneDao;
    private LiveData<List<Phone>> phoneList;

    public PhoneRepository(Application application) {
        PhoneDatabase db = PhoneDatabase.getDatabase(application);
        phoneDao = db.phoneDao();
        phoneList = phoneDao.getAll();
    }

    public LiveData<List<Phone>> getAllPhones() {
        return this.phoneList;
    }

    public void deleteAllPhones() {
        PhoneDatabase.dbWriteExecutor.execute(() -> {
            this.phoneDao.deleteAll();
        });
    }

    public void insertPhone(Phone newPhone) {
        PhoneDatabase.dbWriteExecutor.execute(() -> {
            this.phoneDao.insert(newPhone);
        });
    }

    public void updatePhone(Phone phone) {
        PhoneDatabase.dbWriteExecutor.execute(() -> {
            this.phoneDao.updatePhone(phone);
        });
    }

    public void deletePhone(Phone phone) {
        PhoneDatabase.dbWriteExecutor.execute(() -> {
            this.phoneDao.deletePhone(phone);
        });
    }
}
