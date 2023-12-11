package com.sro.schoolmanagementapp.Activity.Repository

import ApiService
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.Model.FileObj
import com.sro.schoolmanagementapp.Model.School
import com.sro.schoolmanagementapp.Model.attendanceLB
import com.sro.schoolmanagementapp.Network.RetrofitClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Query
import java.security.PrivateKey

class Repository(private val service: ApiService) {
    val TAG = "repos"

    private val classAttendanceList = MutableLiveData<List<Attendance>>()
    val classAttendanceLists: LiveData<List<Attendance>> get() = classAttendanceList


    private val fileList = MutableLiveData<List<FileObj>>()
    val fileLists:LiveData<List<FileObj>> get() = fileList


    suspend fun getClassAttendanceList(
        schoolCode: String,
        studentClass: String
    ) {
        val response: Response<List<Attendance>> =
            service.getClassAttendance(schoolCode, studentClass)
        try {
            if (response.isSuccessful) {
                classAttendanceList.postValue(response.body())
                Log.d("gdfhe", response.body().toString())
            }
        } catch (e: Exception) {
            Log.d("gdfhe", e.localizedMessage!!)
        }

    }

    suspend fun uploadFile(
        fileType: String,
        fileName: String,
        fileUri: Uri,
        context: Context
    ) {
        val inputStream = context.contentResolver.openInputStream(fileUri)
        val byteArray = inputStream?.readBytes()

        if (byteArray != null) {
            val requestBody = RequestBody.create(MediaType.parse("application/pdf"), byteArray)
            val filePart = MultipartBody.Part.createFormData("file", fileName, requestBody)

            withContext(Dispatchers.Main) {
                try {
                    val response = service.uploadFileToServer(
                        fileType, fileName,
                        context.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                            .getString("teacherschool", "")!!,
                        context.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                            .getString("teacherclass", "")!!,
                        context.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                            .getString("teachersection", "")!!,

                        filePart
                    )

                    if (response.isSuccessful) {
                        Toast.makeText(context, "File uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to upload file. Code: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(context, "Failed to read the file", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getFiles(
        schoolName: String,
        className: String,
        sectionName: String,
        fileType: String
    ) {

        try {
            var response = service.getFiles(schoolName, className, sectionName, fileType)

            fileList.postValue(response.body())

            Log.d(
                TAG, response.body().toString()
            )
        } catch (e: Exception) {
            Log.d(TAG, e.localizedMessage!!)

        }


    }


}
