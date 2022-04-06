package app.snack.ui.onboarding

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentOnboardingBinding
import app.snack.ui.onboarding.adapter.OnboardingPagerAdapter
import app.snack.utils.Screen
import app.snack.utils.extensions.nextPage
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.onPageSelected
import splitties.toast.toast

class OnboardingFragment : BindingFragment<FragmentOnboardingBinding, OnboardingViewModel>() {

    override val viewModel by viewModels<OnboardingViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentOnboardingBinding::inflate

    override fun setupUI() {
        binding.btnNext.onClick { binding.pager.nextPage() }

        binding.pager.adapter = OnboardingPagerAdapter(childFragmentManager, lifecycle)
        binding.pager.onPageSelected { position ->
            binding.btnNext.text = if (position == 3) getString(R.string.sign_in) else getString(R.string.next)

            if (position == 3) {
                binding.btnNext.onClick { showScreen(Screen.LOGIN) }
            } else {
                binding.btnNext.onClick { binding.pager.nextPage() }
            }

        }

        binding.dots.setViewPager2(binding.pager)
    }
}