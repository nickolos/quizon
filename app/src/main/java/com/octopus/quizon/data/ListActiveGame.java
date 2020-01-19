package com.octopus.quizon.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListActiveGame {

    @SerializedName("listActiveGames")
    @Expose
    private List<Long> listActiveGames;

    public List<Long> getActiveGamesList()
    {
        return listActiveGames;
    }
 }
