package uz.os3ketchup.bozor.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.databinding.ActivityLoginBinding
import uz.os3ketchup.bozor.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.auth_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        navController.navigateUp()
    }

}