package com.no1ks.madbrains_android_course

import io.realm.RealmObject

open class Repository : RealmObject() {
    var          id: Int = 0
    lateinit var node_id: String
    lateinit var name: String
    lateinit var full_name: String
    var          private: Boolean = false
    lateinit var owner_login: String
    var          owner_id: Int = 0
    lateinit var owner_node_id: String
    lateinit var owner_avatar_url: String
    lateinit var owner_gravatar_id: String
    lateinit var owner_url: String
    lateinit var owner_html_url: String
    lateinit var owner_followers_url: String
    lateinit var owner_following_url: String
    lateinit var owner_gists_url: String
    lateinit var owner_starred_url: String
    lateinit var owner_subscriptions_url: String
    lateinit var owner_organizations_url: String
    lateinit var owner_repos_url: String
    lateinit var owner_events_url: String
    lateinit var owner_received_events_url: String
    lateinit var owner_type: String
    var          owner_site_admin: Boolean = false
    lateinit var html_url: String
    lateinit var description: String
    var          fork: Boolean = false
    lateinit var url: String
    lateinit var forks_url: String
    lateinit var keys_url: String
    lateinit var collaborators_url: String
    lateinit var teams_url: String
    lateinit var hooks_url: String
    lateinit var issue_events_url: String
    lateinit var events_url: String
    lateinit var assignees_url: String
    lateinit var branches_url: String
    lateinit var tags_url: String
    lateinit var blobs_url: String
    lateinit var git_tags_url: String
    lateinit var git_refs_url: String
    lateinit var trees_url: String
    lateinit var statuses_url: String
    lateinit var languages_url: String
    lateinit var stargazers_url: String
    lateinit var contributors_url: String
    lateinit var subscribers_url: String
    lateinit var subscription_url: String
    lateinit var commits_url: String
    lateinit var git_commits_url: String
    lateinit var comments_url: String
    lateinit var issue_comment_url: String
    lateinit var contents_url: String
    lateinit var compare_url: String
    lateinit var merges_url: String
    lateinit var archive_url: String
    lateinit var downloads_url: String
    lateinit var issues_url: String
    lateinit var pulls_url: String
    lateinit var milestones_url: String
    lateinit var notifications_url: String
    lateinit var labels_url: String
    lateinit var releases_url: String
    lateinit var deployments_url: String

    var starsNumber: Int = 0
    var forksNumber: Int = 0
    var language: String = ""
}
