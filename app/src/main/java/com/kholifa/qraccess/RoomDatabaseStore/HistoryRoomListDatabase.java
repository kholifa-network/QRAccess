package com.kholifa.qraccess.RoomDatabaseStore;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HistoryList.class, CreationHistory.class}, version = 1)
public abstract class HistoryRoomListDatabase extends RoomDatabase {

    public abstract HistoryListDao historyDao();


    private static volatile HistoryRoomListDatabase instance;
    private static final int number_of_threads = 4;

    static final ExecutorService database_write_executor =
            Executors.newFixedThreadPool(number_of_threads);

    static HistoryRoomListDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (HistoryRoomListDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            HistoryRoomListDatabase.class, "history_database")
                            .build();
                }
            }
        }
        return instance;
    }


}
