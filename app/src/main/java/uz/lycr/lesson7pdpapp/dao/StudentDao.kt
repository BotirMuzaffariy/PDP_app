package uz.lycr.lesson7pdpapp.dao

import androidx.room.*
import uz.lycr.lesson7pdpapp.entity.Student

@Dao
interface StudentDao {

    @Insert
    fun addStudent(student: Student)

    @Update
    fun updateStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Query("select * from student")
    fun getAllStudents(): List<Student>

    @Query("select * from student where id=:id")
    fun getStudentById(id: Int): Student

    @Query("select id from student where name=:name and surname=:surname and father_name=:fatherName and register_date=:registerDate and mentor_id=:mentorId and lessons_days=:lessonsDays and group_time=:groupTime and group_id=:groupId and course_id=:courseId")
    fun getIdByStudent(name: String, surname: String, fatherName: String, registerDate: String, mentorId: Int, lessonsDays: String, groupTime: String, groupId: Int, courseId: Int): Int

}