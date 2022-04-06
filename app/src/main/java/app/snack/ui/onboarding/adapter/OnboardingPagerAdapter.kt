package app.snack.ui.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.snack.ui.onboarding.screens.Onboarding1Fragment
import app.snack.ui.onboarding.screens.Onboarding2Fragment
import app.snack.ui.onboarding.screens.Onboarding3Fragment
import app.snack.ui.onboarding.screens.Onboarding4Fragment

class OnboardingPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fm, lifecycle) {

    private val screens = listOf(
        Onboarding1Fragment(),
        Onboarding2Fragment(),
        Onboarding3Fragment(),
        Onboarding4Fragment()
    )

    override fun getItemCount(): Int = screens.size

    override fun createFragment(position: Int): Fragment = screens[position]
}