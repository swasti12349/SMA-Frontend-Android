package com.sro.schoolmanagementapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sro.schoolmanagementapp.Activity.DashBoard.Dashboard
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Model.Teacher
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    lateinit var viewBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.buttonSubmit.setOnClickListener {

            if (intent.getStringExtra("type").toString() == "student") {
                loginStudent()
            }

            if (intent.getStringExtra("type").toString() == "teacher") {
                loginTeacher()
            }
        }


    }

    fun loginStudent() {
        val call = RetrofitClass().getInstance()
            .loginStudent(viewBinding.editTextMobileNumber.text.toString())

        call.enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@Login, Dashboard::class.java)
                    val student = response.body()
                    intent.putExtra("object", student)
                    saveStudent(
                        student?.name!!,
                        student.classs,
                        student.mobile,
                        student.schoolID,
                        student.section,
                        student.roll
                    )
                    saveAccType("student")
                    startActivity(intent)
                    finishAffinity()
                    Log.d("dfgre", student.toString())

                } else {
                    Toast.makeText(this@Login, "No User found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Log.d("dfgre", t.localizedMessage)
            }

        })
    }

    private fun saveStudent(
        name: String,
        classs: String,
        mobile: String,
        schoolID: String,
        section: String,
        roll: String
    ) {
        val sharedPreferences = getSharedPreferences("student", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("studentmobile", mobile)
        editor.putString("studentname", name)
        editor.putString("studentclass", classs)
        editor.putString("studentschool", schoolID)
        editor.putString("studentroll", roll)
        editor.putString("studentsection", section)
        editor.apply()
    }


    private fun loginTeacher() {
        val call = RetrofitClass().getInstance()
            .loginTeacher(viewBinding.editTextMobileNumber.text.toString())

        call.enqueue(object : Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@Login, Dashboard::class.java)
                    val teacher = response.body()
                    intent.putExtra("object", teacher)
                    saveAccType("teacher")
                    if (teacher != null) {
                        saveTeacher(
                            teacher.name,
                            teacher.classs,
                            teacher.mobile,
                            teacher.schoolCode,
                            teacher.gender,
                            teacher.section,
                            teacher.subject

                        )
                    }
                    startActivity(intent)
                    finishAffinity()
                    Log.d("Sgfdg", teacher.toString())
                } else {
                    Toast.makeText(this@Login, "No User Found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {

            }

        })
    }

    private fun saveTeacher(
        name: String,
        classs: String,
        mobile: String,
        schoolCode: String,
        gender: String,
        section: String,
        subject: String
    ) {
        val sharedPreferences = getSharedPreferences("teacher", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("teachermobile", mobile)
        editor.putString("teachername", name)
        editor.putString("teacherclass", classs)
        editor.putString("teacherschool", schoolCode)
        editor.putString("teachergender", gender)
        editor.putString("teachersection", section)
        editor.putString("teachersubject", subject)
        editor.apply()
    }

    private fun saveAccType(s: String) {
        val sharedPreferences = getSharedPreferences("accfile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("acctype", s)
        editor.apply()
    }
}