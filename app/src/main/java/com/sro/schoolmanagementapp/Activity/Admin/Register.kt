package com.sro.schoolmanagementapp.Activity.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sro.schoolmanagementapp.Model.School
import com.sro.schoolmanagementapp.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {

    lateinit var viewBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.buttonSubmit.setOnClickListener {

            if (validation()) {
                val school = School()
                school.schoolType = viewBinding.editTextSchoolType.text.toString()
                school.schoolName = viewBinding.editTextSchoolName.text.toString()
                school.adminName = viewBinding.editTextAdminName.text.toString()
                school.mobileNumber = viewBinding.editTextMobileNumber.text.toString().toInt()
                val intent = Intent(this@Register, OtpActivity::class.java)
                intent.putExtra("school", school)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validation(): Boolean {

        if (!viewBinding.editTextAdminName.text!!.isEmpty() &&
            !viewBinding.editTextMobileNumber.text!!.isEmpty() &&
            !viewBinding.editTextSchoolName.text!!.isEmpty() &&
            !viewBinding.editTextSchoolType.text!!.isEmpty()
        ) {
            return true
        } else {
            Toast.makeText(this, "Some fields are empty", Toast.LENGTH_SHORT).show()
        }

        return false
    }

}
