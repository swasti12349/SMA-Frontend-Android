package com.sro.schoolmanagementapp.Activity.Teacher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.sro.schoolmanagementapp.Activity.MainActivity
import com.sro.schoolmanagementapp.Model.ResponseObject
import com.sro.schoolmanagementapp.Model.Teacher
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.ActivityTeacherSignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherSignUp : AppCompatActivity() {
    var isOTPSent = false

    lateinit var binding: ActivityTeacherSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherSignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            if (validation() && !isOTPSent) {
                sendOTP()

            } else if (isOTPSent) {

                val teacher = Teacher()
                teacher.name = binding.editTextTeacherName.text.toString()
                teacher.subject = binding.editTextTeacherSubject.text.toString()
                teacher.gender = binding.editTextTeacherGender.text.toString()
                teacher.mobile = binding.editTextMobile.text.toString()
                teacher.schoolCode = binding.editTextSchoolCode.text.toString()
                teacher.classs = binding.editTextSchoolCode.text.toString()
                teacher.section = binding.editTextSection.text.toString()
                postTeacher(teacher)
            }
        }
    }

    private fun validation(): Boolean {

        if (binding.editTextTeacherName.text.isNullOrBlank()) {
            binding.editTextTeacherName.error = "Empty"
            return false
        }

        if (binding.editTextTeacherGender.text.isNullOrBlank()) {
            binding.editTextTeacherGender.error = "Empty"
            return false
        }
        if (binding.editTextTeacherSubject.text.isNullOrBlank()) {
            binding.editTextTeacherSubject.error = "Empty"
            return false
        }
        if (binding.editTextMobile.text.isNullOrBlank()) {
            binding.editTextMobile.error = "Empty"
            return false
        }

        if (binding.editTextSection.text.isNullOrBlank()) {
            binding.editTextSection.error = "Empty"
            return false
        }
        return true

    }

    private fun sendOTP() {
        isOTPSent = true
        binding.editTextOTP.visibility = View.VISIBLE
        binding.tilotp.visibility = View.VISIBLE
    }

    private fun postTeacher(teacher: Teacher) {
        if (validateOTP()) {

            try {
                val call = RetrofitClass().getInstance().postTeacher(teacher)
                call.enqueue(object : Callback<ResponseObject> {
                    override fun onResponse(
                        call: Call<ResponseObject>,
                        response: Response<ResponseObject>
                    ) {
                        Log.d("fddg", response.body()?.status.toString())
                        Toast.makeText(this@TeacherSignUp, "Registered", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@TeacherSignUp, MainActivity::class.java))
                        finishAffinity()

                    }

                    override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                        Log.d("fddgf", t.localizedMessage)
                        Toast.makeText(this@TeacherSignUp, "Error", Toast.LENGTH_SHORT).show()

                    }

                })
            } catch (e: Exception) {
                Log.d("fddge", e.localizedMessage)
            }

        } else {
            Toast.makeText(this@TeacherSignUp, "Wrong OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateOTP(): Boolean {

        if (binding.editTextOTP.text.toString() == "123456") {
            return true
        }
        return false
    }
}