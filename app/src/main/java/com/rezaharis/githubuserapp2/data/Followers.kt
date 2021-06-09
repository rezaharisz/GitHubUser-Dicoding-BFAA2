package com.rezaharis.githubuserapp2.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Followers(
    var login:String = "",
    var avatar_url: String = "",
    var name:String = "",
    var company:String = ""
):Parcelable