package com.sro.schoolmanagementapp.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sro.schoolmanagementapp.Activity.Student.AttendanceActivity
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Model.Teacher
import com.sro.schoolmanagementapp.R
import com.sro.schoolmanagementapp.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Home"

        if (requireActivity().getSharedPreferences("accfile", Context.MODE_PRIVATE).getString(
                "acctype",
                ""
            ) == "student"
        ) {
            val student: Student? = activity?.intent?.getSerializableExtra("object") as? Student

            if (student != null) {
                binding.nameTxt.text = "Name: " + student.name
                binding.classSectionTxt.text = "Class: " + student.classs + student.section
                binding.schoolNameTxt.text = "School ID: " + student.schoolID
                binding.studentmobiletxt.text = "Mobile: " + student.mobile

            }



            binding.attendance.setOnClickListener {
                if (activity?.getSharedPreferences("accfile", Context.MODE_PRIVATE)
                        ?.getString("acctype", "") == "teacher"
                ) {


                } else {

                    startActivity(Intent(activity, AttendanceActivity::class.java))
                }

            }

            if (student != null) {

                val studentName = student.name
                val schoolID = student.schoolID
                val classs = student.classs
                val mobile = student.mobile
                val section = student.section
                val roll = student.roll

                saveData(studentName, schoolID, classs, mobile, section, roll)

            } else {

            }
        } else {
            val teacher: Teacher? = activity?.intent?.getSerializableExtra("object") as? Teacher

            if (teacher != null) {
                binding.nameTxt.text = "Name: " + teacher.name
                binding.classSectionTxt.text = "Class Teacher: " + teacher.classs + teacher.section
                binding.schoolNameTxt.text = "School ID: " + teacher.schoolCode
                binding.studentmobiletxt.text = "Mobile: " + teacher.mobile

            }

            binding.attendance.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.fragmentContainer,
                    TodaysAttendance()
                )?.addToBackStack(null)?.commit()
            }


            binding.syllabus.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("filetype", "syllabus")

                val secondFragment = Syllabus()
                secondFragment.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.fragmentContainer,
                    secondFragment  // Use the instance with arguments here
                )?.addToBackStack(null)?.commit()
            }

            binding.timetablel.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("filetype", "timetable")

                val secondFragment = Syllabus()
                secondFragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(
                    R.id.fragmentContainer, secondFragment
                )?.addToBackStack(null)?.commit()
            }
        }

        binding.homeworkl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("filetype", "homework")

            val secondFragment = Homework()
            secondFragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer,
                secondFragment
            )?.addToBackStack(null)?.commit()
        }

        binding.classworkl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("filetype", "classwork")

            val secondFragment = Classwork()
            secondFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer, secondFragment
            )?.addToBackStack(null)?.commit()
        }

        binding.noticel.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("filetype", "notice")
//
            val secondFragment = Notice()
            secondFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer, secondFragment
            )?.addToBackStack(null)?.commit()
        }

        binding.resultl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("filetype", "result")
//
            val secondFragment = Result()
            secondFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer, secondFragment
            )?.addToBackStack(null)?.commit()
        }

        binding.feedbackl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("filetype", "feedback")
//
            val secondFragment = Feedback()
            secondFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer, secondFragment
            )?.addToBackStack(null)?.commit()
        }
        binding.examsl.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("filetype", "exams")
//
            val secondFragment = Exams()
            secondFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer, secondFragment
            )?.addToBackStack(null)?.commit()
        }

        binding.feedbackl.setOnClickListener {
            val bundle = Bundle()
//                bundle.putString("filetype", "timetable")
//
            val secondFragment = Feedback()
//                secondFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer, secondFragment
            )?.addToBackStack(null)?.commit()
        }
        binding.examsl.setOnClickListener {
            val bundle = Bundle()
//                bundle.putString("filetype", "timetable")
//
            val secondFragment = Exams()
//                secondFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragmentContainer, secondFragment
            )?.addToBackStack(null)?.commit()
        }
    }

    private fun saveData(
        studentName: String,
        schoolID: String,
        classs: String,
        mobile: String,
        section: String,
        roll: String
    ) {
        val sharedPreferences = activity?.getSharedPreferences("student", Context.MODE_PRIVATE)

        val editor = sharedPreferences?.edit()

        editor?.putString("studentName", studentName)
        editor?.putString("studentschool", schoolID)
        editor?.putString("studentclass", classs)
        editor?.putString("studentmobile", mobile)
        editor?.putString("studentsection", section)
        editor?.putString("studentroll", roll)

        editor?.apply()

    }

}