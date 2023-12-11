package com.sro.schoolmanagementapp.Activity.Student

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.sro.schoolmanagementapp.Activity.DashBoard.Dashboard
import com.sro.schoolmanagementapp.Model.ResponseObject
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.ActivityStudentSignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentSignUp : AppCompatActivity() {
    lateinit var binding: ActivityStudentSignUpBinding

    var isOTPSent = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {

            if (validation() && !isOTPSent) {
                sendOTP()

            } else if (isOTPSent) {
                val student = Student()

                student.name = binding.editTextStudentName.text.toString()
                student.classs = binding.editTextClass.text.toString()
                student.section = binding.editTextsection.text.toString()
                student.mobile = binding.editTextMobile.text.toString()
                student.schoolID = binding.editTextSchoolCode.text.toString()
                student.roll = binding.editTextSchoolCode.text.toString()

                postStudent(student)
            }

        }
    }

    private fun validation(): Boolean {

        if (binding.editTextStudentName.text.isNullOrBlank()) {
            binding.editTextStudentName.error = "Empty"
            return false
        }

        if (binding.editTextClass.text.isNullOrBlank()) {
            binding.editTextClass.error = "Empty"
            return false
        }
        if (binding.editTextsection.text.isNullOrBlank()) {
            binding.editTextsection.error = "Empty"
            return false
        }
        if (binding.editTextMobile.text.isNullOrBlank()) {
            binding.editTextMobile.error = "Empty"
            return false
        }

        if (binding.roll.text.isNullOrBlank()) {
            binding.roll.error = "Empty"
            return false
        }
        return true

    }

    private fun sendOTP() {
        isOTPSent = true
        binding.editTextOTP.visibility = View.VISIBLE
        binding.tilotp.visibility = View.VISIBLE
    }

    private fun postStudent(student: Student) {
        if (validateOTP()) {

            try {
                val call = RetrofitClass().getInstance().postStudent(student)
                call.enqueue(object : Callback<ResponseObject> {
                    override fun onResponse(
                        call: Call<ResponseObject>,
                        response: Response<ResponseObject>
                    ) {
                        val intent = Intent(this@StudentSignUp, Dashboard::class.java)
                        intent.putExtra("object", student)
                        Toast.makeText(this@StudentSignUp, "Regisstered", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finishAffinity()
                    }

                    override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                        Toast.makeText(this@StudentSignUp, "Error", Toast.LENGTH_SHORT).show()

                    }

                })
            } catch (e: Exception) {
                Log.d("Sdfr", e.localizedMessage)
            }

        } else {
            Toast.makeText(this@StudentSignUp, "Wrong OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateOTP(): Boolean {

        if (binding.editTextOTP.text.toString() == "123456") {
            return true
        }
        return false
    }
}