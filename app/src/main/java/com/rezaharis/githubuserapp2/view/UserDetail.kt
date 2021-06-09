package com.rezaharis.githubuserapp2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rezaharis.githubuserapp2.R
import com.rezaharis.githubuserapp2.adapter.SectionsPagerAdapter
import com.rezaharis.githubuserapp2.databinding.ActivityUserDetailBinding
import com.rezaharis.githubuserapp2.data.User
import java.lang.StringBuilder

class UserDetail : AppCompatActivity() {

    private lateinit var binding:ActivityUserDetailBinding

    companion object{
        const val USER = "user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayShowHomeEnabled(true)

        getData()
    }

    private fun getData(){
        val user = intent.getParcelableExtra<User>(USER) as User
        Glide.with(this)
            .load(user.avatar_url)
            .override(150,150)
            .into(binding.avatarImage)
        binding.tvName.text = user.name
        binding.tvUsername.text = StringBuilder("@").append(user.login)
        binding.tvCompany.text = user.company
        binding.tvLocation.text = user.location
        binding.tvRepository.text = user.repo
        binding.tvFollowers.text = user.followers
        binding.tvFollowing.text = user.following

        getPagerAdapter(user)
    }

    private fun getPagerAdapter(user: User){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user.login
        val viewPager2: ViewPager2 = findViewById(R.id.view_pager2)
        viewPager2.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager2){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}