package com.example.kdmeudinheiro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.kdmeudinheiro.databinding.MainActivityBinding
import com.example.kdmeudinheiro.databinding.MainFragmentBinding
import com.example.kdmeudinheiro.view.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var mAppBarConfiguration: AppBarConfiguration
    lateinit var mNavController: NavController
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Nav controler que vai controlar a transição entre os fragments
         * realocando eles no hostfragment criado dentro da activity
         */
        mNavController = findNavController(R.id.hostFragment)
        binding.bottomNavMain.apply {
            this.setupWithNavController(mNavController)
        }



        mAppBarConfiguration = AppBarConfiguration(mNavController.graph, binding.drawerLayoutMain )
        /**
         * Botao de sanduiche para abrir o menu
         */
        NavigationUI.setupActionBarWithNavController(this, mNavController, binding.drawerLayoutMain)


        /**
         * seta navigation viewer com o controle do NavController
         */
        NavigationUI.setupWithNavController(binding.navViewMain , mNavController)
    }

    /**
     * Para onde voltar quando o botao de voltar for clicado
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
    }
}