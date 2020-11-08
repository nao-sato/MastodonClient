package io.keiji.sample.mastodonclient

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// データ取得処理を行うところ

class TootRepository(instanceUrl: String) {

    //Moshiのビルド
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //Moshiを渡したretrofitを作る
    private val retrofit = Retrofit.Builder()
        .baseUrl(instanceUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    //MastodonApiのURLと合体
    private val api = retrofit.create(MastodonApi::class.java)

    suspend fun fetchPublicTimeline(
        maxId: String?, onlyMedia:  Boolean
    ) = withContext(Dispatchers.IO) {
        api.fetchPublicTimeline(
            maxId =  maxId,
            onlyMedia = onlyMedia
        )
    }
}