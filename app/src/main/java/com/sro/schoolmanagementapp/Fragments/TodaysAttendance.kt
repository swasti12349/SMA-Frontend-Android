package com.sro.schoolmanagementapp.Fragments

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sro.schoolmanagementapp.Activity.Repository.Repository
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModel
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModelFactory
import com.sro.schoolmanagementapp.Adapter.attendanceLeaderboardAdapter
import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.FragmentTeacherAttendanceBinding
import java.util.Collections
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class TodaysAttendance : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentTeacherAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherAttendanceBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Today's Attendance"

        val repository = Repository(RetrofitClass().getInstance())
        var mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.getClassAttendance(
            activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                ?.getString("schoolID", "")!!,
            activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)!!
                .getString("classs", "")!!
        )

        mainViewModel.classAttendanceList.observe(viewLifecycleOwner, Observer { attendanceList ->
            Log.d("gdfh", attendanceList.toString())

            val layoutManager = LinearLayoutManager(binding.root.context)
            binding.teacherattendancerv.layoutManager = layoutManager

            Collections.sort(attendanceList, Comparator.comparing(Attendance::studentRoll))

            binding.teacherattendancerv.addItemDecoration(
                DividerItemDecoration(
                    binding.teacherattendancerv.context,
                    DividerItemDecoration.VERTICAL
                )
            )

            val adapter = attendanceLeaderboardAdapter(
                binding.root.context,
                "classAttendance",
                attendanceList
            )
            binding.teacherattendancerv.adapter = adapter
        })


        Handler().postDelayed({
            binding.svc.visibility = View.GONE
        }, 3000)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodaysAttendance().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}