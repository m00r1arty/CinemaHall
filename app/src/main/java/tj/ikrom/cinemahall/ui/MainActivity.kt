package tj.ikrom.cinemahall.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import tj.ikrom.cinemahall.R
import tj.ikrom.cinemahall.databinding.ActivityMainBinding
import tj.ikrom.cinemahall.ui.fragments.PaymentFragment
import tj.ikrom.cinemahall.ui.fragments.SeatsFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}