package uz.lycr.lesson7pdpapp.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import uz.lycr.lesson7pdpapp.entity.Student
import androidx.recyclerview.widget.RecyclerView
import uz.lycr.lesson7pdpapp.databinding.ItemStudentsBinding

class StudentsAdapter(var list: List<Student>, var listener: MyOnClickListenerStudents) :
    RecyclerView.Adapter<StudentsAdapter.VhStudents>() {

    inner class VhStudents(var itemBinding: ItemStudentsBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    interface MyOnClickListenerStudents {
        fun onEditBtnClickListener(student: Student, position: Int)
        fun onDeleteBtnClickListener(student: Student, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhStudents {
        return VhStudents(ItemStudentsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VhStudents, position: Int) {
        val student = list[position]

        holder.itemBinding.tvFatherName.text = student.fatherName
        holder.itemBinding.tvRegisterDate.text = student.registerDate
        holder.itemBinding.tvName.text = "${student.name} ${student.surname}"

        holder.itemBinding.cvEdit.setOnClickListener { listener.onEditBtnClickListener(student, position) }
        holder.itemBinding.cvDelete.setOnClickListener { listener.onDeleteBtnClickListener(student, position) }
    }

    override fun getItemCount() = list.size

}