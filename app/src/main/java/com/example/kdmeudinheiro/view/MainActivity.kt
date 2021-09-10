package com.example.kdmeudinheiro.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.LoginActivity
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.HeaderDrawerBinding
import com.example.kdmeudinheiro.databinding.MainActivityBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loadComponents()
        loadViewModels()
        viewModel.userLoged()
    }

    fun updateUser(){
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

    fun loadViewModels(){
        viewModel.mUserModel.observe(this, {
            binding.drawerMenuMain.getHeaderView(0).apply {
                binding2 = HeaderDrawerBinding.bind(this)
                binding2.userEmailLoged.text = it.email
                binding2.userNameLoged.text = it.name

                binding2.userAvatarDrawer.let {
                    Glide.with(it)
                        .load(R.drawable.man_png)
                        .into(it)
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
        when(item.itemId){
            R.id.btnLogout -> {
                viewModel.logoutUser()
                val initi = Intent(this, LoginActivity::class.java)
                startActivity(initi)
            }
            R.id.btnUserPreferences -> {
                mNavController.navigate(R.id.action_mainFragment_to_userPreferencesFragment)
            }
        }
        return true
    }
}