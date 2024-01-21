package com.sro.schoolmanagementapp.Activity.DashBoard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.sro.schoolmanagementapp.Activity.MainActivity
import com.sro.schoolmanagementapp.Fragments.AttendanceFragment
import com.sro.schoolmanagementapp.Fragments.Home
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
            .replace(R.id.fragmentContainer, Home()).commit()

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
                        .replace(R.id.fragmentContainer, Home()).addToBackStack(null).commit()

                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }


                R.id.logout -> {
                    startActivity(Intent(this@Dashboard, MainActivity::class.java))
                    forgetUser(
                        getSharedPreferences(
                            "accfile",
                            Context.MODE_PRIVATE
                        ).getString("acctype", "")
                    )
                    finishAffinity()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }

                else -> false
            }
        }


    }

    private fun forgetUser(acctype: String?) {
        if (acctype == "student") {
            var sp = getSharedPreferences("student", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putString("studentmobile", "")
            editor.apply()
        } else {
            var sp = getSharedPreferences("teacher", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putString("teachermobile", "")
            editor.apply()
        }

    }
}