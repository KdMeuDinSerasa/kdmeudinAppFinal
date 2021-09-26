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
import com.example.kdmeudinheiro.services.WorkManagerBuilder
import com.example.kdmeudinheiro.utils.checkConnection
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
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
        if (!checkConnection()) feedback(binding.root, R.string.no_connection_slow_warning, R.color.failure)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loadComponents()
        loadViewModels()
        mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)

        val mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        mSharedPreferences.getString(KeysShared.USERID.key, "")?.let { viewModel.getUserById(it) }
    }

    /* invoke the work manager builder class to make a notification scheduler.*/


    fun updateUser() {
        viewModel.userLoged()
    }

    fun loadComponents() {
        /**
         * Nav controler que vai controlar a transição entre os fragments
         * realocando eles no hostfragment criado dentro da activity
         */
        mNavController = findNavController(R.id.hostFragment)
        binding.bottomNavMain.apply {
            this.setupWithNavController(mNavController)
        }
        mAppBarConfiguration = AppBarConfiguration(mNavController.graph, binding.drawerLayoutMain)
        /**
         * Botao de sanduiche para abrir o menu
         */
        NavigationUI.setupActionBarWithNavController(this, mNavController, binding.drawerLayoutMain)


        /**
         * seta navigation viewer com o controle do NavController
         */
        NavigationUI.setupWithNavController(binding.drawerMenuMain, mNavController)

        binding.drawerMenuMain.setNavigationItemSelectedListener(this)

    }

    fun loadViewModels() {
        viewModel.mUserModel.observe(this, { userDetails ->
            binding.drawerMenuMain.getHeaderView(0).apply {
                binding2 = HeaderDrawerBinding.bind(this)
                binding2.userEmailLoged.text = userDetails.email
                binding2.userNameLoged.text = userDetails.name
                if (userDetails.img == null) {
                    binding2.userAvatarDrawer.let {
                        Glide.with(it)
                            .load(R.drawable.man_png)
                            .into(it)
                    }
                } else {
                    binding2.userAvatarDrawer.let {
                        Glide.with(it)
                            .load(userDetails.img)
                            .into(it)
                    }
                }
            }
        })

        viewModel.mFirebaseUser.observe(this, {
            if (it != null) {
                viewModel.getUserById(it.uid)
            }
        })
    }

    /**
     * Para onde voltar quando o botao de voltar for clicado
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //close menu when clicked
        binding.drawerLayoutMain.closeDrawer(GravityCompat.START)

        when (item.itemId) {
            R.id.btnLogout -> {
                viewModel.logoutUser()
                mSharedPreferences.edit {
                    this.putString(KeysShared.USERID.key, "")
                    this.putBoolean(KeysShared.REMEMBERME.key, false)
                }
                val initi = Intent(this, LoginActivity::class.java)
                startActivity(initi)
                finish()
            }
            R.id.btnUserPreferences -> {
                mNavController.navigate(R.id.action_mainFragment_to_userPreferencesFragment)
            }
        }
        return true
    }

    override fun onBackPressed() {
        mNavController.popBackStack()
    }
}