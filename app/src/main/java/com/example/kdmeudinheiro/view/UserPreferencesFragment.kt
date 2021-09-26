package com.example.kdmeudinheiro.view

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.UserPreferencesFragmentBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.services.NotificationHandler
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.viewModel.UserPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserPreferencesFragment : Fragment(R.layout.user_preferences_fragment) {

    companion object {
        fun newInstance() = UserPreferencesFragment()
    }

    @Inject
    lateinit var mNotificationHandler: NotificationHandler
    private lateinit var viewModel: UserPreferencesViewModel
    private lateinit var binding: UserPreferencesFragmentBinding
    private lateinit var mUserModel: UserModel
    private lateinit var imgUri: Uri
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var notificationManager: NotificationManagerCompat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserPreferencesViewModel::class.java)
        binding = UserPreferencesFragmentBinding.bind(view)
        mSharedPreferences =
            requireContext().getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        mSharedPreferences.getString(KeysShared.USERID.key, "")?.let { viewModel.getUserById(it) }
        notificationManager = NotificationManagerCompat.from(requireContext())
        loadViewModels()
        loadComponents()

        checkSavedValues()
    }

    fun loadViewModels() {
        /**
         * Load user details
         */
        viewModel.mUserModel.observe(viewLifecycleOwner, { userDetails ->
            mUserModel = userDetails
            binding.tvUserEmail.text = mUserModel.email
            binding.tvUserName.text = mUserModel.name
            if (userDetails.img == null) {
                binding.userAvatarUserPrefs.let {
                    Glide.with(it)
                        .load(R.drawable.man_png)
                        .into(it)
                }
            } else {
                binding.userAvatarUserPrefs.let {
                    Glide.with(it)
                        .load(mUserModel.img)
                        .into(it)
                }
            }
            binding.progressBar.visibility = View.INVISIBLE
        })
        /**
         * Update data when the user edits img
         */
        viewModel.imgUser.observe(viewLifecycleOwner, {
            mUserModel.img = it.toString()
            viewModel.editUser(mUserModel)
            mSharedPreferences.getString(KeysShared.USERID.key, "")
                ?.let { viewModel.getUserById(it) }
            (requireActivity() as? MainActivity?)?.updateUser()
        })
        viewModel.error.observe(viewLifecycleOwner, {
            feedback(requireView(), R.string.error_to_excute_action, R.color.failure)
        })


        viewModel.notificationType.observe(viewLifecycleOwner, {
            when (it[0]) {
                1 -> {
                    mNotificationHandler.createNotification(
                        "Você tem contas vencidas!",
                        "Total de contas vencidas: ${it[1]}"
                    ).apply {
                        notificationManager.notify(1, this)
                    }
                }
                2 -> {
                    mNotificationHandler.createNotification(
                        "Você tem contas há vence!r",
                        "Total de contas há vencer: ${it[1]}"
                    ).apply {
                        notificationManager.notify(1, this)
                    }
                }
                3 -> {
                    mNotificationHandler.createNotification(
                        "Você não possui contas vencidas",
                        "Parabéns!! Contas em dia!!"
                    ).apply {
                        notificationManager.notify(1, this)
                    }
                }
            }
        })

    }

    fun loadComponents() {
        binding.btnEdit.setOnClickListener {
            editDialog()
        }
        binding.userAvatarUserPrefs.setOnClickListener {
            userLoadImg()
        }
        binding.btnNotification.setOnClickListener {
            sendNotification()
        }
    }

    fun sendNotification() {
        val mSharedPreferences =
            requireContext().getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        viewModel.sendNotifications(
            mSharedPreferences.getString(KeysShared.USERID.key, ""),
            mSharedPreferences.getBoolean(KeysShared.ACTIVE_PUSH.key, false),
            requireContext()
        )

    }

    fun userLoadImg() {
        val galeryIntent = Intent()
        galeryIntent.type = "image/*"
        galeryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(galeryIntent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && data != null && resultCode == RESULT_OK) {
            imgUri = data.data!!
            viewModel.uploadImgToFirebase(getFileExtension(imgUri), imgUri)
            binding.progressBar.visibility = View.VISIBLE

        }
    }

    fun getFileExtension(imgUri: Uri): String {
        val mime = MimeTypeMap.getSingleton()
        val imgDone =
            mime.getExtensionFromMimeType(requireContext().contentResolver.getType(imgUri))
        return imgDone!!
    }

    fun editDialog() {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.apply {
                setMessage("Insira os dados a serem editados")
                setTitle("Edição")

                val layout = LinearLayout(requireContext())
                layout.orientation = LinearLayout.VERTICAL

                val newUserName = EditText(requireContext())
                newUserName.hint = "Nome"
                layout.addView(newUserName)
                this.setView(layout)

                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        mUserModel.name = newUserName.text.toString()
                        feedback(requireView(), R.string.edited_with_success, R.color.success)
                        viewModel.editUser(mUserModel)
                        mSharedPreferences.getString(KeysShared.USERID.key, "")
                            ?.let { viewModel.getUserById(it) }
                        (requireActivity() as? MainActivity?)?.updateUser()
                    })
                setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
            }

            builder.create()
        }
        alertDialog.window?.setLayout(100, 100)
        alertDialog.show()
    }

    private fun checkSavedValues() {

        binding.emailSwitch.isChecked =
            mSharedPreferences.getBoolean(KeysShared.RECEIVE_EMAIL.key, false)
        binding.pushSwitch.isChecked =
            mSharedPreferences.getBoolean(KeysShared.ACTIVE_PUSH.key, false)

        binding.emailSwitch.setOnCheckedChangeListener { _, value ->
            mSharedPreferences.edit {
                this.putBoolean(KeysShared.RECEIVE_EMAIL.key, value)
            }
        }

        binding.pushSwitch.setOnCheckedChangeListener { _, value ->
            mSharedPreferences.edit {
                this.putBoolean(KeysShared.ACTIVE_PUSH.key, value)
            }
        }
    }
}