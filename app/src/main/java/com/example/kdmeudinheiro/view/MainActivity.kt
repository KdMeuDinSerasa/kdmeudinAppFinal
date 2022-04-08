package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.HeaderDrawerBinding
import com.example.kdmeudinheiro.databinding.MainActivityBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.utils.checkConnection
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.utils.hideKeyboard
import com.example.kdmeudinheiro.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    NavigationBarView.OnItemSelectedListener {
    lateinit var mAppBarConfiguration: AppBarConfiguration
    lateinit var mNavController: NavController
    private lateinit var binding: MainActivityBinding
    private lateinit var binding2: HeaderDrawerBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkConnectionBeforeAll()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        loadNavigationComponents()
        loadViewModelsObservers()
        mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        mSharedPreferences.getString(KeysShared.USERID.key, "")?.let { viewModel.getUserById(it) }
    }

    private fun checkConnectionBeforeAll() {
        runBlocking {
            if (!checkConnection()) feedback(
                binding.root,
                R.string.no_connection_slow_warning,
                R.color.failure
            )
        }
    }

    /* invoke the work manager builder class to make a notification scheduler.*/

    fun updateUser() {
        viewModel.userLoged()
    }

    private fun loadNavigationComponents() {
        /**
         * Nav controller who will control fragments transitions
         * relocating then on hostfragment created inside activity
         */
        mNavController = findNavController(R.id.hostFragment)
        binding.bottomNavMain.apply {
            this.setupWithNavController(mNavController)
        }

        mAppBarConfiguration = AppBarConfiguration(mNavController.graph, binding.drawerLayoutMain)
        /**
         * sandwich button to open navbar
         */
        NavigationUI.setupActionBarWithNavController(this, mNavController, binding.drawerLayoutMain)

        /**
         * setups navigation viewer with NavController
         */
        NavigationUI.setupWithNavController(binding.drawerMenuMain, mNavController)

        binding.drawerMenuMain.setNavigationItemSelectedListener(this)
        binding.bottomNavMain.setOnItemSelectedListener(this)

    }

    private fun loadViewModelsObservers() {
        viewModel.mUserModel.observe(this, { setupDrawerMenuWithUserDetails(it) })

        viewModel.mFirebaseUser.observe(this, { if (it != null) viewModel.getUserById(it.uid) })
    }

    private fun setupDrawerMenuWithUserDetails(userDetails: UserModel) {
        binding.drawerMenuMain.getHeaderView(0).apply {
            binding2 = HeaderDrawerBinding.bind(this)
            binding2.userEmailLoged.text = userDetails.email
            binding2.userNameLoged.text = userDetails.name

            when (userDetails.img) {
                null -> {
                    binding2.userAvatarDrawer.let {
                        Glide.with(it)
                            .load(R.drawable.man_png)
                            .into(it)
                    }
                }
                else -> {
                    binding2.userAvatarDrawer.let {
                        Glide.with(it)
                            .load(userDetails.img)
                            .into(it)
                    }
                }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayoutMain.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.btnLogout -> {
                logoutUser()
            }
            R.id.btnUserPreferences -> {
                mNavController.navigate(R.id.action_mainFragment_to_userPreferencesFragment)
            }
            R.id.btnNews -> {
                navigateToNews()
            }
            R.id.btnBills -> {
                mNavController.navigate(R.id.BillsFragment)
            }
        }
        return true
    }

    private fun logoutUser() {
        viewModel.logoutUser()
        clearSharedPreferences()
        navigateBackToLogin()
    }

    private fun clearSharedPreferences() {
        mSharedPreferences.edit {
            this.putString(KeysShared.USERID.key, "")
            this.putBoolean(KeysShared.REMEMBERME.key, false)
        }
    }

    private fun navigateBackToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun navigateToNews() {
        hideKeyboard()
        if (checkConnection()) mNavController.navigate(R.id.newsLetterFragment)
        else startActivity(Intent(this, NoConnectionActivity::class.java))
    }

    override fun onBackPressed() {
        mNavController.popBackStack()
    }
}