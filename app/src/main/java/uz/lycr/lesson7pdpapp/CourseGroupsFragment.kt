package uz.lycr.lesson7pdpapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import uz.lycr.lesson7pdpapp.adapters.ViewPagerAdapter
import uz.lycr.lesson7pdpapp.databinding.FragmentCourseGroupsBinding
import uz.lycr.lesson7pdpapp.db.AppDatabase
import uz.lycr.lesson7pdpapp.entity.Course

class CourseGroupsFragment : Fragment() {

    private var courseId: Int = -1

    private lateinit var mainCourse: Course
    private lateinit var dbHelper: AppDatabase
    private lateinit var binding: FragmentCourseGroupsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("course_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        mainCourse = dbHelper.courseDao().getCourseById(courseId)
        binding = FragmentCourseGroupsBinding.inflate(inflater, container, false)

        binding.tvCourseName.text = mainCourse.name

        val vpAdapter = ViewPagerAdapter(
            childFragmentManager,
            listOf("Ochilgan guruhlar", "Ochilayotgan guruhlar"), courseId
        )

        binding.vp.adapter = vpAdapter

        binding.tableLayout.setupWithViewPager(binding.vp)

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.ivAdd.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("courseId", courseId)
            bundle.putInt("groupId", -1)
            bundle.putBoolean("isEdit", false)
            findNavController().navigate(R.id.addGroupFragment, bundle)
        }

        binding.vp.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 1) {
                    binding.ivAdd.visibility = View.VISIBLE
                } else {
                    binding.ivAdd.visibility = View.INVISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        return binding.root
    }

}