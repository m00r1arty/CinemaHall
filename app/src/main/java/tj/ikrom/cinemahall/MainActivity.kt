package tj.ikrom.cinemahall

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import tj.ikrom.cinemahall.ui.fragments.SeatsFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()

        supportFragmentManager.beginTransaction()
            .replace(R.id.main, SeatsFragment())
            .commit()

    }
}