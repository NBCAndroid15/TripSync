package com.example.tripsync.data

import android.util.Log
import com.example.tripsync.model.Plan
import com.example.tripsync.model.UserPlan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PlanRepositoryImpl {
    private val plansRef = FirebaseFirestore.getInstance().collection("plans")

    suspend fun getPlanList(): List<Plan>? = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = plansRef.whereEqualTo("uid", curUser.uid).get().await()

            if (result.documents.size > 0) result.documents[0].toObject(UserPlan::class.java)?.planList else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun isExistPlan(plan: Plan) =
        withContext(Dispatchers.IO) {
            try {
                val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext false
                val result = plansRef.whereEqualTo("uid", curUser.uid).get().await()

                if (result.documents.size == 0) {
                    false
                } else {
                    val userPlan = result.documents[0].toObject(UserPlan::class.java)
                    val planList = userPlan?.planList?.toMutableList()
                    planList?.find {
                        it.title == plan.title
                    } != null
                }

            } catch (e: Exception) {
                Log.d("fbisexist", e.message ?: "unknown error")
                false
            }
        }


    suspend fun addPlan(plan: Plan) = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = plansRef.whereEqualTo("uid", curUser.uid).get().await()

            if (isExistPlan(plan)) return@withContext null

            if (result.documents.size == 0) {
                val userPlan = UserPlan(curUser.uid, listOf(plan))
                plansRef.document().set(userPlan).await()
            } else {
                val userPlan = result.documents[0].toObject(UserPlan::class.java)
                val planList = userPlan?.planList?.toMutableList() ?: mutableListOf()
                planList.add(plan)
                val newPlan = userPlan?.copy(planList = planList) ?: UserPlan()
                plansRef.document(result.documents[0].id).set(newPlan).await()
            }

        } catch (e: Exception) {
            Log.d("fbadd", e.message ?: "unknown error")
            null
        }
    }

    suspend fun deletePlan(plan: Plan) = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = plansRef.whereEqualTo("uid", curUser.uid).get().await()

            if (result.documents.size == 0) {
                null
            } else {
                val userPlan = result.documents[0].toObject(UserPlan::class.java)
                val planList = userPlan?.planList?.toMutableList()
                planList?.remove(planList.find {
                    it.title == plan.title
                })
                val newBookmark = userPlan?.copy(planList = planList) ?: UserPlan()
                plansRef.document(result.documents[0].id).set(newBookmark).await()
            }

        } catch (e: Exception) {
            Log.d("fbdelete", e.message ?: "unknown error")
            null
        }
    }

}