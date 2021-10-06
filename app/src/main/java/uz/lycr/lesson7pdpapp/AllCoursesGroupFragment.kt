package uz.lycr.lesson7pdpapp

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lycr.lesson7pdpapp.db.AppDatabase
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.adapters.AllCoursesAdapter
import uz.lycr.lesson7pdpapp.databinding.FragmentAllCoursesGroupBinding

class AllCoursesGroupFragment : Fragment() {

    private lateinit var dbHelper: AppDatabase
    private lateinit var binding: FragmentAllCoursesGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        binding = FragmentAllCoursesGroupBinding.inflate(inflater, container, false)

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.rv.adapter = AllCoursesAdapter(dbHelper.courseDao().getAllCourses(), object : AllCoursesAdapter.MyOnClickListenerCourses{
            override fun onItemClickListener(courseId: Int?) {
                val bundle = Bundle()
                bundle.putInt("course_id", courseId!!)
                findNavController().navigate(R.id.courseGroupsFragment, bundle)
            }
        })

        return binding.root
    }

}