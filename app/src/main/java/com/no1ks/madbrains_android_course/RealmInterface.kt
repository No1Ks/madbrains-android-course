package com.no1ks.madbrains_android_course

import android.content.Context
import com.no1ks.madbrains_android_course.entity.Repository
import com.no1ks.madbrains_android_course.entity.User
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

object RealmInterface {

    private var mUsers: MutableList<User> = mutableListOf()

    fun initRealm(context: Context) {
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    fun addRepositoryToFavourite(username: String, repo: Repository) {
        for (user in mUsers) {
            if (user.username == username) {
                // check if repository already added to favourites
                if (user.favouriteRepositories.find { r -> r.full_name == repo.full_name } == null) {
                    user.favouriteRepositories.add(repo)
                }
                return
            }
        }
        val newUser = User()
        newUser.username = username
        newUser.favouriteRepositories.add(repo)
        mUsers.add(newUser)
    }

    fun favouriteRepositoriesOf(username: String): List<Repository> {
        for (user in mUsers) {
            if (user.username == username) {
                return user.favouriteRepositories
            }
        }
        return emptyList()
    }

    fun removeRepositoryFromFavourite(username: String, repo: Repository) {
        for (user in mUsers) {
            if (user.username == username) {
                user.favouriteRepositories.removeAll { r -> r.full_name == repo.full_name }
                break
            }
        }
    }

    fun restoreFromDatabase() {
        val realm = Realm.getDefaultInstance()
        mUsers = realm.copyFromRealm(realm.where(User::class.java).findAll())
    }

    fun backupToDatabase() {
        if (mUsers.isEmpty()) return
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.insertOrUpdate(mUsers)
        realm.commitTransaction()
    }

    fun deleteAll() {
        val realm = Realm.getDefaultInstance()
        val results: RealmResults<User> = realm.where(User::class.java).findAll()
        realm.beginTransaction()
        results.deleteAllFromRealm()
        realm.commitTransaction()
    }
}