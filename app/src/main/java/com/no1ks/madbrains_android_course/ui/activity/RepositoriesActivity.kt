package com.no1ks.madbrains_android_course.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.no1ks.madbrains_android_course.*
import com.no1ks.madbrains_android_course.entity.Repository
import com.no1ks.madbrains_android_course.ui.adapter.FragmentsPagerAdapter
import com.no1ks.madbrains_android_course.ui.adapter.RepositoryAdapter
import com.no1ks.madbrains_android_course.ui.adapter.SimpleItemTouchHelperCallback

private val TAB_TITLES = arrayOf(
    R.string.tab_all,
    R.string.tab_favourite
)

class RepositoriesActivity : AppCompatActivity(),
    RepositoriesLoader.ResponseListener,
    RepositoryAdapter.RepositoryListChangesListener {

    private var mRepositoriesAdapter = RepositoryAdapter()
    private var mFavourireRepositoriesAdapter = RepositoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        RealmInterface.initRealm(this)
        RealmInterface.restoreFromDatabase()

        mRepositoriesAdapter.setCustomListener(this)
        mFavourireRepositoriesAdapter.setCustomListener(this)

        setupViewPagerWithTabLayout()
        setupActionBar()
        loadRepositories()
    }

    private fun startLoadingScreen() {
        val intent = Intent(this, LoadingActivity::class.java)
        this.startActivity(intent)
    }

    private fun finishLoadingScreen() {
        sendBroadcast(Intent(getString(R.string.broadcast_close_loading_screen)))
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.logout)
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
        val queue = Volley.newRequestQueue(this)
        RepositoriesLoader.setCustomListener(this)
        startLoadingScreen()
        RepositoriesLoader.loadRepositoriesFromNetwork(queue)
    }

    private fun showRepositoriesList(repositories: MutableList<Repository>) {
        // all repositories
        mRepositoriesAdapter.repositories = repositories
        val recycler = findViewById<RecyclerView>(R.id.recyclerRepositoriesId)
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
        val recyclerFavourite = findViewById<RecyclerView>(R.id.recyclerFavouriteRepositoriesId)
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
        }
        mRepositoriesAdapter.notifyDataSetChanged()
        mFavourireRepositoriesAdapter.notifyDataSetChanged()
        sortRepositories()
        Toast.makeText(this, getString(R.string.swipe_info), Toast.LENGTH_SHORT).show()
    }

    private fun sortRepositories() {
        mRepositoriesAdapter.repositories.sortBy { it.id }
        mFavourireRepositoriesAdapter.repositories.sortBy { it.id }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResponseReady() {
        if (RepositoriesLoader.numberOfRequestsQueued <= 0) {
            showRepositoriesList(RepositoriesLoader.repositories)
            finishLoadingScreen()
        }
    }

    override fun onResponseFailed() {
        Toast.makeText(this,
            "Failed to download repositories: \n${RepositoriesLoader.queueResult}",
            Toast.LENGTH_SHORT).show()
        finishLoadingScreen()
        finish()
    }

    override fun onRepositoryFavourited(repo: Repository) {
        mRepositoriesAdapter.repositories.removeAll { r -> r.full_name == repo.full_name }
        mFavourireRepositoriesAdapter.repositories.add(repo)
        RealmInterface.addRepositoryToFavourite(LoggedUser.username, repo)
        sortRepositories()
        mRepositoriesAdapter.notifyDataSetChanged()
        mFavourireRepositoriesAdapter.notifyDataSetChanged()
        RealmInterface.backupToDatabase()
    }

    override fun onRepositoryUnfavourited(repo: Repository) {
        mFavourireRepositoriesAdapter.repositories.removeAll { r -> r.full_name == repo.full_name }
        mRepositoriesAdapter.repositories.add(repo)
        RealmInterface.removeRepositoryFromFavourite(LoggedUser.username, repo)
        sortRepositories()
        mRepositoriesAdapter.notifyDataSetChanged()
        mFavourireRepositoriesAdapter.notifyDataSetChanged()
        RealmInterface.backupToDatabase()
    }
}
