package uz.lycr.lesson7pdpapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.adapters.GroupsAdapter
import uz.lycr.lesson7pdpapp.databinding.FragmentViewPagerGroupBinding
import uz.lycr.lesson7pdpapp.db.AppDatabase
import uz.lycr.lesson7pdpapp.entity.Group

class ViewPagerGroupFragment : Fragment() {

    private var position: Int = -1
    private var courseId: Int = -1

    private lateinit var dbHelper: AppDatabase
    private lateinit var adapter: GroupsAdapter
    private lateinit var binding: FragmentViewPagerGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("vpPosition")
            courseId = it.getInt("courseId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        binding = FragmentViewPagerGroupBinding.inflate(inflater, container, false)

        val list: ArrayList<Group> = if (position == 0) {
            ArrayList(
                dbHelper.groupDao().getAllGroups().filter { (it.courseId == courseId) && (it.isFull == 1) })
        } else {
            ArrayList(
                dbHelper.groupDao().getAllGroups().filter { (it.courseId == courseId) && (it.isFull == 0) })
        }

        adapter = GroupsAdapter(list, object : GroupsAdapter.MyOnClickListenerGroups {
            override fun onShowBtnClickListener(courseId: Int, groupId: Int) {
                val bundle = Bundle()
                bundle.putInt("group_id", groupId)
                findNavController().navigate(R.id.aboutGroupFragment, bundle)
            }

            override fun onEditBtnClickListener(group: Group, position: Int) {
                val bundle = Bundle()
                bundle.putInt("courseId", courseId)
                bundle.putInt("groupId", group.id!!)
                bundle.putBoolean("isEdit", true)
                findNavController().navigate(R.id.addGroupFragment, bundle)
            }

            override fun onDeleteBtnClickListener(group: Group, position: Int) {
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Guruhni o`chirish")
                builder.setMessage("Guruhni o`chirishga ishonchingiz komilmi?\n\nOgohlantirish: Guruhni o`chirsangiz guruhning barcha ma'lumotlari hamda guruhdagi barcha talabalar ham o`chib ketadi!")

                builder.setPositiveButton("Ha") { dialog, _ ->
                    for (student in dbHelper.studentDao().getAllStudents()
                        .filter { (it.courseId == courseId) && (it.groupId == group.id) }) {
                        dbHelper.studentDao().deleteStudent(student)
                    }

                    dbHelper.groupDao().deleteGroup(group)

                    list.removeAt(position)
                    adapter.notifyItemRemoved(position)

                    dialog.dismiss()
                }

                builder.setNegativeButton("Yo`q") { dialog, _ -> dialog.dismiss() }

                builder.show()
            }
        })

        binding.rv.adapter = adapter

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, courseId: Int) =
            ViewPagerGroupFragment().apply {
                arguments = Bundle().apply {
                    putInt("vpPosition", position)
                    putInt("courseId", courseId)
                }
            }
    }
}