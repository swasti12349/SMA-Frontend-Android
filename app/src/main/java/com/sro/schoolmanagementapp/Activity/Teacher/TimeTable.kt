package com.sro.schoolmanagementapp.Activity.Teacher

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sro.schoolmanagementapp.Activity.Repository.Repository
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModel
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModelFactory
import com.sro.schoolmanagementapp.Dialog.CustomDialog
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.R
import com.sro.schoolmanagementapp.databinding.FragmentTimeTableBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TimeTable : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentTimeTableBinding
    private lateinit var repository: Repository
    private lateinit var viewModel: MainViewModel
    lateinit var fileType: String

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
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Time Table"

        binding = FragmentTimeTableBinding.inflate(inflater, container, false)
        fileType = arguments?.getString("filetype", "")!!

        repository = Repository(RetrofitClass().getInstance())
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        binding.addpdf.setOnClickListener {
            pickPdfFile()
        }
        return binding.root
    }

    private fun fetchTimeTable() {
        CoroutineScope(Dispatchers.IO).launch {
            if (requireActivity().getSharedPreferences("accfile", Context.MODE_PRIVATE).getString(
                    "acctype",
                    ""
                ) == "student"
            ) {
                viewModel.getFiles(
                    context?.getSharedPreferences("student", Context.MODE_PRIVATE)
                        ?.getString("studentschool", "").toString(),
                    context?.getSharedPreferences("student", Context.MODE_PRIVATE)
                        ?.getString("studentclass", "").toString(),
                    context?.getSharedPreferences("student", Context.MODE_PRIVATE)
                        ?.getString("studentsection", "").toString(),

                    fileType!!

                )

            } else {
                viewModel.getFiles(
                    context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                        ?.getString("teacherschool", "").toString(),
                    context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                        ?.getString("teacherclass", "").toString(),
                    context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                        ?.getString("teachersection", "").toString(),

                    fileType!!

                )

            }
        }

    }

    private fun pickPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(
            intent,
            100
        )
    }

    override fun onResume() {
        super.onResume()
        fetchTimeTable()
        Log.d("fdsds", "onresume")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            CustomDialog.show(binding.root.context, object : CustomDialog.CustomDialogInterface {
                override fun onPositiveButtonClick(dialog: Dialog?, text: String?) {
                    uploadFile(
                        fileType,
                        text!!,
                        context!!,
                        data.data!!
                    )


                }

                override fun onCancelButtonClick(dialog: Dialog?) {

                    dialog?.dismiss()
                }


            }, "Upload Syllabus")
        }
    }

    private fun uploadFile(fileType: String, fileName: String, context: Context, file: Uri) {
        val repository = Repository(RetrofitClass().getInstance())
        var mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            mainViewModel.uploadFile(
                fileType,
                fileName,
                context,
                context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teacherschool", "").toString(),
                context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teacherclass", "").toString(),
                context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teachersection", "").toString(),
                file
            )

        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimeTable().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}