package uz.lycr.lesson7pdpapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var surname: String,
    @ColumnInfo(name = "father_name")
    var fatherName: String,
    @ColumnInfo(name = "register_date")
    var registerDate: String,
    @ColumnInfo(name = "mentor_id")
    var mentorId: Int,
    @ColumnInfo(name = "lessons_days")
    var lessonsDays: String,
    @ColumnInfo(name = "group_time")
    var groupTime: String,
    @ColumnInfo(name = "group_id")
    var groupId: Int,
    @ColumnInfo(name = "course_id")
    var courseId: Int
)