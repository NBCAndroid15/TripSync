package com.example.tripsync.ui.fragment.plan

import android.os.Parcelable
import com.example.tripsync.model.Plan
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PlanViewType : Parcelable {

    data object Normal : PlanViewType() {
        const val INT = 0
    }

    data object Edit : PlanViewType() {
        const val INT = 1
    }
}