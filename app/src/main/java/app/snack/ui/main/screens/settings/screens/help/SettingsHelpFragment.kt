package app.snack.ui.main.screens.settings.screens.help

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsHelpBinding
import app.snack.model.response.QuestionsResponse
import app.snack.utils.Screen
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.setDrawableEnd
import app.snack.utils.extensions.showed

class SettingsHelpFragment :
    BindingFragment<FragmentSettingsHelpBinding, SettingsHelpViewModel>() {

    override val viewModel by viewModels<SettingsHelpViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsHelpBinding::inflate

    override fun setupUI() {
        with(binding) {
            toolbar.setNavigationOnClickListener { closeScreen() }
            btnCard1.onClick {
                group1.showed(group1.isShown.not())
                btnCard1.setDrawableEnd(if (group1.isShown) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            }
            btnCard2.onClick {
                group2.showed(group2.isShown.not())
                btnCard2.setDrawableEnd(if (group2.isShown) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            }
            btnCard3.onClick {
                group3.showed(group3.isShown.not())
                btnCard3.setDrawableEnd(if (group3.isShown) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            }
            btnCard4.onClick {
                group4.showed(group4.isShown.not())
                btnCard4.setDrawableEnd(if (group4.isShown) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            }

            // GENERAL

            // How does Snack work?

            val questions = QuestionsResponse.mockProfileResponse()
            btn11.text = questions.data[0].questionFromLocale
            btn11.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 1) }
            // How to get started?
            btn12.text = questions.data[1].questionFromLocale
            btn12.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 2) }
            // How to make a payout?
            btn13.text = questions.data[2].questionFromLocale
            btn13.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 3) }
            // How to stop sharing traffic?
            btn14.text = questions.data[3].questionFromLocale
            btn14.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 4) }
            // Is it secure?
            btn15.text = questions.data[4].questionFromLocale
            btn15.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 5) }

            // TECHNICAL

//            btn21.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 6) }
//            btn22.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 7) }
//            btn23.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 8) }
//            btn24.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 9) }

            // PAYOUT
            btn31.text = questions.data[5].questionFromLocale
            btn31.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 6) }

            btn32.text = questions.data[6].questionFromLocale
            btn32.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 7) }

            btn33.text = questions.data[7].questionFromLocale
            btn33.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 8) }

            btn34.text = questions.data[8].questionFromLocale
            btn34.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 9) }

            // ACCOUNT

            btn41.text = questions.data[9].questionFromLocale
            btn41.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 10) }

            btn42.text = questions.data[10].questionFromLocale
            btn42.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 11) }

            btn43.text = questions.data[11].questionFromLocale
            btn43.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 12) }

            btn44.text = questions.data[12].questionFromLocale
            btn44.onClick { addScreen(Screen.SETTINGS_HELP_DETAILS, 13) }

            fabSupport.onClick { addScreen(Screen.SETTINGS_SUPPORT) }
        }
    }
}
