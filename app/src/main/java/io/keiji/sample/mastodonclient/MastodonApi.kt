package io.keiji.sample.mastodonclient

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

//ここはただAPIの定義をしてるだけ
interface MastodonApi {
    @GET("api/v1/timelines/public")
    suspend fun fetchPublicTimeline(
        @Query("max_id") maxId: String? = null,
        @Query("only_media") onlyMedia: Boolean = false
    ): List<Toot>

    @GET("api/v1/timelines/home")
    suspend fun fetchHomeTimeline(
        @Header("Authorization") accessToken: String,
        @Query("max_id") maxId: String? = null
    ): List<Toot>

    @GET("api/v1/account/Verify_credentials")
    suspend fun verifyAccountCredentials(
        @Header("Authorization") accessToken: String
    ):Account
}



