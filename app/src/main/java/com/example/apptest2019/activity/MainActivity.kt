package com.example.apptest2019.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.apptest2019.App
import com.example.apptest2019.R
import com.example.apptest2019.databinding.ActivityMainBinding
import com.example.apptest2019.fragments.fragment_login_page.FragmentLoginPageDirections
import com.example.apptest2019.interfaces.FragmentBackButtonPressed

class MainActivity : AppCompatActivity(), DrawerLockerHandlerInterface, ActionBarHandlerInterface {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBar: ActionBar
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
         navController = this.findNavController(R.id.navHostFragment)

        drawerLayout = binding.drawerLayout//получить DrawerLayout меню(боковая шторка)
        actionBar = supportActionBar!!
        //конфигурирует меню верхнее и соединяет с навигатором navigation_base
        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            drawerLayout
        )
        // конфигурирует шторку меню соединяя с навигатором navigation_base
        NavigationUI.setupWithNavController(
            binding.navView,
            navController
        )

        // перенаправляет пользователя на страницу LoginPage от изменения тригера isTrustedUser ///////////
        val currentIntPlusTwo = Observer<Boolean> { item ->                                             ///
            if (!item) {                                                                                ///
                Toast.makeText(                                                                         ///
                    applicationContext,                                                                 ///
                    "сессия истекла, введите логин и пароль заново",                                ///
                    Toast.LENGTH_SHORT                                                                  ///
                ).show()                                                                                ///
                navController.navigate(                                                                 ///
                    FragmentLoginPageDirections.actionGlobalFragmentLoginPage()                         ///
                )                                                                                       ///
            }                                                                                           ///
        }                                                                                               ///
        App.isTrustedUser.observe(                                                                      ///
            this,                                                                                ///
            currentIntPlusTwo                                                                           ///
        )                                                                                               ///
        ///////////////////////////////////////////////////////////////////////////////////////////////////

    }

    override fun onSupportNavigateUp(): Boolean {
        // устанавливает navHostFragment в кнопку назад для перемещения по ниму
        val navController = this.findNavController(R.id.navHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)

    }

    // позволяет управлять действиями кнопки Back на каждом фрагменте ///////////////////////////////
    override fun onBackPressed() {                                                                ///
        val fragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)   ///
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0)        ///
        if(currentFragment != null ){                                                             ///
            //cюда добавть фрагменты на которых ожидаются действия с кнопкой назад                ///
            (currentFragment as? FragmentBackButtonPressed)?.onBackPressed()                      ///
        }else{                                                                                    ///
            super.onBackPressed()                                                                 ///
        }                                                                                         ///
    }                                                                                             ///
    /////////////////////////////////////////////////////////////////////////////////////////////////


    override fun onResume() {
        super.onResume()
            //выполняет проверку перед на доверие юзеру
        if( App.isTrustedUser.value == false)
            navController.navigate(
                FragmentLoginPageDirections.actionGlobalFragmentLoginPage()
            )
    }
    override fun setTitle(title: String) {
        actionBar.title = title
    }

    override fun lockDrawerMenu() {
        actionBar.hide()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun unlockDrawerMenu() {
        actionBar.show()
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }


}

interface DrawerLockerHandlerInterface {
    fun lockDrawerMenu()
    fun unlockDrawerMenu()
}

interface ActionBarHandlerInterface {
    fun setTitle(title: String)
}
