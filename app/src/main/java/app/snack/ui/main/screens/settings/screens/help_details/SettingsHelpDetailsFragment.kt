package app.snack.ui.main.screens.settings.screens.help_details

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsHelpDetailsBinding
import app.snack.model.response.QuestionsResponse
import app.snack.utils.Screen
import app.snack.utils.extensions.onClick

class SettingsHelpDetailsFragment :
    BindingFragment<FragmentSettingsHelpDetailsBinding, SettingsHelpDetailsViewModel>() {

    override val viewModel by viewModels<SettingsHelpDetailsViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsHelpDetailsBinding::inflate

    override fun setupUI() {

        arguments?.let {
            if(it.containsKey("id")) {
                val id = it.getInt("id")
                QuestionsResponse.mockProfileResponse().data.firstOrNull { x -> x.id == id }.let { item ->
                    if(item != null) {
                        binding.toolbar.title = item.questionFromLocale
                        binding.hint.text = item.answerFromLocale
                    }
                }
            }
        }


        with(binding) {
            toolbar.setNavigationOnClickListener { closeScreen() }
            fabSupport.onClick { addScreen(Screen.SETTINGS_SUPPORT) }
        }
    }
}
