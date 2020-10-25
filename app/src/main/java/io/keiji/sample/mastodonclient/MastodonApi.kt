package io.keiji.sample.mastodonclient

import okhttp3.ResponseBody
import retrofit2.http.GET

interface MastodonApi {
    @GET("api/v2/items?page=1&per_page=20")
    suspend fun fetchPublicTimeline(
    ): List<Toot>
}