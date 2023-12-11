package com.sro.schoolmanagementapp.Activity.Student.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sro.schoolmanagementapp.Activity.Student.AttendanceActivity
import com.sro.schoolmanagementapp.Activity.Teacher.Syllabus
import com.sro.schoolmanagementapp.Activity.Teacher.TodaysAttendance
import com.sro.schoolmanagementapp.Model.Student
import com.sro.schoolmanagementapp.Model.Teacher
import com.sro.schoolmanagementapp.R
import com.sro.schoolmanagementapp.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home.newInstance] factory method to
 * create an instance of this fragment.
 */
class home : Fragment() {
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
                binding.classSectionTxt.text = "Class: " + student.classs
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
                binding.classSectionTxt.text = "Class: " + teacher.classs
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
                )?.commit()
            }


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
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences?.edit()

        editor?.putString("studentName", studentName)
        editor?.putString("schoolID", schoolID)
        editor?.putString("classs", classs)
        editor?.putString("mobile", mobile)
        editor?.putString("section", section)
        editor?.putString("roll", roll)

        editor?.apply()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}