package com.example.kdmeudinheiro.view

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.UserPreferencesFragmentBinding
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.viewModel.UserPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPreferencesFragment : Fragment(R.layout.user_preferences_fragment) {

    companion object {
        fun newInstance() = UserPreferencesFragment()
    }

    private lateinit var viewModel: UserPreferencesViewModel
    private lateinit var binding: UserPreferencesFragmentBinding
    private lateinit var mUserModel: UserModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserPreferencesViewModel::class.java)
        binding = UserPreferencesFragmentBinding.bind(view)
        viewModel.userLoged()
        loadViewModels()
        loadComponents()


    }

    fun loadViewModels(){
        viewModel.mFirebaseUser.observe(viewLifecycleOwner, {
            if (it != null) {
                viewModel.getUserById(it.uid)
            }
        })
        viewModel.mUserModel.observe(viewLifecycleOwner, {
            mUserModel = it
            binding.tvUserEmail.text = mUserModel.email
            binding.tvUserName.text = mUserModel.name
        })

    }

    fun loadComponents(){
        binding.btnEdit.setOnClickListener {
            editDialog()
        }
        binding.userAvatarUserPrefs.let {
            Glide.with(it)
                .load(R.drawable.man_png)
                .into(it)
        }
    }


    fun editDialog(){
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setMessage("Informe os dados para serem editador")
                setTitle("Edição")

                val layout = LinearLayout(requireContext())
                layout.orientation = LinearLayout.VERTICAL

                val newUserName = EditText(requireContext())
                newUserName.setHint("Nome")
                layout.addView(newUserName)
                this.setView(layout)

                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        mUserModel.name = newUserName.text.toString()
                        Toast.makeText(requireContext(), "Editado com sucesso", Toast.LENGTH_SHORT).show()
                        viewModel.editUser(mUserModel)
                        viewModel.userLoged()
                        (requireActivity() as? MainActivity?)?.updateUser()
                    })
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
            }

            builder.create()
        }
        alertDialog.window?.setLayout(100 , 100)
        alertDialog.show()
    }
}