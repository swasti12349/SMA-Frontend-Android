package com.sro.schoolmanagementapp.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sro.schoolmanagementapp.Adapter.attendanceLeaderboardAdapter
import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.R
import com.sro.schoolmanagementapp.databinding.FragmentAttendanceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AttendanceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var attendanceLBRV: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: attendanceLeaderboardAdapter
    private lateinit var alblist: List<Attendance>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        alblist = ArrayList()
        layoutManager = LinearLayoutManager(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("student", Context.MODE_PRIVATE)!!
        var view = inflater.inflate(R.layout.fragment_attendance, container, false)

        attendanceLBRV = view.findViewById(R.id.alrv)
        attendanceLBRV.layoutManager = layoutManager

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Attendance Leaderboard"



        CoroutineScope(Dispatchers.IO).launch {
            networkCall(
                sharedPreferences?.getString("studentschool", "")!!,
                sharedPreferences.getString("studentclass", "")!!
            )
        }

        return view
    }

    private fun networkCall(schoolCode: String, studentClass: String) {
        val attendanceApi =
            RetrofitClass().getInstance().getAttendanceLeaderboardList(schoolCode, studentClass)

        var list: List<Attendance>

        attendanceApi.enqueue(object : Callback<List<Attendance>> {

            override fun onResponse(
                call: Call<List<Attendance>>,
                response: Response<List<Attendance>>
            ) {
                if (response.isSuccessful) {
                    list = ArrayList(response.body() ?: emptyList())

                    for (i in list) {
                        val item = Attendance(
                            sharedPreferences?.getString("studentschool", "")!!,
                            sharedPreferences?.getString("studentclass", "")!!,
                            i.studentMobile,
                            i.studentName,
                            i.studentDates,
                            i.studentRoll
                        )
                        alblist = alblist + item
                    }

                    adapter = attendanceLeaderboardAdapter(
                        activity!!.applicationContext,
                        "attendanceLeaderboard",
                        alblist
                    )
                    binding.alrv.addItemDecoration(
                        DividerItemDecoration(
                            binding.alrv.context,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    attendanceLBRV.adapter = adapter
                    Log.d("hih", alblist.toString())
                } else {
                    Log.d("Dsfsd", "response is not successful")
                }
            }

            override fun onFailure(call: Call<List<Attendance>>, t: Throwable) {
            }
        })
    }


}