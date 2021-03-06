package com.no1ks.madbrains_android_course.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.no1ks.madbrains_android_course.LoggedUser
import com.no1ks.madbrains_android_course.R
import com.no1ks.madbrains_android_course.RealmInterface
import com.no1ks.madbrains_android_course.RepositoriesLoader
import com.no1ks.madbrains_android_course.entity.Repository
import com.no1ks.madbrains_android_course.ui.adapter.FragmentsPagerAdapter
import com.no1ks.madbrains_android_course.ui.adapter.RepositoryAdapter
import com.no1ks.madbrains_android_course.ui.adapter.SimpleItemTouchHelperCallback

class RepositoriesActivity : AppCompatActivity(),
    RepositoriesLoader.ResponseListener,
    RepositoryAdapter.RepositoryListChangesListener {

    private val TAB_TITLES = arrayOf(
        R.string.tab_all,
        R.string.tab_favourite
    )

    private var mRepositoriesAdapter = RepositoryAdapter(this)
    private var mFavourireRepositoriesAdapter = RepositoryAdapter(this)
    private lateinit var mPullToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        RealmInterface.initRealm(this)
        RealmInterface.restoreFromDatabase()

        RepositoriesLoader.setCustomListener(this)
        mRepositoriesAdapter.setCustomListener(this)
        mFavourireRepositoriesAdapter.setCustomListener(this)

        setupViewPagerWithTabLayout()
        setupPullToRefresh()
        loadRepositories()
        setupActionBarLoading()

        Toast.makeText(this, getString(R.string.swipe_info), Toast.LENGTH_LONG).show()
    }

    override fun onRestart() {
        super.onRestart()
        showRepositoriesList(RepositoriesLoader.repositories)
        RepositoriesLoader.setCustomListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResponseReady() {
        if (RepositoriesLoader.numberOfRequestsQueued <= 0) {
            showRepositoriesList(RepositoriesLoader.repositories)
            mPullToRefresh.isRefreshing = false
            setupActionBarLogout()
        }
    }

    override fun onResponseFailed() {
        Toast.makeText(this,
            "Failed to download repositories: \n${RepositoriesLoader.queueResult}\n Only favourite available",
            Toast.LENGTH_LONG).show()
        showRepositoriesList(RepositoriesLoader.repositories)
        RepositoriesLoader.repositories = RealmInterface.favouriteRepositoriesOf(LoggedUser.username).toMutableList()
        mPullToRefresh.isRefreshing = false
        setupActionBarLogout()
    }

    override fun onRepositoryFavourited(repo: Repository) {
        mRepositoriesAdapter.repositories.removeAll { r -> r.full_name == repo.full_name }
        mFavourireRepositoriesAdapter.repositories.add(repo)
        RepositoriesLoader.repositories.find { r -> r.full_name == repo.full_name } ?.isFavourite  = true
        RealmInterface.addRepositoryToFavourite(LoggedUser.username, repo)
        sortRepositories()
        mRepositoriesAdapter.notifyDataSetChanged()
        mFavourireRepositoriesAdapter.notifyDataSetChanged()
        RealmInterface.backupToDatabase()
    }

    override fun onRepositoryUnfavourited(repo: Repository) {
        mFavourireRepositoriesAdapter.repositories.removeAll { r -> r.full_name == repo.full_name }
        mRepositoriesAdapter.repositories.add(repo)
        RepositoriesLoader.repositories.find { r -> r.full_name == repo.full_name } ?.isFavourite  = false
        RealmInterface.removeRepositoryFromFavourite(LoggedUser.username, repo)
        sortRepositories()
        mRepositoriesAdapter.notifyDataSetChanged()
        mFavourireRepositoriesAdapter.notifyDataSetChanged()
        RealmInterface.backupToDatabase()
    }

    private fun setupPullToRefresh() {
        mPullToRefresh = findViewById(R.id.pull_to_refresh)
        mPullToRefresh.setOnRefreshListener {
            loadRepositories()
        }
    }

    private fun setupActionBarLogout() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.logout)
        }
    }

    private fun setupActionBarLoading() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.loading)
        }
    }

    private fun setupViewPagerWithTabLayout() {
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = 2 // prevent deleting old fragments
        val adapter = FragmentsPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun loadRepositories() {
        setupActionBarLoading()
        mRepositoriesAdapter.repositories.clear()
        mFavourireRepositoriesAdapter.repositories.clear()
        mRepositoriesAdapter.notifyDataSetChanged()
        mFavourireRepositoriesAdapter.notifyDataSetChanged()
        mPullToRefresh.isRefreshing = true
        val queue = Volley.newRequestQueue(this)
        RepositoriesLoader.loadRepositoriesFromNetwork(queue)
    }

    private fun showRepositoriesList(repositories: MutableList<Repository>) {
        // all repositories
        mRepositoriesAdapter.repositories = repositories.toMutableList()
        val recycler = findViewById<RecyclerView>(R.id.rclr_repositories)
        recycler.adapter = mRepositoriesAdapter
        recycler.layoutManager = LinearLayoutManager(this)
        val callback: ItemTouchHelper.Callback =
            SimpleItemTouchHelperCallback(
                mRepositoriesAdapter,
                true
            )
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recycler)

        // favourite
        mFavourireRepositoriesAdapter.repositories.clear()
        val recyclerFavourite = findViewById<RecyclerView>(R.id.rclr_favourite_repositories)
        recyclerFavourite.adapter = mFavourireRepositoriesAdapter
        recyclerFavourite.layoutManager = LinearLayoutManager(this)
        val callbackFavourite: ItemTouchHelper.Callback =
            SimpleItemTouchHelperCallback(
                mFavourireRepositoriesAdapter,
                false
            )
        val touchHelperFavourite = ItemTouchHelper(callbackFavourite)
        touchHelperFavourite.attachToRecyclerView(recyclerFavourite)

        // check which repositories already favourited
        val favourites = RealmInterface.favouriteRepositoriesOf(LoggedUser.username)
        for (repo in favourites) {
            mRepositoriesAdapter.repositories.removeAll { r -> r.full_name == repo.full_name }
            mFavourireRepositoriesAdapter.repositories.add(repo)
            RepositoriesLoader.repositories.find { r -> r.full_name == repo.full_name } ?.isFavourite  = true
        }
        mRepositoriesAdapter.notifyDataSetChanged()
        mFavourireRepositoriesAdapter.notifyDataSetChanged()
        sortRepositories()
    }

    private fun sortRepositories() {
        mRepositoriesAdapter.repositories.sortBy { it.id }
        mFavourireRepositoriesAdapter.repositories.sortBy { it.id }
    }
}
