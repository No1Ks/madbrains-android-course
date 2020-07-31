package com.no1ks.madbrains_android_course.entity

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Repository : RealmObject() {
    var id: Int = 0
    var node_id: String = ""
    var name: String = ""
    @PrimaryKey
    var full_name: String = ""
    var private: Boolean = false
    var owner_login: String = ""
    var owner_id: Int = 0
    var owner_node_id: String = ""
    var owner_avatar_url: String = ""
    var owner_gravatar_id: String = ""
    var owner_url: String = ""
    var owner_html_url: String = ""
    var owner_followers_url: String = ""
    var owner_following_url: String = ""
    var owner_gists_url: String = ""
    var owner_starred_url: String = ""
    var owner_subscriptions_url: String = ""
    var owner_organizations_url: String = ""
    var owner_repos_url: String = ""
    var owner_events_url: String = ""
    var owner_received_events_url: String = ""
    var owner_type: String = ""
    var owner_site_admin: Boolean = false
    var html_url: String = ""
    var description: String = ""
    var fork: Boolean = false
    var url: String  = ""
    var forks_url: String = ""
    var keys_url: String = ""
    var collaborators_url: String = ""
    var teams_url: String = ""
    var hooks_url: String = ""
    var issue_events_url: String = ""
    var events_url: String = ""
    var assignees_url: String = ""
    var branches_url: String = ""
    var tags_url: String = ""
    var blobs_url: String = ""
    var git_tags_url: String = ""
    var git_refs_url: String = ""
    var trees_url: String = ""
    var statuses_url: String = ""
    var languages_url: String = ""
    var stargazers_url: String = ""
    var contributors_url: String = ""
    var subscribers_url: String = ""
    var subscription_url: String = ""
    var commits_url: String = ""
    var git_commits_url: String = ""
    var comments_url: String = ""
    var issue_comment_url: String = ""
    var contents_url: String = ""
    var compare_url: String = ""
    var merges_url: String = ""
    var archive_url: String = ""
    var downloads_url: String = ""
    var issues_url: String = ""
    var pulls_url: String = ""
    var milestones_url: String = ""
    var notifications_url: String = ""
    var labels_url: String = ""
    var releases_url: String = ""
    var deployments_url: String = ""

    var starsNumber: Int = 0
    var forksNumber: Int = 0
    var language: String = ""
    var commits: RealmList<Commit> = RealmList()
    var isFavourite: Boolean = false
}
