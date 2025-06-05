package com.kholifa.qraccess.RoomDatabaseStore;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "createhistory_table")
public class CreationHistory {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "data")
    private String data;

    @ColumnInfo(name = "datatype")
    private String datatype;


    @ColumnInfo(name = "code")
    private String codebitmap;

    public CreationHistory(@NonNull String data, String datatype, String codebitmap) {
        this.data = data;
        this.datatype = datatype;
        this.codebitmap = codebitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatatype() {
        return datatype;
    }

    @NonNull
    public String getData() {
        return data;
    }

    public String getCodebitmap() {
        return codebitmap;
    }

}

