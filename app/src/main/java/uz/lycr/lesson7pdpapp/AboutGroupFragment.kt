package uz.lycr.lesson7pdpapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lycr.lesson7pdpapp.db.AppDatabase
import uz.lycr.lesson7pdpapp.entity.Student
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.adapters.StudentsAdapter
import uz.lycr.lesson7pdpapp.databinding.FragmentAboutGroupBinding

class AboutGroupFragment : Fragment() {

    private var groupId: Int = -1

    private lateinit var dbHelper: AppDatabase
    private lateinit var adapter: StudentsAdapter
    private lateinit var binding: FragmentAboutGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupId = it.getInt("group_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        val currentGroup = dbHelper.groupDao().getGroupById(groupId)
        binding = FragmentAboutGroupBinding.inflate(layoutInflater)

        binding.tvTime.append(currentGroup.time)
        binding.tvTopName.text = currentGroup.name
        binding.tvGroupName.text = currentGroup.name
        binding.tvStudentsCount.append("${currentGroup.studentsCount} ta")

        val list = ArrayList(dbHelper.studentDao().getAllStudents()
            .filter { it.courseId == currentGroup.courseId && it.groupId == groupId })

        adapter = StudentsAdapter(list, object : StudentsAdapter.MyOnClickListenerStudents {
            override fun onEditBtnClickListener(student: Student, position: Int) {
                val bundle = Bundle()
                bundle.putBoolean("is_edit", true)
                bundle.putInt("student_id", student.id!!)
                bundle.putInt("course_id", student.courseId)
                findNavController().navigate(R.id.addStudentFragment, bundle)
            }

            override fun onDeleteBtnClickListener(student: Student, position: Int) {
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Talabani o`chirish")
                builder.setMessage("${student.name} ${student.surname}ni o`chirishga ishonchingiz komilmi?")

                builder.setPositiveButton("Ha") { dialog, _ ->
                    list.removeAt(position)
                    dbHelper.studentDao().deleteStudent(student)

                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, list.size)

                    currentGroup.studentsCount--
                    dbHelper.groupDao().updateGroup(currentGroup)
                    binding.tvStudentsCount.text =
                        "O`quvchilar soni ${currentGroup.studentsCount} ta"

                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Talaba muvaffaqiyatli o`chirildi !!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                builder.setNegativeButton("Yo`q") { dialog, _ -> dialog.dismiss() }

                builder.show()
            }
        })

        binding.rv.adapter = adapter

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.ivAdd.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("course_id", currentGroup.courseId)
            findNavController().navigate(R.id.addStudentFragment, bundle)
        }

        binding.btnStartLesson.setOnClickListener {
            if (currentGroup.studentsCount == 0) {
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Ma'lumot")
                builder.setMessage("Darsni boshlash uchun guruhda hech bo`lmaganda bitta talaba bo`lishi kerak")
                builder.setPositiveButton("Yopish") { dialog, _ -> dialog.dismiss() }

                builder.show()
            } else {
                if (currentGroup.isFull == 0) {
                    currentGroup.isFull = 1
                    dbHelper.groupDao().updateGroup(currentGroup)
                }

                findNavController().popBackStack()
            }
        }

        return binding.root
    }

}