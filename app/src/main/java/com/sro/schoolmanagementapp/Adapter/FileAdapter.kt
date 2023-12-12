package com.sro.schoolmanagementapp.Adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
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
                                    saveFileToDownloadandOpeninPDFViewer(responseBody, fileName)
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

    private fun saveFileToDownloadandOpeninPDFViewer(
        responseBody: ResponseBody,
        fileName: String
    ) {
        try {
            // Ensure the file name ends with ".pdf"
            var fileName = fileName
            fileName = "$fileName.pdf"

            // Define the directory for downloads
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            // Create a File object for the PDF
            val file = File(downloadsFolder, fileName)

            // Create an intent to view the PDF file
            val pdfViewIntent = Intent(Intent.ACTION_VIEW)

            // Get a content URI for the file using FileProvider
            val fileUri = FileProvider.getUriForFile(
                context,
                "${context.applicationContext.packageName}.provider",
                file
            )

            // Set the data type and content URI for the intent
            pdfViewIntent.setDataAndType(fileUri, "application/pdf")

            // Add necessary flags to the intent
            pdfViewIntent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_ACTIVITY_NO_HISTORY
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
            )

            // Read the PDF content from the response body and write it to the file
            val inputString = responseBody.byteStream()
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int

            while (inputString.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            // Close the streams
            outputStream.close()
            inputString.close()

            // Create an intent to trigger media scanning for the newly saved file
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = fileUri

            // Broadcast the media scan intent to update the MediaStore
            context.sendBroadcast(mediaScanIntent)

            // Launch the PDF viewer intent
            CoroutineScope(Dispatchers.Main).launch {
                // Show a toast indicating that the file has been saved
                Toast.makeText(
                    context.applicationContext,
                    "Saved to download",
                    Toast.LENGTH_SHORT
                ).show()

                // Create a chooser for the PDF viewer intent and start the activity
                val intent = Intent.createChooser(pdfViewIntent, "Open File")
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                     Log.d("dsfsdfs", e.localizedMessage)
                }
            }

        } catch (e: IOException) {
             e.printStackTrace()
            Log.d("rsgtrg", e.localizedMessage)
        }
    }

}
