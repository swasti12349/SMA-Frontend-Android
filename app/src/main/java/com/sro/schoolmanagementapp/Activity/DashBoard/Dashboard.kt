package com.sro.schoolmanagementapp.Activity.DashBoard

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.sro.schoolmanagementapp.Activity.Student.fragment.AttendanceFragment
import com.sro.schoolmanagementapp.Activity.Student.fragment.home
import com.sro.schoolmanagementapp.R
import com.sro.schoolmanagementapp.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )


        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, home()).commit()

        binding.navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.AttendanceLeaderBoard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, AttendanceFragment()).addToBackStack(null)
                        .commit()

                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true

                }

                R.id.Home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, home()).addToBackStack(null).commit()

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }

                else -> false
            }
        }


    }
}