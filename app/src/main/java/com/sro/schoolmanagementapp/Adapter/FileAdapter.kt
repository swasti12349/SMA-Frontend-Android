package com.sro.schoolmanagementapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sro.schoolmanagementapp.Model.FileObj
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.R
import kotlinx.coroutines.CoroutineScope
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.log

class FileAdapter(
    private val context: Context,
    private val list: List<FileObj>,
    private val fileType: String
) :
    RecyclerView.Adapter<FileAdapter.TeacherViewHolder>() {

    class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTxt = itemView.findViewById<TextView>(R.id.fileName)
        val fileDate = itemView.findViewById<TextView>(R.id.fileDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false)
        return TeacherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun formatDate(date: Date): String {
        val pattern = "dd-MM-yyyy hh:mm a"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val fileObj = list[position]

        holder.fileNameTxt.text = fileObj.fileName
        holder.fileDate.text = formatDate(fileObj.fileDate!!)

        holder.itemView.setOnClickListener {
            downloadFile(fileObj.fileName)
        }
    }

    private fun downloadFile(fileName: String) {

        GlobalScope.launch(Dispatchers.IO) {
            try {

                var response = RetrofitClass().getInstance().downloadFile(
                    context.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                        .getString("teacherschool", "")!!,
                    context.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                        .getString("teacherclass", "")!!,
                    context.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                        .getString("teachersection", "")!!,
                    fileName, fileType
                )

                response.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {

                                CoroutineScope(Dispatchers.IO).launch {
                                    saveFileToDownload(responseBody, fileName)
                                }
                            } else {
                                Log.d("rsgtrg", "download is null")
                            }
                        } else {
                            Log.d("rsgtrge", "downloading error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("rsgtrge", "Network request failed", t)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun saveFileToDownload(responseBody: ResponseBody, fileName: String) {
        try {
            var fileName = fileName
            fileName = "$fileName.pdf"
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
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsFolder, fileName)

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
            context.sendBroadcast(mediaScanIntent)

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    context.applicationContext,
                    "Saved to download",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("rsgtrg", e.localizedMessage)
        }

    }
}
