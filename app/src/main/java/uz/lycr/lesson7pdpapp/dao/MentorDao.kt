package uz.lycr.lesson7pdpapp.dao

import androidx.room.*
import uz.lycr.lesson7pdpapp.entity.Mentor

@Dao
interface MentorDao {

    @Insert
    fun addMentor(mentor: Mentor)

    @Update
    fun updateMentor(mentor: Mentor)

    @Delete
    fun deleteMentor(mentor: Mentor)

    @Query("select * from mentor")
    fun getAllMentors(): List<Mentor>

    @Query("select * from mentor where id=:id")
    fun getMentorById(id: Int): Mentor

    @Query("select id from mentor where name=:name and surname=:surname and father_name=:fatherName and course_id=:courseId")
    fun getIdByMentor(name: String, surname: String, fatherName: String, courseId: Int): Int

}