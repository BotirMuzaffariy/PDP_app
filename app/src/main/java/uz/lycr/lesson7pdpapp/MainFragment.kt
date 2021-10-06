package uz.lycr.lesson7pdpapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.lycr.lesson7pdpapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.cvCourses.setOnClickListener { findNavController().navigate(R.id.allCoursesFragment) }
        binding.cvGroups.setOnClickListener { findNavController().navigate(R.id.allCoursecGroupFragment) }
        binding.cvMentors.setOnClickListener { findNavController().navigate(R.id.allCoursesMentorFragment) }

        return binding.root
    }

}