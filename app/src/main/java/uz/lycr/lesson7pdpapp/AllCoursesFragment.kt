package uz.lycr.lesson7pdpapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lycr.lesson7pdpapp.entity.Course
import uz.lycr.lesson7pdpapp.db.AppDatabase
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.adapters.AllCoursesAdapter
import uz.lycr.lesson7pdpapp.databinding.DialogAddCourseBinding
import uz.lycr.lesson7pdpapp.databinding.FragmentAllCoursesBinding

class AllCoursesFragment : Fragment() {

    private lateinit var dbHelper: AppDatabase
    private lateinit var binding: FragmentAllCoursesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        binding = FragmentAllCoursesBinding.inflate(inflater, container, false)

        val list = ArrayList(dbHelper.courseDao().getAllCourses())
        val adapter = AllCoursesAdapter(
            list,
            object : AllCoursesAdapter.MyOnClickListenerCourses {
                override fun onItemClickListener(courseId: Int?) {
                    val bundle = Bundle()
                    bundle.putInt("courseId", courseId!!)
                    findNavController().navigate(R.id.aboutCourseFragment, bundle)
                }
            })

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivAdd.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogBinding = DialogAddCourseBinding.inflate(layoutInflater)
            builder.setView(dialogBinding.root)
            val dialog = builder.create()

            dialogBinding.tvClose.setOnClickListener { dialog.dismiss() }
            dialogBinding.tvAdd.setOnClickListener {
                val name = dialogBinding.etName.text.toString()
                val about = dialogBinding.etAbout.text.toString()

                if (name.isNotEmpty() && about.isNotEmpty()) {
                    var isHave = false
                    for (course in dbHelper.courseDao().getAllCourses()) {
                        if (course.name.equals(name, ignoreCase = true)) {
                            isHave = true
                            break
                        }
                    }

                    if (isHave) {
                        Toast.makeText(requireContext(), "Bu kurs allaqachon qo`shilgan", Toast.LENGTH_SHORT).show()
                    } else {
                        val course = Course(name = name, about = about)
                        dbHelper.courseDao().addCourse(course)

                        course.id = dbHelper.courseDao().getIdByCourse(name, about)
                        list.add(course)

                        adapter.notifyItemInserted(list.size)
                        dialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, "Ma'lumotlarni to`liq kiriting", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            dialog.show()
        }

        binding.rv.adapter = adapter

        return binding.root
    }

}