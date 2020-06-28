package com.example.rxjavademo.di.module

import android.app.Application
import com.example.rxjavademo.data.rest.NewsService
import com.example.rxjavademo.utils.InternetUtil.isInternetOn
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import javax.inject.Singleton


@Module(includes = arrayOf(ViewModelModule::class))
class RetrofitModule {
    private val BASE_URL = "https://newsapi.org/"

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
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

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        //setup cache
        val httpCacheDirectory = File(application.getCacheDir(), "responses")
        return Cache(httpCacheDirectory, cacheSize.toLong())
    }

    @Provides
    fun provideHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val response: Response = chain.proceed(chain.request())
                    val maxAge =
                        60 // read from cache for 60 seconds even if there is internet connection
                    return response.newBuilder()
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .removeHeader("Pragma")
                        .build()
                }
            })
            .addNetworkInterceptor(object :Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request: Request = chain.request()
                    if (!isInternetOn()) {
                        val maxStale =
                            60 * 60 * 24 * 30 // Offline cache available for 30 days
                        request = request.newBuilder()
                            .header(
                                "Cache-Control",
                                "public, only-if-cached, max-stale=$maxStale"
                            )
                            .removeHeader("Pragma")
                            .build()
                    }
                    return chain.proceed(request)
                }

            })
        client.cache(cache)
        return client.build()
    }


}
