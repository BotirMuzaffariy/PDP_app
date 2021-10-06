package uz.lycr.lesson7pdpapp.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Course(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var about: String)