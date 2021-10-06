package uz.lycr.lesson7pdpapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Mentor(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var surname: String,
    @ColumnInfo(name = "father_name")
    var fatherName: String,
    @ColumnInfo(name = "course_id")
    var courseId: Int
)