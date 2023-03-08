package uz.os3ketchup.bozor.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.ActivityMainBinding
import uz.os3ketchup.bozor.presentation.fragments.BazarListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var myDatabase: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RxJavaPlugins.setErrorHandler(Timber::e)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
        navController = navHostFragment.navController

        myDatabase = MyDatabase.getInstance(this)

    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        navController.navigateUp()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // get reference to the Room database


            // change the value of the variable to false

            myDatabase.orderProductDao().getAllOrderProduct().forEach {
                myDatabase.orderProductDao()
                    .editOrderProduct(it.copy(isLongClicked = false, isChecked = false))
            }


            // pop the fragment from the back stack
            supportFragmentManager.popBackStack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}