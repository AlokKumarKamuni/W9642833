package uk.ac.tees.mad.w9642833.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("api/_search")
    fun getHouseList(
        @Query("locationIdentifier") locationIdentifier: String,
        @Query("channel") channel: String,
        @Query("currencyCode") currencyCode: String
    ): Call<Houses>
}