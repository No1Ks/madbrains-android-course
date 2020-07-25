package com.no1ks.madbrains_android_course.entity

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User: RealmObject() {
    @PrimaryKey
    var username: String = ""
    var favouriteRepositories: RealmList<Repository> = RealmList()
}