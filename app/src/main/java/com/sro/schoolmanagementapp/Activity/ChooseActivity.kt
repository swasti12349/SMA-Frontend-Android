package com.sro.schoolmanagementapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sro.schoolmanagementapp.Activity.Student.StudentSignUp
import com.sro.schoolmanagementapp.Activity.Teacher.TeacherSignUp
import com.sro.schoolmanagementapp.databinding.ActivityChooseBinding

class ChooseActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityChooseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityChooseBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.student.setOnClickListener {
            startActivity(Intent(this@ChooseActivity, StudentSignUp::class.java))
        }

        viewBinding.teacher.setOnClickListener {
            startActivity(Intent(this@ChooseActivity, TeacherSignUp::class.java))
        }

    }
}