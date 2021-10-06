package uz.lycr.lesson7pdpapp.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import uz.lycr.lesson7pdpapp.entity.Mentor
import androidx.recyclerview.widget.RecyclerView
import uz.lycr.lesson7pdpapp.databinding.ItemCourseMentorsBinding

class CourseMentorsAdapter(var list: List<Mentor>, var listener: MyOnClickListenerCourseMentors) :
    RecyclerView.Adapter<CourseMentorsAdapter.VhCourseMentors>() {

    inner class VhCourseMentors(var itemBinding: ItemCourseMentorsBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    interface MyOnClickListenerCourseMentors {
        fun onEditBtnClickListener(mentor: Mentor, position: Int)
        fun onDeleteBtnClickListener(mentor: Mentor, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhCourseMentors {
        return VhCourseMentors(ItemCourseMentorsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VhCourseMentors, position: Int) {
        holder.itemBinding.tvFatherName.text = list[position].fatherName
        holder.itemBinding.tvName.text = "${list[position].name} ${list[position].surname}"

        holder.itemBinding.cvEdit.setOnClickListener { listener.onEditBtnClickListener(list[position], position) }
        holder.itemBinding.cvDelete.setOnClickListener { listener.onDeleteBtnClickListener(list[position], position) }
    }

    override fun getItemCount() = list.size

}