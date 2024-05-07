package com.betrybe.currencyview.data.api

import com.betrybe.currencyview.data.models.CurrencyRateResponse
import com.betrybe.currencyview.data.models.CurrencySymbolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("symbols")
    suspend fun getSymbols(
        @Header("apiKey") apiKey: String = "3xt1gldWPOEU3tAN7Ox3hrqTmzxxBPuT"
    ): Response<CurrencySymbolResponse>

    @GET("latest")
    suspend fun getLatestRates(
        @Header("apiKey") apiKey: String = "3xt1gldWPOEU3tAN7Ox3hrqTmzxxBPuT",
        @Query("base") base: String
    ): Response<CurrencyRateResponse>
}