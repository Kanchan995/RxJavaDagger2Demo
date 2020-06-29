package com.example.rxjavademo.di.module

import android.util.Log
import com.example.rxjavademo.base.BaseApplication
import com.example.rxjavademo.data.rest.NewsService
import com.example.rxjavademo.utils.InternetUtil.isInternetOn
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.CacheControl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = arrayOf(ViewModelModule::class))
class RetrofitModule {
    private val BASE_URL = "https://newsapi.org/"


    private val CONNECT_TIMEOUT = 45
    private val WRITE_TIMEOUT = 45
    private val READ_TIMEOUT = 45
    private val CACHE_SIZE = 10 * 1024 * 1024 // 10 MB
        .toLong()

    //for making it private
    private fun provideHttpCache(): Cache? {
        val cacheFile =
            File(BaseApplication.instance?.cacheDir, "newfile")
        return Cache(cacheFile, CACHE_SIZE)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
        client.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        client.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        client.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        client.cache(provideHttpCache())


        client.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {

                val cacheBuilder = CacheControl.Builder()
                cacheBuilder.maxAge(0, TimeUnit.SECONDS)
                cacheBuilder.maxStale(365, TimeUnit.DAYS)
                val cacheControl = cacheBuilder.build()

                var request: Request = chain.request()
                if (isInternetOn()) {
                    request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()
                }


                request = if (isInternetOn()) {
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 60)
                        .build()
                } else {
                    Log.d("Cache ","Loading")
                    request.newBuilder().header(
                        "Cache-Control",
                        "max-age=" + 60 * 60 * 24 * 7
                    )
                        .removeHeader("Pragma").build() // 1 week
                }
                return chain.proceed(request)
            }
        })

        val okHttpClient=client.build()
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): NewsService {
        return retrofit.create<NewsService>(NewsService::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

}
