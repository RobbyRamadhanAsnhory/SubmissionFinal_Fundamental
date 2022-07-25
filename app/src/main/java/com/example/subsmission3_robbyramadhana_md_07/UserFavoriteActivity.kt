package com.example.subsmission3_robbyramadhana_md_07

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmission3_robbyramadhana_md_07.databinding.ActivityUserFavoriteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity(), DescCallback<List<UserData>> {

    private lateinit var bindingFavorite: ActivityUserFavoriteBinding
    private lateinit var userAdapter: Adapter
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingFavorite = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(bindingFavorite.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        userAdapter = Adapter()

        bindingFavorite.listFavorite.apply {
            adapter = userAdapter
            layoutManager =
                LinearLayoutManager(this@UserFavoriteActivity, LinearLayoutManager.VERTICAL, false)
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@UserFavoriteActivity, {
                when (it) {
                    is Desc.Error -> onFailed(it.message)
                    is Desc.Loading -> onLoading()
                    is Desc.Success -> it.data?.let { it1 -> onSuccess(it1) }
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option_fav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_theme_settings -> {
                val intent = Intent(this@UserFavoriteActivity, ThemeActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onSuccess(data: List<UserData>) {
        bindingFavorite.apply {
            pbFavorite.visibility = invisible

        }
        userAdapter.setAllData(data)
    }

    override fun onLoading() {
        bindingFavorite.apply {
            pbFavorite.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        if (message == null) {
            bindingFavorite.apply {
                pbFavorite.visibility = invisible

            descFavoriteError.apply {
                text = resources.getString(R.string.no_favorite)
                visibility = visible
            }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@UserFavoriteActivity, {
                when (it) {
                    is Desc.Error -> onFailed(it.message)
                    is Desc.Loading -> onLoading()
                    is Desc.Success -> it.data?.let { it1 -> onSuccess(it1) }
                }
            })
        }
    }
}