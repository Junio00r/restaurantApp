package com.devmobile.android.restaurant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devmobile.android.restaurant.Food
import com.devmobile.android.restaurant.enums.FoodSection

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(foods: List<Food>)

    /**
     *  @return a value of lines quantity deleted
     */
    @Delete
    fun deleteAll(foods: List<Food>)

    /**
     *  @return a value of lines quantity updated
     */
    @Update
    fun updateFood(foods: List<Food>)

    /*
     * Métodos de consultas usado para instrucoes mais especificas e complexas
     * O Room avalia as consultas no momento da compilação, evitando problemas
     * no momento de execucao, caso tenha um erro de compilação é lançado.
     **/

    /**
     * Retrieves a list of Food items with the same section as specified by [foodSection].
     *
     * @param foodSection the section to search for in the database
     * @return a list of Food items with the specified section, or null if not found
     */
    @Query("SELECT * FROM foods WHERE section = :foodSection")
    fun getFoodsBySection(foodSection: FoodSection): List<Food?>

    /**
     * Retrieves a Food item with the specified ID.
     *
     * @param foodId the ID of the food to retrieve
     * @return the Food item with the specified ID, or null if not found
     */
    @Query("SELECT * FROM foods WHERE mId = :foodId")
    fun getFoodById(foodId: Long): Food?

    /**
     * Retrieves the total number of foods in the database.
     *
     * @return the total number of foods in the database
     */
    @Query("SELECT COUNT(mId) FROM foods")
    fun getFoodsSize(): Int

    /**
     * Retrieves all foods stored in the database.
     *
     * @return a list containing all Food items stored in the database
     */
    @Query("SELECT * FROM foods")
    fun getAllFoods(): List<Food?>

    /**
     * Deletes all foods stored in the database.
     */
    @Query("DELETE FROM foods")
    fun deleteAllTable()

}
