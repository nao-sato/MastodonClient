package io.keiji.sample.mastodonclient

import retrofit2.http.GET
import retrofit2.http.Query

//ここはただAPIの定義をしてるだけ
interface MastodonApi {
    @GET("api/v1/timelines/public")
    suspend fun fetchPublicTimeline(
        //全てのIDの最大値未満を持ってきてね
        @Query("max_id") maxId: String? = null,
        //画像データがあるやつのみの投稿のブール値
        @Query("only_media") onlyMedia: Boolean = false
    ): List<Toot>//←リストのTootクラスの型でお願いね
}