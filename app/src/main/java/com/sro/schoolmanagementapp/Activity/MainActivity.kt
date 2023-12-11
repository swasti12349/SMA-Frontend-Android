package com.sro.schoolmanagementapp.Activity

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sro.schoolmanagementapp.Activity.Admin.Register
import com.sro.schoolmanagementapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.registerbtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, Register::class.java))
        }

        viewBinding.signup.setOnClickListener {
            startActivity(Intent(this@MainActivity, ChooseActivity::class.java))
        }

        viewBinding.btnlogin.setOnClickListener {
            startActivity(Intent(this@MainActivity, ChooseLogin::class.java))
        }

        if (getSetting() == "") {
            askPermission()
        }
    }

    private fun askPermission() {
        if (getSetting() != null) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data =
                        Uri.parse(String.format("package:%s", applicationContext.packageName))
                    saveSetting()
                    startActivityForResult(intent, 2296)
                } catch (e: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivityForResult(intent, 2296)
                }
            } else {
                //below android 11
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf<String>(WRITE_EXTERNAL_STORAGE),
                    100
                )
                saveSetting()
            }
        }
    }

    private fun saveSetting() {
        val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("permission", "granted")
        editor.apply()
    }

    private fun getSetting(): String? {
        return getSharedPreferences("settings", MODE_PRIVATE).getString(
            "permission",
            ""
        )
    }
}