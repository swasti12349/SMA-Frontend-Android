package com.sro.schoolmanagementapp.Network

import ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClass {
    var retrofit: Retrofit? = null

    fun getInstance(): ApiService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.139.50:9091")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiService::class.java)
    }
}