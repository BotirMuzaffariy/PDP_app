package uz.lycr.lesson7pdpapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.databinding.FragmentAddGroupBinding
import uz.lycr.lesson7pdpapp.db.AppDatabase
import uz.lycr.lesson7pdpapp.entity.Group

class AddGroupFragment : Fragment() {

    private var courseId = -1
    private var groupId = -1
    private var isEdit = false

    private lateinit var binding: FragmentAddGroupBinding
    private lateinit var dbHelper: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
            groupId = it.getInt("groupId")
            isEdit = it.getBoolean("isEdit")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        binding = FragmentAddGroupBinding.inflate(inflater, container, false)

        if (isEdit) {
            val group = dbHelper.groupDao().getGroupById(groupId)

            binding.btnSave.text = "O`zgartirish"
            binding.tvTop.text = "Guruhni tahrirlash"

            binding.etName.setText(group.name)
            binding.actTime.setText(group.time)
            binding.actMentor.setText("${dbHelper.mentorDao().getMentorById(group.mentorId)!!.name} ${dbHelper.mentorDao().getMentorById(group.mentorId)!!.surname}")
        }

        val mentorsNames = ArrayList<String>()
        val mentorsIdes = ArrayList<Int>()
        for (mentor in dbHelper.mentorDao().getAllMentors().filter { it.courseId == courseId }) {
            mentorsNames.add("${mentor.name} ${mentor.surname}")
            mentorsIdes.add(mentor.id!!)
        }

        binding.actMentor.setOnClickListener {
            if (mentorsNames.size != 0) binding.actMentor.showDropDown() else Toast.makeText(
                requireContext(),
                "${dbHelper.courseDao().getCourseById(courseId).name} kursida hali hech qanday mentorlar yo`q",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.actTime.setOnClickListener { binding.actTime.showDropDown() }

        binding.actMentor.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                mentorsNames
            )
        )

        binding.actTime.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                listOf("16:30 - 18:30", "19:00 - 21:00")
            )
        )

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val mentor = binding.actMentor.text.toString()
            val time = binding.actTime.text.toString()

            if (name.isNotEmpty() && mentor.isNotEmpty() && time.isNotEmpty()) {
                val mentorId = mentorsIdes[mentorsNames.indexOf(mentor)]

                var isHave = false
                for (group in dbHelper.groupDao().getAllGroups()) {
                    if (group.name.equals(
                            name,
                            ignoreCase = true
                        ) && group.mentorId == mentorId && group.time.equals(
                            time,
                            true
                        ) && group.courseId == courseId
                    ) {
                        isHave = true
                        break
                    }
                }

                if (isHave) {
                    Toast.makeText(
                        requireContext(),
                        "Bu guruh allaqachon qo`shilgan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (isEdit) {
                        val newGroup = dbHelper.groupDao().getGroupById(groupId)

                        newGroup.mentorId = mentorId
                        newGroup.time = time
                        newGroup.name = name

                        dbHelper.groupDao().updateGroup(newGroup)

                        Toast.makeText(
                            requireContext(),
                            "Guruh muvaffaqiyatli tahrirlandi !!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        dbHelper.groupDao().addGroup(
                            Group(
                                name = name,
                                time = time,
                                courseId = courseId,
                                mentorId = mentorId
                            )
                        )
                        Toast.makeText(
                            requireContext(),
                            "Guruh muvaffaqiyatli qo`shildi !!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    findNavController().popBackStack()
                }
            } else {
                Toast.makeText(context, "Ma'lumotlarni to`liq kiriting", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        return binding.root
    }

}