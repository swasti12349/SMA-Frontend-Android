package com.sro.schoolmanagementapp.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sro.schoolmanagementapp.Activity.Repository.Repository
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModel
import com.sro.schoolmanagementapp.Activity.ViewModel.MainViewModelFactory
import com.sro.schoolmanagementapp.Adapter.FileAdapter
import com.sro.schoolmanagementapp.Dialog.CustomDialog
import com.sro.schoolmanagementapp.Network.RetrofitClass
import com.sro.schoolmanagementapp.databinding.FragmentHomeworkBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Homework.newInstance] factory method to
 * create an instance of this fragment.
 */
class Homework : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHomeworkBinding
    private lateinit var fileType: String
    lateinit var viewModel: MainViewModel
    lateinit var repository: Repository
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
        binding = FragmentHomeworkBinding.inflate(layoutInflater, container, false)
        binding.shimmerViewContainer.startShimmerAnimation()


        Handler().postDelayed({
            binding.shimmerViewContainer.visibility = View.GONE
        }, 3000)

        fileType = "homework"

        repository = Repository(RetrofitClass().getInstance())
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        viewModel.fileList.observe(viewLifecycleOwner, Observer { fileList ->

            if (fileList != null) {
                val layoutManager = LinearLayoutManager(binding.root.context)
                binding.homeworkrv.layoutManager = layoutManager
                var adapter = FileAdapter(requireContext(), fileList, fileType)
                binding.homeworkrv.adapter = adapter
                binding.shimmerViewContainer.stopShimmerAnimation()
                binding.shimmerViewContainer.visibility = View.GONE
            }
        })




        binding.addhomewrok.setOnClickListener {
            pickImageFile()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchHomework()
    }

    private fun fetchHomework() {
        CoroutineScope(Dispatchers.IO).launch {
            if (requireActivity().getSharedPreferences("accfile", Context.MODE_PRIVATE).getString(
                    "acctype",
                    ""
                ) == "student"
            ) {

                val school = context?.getSharedPreferences("student", Context.MODE_PRIVATE)
                    ?.getString("studentschool", "").toString()
                val className = context?.getSharedPreferences("student", Context.MODE_PRIVATE)
                    ?.getString("studentclass", "").toString()
                val section = context?.getSharedPreferences("student", Context.MODE_PRIVATE)
                    ?.getString("studentsection", "").toString()

                viewModel.getFiles(

                    mapOf<String, String>(

                        "a" to school,

                        "b" to className,

                        "c" to section,

                        "d" to fileType!!
                    )
                )

            } else {
                val school = context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teacherschool", "").toString()
                val className = context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teacherclass", "").toString()
                val section = context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                    ?.getString("teachersection", "").toString()

                viewModel.getFiles(

                    mapOf<String, String>(

                        "a" to school,

                        "b" to className,

                        "c" to section,

                        "d" to fileType!!
                    )
                )
            }
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            CustomDialog.show(binding.root.context, object : CustomDialog.CustomDialogInterface {
                override fun onPositiveButtonClick(dialog: Dialog?, text: String?, subName: String?) {
                    addHomework(
                        context!!,
                        context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                            ?.getString("teacherschool", "").toString(),
                        context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                            ?.getString("teacherclass", "").toString(),
                        context?.getSharedPreferences("teacher", Context.MODE_PRIVATE)
                            ?.getString("teachersection", "").toString(),
                          subName!!,
                        fileType,
                        text!!,
                        data.data!!
                    )

                }

                override fun onCancelButtonClick(dialog: Dialog?) {

                    dialog?.dismiss()
                }


            }, "Homework is selected", true)
        }
    }

    fun addHomework(
        context: Context,
        schoolName: String,
        className: String,
        sectionName: String,
        subjectName: String,
        fileType: String,
        fileName: String,
        fileUri: Uri
    ) {
        viewModel.addHomework(
            context,
            schoolName,
            className,
            sectionName,
            subjectName,
            fileType,
            fileName,
            fileUri
        )
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Homework().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}