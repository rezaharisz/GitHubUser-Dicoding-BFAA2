package com.rezaharis.githubuserapp2.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        var login:String = "",
        var avatar_url: String = "",
        var name:String = "",
        var company:String = "",
        var location:String = "",
        var repo:String = "",
        var followers:String = "",
        var following:String = ""
):Parcelable