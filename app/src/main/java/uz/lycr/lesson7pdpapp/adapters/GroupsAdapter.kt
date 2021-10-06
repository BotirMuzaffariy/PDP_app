package uz.lycr.lesson7pdpapp.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import uz.lycr.lesson7pdpapp.entity.Group
import androidx.recyclerview.widget.RecyclerView
import uz.lycr.lesson7pdpapp.databinding.ItemGroupsBinding

class GroupsAdapter(var list: List<Group>, var listener: MyOnClickListenerGroups): RecyclerView.Adapter<GroupsAdapter.MyVhGroupsAdapter>() {

    inner class MyVhGroupsAdapter(var itemBinding: ItemGroupsBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface MyOnClickListenerGroups{
        fun onShowBtnClickListener(courseId: Int, groupId: Int)
        fun onEditBtnClickListener(group: Group, position: Int)
        fun onDeleteBtnClickListener(group: Group, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVhGroupsAdapter {
        return MyVhGroupsAdapter(ItemGroupsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyVhGroupsAdapter, position: Int) {
        val group = list[position]

        holder.itemBinding.tvName.text = group.name
        holder.itemBinding.tvFatherName.text = "O`quvchilar soni: ${group.studentsCount} ta"

        holder.itemBinding.cvEdit.setOnClickListener { listener.onEditBtnClickListener(group, position) }
        holder.itemBinding.cvDelete.setOnClickListener { listener.onDeleteBtnClickListener(group, position) }
        holder.itemBinding.cvShow.setOnClickListener { listener.onShowBtnClickListener(group.courseId, group.id!!) }
    }

    override fun getItemCount() = list.size

}