package com.devmobile.android.restaurant.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import com.devmobile.android.restaurant.CalledFromXML
import com.devmobile.android.restaurant.IShowError
import com.devmobile.android.restaurant.R
import com.devmobile.android.restaurant.databinding.FragmentRegisterUserBinding
import com.devmobile.android.restaurant.databinding.LayoutTextInputBinding
import com.devmobile.android.restaurant.model.repository.remotedata.RegisterRepository
import com.devmobile.android.restaurant.view.customelements.LoadingTransition
import com.devmobile.android.restaurant.viewmodel.RegisterViewModel
import com.devmobile.android.restaurant.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class RegisterFragment : AppCompatActivity(), IShowError {
    private lateinit var registerBinding: FragmentRegisterUserBinding

    private lateinit var textUserName: LayoutTextInputBinding
    private lateinit var textUserLastName: LayoutTextInputBinding
    private lateinit var textUserEmail: LayoutTextInputBinding
    private lateinit var textUserPassword: LayoutTextInputBinding

    private val registerRepository = RegisterRepository(this)
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(registerRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerBinding = DataBindingUtil.setContentView(
            this, R.layout.fragment_register_user
        )

        registerBinding.lifecycleOwner = this
        registerBinding.registerView = this

        // initialize variables
        textUserName = registerBinding.textUserName
        textUserLastName = registerBinding.textUserLastName
        textUserEmail = registerBinding.textUserEmail
        textUserPassword = registerBinding.textUserPassword

        // methods
        subscribeObservables()
        setTextInputParameters()
    }

    private fun setTextInputParameters() {

        // Set Hints
        registerBinding.textUserName.textInputForm.hint = "Username *"
        registerBinding.textUserLastName.textInputForm.hint = "Lastname"
        registerBinding.textUserEmail.textInputForm.hint = "UserEmail *"
        registerBinding.textUserPassword.textInputForm.hint = "Password *"

        // Set InputType
        textUserName.textInputEditText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        textUserLastName.textInputEditText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        textUserEmail.textInputEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        textUserPassword.textInputEditText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Set icons
        textUserPassword.textInputForm.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        textUserPassword.textInputForm.isEndIconVisible = true

        // Enable Counter
        textUserPassword.textInputForm.isCounterEnabled = true
    }

    private fun subscribeObservables() {

        registerViewModel.userNameError.observe(this@RegisterFragment) { error ->

            handleError(textUserPassword, error)
        }

        registerViewModel.userEmailError.observe(this@RegisterFragment) { error ->

            handleError(textUserPassword, error)
        }

        registerViewModel.userPasswordError.observe(this@RegisterFragment) { error ->

            handleError(textUserPassword, error)
        }

        registerViewModel.loadingProgress.observe(this@RegisterFragment) { loadState ->

            handleLoadState(loadState)
        }
    }

    private fun handleError(inputText: LayoutTextInputBinding, error: String?) {

        if (error == RegisterViewModel.VALID_DATA) {

            inputText.textInputForm.error = null

        } else {

            inputText.textInputForm.error = error
        }
    }

    private fun handleLoadState(loadState: LoadState) {

        when (loadState) {

            is LoadState.Loading -> {

                LoadingTransition.getInstance(R.layout.layout_loading)
                    .start(supportFragmentManager, R.id.registerContainer)
            }

            is LoadState.NotLoading -> {

                Log.i("Test", "Passou aqui")
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        startActivity(Intent(this@RegisterFragment, MainActivity::class.java))
                        finish()
                    }, 3000
                )

                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        LoadingTransition.getInstance(null).stop(supportFragmentManager)

                    }, 4000
                )
            }

            is LoadState.Error -> {

                LoadingTransition.getInstance(null).stop(supportFragmentManager)

                showErrorMessage(loadState.error.message ?: "Error")
            }
        }
    }

    private fun opacityViews() {
        textUserName.textInputForm.visibility = View.GONE
        textUserLastName.textInputForm.visibility = View.GONE
        textUserEmail.textInputForm.visibility = View.GONE
        textUserPassword.textInputForm.visibility = View.GONE
    }

    @CalledFromXML
    fun startRequestRegister() {

        val userName = textUserName.textInputForm.editText?.text.toString()
        val userLastName = textUserLastName.textInputForm.editText?.text.toString()
        val userEmail = textUserEmail.textInputForm.editText?.text.toString()
        val userPassword = textUserPassword.textInputForm.editText?.text.toString()

        registerViewModel.registerTrigger(userName, userLastName, userEmail, userPassword)
    }

    @CalledFromXML
    fun cancelRegister() {

        finish()
    }

    override fun showErrorMessage(errorMessage: String) {
        val mySnackBar = Snackbar.make(registerBinding.registerContainer, errorMessage, 2000)

        mySnackBar.setAction("OK") {
            mySnackBar.dismiss()
        }.show()
    }
}