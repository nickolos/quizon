package com.octopus.quizon.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JSONPlaceHolderAPI {
    @POST("/quizon/play/{id_game}/{id_round}/{number}")
    Call<ResponseQuestionFifthRound> getQuestion(@Path("id_game") Long idGame, @Path("id_round") int id_round, @Path("number") int idQuestion);

    @POST("quizon/game/create")
    Call<ResponseGameId> create(@Body ResponseUser user);

    @POST("quizon/game/join/{id_game}")
    Call<ResponseGameId> joinToGame(@Path("id_game")Long idGame, @Body ResponseUser user);

    @POST("quizon/result/save")
    Call<ResponseSave> save(@Body ResultFromUser userAnswer);


    @POST("quizon/result/table/{id_game}")
    Call<TableResultGame> tableresult(@Path("id_game") Long idGame);

    @POST("quizon/result/moreresult")
    Call<ResponseMoreResult> moreresult(@Body RequestMoreResult req);

    @POST("quizon/result/moreresult")
    Call<ResponseResult> getLastGamesRusults(@Body ResponseUser user);

    @GET("quizon/games/now_active")
    Call<ListActiveGame> getListActiveGames();

    @GET("login")
    Call<ResponseUser> login();

}