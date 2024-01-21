package com.sro.schoolmanagementapp.Activity.Student


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.Model.ResponseObject
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.R
import com.sro.schoolmanagementapp.databinding.ActivityAttendanceBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AttendanceActivity : AppCompatActivity() {
    lateinit var binding: ActivityAttendanceBinding
    lateinit var studentName: String
    lateinit var schoolID: String
    lateinit var classs: String
    lateinit var mobile: String
    lateinit var section: String
    lateinit var yesterday: String
    private val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Attendance"

        if (getAttendance() != null) {
            binding.marktxt.setTextColor(
                ContextCompat.getColor(
                    this@AttendanceActivity,
                    R.color.green
                )
            )
        }

        if (getAttendance() != null) {
            if (getAttendance() == getToday()) {
                binding.marktxt.text = "Marked"
                binding.marktxt.setTextColor(
                    ContextCompat.getColor(
                        this@AttendanceActivity,
                        R.color.green
                    )
                )
            } else {
                binding.marktxt.text = "Not Marked"
                binding.marktxt.setTextColor(
                    ContextCompat.getColor(
                        this@AttendanceActivity,
                        R.color.red
                    )
                )
            }
        }

        binding.attendanceview.setOnClickListener {

            if (binding.marktxt.text != "Marked") {
                binding.pb.visibility = View.VISIBLE
                binding.markattendance.visibility = View.INVISIBLE
                markAttendance()
            } else {
                Toast.makeText(this@AttendanceActivity, "Already marked", Toast.LENGTH_SHORT).show()
            }

        }

        binding.downloadattendance.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val apiService = RetrofitClass().getInstance().getAttendancePdf(mobile)
                    val response = apiService.execute()

                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {

                            saveAttendanceFile(responseBody)

                        } else {
                            Log.d("rsgtrg", "download is null")
                        }
                    } else {
                        Log.d("rsgtrg", "download  is not successfull")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun getToday(): String {
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {

        }

        return currentDate.toString()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // WRITE_EXTERNAL_STORAGE permission denied, handle the situation, e.g., show a message to the user
            }
        }
    }

    private fun saveAttendanceFile(responseBody: ResponseBody) {
        try {
            val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val formattedDateTime = currentDateTime.format(formatter)
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsFolder, "attendance.xlsx")

            val inputString = responseBody.byteStream()
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int


            while (inputString.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)

            }
            outputStream.close()
            inputString.close()

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(file)
            sendBroadcast(mediaScanIntent)
            runOnUiThread {
                Toast.makeText(
                    this@AttendanceActivity,
                    "File saved to Download folder",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("rsgtrg", e.localizedMessage)
        }

    }


    private fun getData() {


        val sharedPreferences =
            this@AttendanceActivity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        studentName = sharedPreferences.getString("studentName", "")!!
        schoolID = sharedPreferences.getString("schoolID", "")!!
        classs = sharedPreferences.getString("classs", "")!!
        mobile = sharedPreferences.getString("mobile", "")!!
        section = sharedPreferences.getString("section", "")!!
        Log.d("dfgr", schoolID)

    }

    private fun markAttendance() {
        val call =
            RetrofitClass().getInstance().markAttendance(Attendance(schoolID, classs, mobile, studentName, "", ""))

        call.enqueue(object : Callback<ResponseObject> {
            override fun onResponse(
                call: Call<ResponseObject>,
                response: Response<ResponseObject>
            ) {
                if (response.body()?.status.toString() == "saved") {
                    binding.markattendance.isClickable = false
                    binding.marktxt.text = "Marked"
                    saveAttendance()
                    binding.marktxt.setTextColor(
                        ContextCompat.getColor(
                            this@AttendanceActivity,
                            R.color.green
                        )
                    )

                    binding.markattendance.visibility = View.VISIBLE
                    binding.pb.visibility = View.INVISIBLE

                } else {
                    Log.d("SDfewfs", "response is null")
                }
            }

            override fun onFailure(call: Call<ResponseObject>, t: Throwable) {
                Log.d("SDfewfs", t.localizedMessage)
            }
        }
        )
    }

    private fun saveAttendance() {
        val shared = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = shared.edit()
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        editor.putString("attendance", currentDate.toString())
        editor.apply()
    }

    private fun getAttendance(): String? {
        val shared = getSharedPreferences("pref", Context.MODE_PRIVATE)
        return shared.getString("attendance", "")


    }
}