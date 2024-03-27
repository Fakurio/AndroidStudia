package com.example.lab.lab3;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Future;

public class PhoneViewModel extends AndroidViewModel {
    private PhoneRepository phoneRepository;
    private LiveData<List<Phone>> phoneList;

    public PhoneViewModel(@NonNull Application application) {
       super(application);
       phoneRepository = new PhoneRepository(application);
       phoneList = phoneRepository.getAllPhones();
    }

    public LiveData<List<Phone>> getAllPhones() {
        return this.phoneList;
    }

    public void deleteAllPhones() {
        this.phoneRepository.deleteAllPhones();
    }

    public void insertNewPhone(Phone newPhone) {
        this.phoneRepository.insertPhone(newPhone);
    }

    public Phone getPhoneByID(long id) {
        for(Phone p : phoneList.getValue()) {
            if(p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void updatePhone(Phone phone) {
        this.phoneRepository.updatePhone(phone);
    }

    public void deletePhone(Phone phone) {
        this.phoneRepository.deletePhone(phone);
    }
}
