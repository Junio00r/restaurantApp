package com.devmobile.android.restaurant

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout.LayoutParams
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import com.devmobile.android.restaurant.viewholders.FoodCardViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.io.FilterInputStream
import java.io.FilterWriter


class ModalBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var viewInflate: View
    private lateinit var inputQuantity: TextInputEditText
    private lateinit var foodCard: FoodCardViewHolder
    lateinit var bottomSheetHidedNotification: BottomSheetNotification

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewInflate = inflater.inflate(R.layout.modal_bottomsheet_layout, container, false)
        val bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
        val bottomSheetLayoutRoot: ViewGroup =
            viewInflate.findViewById(R.id.frameBottomSheetFoodSelectedBottomSheet)
        bottomSheetBehavior.peekHeight = bottomSheetLayoutRoot.resources.displayMetrics.heightPixels

        // AddedImageFood
        var foodView: ShapeableImageView = viewInflate.findViewById(R.id.imageFoodBottomSheet)
        val drawable = foodCard.imageFood.drawable
        foodView.setImageDrawable(drawable)
        foodView.scaleType = ImageView.ScaleType.CENTER_CROP
        val bsFoodPreferences: TextInputEditText =
            viewInflate.findViewById(R.id.textFoodPreferencesDescriptionsBottomSheet)
        bsFoodPreferences.letterSpacing = 0.04f
        var descriptionsHeight = bsFoodPreferences.height

        bsFoodPreferences.post {
            val teste = bsFoodPreferences.width
            bsFoodPreferences.filters =
                arrayOf(InputFilter.LengthFilter(bsFoodPreferences.width / 8))

            descriptionsHeight = bsFoodPreferences.height
        }
        inputQuantity =
            bottomSheetLayoutRoot.findViewById(R.id.edittextFoodQuantityPedidoBottomSheet)
        val descriptions: MaterialTextView =
            bottomSheetLayoutRoot.findViewById(R.id.textFoodDescriptionBottomSheet)
        val bsFoodPreferencesHeight = bsFoodPreferences.height
        var inputQuantityHasFocused = false

        inputQuantity.setOnFocusChangeListener { view, hasFocus ->

            inputQuantityHasFocused = hasFocus
        }

        activity?.let {
            KeyboardVisibilityEvent.setEventListener(
                it,
                object : KeyboardVisibilityEventListener {
                    override fun onVisibilityChanged(isOpen: Boolean) {

                        if (isOpen) {
                            if (inputQuantityHasFocused) {
                                descriptions.updateLayoutParams { this.height -= 100 }

                                bsFoodPreferences.updateLayoutParams { this.height = 0 }
                                return
                            }
                        }

                        if (inputQuantityHasFocused) {
                            descriptions.updateLayoutParams { this.height += 100 }
                            bsFoodPreferences.updateLayoutParams { this.height = descriptionsHeight }
                        }
                    }
                }
            )
        }

        init()
        return viewInflate
    }

    companion object {
        const val TAG = "ModalBottomSheet"
        private const val INITIAL_COUNT = 1
    }

    override fun onClick(v: View) {

        if (v.id == R.id.buttonDecrementQuantityBottomSheet) {

            if (getEdittextFoodQuantity() - 1 >= 0) {

                inputQuantity.setText((getEdittextFoodQuantity() - 1).toString())
            }

        } else if (v.id == R.id.buttonIncrementQuantityBottomSheet) {

            if ((getEdittextFoodQuantity() + 1).toString()
                    .count() <= inputQuantity.getMaxLength()
            ) {

                inputQuantity.setText((getEdittextFoodQuantity() + 1).toString())
            }
        }
    }

    private fun init() {
        viewInflate.findViewById<Button>(R.id.buttonDecrementQuantityBottomSheet)
            .setOnClickListener(this)
        viewInflate.findViewById<Button>(R.id.buttonIncrementQuantityBottomSheet)
            .setOnClickListener(this)


        inputQuantity = viewInflate.findViewById(R.id.edittextFoodQuantityPedidoBottomSheet)
        inputQuantity.setText(INITIAL_COUNT.toString())
    }

    private fun getEdittextFoodQuantity(): Int {

        return viewInflate.findViewById<TextInputEditText>(R.id.edittextFoodQuantityPedidoBottomSheet).text.toString()
            .toInt()
    }

    private fun TextInputEditText.getMaxLength(): Int {
        filters.forEach {
            if (it is InputFilter.LengthFilter) {
                return it.max
            }
        }

        return -1
    }

    fun setBottomSheetAttributes(v: FoodCardViewHolder) {
        foodCard = v
    }
}