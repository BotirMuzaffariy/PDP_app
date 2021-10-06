package uz.lycr.lesson7pdpapp

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.databinding.FragmentAddStudentBinding
import uz.lycr.lesson7pdpapp.db.AppDatabase
import uz.lycr.lesson7pdpapp.entity.Student
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddStudentFragment : Fragment() {

    private var courseId: Int? = null
    private var studentId: Int = -1
    private var isEdit: Boolean = false

    private lateinit var binding: FragmentAddStudentBinding
    private lateinit var dbHelper: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("course_id")
            studentId = it.getInt("student_id")
            isEdit = it.getBoolean("is_edit")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)

        if (isEdit) {
            val student = dbHelper.studentDao().getStudentById(studentId)
            val mentor = dbHelper.mentorDao().getMentorById(student.mentorId)

            binding.btnSave.text = "O`zgartirish"
            binding.tvTop.text = "Talabani tahrirlash"

            binding.etName.setText(student.name)
            binding.actTime.setText(student.groupTime)
            binding.etSurname.setText(student.surname)
            binding.actDays.setText(student.lessonsDays)
            binding.etFatherName.setText(student.fatherName)
            binding.etRegisterDate.setText(student.registerDate)
            binding.actMentor.setText("${mentor!!.name} ${mentor.surname}")
            binding.actGroup.setText(dbHelper.groupDao().getGroupById(student.groupId).name)
        } else binding.etRegisterDate.hint = SimpleDateFormat("dd/MM/yyyy").format(Date())

        binding.etRegisterDate.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext())
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                binding.etRegisterDate.setText(date)
            }
            datePicker.show()
        }

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        val groupsNames = ArrayList<String>()
        val mentorsNames = ArrayList<String>()

        val groupsIdes = ArrayList<Int>()
        val mentorsIdes = ArrayList<Int>()

        for (group in dbHelper.groupDao().getAllGroups().filter { it.courseId == courseId }) {
            groupsNames.add(group.name)
            groupsIdes.add(group.id!!)
        }
        for (mentor in dbHelper.mentorDao().getAllMentors().filter { it.courseId == courseId }) {
            mentorsNames.add("${mentor.name} ${mentor.surname}")
            mentorsIdes.add(mentor.id!!)
        }

        binding.actDays.setOnClickListener { binding.actDays.showDropDown() }
        binding.actTime.setOnClickListener { binding.actTime.showDropDown() }
        binding.actGroup.setOnClickListener {
            if (groupsNames.size != 0) binding.actGroup.showDropDown() else Toast.makeText(
                requireContext(),
                "Bu kursda hali hech qanday guruhlar yo`q",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.actMentor.setOnClickListener {
            if (mentorsNames.size != 0) binding.actMentor.showDropDown() else Toast.makeText(
                requireContext(),
                "Bu kursda hali hech qanday mentorlar yo`q",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.actGroup.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                groupsNames
            )
        )
        binding.actMentor.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                mentorsNames
            )
        )
        binding.actDays.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                listOf("Toq kunlar", "Juft kunlar")
            )
        )
        binding.actTime.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                listOf("16:30 - 18:30", "19:00 - 21:00")
            )
        )

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val days = binding.actDays.text.toString()
            val time = binding.actTime.text.toString()
            val group = binding.actGroup.text.toString()
            val mentor = binding.actMentor.text.toString()
            val surname = binding.etSurname.text.toString()
            val fatherName = binding.etFatherName.text.toString()
            var registerDate = binding.etRegisterDate.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && fatherName.isNotEmpty() && mentor.isNotEmpty() && days.isNotEmpty() && time.isNotEmpty() && group.isNotEmpty()) {
                if (registerDate.isEmpty()) registerDate = binding.etRegisterDate.hint.toString()
                val mentorId = mentorsIdes[mentorsNames.indexOf(mentor)]
                var groupId = groupsIdes[groupsNames.indexOf(group)]

                var isHave = false
                for (student in dbHelper.studentDao().getAllStudents()) {
                    if (student.name.equals(name, ignoreCase = true) &&
                        student.surname.equals(surname, ignoreCase = true) &&
                        student.fatherName.equals(fatherName, ignoreCase = true) &&
                        student.mentorId == mentorId &&
                        student.lessonsDays.equals(days, ignoreCase = true) &&
                        student.groupTime.equals(time, ignoreCase = true) &&
                        student.groupId == groupId &&
                        student.courseId == courseId
                    ) {
                        isHave = true
                        break
                    }
                }

                if (isHave) {
                    Toast.makeText(
                        requireContext(),
                        "Bu talaba allaqachon qo`shilgan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val group1 = dbHelper.groupDao().getGroupById(groupId)

                    if (group1.studentsCount < 20) {
                        if (isEdit) {
                            val student = dbHelper.studentDao().getStudentById(studentId)

                            student.name = name
                            student.surname = surname
                            student.fatherName = fatherName
                            student.registerDate = registerDate
                            student.mentorId = mentorId
                            student.lessonsDays = days
                            student.groupTime = time

                            if (student.groupId != groupId) {
                                val otherGroup = dbHelper.groupDao().getGroupById(groupId)
                                val currentGroup = dbHelper.groupDao().getGroupById(student.groupId)

                                otherGroup.studentsCount++
                                currentGroup.studentsCount--

                                dbHelper.groupDao().updateGroup(otherGroup)
                                dbHelper.groupDao().updateGroup(currentGroup)
                            }

                            student.groupId = groupId

                            dbHelper.studentDao().updateStudent(student)
                            Toast.makeText(requireContext(), "Talaba muvaffaqiyatli tahrirlandi !!!", Toast.LENGTH_SHORT).show()
                        } else {
                            dbHelper.studentDao().addStudent(
                                Student(
                                    name = name,
                                    surname = surname,
                                    fatherName = fatherName,
                                    registerDate = registerDate,
                                    mentorId = mentorId,
                                    lessonsDays = days,
                                    groupTime = time,
                                    groupId = groupId,
                                    courseId = courseId!!
                                )
                            )

                            group1.studentsCount++
                            if (group1.studentsCount == 20) group1.isFull = 1

                            dbHelper.groupDao().updateGroup(group1)

                            Toast.makeText(
                                requireContext(),
                                "Talaba muvaffaqiyatli qo`shildi !!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${group1.name} guruhi to`lib bo`lgan !!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Ma'lumotlarni to`liq kiriting",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        return binding.root
    }

}