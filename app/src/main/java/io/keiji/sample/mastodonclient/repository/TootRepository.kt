package io.keiji.sample.mastodonclient.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.keiji.sample.mastodonclient.ui.MastodonApi
import io.keiji.sample.mastodonclient.entity.UserCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// ネットワークアクセスに関係するコード（IOスレッド）

class TootRepository(private val userCredential: UserCredential) {

    //Moshiのビルド
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //Moshiを渡したretrofitを作る
    private val retrofit = Retrofit.Builder()
        .baseUrl(userCredential.instanceUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build())
        .build()

    //MastodonApiのURLと合体
    private val api = retrofit.create(MastodonApi::class.java)
    suspend fun fetchPublicTimeline(maxId: String?, onlyMedia:  Boolean)
            = withContext(Dispatchers.IO) {
        api.fetchPublicTimeline(maxId =  maxId, onlyMedia = onlyMedia)
    }

    suspend fun fetchHomeTimeline(maxId: String?)
            = withContext(Dispatchers.IO) {
        api.fetchHomeTimeline(accessToken = "Bearer ${userCredential.accessToken}", maxId = maxId)
    }
}