package uz.lycr.lesson7pdpapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import uz.lycr.lesson7pdpapp.entity.Mentor
import uz.lycr.lesson7pdpapp.db.AppDatabase
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.adapters.CourseMentorsAdapter
import uz.lycr.lesson7pdpapp.databinding.DialogAddMentorBinding
import uz.lycr.lesson7pdpapp.databinding.FragmentCourseMentorsBinding

class CourseMentorsFragment : Fragment() {

    private var courseId: Int? = null

    private lateinit var adapter: CourseMentorsAdapter
    private lateinit var dbHelper: AppDatabase
    private lateinit var binding: FragmentCourseMentorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbHelper = AppDatabase.getInstance(requireContext())
        binding = FragmentCourseMentorsBinding.inflate(inflater, container, false)

        binding.tvCourseName.text = dbHelper.courseDao().getCourseById(courseId!!).name

        val allMentors = dbHelper.mentorDao().getAllMentors()
        val list = ArrayList(allMentors.filter { it.courseId == courseId })

        adapter = CourseMentorsAdapter(
            list,
            object : CourseMentorsAdapter.MyOnClickListenerCourseMentors {
                override fun onEditBtnClickListener(mentor: Mentor, position: Int) {
                    val builder = AlertDialog.Builder(requireContext())
                    val dialogBinding = DialogAddMentorBinding.inflate(layoutInflater)
                    builder.setView(dialogBinding.root)
                    val dialog = builder.create()

                    dialogBinding.tvAdd.text = "O`zgartirish"
                    dialogBinding.tvDialogName.text = "Mentorni tahrirlash"

                    dialogBinding.etName.setText(mentor.name)
                    dialogBinding.etSurname.setText(mentor.surname)
                    dialogBinding.etFatherName.setText(mentor.fatherName)

                    dialogBinding.tvClose.setOnClickListener { dialog.dismiss() }
                    dialogBinding.tvAdd.setOnClickListener {
                        val name = dialogBinding.etName.text.toString()
                        val surname = dialogBinding.etSurname.text.toString()
                        val fatherName = dialogBinding.etFatherName.text.toString()

                        if (name.isNotEmpty() && surname.isNotEmpty() && fatherName.isNotEmpty()) {
                            var isHave = false
                            for (mentor1 in dbHelper.mentorDao().getAllMentors()) {
                                if (mentor1.name.equals(
                                        name,
                                        ignoreCase = true
                                    ) && mentor1.surname.equals(
                                        surname,
                                        true
                                    ) && mentor1.fatherName.equals(
                                        fatherName,
                                        true
                                    ) && mentor1.courseId == courseId
                                ) {
                                    isHave = true
                                    break
                                }
                            }

                            if (isHave) {
                                Toast.makeText(
                                    requireContext(),
                                    "Bu mentor allaqachon qo`shilgan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                mentor.name = name
                                mentor.surname = surname
                                mentor.fatherName = fatherName
                                mentor.courseId = courseId!!

                                dbHelper.mentorDao().updateMentor(mentor)
                                list[position] = mentor

                                adapter.notifyItemChanged(position)
                                dialog.dismiss()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Ma'lumotlarni to`liq kiriting",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    }

                    dialog.show()
                }

                override fun onDeleteBtnClickListener(mentor: Mentor, position: Int) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Mentorni o`chirish")
                    builder.setMessage("${mentor.name} ${mentor.surname}ni o`chirishga ishonchingiz komilmi?")

                    builder.setPositiveButton("Ha") { dialog, _ ->
                        if (dbHelper.groupDao().getAllGroups().filter { it.mentorId == mentor.id }
                                .count() == 0) {
                            dbHelper.mentorDao().deleteMentor(mentor)
                            list.remove(mentor)

                            Toast.makeText(
                                requireContext(),
                                "Mentor muvaffaqiyatli o`chirildi !!!",
                                Toast.LENGTH_SHORT
                            ).show()

                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, list.size)

                            dialog.dismiss()
                        } else {
                            val find = dbHelper.groupDao().getAllGroups().find { it.mentorId == mentor.id }
                            showInfoDialog(mentor, find?.name)
                        }
                    }

                    builder.setNegativeButton("Yo`q") { dialog, _ ->
                        dialog.dismiss()
                    }

                    builder.show()
                }
            })

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivAdd.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogBinding = DialogAddMentorBinding.inflate(layoutInflater)
            builder.setView(dialogBinding.root)
            val dialog = builder.create()

            dialogBinding.tvClose.setOnClickListener { dialog.dismiss() }
            dialogBinding.tvAdd.setOnClickListener {
                val name = dialogBinding.etName.text.toString()
                val surname = dialogBinding.etSurname.text.toString()
                val fatherName = dialogBinding.etFatherName.text.toString()

                if (name.isNotEmpty() && surname.isNotEmpty() && fatherName.isNotEmpty()) {
                    var isHave = false
                    for (mentor in dbHelper.mentorDao().getAllMentors()) {
                        if (mentor.name.equals(name, ignoreCase = true) && mentor.surname.equals(
                                surname,
                                true
                            ) && mentor.fatherName.equals(
                                fatherName,
                                true
                            ) && mentor.courseId == courseId
                        ) {
                            isHave = true
                            break
                        }
                    }

                    if (isHave) {
                        Toast.makeText(
                            requireContext(),
                            "Bu mentor allaqachon qo`shilgan",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val mentor = Mentor(
                            name = name,
                            surname = surname,
                            fatherName = fatherName,
                            courseId = courseId!!
                        )
                        dbHelper.mentorDao().addMentor(mentor)

                        mentor.id = dbHelper.mentorDao().getIdByMentor(mentor.name, mentor.surname, mentor.fatherName, mentor.courseId)
                        list.add(mentor)

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

    private fun showInfoDialog(mentor: Mentor, name: String?) {
        val builder = AlertDialog.Builder(requireContext())
        val groupName = if (name == null) "qaysidir guruhga" else "'$name' guruhiga"

        builder.setTitle("Ma'lumot")
        builder.setMessage("${mentor.name} ${mentor.surname} $groupName mentorlik qilmoqda. Avval guruhning mentorini almashtiring yoki guruhni o`chirib yuboring !!!")

        builder.setPositiveButton("Yopish") { dialog, _ -> dialog.dismiss() }

        builder.show()
    }

}