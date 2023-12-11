package com.sro.schoolmanagementapp.Activity.Teacher

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sro.schoolmanagementapp.Activity.Repository.Repository
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModel
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModelFactory
import com.sro.schoolmanagementapp.Adapter.FileAdapter
import com.sro.schoolmanagementapp.Adapter.attendanceLeaderboardAdapter
import com.sro.schoolmanagementapp.Dialog.CustomDialog
import com.sro.schoolmanagementapp.Model.Attendance
import com.sro.schoolmanagementapp.Model.FileObj
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.FragmentSyllabusBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Syllabus : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSyllabusBinding
    private lateinit var layoutManager: LayoutManager
    lateinit var fileType: String
    lateinit var viewModel: MainViewModel
    lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutManager = LinearLayoutManager(activity)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSyllabusBinding.inflate(layoutInflater, container, false)

        fileType = arguments?.getString("filetype", "")!!

        repository = Repository(RetrofitClass().getInstance())
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        fetchSyllabus()

        viewModel.fileList.observe(viewLifecycleOwner, Observer { fileList ->

            if (fileList != null) {
                val layoutManager = LinearLayoutManager(binding.root.context)
                binding.syllabusrv.layoutManager = layoutManager
                var adapter = FileAdapter(requireContext(), fileList, fileType)
                binding.syllabusrv.adapter = adapter
            }
        })

        binding.addpdf.setOnClickListener {
            pickPdfFile()
        }

        return binding.root
    }


    private fun fetchSyllabus() {
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
        viewModel.fileList.observe(viewLifecycleOwner, Observer { fileList ->

            if (fileList != null) {
                val layoutManager = LinearLayoutManager(binding.root.context)
                binding.syllabusrv.layoutManager = layoutManager
                var adapter = FileAdapter(requireContext(), fileList, fileType)
                binding.syllabusrv.adapter = adapter
            }
        })
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
        fetchSyllabus()
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
            mainViewModel.uploadFile(fileType, fileName, context,  context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                ?.getString("teacherschool", "").toString(),
                context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teacherclass", "").toString(),
                context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teachersection", "").toString(), file)

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Syllabus.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Syllabus().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}