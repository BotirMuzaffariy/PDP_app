package uz.lycr.lesson7pdpapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.lycr.lesson7pdpapp.ViewPagerGroupFragment

class ViewPagerAdapter(fm: FragmentManager, var titleList: List<String>, var courseId: Int) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = titleList.size

    override fun getItem(position: Int): Fragment {
        return ViewPagerGroupFragment.newInstance(position, courseId)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titleList[position]
    }

}