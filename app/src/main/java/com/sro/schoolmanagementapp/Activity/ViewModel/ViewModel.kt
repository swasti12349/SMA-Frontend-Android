package com.sro.schoolmanagementapp.Activity.ViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sro.schoolmanagementapp.Activity.Repository.Repository
import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.Model.FileObj
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getClassAttendance(schoolCode: String, studentClass: String) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.getClassAttendanceList(schoolCode, studentClass)
        }
    }

    val classAttendanceList: LiveData<List<Attendance>> get() = repository.classAttendanceLists
    val fileList: LiveData<List<FileObj>> get() = repository.fileLists

    fun uploadFile(
        fileType: String,
        fileName: String,
        context: Context,
        schoolName: String,
        className: String,
        sectionName: String,
        file: Uri
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadFile(fileType, fileName, file, context)
            getFiles(schoolName, className, sectionName, fileType)
        }
    }

    fun getFiles(
        schoolName: String,
        className: String,
        sectionName: String,
        fileType: String
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.getFiles(schoolName, className, sectionName, fileType)


        }
    }


}