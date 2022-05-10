/*package com.carmelart.homeart.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import database.ProductsEntity

@Dao
interface ProductsDAO {

    @Query("SELECT * FROM ProductsEntity")
    fun getAllProducts(): List<ProductsEntity>

    @Query("SELECT * FROM ProductsEntity WHERE id = :productId LIMIT 1")
    fun getOneProduct(productId: String): ProductsEntity

    @Insert
    fun addProduct(product: ProductsEntity)

    @Update
    fun updateProduct(product: ProductsEntity)

    @Delete
    fun deleteProduct(product: ProductsEntity)

}*/



