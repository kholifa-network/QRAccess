package com.kholifa.qraccess.RoomDatabaseStore;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryListViewModel extends AndroidViewModel {
    private HistoryListRepository history_repository;
    LiveData<List<HistoryList>> history_data;

    public HistoryListViewModel(@NonNull Application application) {
        super(application);
        history_repository = new HistoryListRepository(application);

    }

    public LiveData<List<HistoryList>> getHistory_data() {

        history_data = history_repository.getAllHistoryData();
        return history_data;
    }

    public void insert(HistoryList history) {
        history_repository.insert(history);
    }

    public void deleteById(int id) {
        history_repository.deleteById(id);
    }

    public void deleteAll() {
        history_repository.deleteAll();
    }

    public void deleteDuplicates() {
        history_repository.deleteDuplicates();
    }


    public LiveData<List<CreationHistory>> getAllCreateHistoryData() {
        return history_repository.getAllCreateHistoryData();
    }

    public void insert(CreationHistory createHistory) {
        history_repository.insert(createHistory);
    }

    public void deleteByIdcreate(int id) {
        history_repository.deleteByIdcreate(id);
    }

    public void deleteAllcreate() {
        history_repository.deleteAllcreate();
    }
    public void createdDeleteDuplicates(){
        history_repository.createdDeleteDuplicates();
    }

}
