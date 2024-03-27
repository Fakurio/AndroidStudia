package com.example.lab.lab3;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Phone.class}, version = 1)
public abstract class PhoneDatabase extends RoomDatabase {
    public abstract PhoneDao phoneDao();
    private static volatile PhoneDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService dbWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static PhoneDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhoneDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PhoneDatabase.class, "phone_database")
                            .addCallback(databaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback databaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            dbWriteExecutor.execute(() -> {
                PhoneDao phoneDao = INSTANCE.phoneDao();
                phoneDao.insert(new Phone("Samsung", "Galaxy", "Android 10", "samsung.com"));
                phoneDao.insert(new Phone("Xiaomi", "Redmi 10", "Android 11", "xiaomi.com"));
                phoneDao.insert(new Phone("Xiaomi", "Redmi 9", "Android 11", "xiaomi.com"));
                phoneDao.insert(new Phone("Nokia", "3310", "Android 11", "nokia.com"));
                phoneDao.insert(new Phone("Huawei", "P60", "Android 11", "huawei.com"));
                phoneDao.insert(new Phone("Sony", "Xperia 10", "Android 11", "sony.com"));
            });
        }
    };
}
