package io.keiji.sample.mastodonclient.ui

import io.keiji.sample.mastodonclient.entity.Account
import io.keiji.sample.mastodonclient.entity.Toot
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface MastodonApi {

    //公開タイムライン
    @GET("api/v1/timelines/public")
    suspend fun fetchPublicTimeline(
        @Query("max_id") maxId: String? = null,
        @Query("only_media") onlyMedia: Boolean = false
    ): List<Toot>

    //ホームタイムライン
    @GET("api/v1/timelines/home")
    suspend fun fetchHomeTimeline(
        @Header("Authorization") accessToken: String,
        @Query("max_id") maxId: String? = null
    ): List<Toot>

    //アカウントの資格情報
  @GET("api/v1/accounts/verify_credentials")
    suspend fun verifyAccountCredential(
        @Header("Authorization") accessToken: String
    ): Account
}



