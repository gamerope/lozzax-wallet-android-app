package io.gamerope.wallet.data.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "swapRecords", indices = [Index(value = arrayOf("swapId"), unique = true)])
data class SwapRecord @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0,
    @ColumnInfo
    var swapId: String,
    @ColumnInfo
    var amountFrom: String = "",
    @ColumnInfo
    var currencyFrom: String = "lozz",
    @ColumnInfo
    var amountTo: String = "",
    @ColumnInfo
    var currencyTo: String = "",
    @ColumnInfo
    var createdAt: String = "0"
) : Parcelable