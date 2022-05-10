/*package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import network.RetrofitResponseModel

@Entity
data class ProductsEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "stock") val stock: Int,
    @ColumnInfo(name = "regularPrice") val regularPrice: Float,
    @ColumnInfo(name = "discountPrice") val discountPrice: Float,
    @ColumnInfo(name = "available") val available: Boolean,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @PrimaryKey(autoGenerate = true) val baseId: Int = 0
)

fun RetrofitResponseModel.toProductsEntity(): ProductsEntity {
    return ProductsEntity(
        id,
        name,
        description,
        stock,
        regularPrice,
        discountPrice,
        available,
        imageUrl
    )
}

fun List<RetrofitResponseModel>?.toProductsEntityList(): List<ProductsEntity> {
    return this?.map { it.toProductsEntity() } ?: emptyList()
}*/