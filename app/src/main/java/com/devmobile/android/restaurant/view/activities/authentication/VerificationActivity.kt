package com.devmobile.android.restaurant.view.activities.authentication

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import com.devmobile.android.restaurant.IShowError
import com.devmobile.android.restaurant.R
import com.devmobile.android.restaurant.databinding.ActivityVerificationCodeBinding
import com.devmobile.android.restaurant.extensions.maxLength
import com.devmobile.android.restaurant.model.repository.authentication.VerificationRepository
import com.devmobile.android.restaurant.view.customelements.TextInput
import com.devmobile.android.restaurant.viewmodel.ViewModelFactory
import com.devmobile.android.restaurant.viewmodel.authentication.VerificationViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class VerificationActivity : AppCompatActivity(), IShowError {

    // references
    private lateinit var _viewBinding: ActivityVerificationCodeBinding
    private val _repository = VerificationRepository(this)
    private val _viewModel: VerificationViewModel by viewModels {

        ViewModelFactory(
            repository = _repository,
            ownerOfStateToSave = this,
            defaultValuesForNulls = null,
        )
    }

    // data
    private val _numbers = ArrayList<TextInput>()

    // Functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(_viewBinding.root)

        _viewBinding.viewModel = _viewModel

        _numbers.addAll(
            arrayOf(
                _viewBinding.number1,
                _viewBinding.number2,
                _viewBinding.number3,
                _viewBinding.number4,
                _viewBinding.number5,
                _viewBinding.number6
            )
        )

        setObservables()
        drawingViews()
    }

    private fun drawingViews() {

        _numbers.forEach { number ->

            // InputType
            number.getTextInputEditText().inputType = InputType.TYPE_CLASS_NUMBER

            // Text Alignment
            number.getTextInputEditText().gravity = Gravity.CENTER

            // Max Chars in Text
            number.getTextInputEditText().maxLength(1)

            number.updateLayoutParams { height = 300 }
            number.getTextInput().updateLayoutParams { height = 300 }

            number.getTextInput().isErrorEnabled = true
            number.getTextInput().errorIconDrawable = null
            number.getTextInput().isHelperTextEnabled = false
        }

        restoreFocus()
    }

    private fun setObservables() {

        _viewBinding.toolbarBack.setNavigationOnClickListener {

            finish()
        }

        _viewModel.codeErrorPropagator.observe(this@VerificationActivity) { error ->

            _numbers.forEach { it.getTextInput().error = "Invalid Code" }
            showErrorMessage(error ?: "Invalid Code")
        }

        // about inputs
        _viewModel.canStillEnterCodes.observe(this@VerificationActivity) { mayEnableInput ->

            _numbers.forEach { it.isEnabled = mayEnableInput }
        }


        _viewModel.canResendCode.observe(this@VerificationActivity) { canResendCode ->

            changedResendTextColor(canResendCode)

            if (!canResendCode) {

                clearInput(_numbers.map { it.getTextInputEditText() })
                _numbers.forEach { it.getTextInput().error = null }
                restoreFocus()
            }
        }

        _numbers[0].getTextInputEditText().doAfterTextChanged {

            _viewModel.saveCode1(it.toString())
            restoreFocus()
        }
        _numbers[1].getTextInputEditText().doAfterTextChanged {

            _viewModel.saveCode2(it.toString())
            restoreFocus()
        }
        _numbers[2].getTextInputEditText().doAfterTextChanged {

            _viewModel.saveCode3(it.toString())
            restoreFocus()
        }
        _numbers[3].getTextInputEditText().doAfterTextChanged {

            _viewModel.saveCode4(it.toString())
            restoreFocus()
        }
        _numbers[4].getTextInputEditText().doAfterTextChanged {

            _viewModel.saveCode5(it.toString())
            restoreFocus()
        }
        _numbers[5].getTextInputEditText().doAfterTextChanged {

            _viewModel.saveCode6(it.toString())
            restoreFocus()
        }
    }

    private fun restoreFocus() {

        _numbers.firstOrNull {

            it.getTextInputEditText().text.isNullOrEmpty()
        }?.requestFocus() ?: _viewModel.sendCodesInsertedTrigger()
    }

    private fun <T : EditText> clearInput(inputs: Collection<T>) {

        inputs.forEach {

            it.text.clear()
        }
    }

    private fun changedResendTextColor(wasCodeResent: Boolean) {

        when (wasCodeResent) {

            true -> {

                _viewBinding.textResendCode.setTextColor(

                    ColorStateList.valueOf(
                        getColor(
                            R.color.black_two
                        )
                    )
                )
            }

            false -> {

                _viewBinding.textResendCode.setTextColor(

                    ColorStateList.valueOf(
                        getColor(
                            R.color.gray_two
                        )
                    )
                )
            }
        }
    }

    override fun showErrorMessage(errorMessage: String) {

        val mySnackBar = Snackbar.make(_viewBinding.container, errorMessage, 1000)

        mySnackBar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        mySnackBar.setBackgroundTintList(ColorStateList.valueOf(this.getColor(R.color.red_light)))
        mySnackBar.show()
    }
}