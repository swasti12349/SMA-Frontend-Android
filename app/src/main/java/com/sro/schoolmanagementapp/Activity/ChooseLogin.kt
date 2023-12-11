package com.sro.schoolmanagementapp.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sro.schoolmanagementapp.Activity.DashBoard.Dashboard
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.ActivityChooseLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseLogin : AppCompatActivity() {
    lateinit var binding: ActivityChooseLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.student.setOnClickListener {
            if (getStudentMobile().length != 10) {

                val intent = Intent(this@ChooseLogin, Login::class.java)
                intent.putExtra("type", "student");
                startActivity(intent)
            } else {
                loginStudent()
            }
        }

        binding.teacher.setOnClickListener {
            val intent = Intent(this@ChooseLogin, Login::class.java)
            intent.putExtra("type", "teacher");
            startActivity(intent)
        }

    }

    fun loginStudent() {
        val call = RetrofitClass().getInstance()
            .loginStudent(getStudentMobile())

        call.enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@ChooseLogin, Dashboard::class.java)
                    val student = response.body()
                    intent.putExtra("object", student)
                    startActivity(intent)
                    finishAffinity()
                    Log.d("dfgre", student.toString())

                } else {
                    Toast.makeText(this@ChooseLogin, "No User found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Log.d("dfgre", t.localizedMessage)
            }

        })
    }

    private fun getStudentMobile(): String {
        val shared = getSharedPreferences("accfile", Context.MODE_PRIVATE)
        return shared.getString("studentmobile", "").toString()
    }
}