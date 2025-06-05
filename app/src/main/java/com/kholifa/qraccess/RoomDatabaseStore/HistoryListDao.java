package com.kholifa.qraccess.RoomDatabaseStore;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HistoryList history);

    @Query("DELETE FROM history_table WHERE id NOT IN (SELECT MIN(id) FROM history_table GROUP BY data, data1,data2,data3,data4,data5,data6,data7,data8,data9,data10,data11,data12,datatype)")
    void deleteDuplicates();

    @Query("DELETE FROM history_table")
    void deleteAll();

    @Query("SELECT * from history_table")
    LiveData<List<HistoryList>> getAllData();

    @Query("DELETE FROM history_table where id= :id")
    void deleteById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CreationHistory createHistory);

    @Query("DELETE FROM createhistory_table")
    void deleteAllCreate();

    @Query("SELECT * from createhistory_table")
    LiveData<List<CreationHistory>> getAllCreateData();

    @Query("DELETE FROM createhistory_table where id= :id")
    void createdeleteById(int id);

    @Query("DELETE FROM createhistory_table WHERE id NOT IN (SELECT MIN(id) FROM createhistory_table GROUP BY data,code,datatype)")
    void createdDeleteDuplicates();

}
