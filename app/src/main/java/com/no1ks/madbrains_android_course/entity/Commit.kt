package com.no1ks.madbrains_android_course.entity

import io.realm.RealmObject

open class Commit : RealmObject() {
    var message: String = ""
    var date: String = ""
    var authorName: String = ""
    var authorAvatarUrl: String = ""
}