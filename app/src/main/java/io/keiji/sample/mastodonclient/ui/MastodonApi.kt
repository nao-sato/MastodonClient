package io.keiji.sample.mastodonclient.ui

import io.keiji.sample.mastodonclient.entity.Account
import io.keiji.sample.mastodonclient.entity.Media
import io.keiji.sample.mastodonclient.entity.ResponseToken
import io.keiji.sample.mastodonclient.entity.Toot
import okhttp3.MultipartBody
import retrofit2.http.*
import java.sql.SQLInvalidAuthorizationSpecException


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

    //投稿
    @FormUrlEncoded
    @POST("api/v1/statuses")
    suspend fun postToot(
        @Header("Authorization") accessToken: String,
        @Field("status") status: String,
        @Field("media_ids[]") mediaIds: List<String>? = null
    ): Toot

    //投稿の削除
    @DELETE("api/v1/statuses/{id}")
    suspend fun deleteToot(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    )

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun token(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("scope") scope: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String
    ): ResponseToken

    @Multipart
    @POST("api/v1/media")
    suspend fun postMedia(
        @Header("Authorization") accessToken: String,
        @Part file: MultipartBody.Part
    ): Media
}



