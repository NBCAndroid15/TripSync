package com.example.tripsync.model

import android.os.Parcelable
import com.example.tripsync.ui.fragment.plan.TestModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Travel(
    val imageUrl: String? = null,
    val title: String? = null,
    val addr: String? = null,
    val mapX: Double? = null,
    val mapY: Double? = null,
    val category: String? = null,
    val contentId: Int? = null,
    val contentTypeId: Int? = null,
    val area: String? = null,
    val tel: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
) : Parcelable

fun Travel.toTestModel() = TestModel(
    imageUrl = imageUrl,
    title = title,
    addr = addr,
    mapX = mapX,
    mapY = mapY,
    category = category,
    area = area,
    tel = tel

)
