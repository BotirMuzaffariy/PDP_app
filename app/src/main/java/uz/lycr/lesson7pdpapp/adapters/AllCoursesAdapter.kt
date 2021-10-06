package uz.lycr.lesson7pdpapp.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import uz.lycr.lesson7pdpapp.entity.Course
import androidx.recyclerview.widget.RecyclerView
import uz.lycr.lesson7pdpapp.databinding.ItemAllCoursesBinding

class AllCoursesAdapter(var list: List<Course>, var listener: MyOnClickListenerCourses) :
    RecyclerView.Adapter<AllCoursesAdapter.VhAllCourses>() {

    inner class VhAllCourses(var itemBinding: ItemAllCoursesBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    interface MyOnClickListenerCourses {
        fun onItemClickListener(courseId: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VhAllCourses {
        return VhAllCourses(ItemAllCoursesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VhAllCourses, position: Int) {
        holder.itemBinding.tvName.text = list[position].name
        holder.itemBinding.cv.setOnClickListener { listener.onItemClickListener(list[position].id) }
    }

    override fun getItemCount() = list.size

}