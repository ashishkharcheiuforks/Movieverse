package com.nandra.moviecatalogue.network.apiservice

import com.nandra.moviecatalogue.network.ConnectivityInterceptor
import com.nandra.moviecatalogue.network.response.YandexResponse
import com.nandra.moviecatalogue.util.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

interface YandexTranslationApiService {
    @POST("api/v1.5/tr.json/translate")
    suspend fun translateText(
        @Query("lang") language: String,
        @Query("text") text: List<String>
    ) : Response<YandexResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor) : YandexTranslationApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", Constant.API_KEY_YANDEX)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YandexTranslationApiService::class.java)
        }
    }
}