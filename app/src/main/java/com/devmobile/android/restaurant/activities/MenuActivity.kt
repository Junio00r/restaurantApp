package com.devmobile.android.restaurant.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.devmobile.android.restaurant.FoodSelectedCallback
import com.devmobile.android.restaurant.InsertDataOnDatabase
import com.devmobile.android.restaurant.R
import com.devmobile.android.restaurant.RestaurantDatabase
import com.devmobile.android.restaurant.adapters.FragmentTabAdapter
import com.devmobile.android.restaurant.databinding.ActivityMenuBinding
import com.devmobile.android.restaurant.enums.FoodSection
import com.devmobile.android.restaurant.viewholders.FragmentTabFoodSection
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator


class MenuActivity : FragmentActivity(), FoodSelectedCallback, View.OnClickListener {

    private lateinit var binding: ActivityMenuBinding

    private lateinit var tabFragmentsInstances: Array<FragmentTabFoodSection>
    private lateinit var floatingButtonCancelFoodOrder: MaterialButton
    private lateinit var floatingButtonPayFoods: MaterialButton
    private lateinit var fragmentTabAdapter: FragmentTabAdapter
    private var dataToRealizarPagamento = ArrayList<Array<*>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        val teste = RestaurantDatabase.getInstance(this)
        teste.clearAllTables()

        InsertDataOnDatabase.addFoodDataToDatabase(this)
        setTabLayouts()
        setExtendedFAT()
    }

    // Métodos de inicialização...
    private fun setTabLayouts() {

        val tabsNameId = addTabsName()

        tabFragmentsInstances = addFragmentsTabSection()
        tabFragmentsInstances.forEach {

            it.addFoodAddedCallback(this)
        }

        val tabLayout = binding.tabFoodSectionsMenuActivity
        val viewPager2 = binding.pagerFoodSectionsMenuActivity
        fragmentTabAdapter = FragmentTabAdapter(this, tabFragmentsInstances)

        viewPager2.adapter = fragmentTabAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = getString(tabsNameId[position])
        }.attach()
    }

    private fun addTabsName(): Array<Int> {

        return arrayOf(
            R.string.tab_item_entradas,
            R.string.tab_item_pratos_principais,
            R.string.tab_item_bebidas,
            R.string.tab_item_sobremesas,
        )
    }

    private fun addFragmentsTabSection(): Array<FragmentTabFoodSection> {

        return arrayOf(
            FragmentTabFoodSection(this, R.layout.tab_food_section_layout, FoodSection.ENTRADA),
            FragmentTabFoodSection(this, R.layout.tab_food_section_layout, FoodSection.PRINCIPAL),
            FragmentTabFoodSection(this, R.layout.tab_food_section_layout, FoodSection.BEBIDA),
            FragmentTabFoodSection(this, R.layout.tab_food_section_layout, FoodSection.SOBREMESA)
        )
    }

    private fun setExtendedFAT() {

        floatingButtonCancelFoodOrder = binding.floatingButtonCancelFoodOrder
        floatingButtonPayFoods = binding.floatingButtonPayFoods
        floatingButtonCancelFoodOrder.setOnClickListener(this)
        floatingButtonPayFoods.setOnClickListener(this)
    }

    private fun showWarning() {

        val message = "Selecione uma comida para Realizar o Pedido!"
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)

        toast.setGravity(Gravity.BOTTOM, 0, 440)
        toast.show()
    }

    override fun onClick(v: View) {

        when (v) {

            floatingButtonCancelFoodOrder -> {

                if (canPlaceOrder()) tabFragmentsInstances!!.forEach { it.cancelFoodSelected() }
            }

            floatingButtonPayFoods -> {

                if (canPlaceOrder()) startFinalizeOrderFragmentActivity()
                else showWarning()
            }
        }
    }

    private fun canPlaceOrder(): Boolean {

        return fragmentTabAdapter.getQuantityOfFoodsSelected() > 0
    }

    private fun startFinalizeOrderFragmentActivity() {

        val intent = Intent(this, FinalizeOrderFragment::class.java)

        insertDataInIntent(intent)
        startActivity(intent)
    }

    private fun insertDataInIntent(intent: Intent) {
        var foodId: Long
        var foodName: String
        var foodPrice: Float
        var foodSectionOrdinal: Int
        var quantityAdded: Int

        dataToRealizarPagamento.forEachIndexed { index, item ->
            foodId = item[0].toString().toLong()
            foodName = item[1].toString()
            foodPrice = item[2].toString().toFloat()
            foodSectionOrdinal = item[3].toString().toInt()
            quantityAdded = item[4].toString().toInt()

            intent.putExtra("foodId_$index", foodId)
            intent.putExtra("foodName_$index", foodName)
            intent.putExtra("foodPrice_$index", foodPrice)
            intent.putExtra("foodSectionOrdinal_$index", foodSectionOrdinal)
            intent.putExtra("quantityAdded_$index", quantityAdded)
        }
    }

    // Métodos de implementacoes de interfaces criadas
    override fun onAddedFood(
        foodId: Long,
        foodName: String?,
        foodPrice: Float?,
        sectionOnSelectedFoodOrdinal: FoodSection?,
        quantityAdded: Int?
    ) {

        dataToRealizarPagamento.add(
            arrayOf(
                foodId, foodName, foodPrice, sectionOnSelectedFoodOrdinal!!.ordinal, quantityAdded
            )
        )
    }

    override fun onRemoveFood(foodId: Long, sectionOnSelectedFood: FoodSection?) {

        dataToRealizarPagamento.removeIf { it[0] == foodId }
    }
}