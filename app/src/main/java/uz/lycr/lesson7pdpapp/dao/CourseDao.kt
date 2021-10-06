package uz.lycr.lesson7pdpapp.dao

import androidx.room.*
import uz.lycr.lesson7pdpapp.entity.Course

@Dao
interface CourseDao {

    @Insert
    fun addCourse(course: Course)

    @Update
    fun updateCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Query("select * from course")
    fun getAllCourses(): List<Course>

    @Query("select * from course where id=:id")
    fun getCourseById(id: Int): Course

    @Query("select id from course where name=:name and about=:about")
    fun getIdByCourse(name: String, about: String): Int

}