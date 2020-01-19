package com.octopus.quizon.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class TableResultGame {

    @SerializedName("table")
    @Expose
    private List<ResponseResult> mTable;

    public TableResultGame(List<ResponseResult> mTable) {
        this.mTable = mTable;
    }

    public List<ResponseResult> getTable()
    {
        return mTable;
    }
}
