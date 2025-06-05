package com.kholifa.qraccess.RoomDatabaseStore;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryListRepository {

    private HistoryListDao history_dao;


    public HistoryListRepository(Application application) {
        HistoryRoomListDatabase db = HistoryRoomListDatabase.getDatabase(application);
        history_dao = db.historyDao();
    }

    LiveData<List<HistoryList>> getAllHistoryData() {
        return history_dao.getAllData();
    }

    void insert(HistoryList history) {
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.insert(history);
        });
    }

    void deleteById(int id) {
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.deleteById(id);
        });
    }

    void deleteAll() {
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.deleteAll();
        });
    }

    void deleteDuplicates() {
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.deleteDuplicates();
        });
    }


    LiveData<List<CreationHistory>> getAllCreateHistoryData() {
        return history_dao.getAllCreateData();
    }

    void insert(CreationHistory createHistory) {
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.insert(createHistory);
        });
    }

    void deleteByIdcreate(int id) {
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.createdeleteById(id);
        });
    }

    void deleteAllcreate() {
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.deleteAllCreate();
        });
    }

    void createdDeleteDuplicates(){
        HistoryRoomListDatabase.database_write_executor.execute(() -> {
            history_dao.createdDeleteDuplicates();
        });
    }

}
