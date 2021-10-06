package uz.lycr.lesson7pdpapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.lycr.lesson7pdpapp.dao.CourseDao
import uz.lycr.lesson7pdpapp.dao.GroupDao
import uz.lycr.lesson7pdpapp.dao.MentorDao
import uz.lycr.lesson7pdpapp.dao.StudentDao
import uz.lycr.lesson7pdpapp.entity.Course
import uz.lycr.lesson7pdpapp.entity.Group
import uz.lycr.lesson7pdpapp.entity.Mentor
import uz.lycr.lesson7pdpapp.entity.Student

@Database(entities = [Course::class, Group::class, Mentor::class, Student::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao
    abstract fun mentorDao(): MentorDao
    abstract fun courseDao(): CourseDao
    abstract fun studentDao(): StudentDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, "pdp_db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }

}