package io.keiji.sample.mastodonclient

import okhttp3.ResponseBody
import retrofit2.http.GET

interface MastodonApi {
    @GET("api/vl/timeline/public")
    suspend fun fetchPublicTimeline(
    ): ResponseBody
}