package com.no1ks.madbrains_android_course

import io.realm.RealmList
import io.realm.RealmObject

object LoggedUser {
    var username: String = ""
    var password: String = ""
}

open class User : RealmObject() {
    var username: String = ""
    var password: String = ""
    var favouriteRepositories: RealmList<String> = RealmList()
}