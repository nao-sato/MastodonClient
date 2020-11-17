package io.keiji.sample.mastodonclient.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
//投稿（toot）のコンポーネントを入れる箱の定義
data class Toot (
    val id: String,
    @Json(name = "created_at") val createdAt: String,
    val url: String,
    @Json(name = "media_attachments") val mediaAttachments: List<Media>,
    val sensitive: Boolean,
    val content: String,
    val account: Account
): Parcelable {
    val topMedia: Media?
        get() = mediaAttachments.firstOrNull()
}
