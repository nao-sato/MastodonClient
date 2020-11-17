package io.keiji.sample.mastodonclient.entity

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

//画像のコンポーネントを入れる箱の定義
data class  Media (
    val id: String?,
    val type: String?,
    val url: String?,
    @Json(name = "preview_url") val previewUrl: String?
):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(type)
        parcel.writeString(url)
        parcel.writeString(previewUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }


        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }
}