package com.example.subsmission3_robbyramadhana_md_07

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.subsmission3_robbyramadhana_md_07.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.subsmission3_robbyramadhana_md_07.Complement.EXTRA_USER
import com.example.subsmission3_robbyramadhana_md_07.Complement.TAB_TITLES
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDetailActivity : AppCompatActivity(), DescCallback<UserData?> {

    private lateinit var bindingDetail: ActivityUserDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDetail = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(bindingDetail.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        val username = intent.getStringExtra(EXTRA_USER)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDetailUser(username.toString()).observe(this@UserDetailActivity, {
                when (it) {
                    is Desc.Error -> onFailed(it.message)
                    is Desc.Loading -> onLoading()
                    is Desc.Success -> onSuccess(it.data)
                }
            })
        }

        val pagerAdapter = FollowPagerAdapter(this, username.toString())

        bindingDetail.apply {
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tlMaterial, viewPager) { tlMaterial, position ->
                tlMaterial.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                val intent = Intent(this@UserDetailActivity, UserFavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_theme_settings -> {
                val intent = Intent(this@UserDetailActivity, ThemeActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onSuccess(data: UserData?) {
        bindingDetail.apply {
            detailUsername.text = data?.username
            detailName.text = data?.name
            detailLocation.text = data?.location
            detailRepository.text = data?.repository.toString()
            detailCompany.text = data?.company
            detailFollowers.text = data?.follower.toString()
            detailFollowing.text = data?.following.toString()

            Glide.with(this@UserDetailActivity)
                .load(data?.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(detailAvatar)

            pbDetail.visibility = invisible

            ivFavorite.visibility = View.VISIBLE

            if (data?.isFavorite == true)
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite))
            else
                ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_unfavorite))

            supportActionBar?.title = data?.username

            ivFavorite.setOnClickListener {
                if (data?.isFavorite == true) {
                    data.isFavorite = false
                    viewModel.deleteFavoriteUser(data)
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_unfavorite))
                } else {
                    data?.isFavorite = true
                    data?.let { it1 -> viewModel.insertFavoriteUser(it1) }
                    ivFavorite.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite))
                }
            }
        }
    }

    override fun onLoading() {
        bindingDetail.apply {
            pbDetail.visibility = visible
        }
        bindingDetail.ivFavorite.visibility = View.INVISIBLE
    }


    override fun onFailed(message: String?) {
        bindingDetail.apply {
            pbDetail.visibility = invisible
        }
        bindingDetail.ivFavorite.visibility = View.INVISIBLE
    }
}




