package tj.ikrom.cinemahall.data.network.api

import retrofit2.Call
import retrofit2.http.GET
import tj.ikrom.cinemahall.data.network.model.SeatsResponse
import tj.ikrom.cinemahall.domain.common.ApiConstants.API_SEATS

interface Api {

    @GET(API_SEATS)
    fun getSeats() : Call<SeatsResponse>

}