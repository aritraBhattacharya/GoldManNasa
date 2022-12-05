package com.aritra.goldmannasa.di.modules

import com.aritra.goldmannasa.NasaApp
import com.aritra.goldmannasa.data.remote.NetworkUtils
import com.aritra.goldmannasa.data.remote.network.BASE_URL
import com.aritra.goldmannasa.data.remote.network.NasaApi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetorfitAPI(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, networkUtils: NetworkUtils): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        clientBuilder.cache(cache)
        clientBuilder.addInterceptor(httpLoggingInterceptor)
        clientBuilder.addNetworkInterceptor(networkUtils.onlineInterceptor)
        clientBuilder.addInterceptor(networkUtils.offlineInterceptor)
        return clientBuilder.build()

    }

    @Provides
    @Singleton
    fun provideGson(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGifApi(retrofit: Retrofit): NasaApi {
        return retrofit.create(NasaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(application: NasaApp): Cache {
        val cacheSize: Long = 1024 * 1024 * 100
        return Cache(application.cacheDir, cacheSize)
    }

}