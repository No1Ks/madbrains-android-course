package com.no1ks.madbrains_android_course

import android.util.Base64
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.no1ks.madbrains_android_course.entity.Repository
import org.json.JSONArray
import org.json.JSONObject

object RepositoriesLoader {
    private const val mRepositoriesUrl = "https://api.github.com/repositories"
    private const val mRepositoriesDetailsUrl = "https://api.github.com/repos/"

    var queueResult: String = "success"
    var numberOfRequestsQueued: Int = 0

    val repositories: MutableList<Repository> = mutableListOf()

    interface ResponseListener {
        fun onResponseReady()
        fun onResponseFailed()
    }
    private var mListener: ResponseListener? = null

    fun setCustomListener(listener: ResponseListener?) {
        mListener = listener
    }

    fun loadRepositoriesFromNetwork(queue: RequestQueue) {
        val stringRequest = StringRequestWithAuth(
            Request.Method.GET,
            mRepositoriesUrl,
            Response.Listener { response ->
                repositories.clear()
                parseJsonRepositoriesList(response)
                numberOfRequestsQueued = repositories.count()
                for (repository in repositories) {
                    loadRepositoryDetailsFromNetwork(
                        queue,
                        mRepositoriesDetailsUrl + repository.full_name,
                        repository
                    )
                }
                mListener?.onResponseReady()
            },
            Response.ErrorListener { error ->
                queueResult = error.toString().split('.').last()
                mListener?.onResponseFailed()
            }
        )
        queue.add(stringRequest)
    }

    class StringRequestWithAuth(
        method: Int, url: String?, listener: Response.Listener<String?>?,
        errorListener: Response.ErrorListener?
    ) : StringRequest(method, url, listener, errorListener) {

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val headers: MutableMap<String, String> = HashMap()
            val credentials = "${LoggedUser.username}:${LoggedUser.password}"
            val auth = ("Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP))
            headers["Authorization"] = auth
            return headers
        }
    }

    private fun loadRepositoryDetailsFromNetwork(queue: RequestQueue, url: String, repository: Repository) {
        val stringRequest = StringRequestWithAuth(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                repository.forksNumber = jsonObject.getInt("forks")
                repository.language = jsonObject.getString("language")
                repository.starsNumber = jsonObject.getInt("stargazers_count")
                --numberOfRequestsQueued
                mListener?.onResponseReady()
            },
            Response.ErrorListener { error ->
                queueResult = error.toString().split('.').last()
                mListener?.onResponseFailed()
            }
        )
        queue.add(stringRequest)
    }

    private fun parseJsonRepositoriesList(responseText: String?) {
        val jsonArray = JSONArray(responseText)
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            val repository =
                Repository()
            repository.id = jsonObject.getInt("id")
            repository.node_id = jsonObject.getString("node_id")
            repository.name = jsonObject.getString("name")
            repository.full_name = jsonObject.getString("full_name")
            repository.private = jsonObject.getBoolean("private")
            // owner {
            repository.owner_login =
                jsonObject.getJSONObject("owner").getString("login")
            repository.owner_id =
                jsonObject.getJSONObject("owner").getInt("id")
            repository.owner_node_id =
                jsonObject.getJSONObject("owner").getString("node_id")
            repository.owner_avatar_url =
                jsonObject.getJSONObject("owner").getString("avatar_url")
            repository.owner_gravatar_id =
                jsonObject.getJSONObject("owner").getString("gravatar_id")
            repository.owner_url =
                jsonObject.getJSONObject("owner").getString("url")
            repository.owner_html_url =
                jsonObject.getJSONObject("owner").getString("html_url")
            repository.owner_followers_url =
                jsonObject.getJSONObject("owner").getString("followers_url")
            repository.owner_following_url =
                jsonObject.getJSONObject("owner").getString("following_url")
            repository.owner_gists_url =
                jsonObject.getJSONObject("owner").getString("gists_url")
            repository.owner_starred_url =
                jsonObject.getJSONObject("owner").getString("starred_url")
            repository.owner_subscriptions_url =
                jsonObject.getJSONObject("owner").getString("subscriptions_url")
            repository.owner_organizations_url =
                jsonObject.getJSONObject("owner").getString("organizations_url")
            repository.owner_repos_url =
                jsonObject.getJSONObject("owner").getString("repos_url")
            repository.owner_events_url =
                jsonObject.getJSONObject("owner").getString("events_url")
            repository.owner_received_events_url =
                jsonObject.getJSONObject("owner").getString("received_events_url")
            repository.owner_type =
                jsonObject.getJSONObject("owner").getString("type")
            repository.owner_site_admin =
                jsonObject.getJSONObject("owner").getBoolean("site_admin")
            // }
            repository.html_url = jsonObject.getString("html_url")
            repository.description = jsonObject.getString("description")
            repository.fork = jsonObject.getBoolean("fork")
            repository.url = jsonObject.getString("url")
            repository.forks_url = jsonObject.getString("forks_url")
            repository.keys_url = jsonObject.getString("keys_url")
            repository.collaborators_url = jsonObject.getString("collaborators_url")
            repository.teams_url = jsonObject.getString("teams_url")
            repository.hooks_url = jsonObject.getString("hooks_url")
            repository.issue_events_url = jsonObject.getString("issue_events_url")
            repository.events_url = jsonObject.getString("events_url")
            repository.assignees_url = jsonObject.getString("assignees_url")
            repository.branches_url = jsonObject.getString("branches_url")
            repository.tags_url = jsonObject.getString("tags_url")
            repository.blobs_url = jsonObject.getString("blobs_url")
            repository.git_tags_url = jsonObject.getString("git_tags_url")
            repository.git_refs_url = jsonObject.getString("git_refs_url")
            repository.trees_url = jsonObject.getString("trees_url")
            repository.statuses_url = jsonObject.getString("statuses_url")
            repository.languages_url = jsonObject.getString("languages_url")
            repository.stargazers_url = jsonObject.getString("stargazers_url")
            repository.contributors_url = jsonObject.getString("contributors_url")
            repository.subscribers_url = jsonObject.getString("subscribers_url")
            repository.subscription_url = jsonObject.getString("subscription_url")
            repository.commits_url = jsonObject.getString("commits_url")
            repository.git_commits_url = jsonObject.getString("git_commits_url")
            repository.comments_url = jsonObject.getString("comments_url")
            repository.issue_comment_url = jsonObject.getString("issue_comment_url")
            repository.contents_url = jsonObject.getString("contents_url")
            repository.compare_url = jsonObject.getString("compare_url")
            repository.merges_url = jsonObject.getString("merges_url")
            repository.archive_url = jsonObject.getString("archive_url")
            repository.downloads_url = jsonObject.getString("downloads_url")
            repository.issues_url = jsonObject.getString("issues_url")
            repository.pulls_url = jsonObject.getString("pulls_url")
            repository.milestones_url = jsonObject.getString("milestones_url")
            repository.notifications_url = jsonObject.getString("notifications_url")
            repository.labels_url = jsonObject.getString("labels_url")
            repository.releases_url = jsonObject.getString("releases_url")
            repository.deployments_url = jsonObject.getString("deployments_url")
            repositories.add(repository)
        }
    }
}
