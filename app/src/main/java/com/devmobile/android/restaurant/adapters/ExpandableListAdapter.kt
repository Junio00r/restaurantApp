package com.devmobile.android.restaurant.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import com.devmobile.android.restaurant.DecimalNumberFormatted
import com.devmobile.android.restaurant.R
import com.devmobile.android.restaurant.enums.FoodSection
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import java.util.ArrayList
import java.util.LinkedList

class ExpandableListAdapter(

    private val context: Context,
    private val expandableData: HashMap<FoodSection, ArrayList<Array<*>>>,

    ) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {

        return expandableData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {

        return expandableData[FoodSection.entries[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): ArrayList<Array<*>> {

        return expandableData[FoodSection.entries[groupPosition]]!!
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {

        return expandableData[FoodSection.entries[groupPosition]]!![groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {

        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {

        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {

        return false
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?
    ): View? {

        var view = convertView

        if (view == null) {

            val layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.expandable_group_list_layout, null)
        }

        view?.let {

            setGroupResources(
                it,
                isExpanded,
                groupPosition,
                expandableData.keys.elementAt(groupPosition),
                expandableData[FoodSection.entries[groupPosition]]!!

            )
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setGroupResources(
        v: View,
        isExpanded: Boolean,
        groupPosition: Int,
        foodSection: FoodSection,
        groupListData: ArrayList<Array<*>>
    ) {

        val textGroupList: MaterialTextView = v.findViewById(R.id.textQuantityAndFood)
        val textValueTotalOfGroup: MaterialTextView = v.findViewById(R.id.textTotalValueForSection)

        var qtdFoodForSection = 0
        var valueTotalForSection = 0F

        // Soma a quantida na determinada seção
        groupListData.forEach { qtdFoodForSection += it[4].toString().toInt() }

        // Soma o valor em cada grupo da lista
        groupListData.forEach {

            valueTotalForSection += it[4].toString().toFloat() * it[2].toString().toFloat()
        }

        // Preenche os texts das views
        textGroupList.text = "${qtdFoodForSection}x  ${foodSection.getFoodSectionName()}"
        textValueTotalOfGroup.text = DecimalNumberFormatted.format(valueTotalForSection)
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        var view = convertView

        if (convertView == null) {

            val layoutInflater = LayoutInflater.from(context)
            view = layoutInflater.inflate(R.layout.expanadable_child_list_expandable, null)
        }

        view?.let {

            setChildResources(
                it,
                groupPosition,
                childPosition,
                expandableData.keys.elementAt(groupPosition),
                expandableData[FoodSection.entries[groupPosition]]!!

            )
        }

        return view!!
    }

    @SuppressLint("SetTextI18n")
    private fun setChildResources(
        v: View,
        groupPosition: Int,
        childPosition: Int,
        foodSection: FoodSection,
        groupListData: ArrayList<Array<*>>
    ) {

        val textChild: MaterialTextView = v.findViewById(R.id.textItemName)
        val textValueTotalOfGroup: MaterialTextView = v.findViewById(R.id.textItemPrice)

        val qtdFoodForSection = groupListData[childPosition][4]
        var valueTotalForQuantityFood = 0F

        // Soma o valor em cada grupo da lista
        valueTotalForQuantityFood = groupListData[childPosition][4].toString()
            .toInt() * groupListData[childPosition][2].toString().toFloat()

        // Preenche os texts das views
        textChild.text =
            "+ ${qtdFoodForSection}x ${groupListData[childPosition][1].toString()}"
        textValueTotalOfGroup.text = DecimalNumberFormatted.format(valueTotalForQuantityFood)
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {

        return false
    }
}