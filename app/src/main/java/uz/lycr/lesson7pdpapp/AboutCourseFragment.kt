package uz.lycr.lesson7pdpapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.db.AppDatabase
import uz.lycr.lesson7pdpapp.databinding.FragmentAboutCourseBinding

class AboutCourseFragment : Fragment() {

    private lateinit var dbHelper: AppDatabase
    private lateinit var binding: FragmentAboutCourseBinding

    private var courseId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        val course = dbHelper.courseDao().getCourseById(courseId)
        binding = FragmentAboutCourseBinding.inflate(inflater, container, false)

        binding.tvAbout.text = course.about
        binding.tvCourseName.text = course.name

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnAddStudent.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("course_id", courseId)
            bundle.putInt("student_id", -1)
            bundle.putBoolean("is_edit", false)
            findNavController().navigate(R.id.addStudentFragment, bundle)
        }

        return binding.root
    }

}