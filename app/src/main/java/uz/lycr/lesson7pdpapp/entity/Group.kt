package uz.lycr.lesson7pdpapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var time: String,
    @ColumnInfo(name = "course_id")
    var courseId: Int,
    @ColumnInfo(name = "mentor_id")
    var mentorId: Int,
    @ColumnInfo(name = "students_count")
    var studentsCount: Int = 0,
    @ColumnInfo(name = "is_full")
    var isFull: Int = 0
)