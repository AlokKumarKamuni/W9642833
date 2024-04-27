package uk.ac.tees.mad.w9642833.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.rightmove.co.uk/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}