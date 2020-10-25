package io.keiji.sample.mastodonclient

import com.squareup.moshi.Json

data class Toot (
    val id: String,
    @Json(name = "create_at") val createdAt: String,
    val url: String,
    val content: String,
    val account: Account
)