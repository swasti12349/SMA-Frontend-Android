package com.sro.schoolmanagementapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sro.schoolmanagementapp.R
import com.sro.schoolmanagementapp.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    lateinit var viewBinding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)



    }
}