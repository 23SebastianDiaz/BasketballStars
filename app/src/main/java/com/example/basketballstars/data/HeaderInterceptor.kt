package com.example.basketballstars.data

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", "027aaf33e3msh5ba03e2a5f51ae6p151424jsn4b2c27d5f407")
            .addHeader("X-RapidAPI-Host", "api-nba-v1.p.rapidapi.com")
            .build()

        return chain.proceed(request)
    }

}