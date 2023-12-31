package com.trip.tripsync.model

import android.os.Parcelable
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
    var isChecked: Boolean = false
) : Parcelable




