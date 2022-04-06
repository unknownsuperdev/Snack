package app.snack

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import app.snack.ui.splash.SplashFragment
import app.snack.utils.extensions.loader
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : FragmentActivity() {

    private var progressLoader: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        statusBar {
            fitWindow = false
            background.colorRes = R.color.status_bar
        }

        supportFragmentManager.commit {
            replace(R.id.container, SplashFragment())
        }
    }

    fun showLoading(isLoading: Boolean) {
        progressLoader?.dismiss()
        progressLoader = null

        if (isLoading) progressLoader = loader()
    }
}