package com.sro.schoolmanagementapp.Activity.Admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sro.schoolmanagementapp.Model.ResponseObject
import com.sro.schoolmanagementapp.Model.School
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.ActivityOtpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.SecureRandom


class OtpActivity : AppCompatActivity() {


    private lateinit var viewBinding: ActivityOtpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.buttonSubmit.setOnClickListener {

            if (viewBinding.editTextOTP.text.toString() == "123456") {

                val code = generateRandomString(8)
                val school = intent.getSerializableExtra("school") as? School

                submitData(school!!)

            } else {
                Toast.makeText(this@OtpActivity, "Empty or wrong otp", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitData(school: School) {

        val retrofit = RetrofitClass().getInstance().postSchool(school)
        retrofit.enqueue(object : Callback<ResponseObject> {
            override fun onResponse(
                call: Call<ResponseObject>,
                response: Response<ResponseObject>
            ) {
                if (response.isSuccessful) {
                    val responseObject = response.body()
                    if (responseObject != null) {
                        Log.d("sdfr", responseObject.status.toString())
                        val intent = Intent(this@OtpActivity, Code::class.java)
                        intent.putExtra("key", school.schoolCode)
                        startActivity(intent)
                    } else {
                        Log.d("sdfr", "Response body is null")
                    }
                } else {
                    Log.d("sdfr", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                Log.d("sdfr", "Failure: ${t.localizedMessage}")
            }
        })
    }

    fun generateRandomString(length: Int): String {
        val Characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val randomString = StringBuilder(length)
        val secureRandom = SecureRandom()
        for (i in 0 until length) {
            val randomIndex = secureRandom.nextInt(Characters.length)
            randomString.append(Characters[randomIndex])
        }
        return randomString.toString()
    }


}