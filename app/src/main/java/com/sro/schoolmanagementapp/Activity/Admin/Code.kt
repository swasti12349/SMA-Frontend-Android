package com.sro.schoolmanagementapp.Activity.Admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sro.schoolmanagementapp.databinding.ActivityCodeBinding


class Code : AppCompatActivity() {

    lateinit var binding: ActivityCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setText(intent.getStringExtra("key").toString()!!)

        binding.btnShare.setOnClickListener{

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra("key").toString()!!)
            startActivity(Intent.createChooser(shareIntent, "Share via"))

        }

    }


}