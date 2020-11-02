package io.keiji.sample.mastodonclient

import com.squareup.moshi.Json

data class Toot (
    val id: String,
    @Json(name = "created_at") val createdAt: String,
    val url: String,
    @Json(name = "media_attachments") val mediaAttachments: List<Media>,
    val sensitive: Boolean,
    val content: String,
    val account: Account
){
    val topMedia: Media?
        get() = mediaAttachments.firstOrNull()
}
