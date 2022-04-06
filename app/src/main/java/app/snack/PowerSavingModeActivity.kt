package app.snack

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import app.snack.ui.splash.SplashFragment
import app.snack.utils.extensions.onClick
import com.zackratos.ultimatebarx.ultimatebarx.statusBar

class PowerSavingModeActivity : FragmentActivity(R.layout.activity_power_saving_mode) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBar { transparent() }
        findViewById<View>(R.id.btnTurnOff).onClick { finish() }
    }
}