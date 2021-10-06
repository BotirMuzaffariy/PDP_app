package uz.lycr.lesson7pdpapp.dao

import androidx.room.*
import uz.lycr.lesson7pdpapp.entity.Group

@Dao
interface GroupDao {

    @Insert
    fun addGroup(group: Group)

    @Update
    fun updateGroup(group: Group)

    @Delete
    fun deleteGroup(group: Group)

    @Query("select * from groups")
    fun getAllGroups(): List<Group>

    @Query("select * from groups where id=:id")
    fun getGroupById(id: Int): Group

    @Query("select id from groups where name=:name and time=:time and course_id=:courseId and mentor_id=:mentorId and students_count=:studentsCount and is_full=:isFull")
    fun getIdByGroup(name: String, time: String, courseId: Int, mentorId: Int, studentsCount: Int, isFull: Int): Int

}