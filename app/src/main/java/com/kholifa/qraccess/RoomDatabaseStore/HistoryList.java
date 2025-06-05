package com.kholifa.qraccess.RoomDatabaseStore;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history_table")
public class HistoryList {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "data")
    private String data;

    @ColumnInfo(name = "data1")
    private String data1;
    @ColumnInfo(name = "data2")
    private String data2;
    @ColumnInfo(name = "data3")
    private String data3;
    @ColumnInfo(name = "data4")
    private String data4;
    @ColumnInfo(name = "data5")
    private String data5;
    @ColumnInfo(name = "data6")
    private String data6;
    @ColumnInfo(name = "data7")
    private String data7;
    @ColumnInfo(name = "data8")
    private String data8;
    @ColumnInfo(name = "data9")
    private String data9;
    @ColumnInfo(name = "data10")
    private String data10;
    @ColumnInfo(name = "data11")
    private String data11;
    @ColumnInfo(name = "data12")
    private String data12;

    @ColumnInfo(name = "datatype")
    private String datatype;

    public HistoryList(@NonNull String data, String data1, String data2, String data3, String data4, String data5, String data6, String data7, String data8, String data9, String data10, String data11, String data12, String datatype) {
        this.data = data;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
        this.data9 = data9;
        this.data10 = data10;
        this.data11 = data11;
        this.data12 = data12;
        this.datatype = datatype;
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

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    public String getData3() {
        return data3;
    }

    public String getData4() {
        return data4;
    }

    public String getData5() {
        return data5;
    }

    public String getData6() {
        return data6;
    }

    public String getData7() {
        return data7;
    }

    public String getData8() {
        return data8;
    }

    public String getData9() {
        return data9;
    }

    public String getData10() {
        return data10;
    }

    public String getData11() {
        return data11;
    }

    public String getData12() {
        return data12;
    }

}
