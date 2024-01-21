package com.sro.schoolmanagementapp.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.sro.schoolmanagementapp.Activity.DashBoard.Dashboard
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Model.Teacher
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.ActivityChooseLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
            if (getStudentMobile().length > 0) {
                CoroutineScope(Dispatchers.IO).launch {
                    loginStudent()

                }

            } else {
                val intent = Intent(this@ChooseLogin, Login::class.java)
                intent.putExtra("type", "student");
                startActivity(intent)

            }
        }

        binding.teacher.setOnClickListener {

            if (getTeacherMobile().length > 1) {
                CoroutineScope(Dispatchers.IO).launch {
                    loginTeacher()

                }
            } else {
                val intent = Intent(this@ChooseLogin, Login::class.java)
                intent.putExtra("type", "teacher");
                startActivity(intent)
            }
        }

    }

    private fun getTeacherMobile(): String {
        val shared = getSharedPreferences("teacher", Context.MODE_PRIVATE)
        return shared.getString("teachermobile", "").toString()

    }

    private fun loginTeacher() {
        val call = RetrofitClass().getInstance()
            .loginTeacher(getTeacherMobile())

        call.enqueue(object : Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@ChooseLogin, Dashboard::class.java)
                    val teacher = response.body()
                    intent.putExtra("object", teacher)
                    startActivity(intent)
                    finishAffinity()
                    Log.d("Sgfdg", teacher.toString())
                } else {
                    Toast.makeText(this@ChooseLogin, "No User Found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {
                Log.d("sgfdgdfg", t.localizedMessage)
            }

        })
    }

    fun loginStudent() {
        val call = RetrofitClass().getInstance()
            .loginStudent(getStudentMobile())

        call.enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.code() == 200) {
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
        val shared = getSharedPreferences("student", Context.MODE_PRIVATE)
        return shared.getString("studentmobile", "").toString()
    }
}