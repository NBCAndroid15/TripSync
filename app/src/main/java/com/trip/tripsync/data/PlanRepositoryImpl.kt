package com.trip.tripsync.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.trip.tripsync.model.Plan
import com.trip.tripsync.model.User
import com.trip.tripsync.model.UserPlan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PlanRepositoryImpl {
    private val plansRef = FirebaseFirestore.getInstance().collection("plans")

    suspend fun getPlanList(): List<Plan>? = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = plansRef.get().await()
            val curUserUid = curUser.uid

            if (result.documents.size > 0) {
                result.documents.map {
                    it.toObject(UserPlan::class.java)
                }.flatMap {
                    it?.planList ?: listOf()
                }.filter {
                    it.group?.contains(curUserUid) ?: false
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getPlanListSnapshot(): Flow<List<Plan>> = callbackFlow {
        var eventsCollection: CollectionReference? = null
        val curUser = FirebaseAuth.getInstance().currentUser
        val curUserUid = curUser?.uid

        try {
            eventsCollection = FirebaseFirestore.getInstance()
                .collection("plans")
        } catch (e: Throwable) {
            close(e)
        }

        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->

            if (snapshot == null) {
                trySend(listOf<Plan>())
                return@addSnapshotListener
            }

            try {
                trySend(snapshot.documents.map {
                    it.toObject(UserPlan::class.java)
                }.flatMap {
                    it?.planList ?: listOf()
                }.filter {
                    it.group?.contains(curUserUid) ?: false
                })
            } catch (e: Throwable) {
                trySend(listOf<Plan>())
            }
        }
        awaitClose { subscription?.remove() }
    }.buffer(Channel.UNLIMITED)

    suspend fun isExistPlan(plan: Plan) =
        withContext(Dispatchers.IO) {
            try {
                val planOwnerUid = plan.group?.get(0) ?: ""
                val result = plansRef.whereEqualTo("uid", planOwnerUid).get().await()

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
            val planOwnerUid = plan.group?.get(0) ?: ""
            val result = plansRef.whereEqualTo("uid", planOwnerUid).get().await()

            if (isExistPlan(plan)) return@withContext null

            if (result.documents.size == 0) {
                val userPlan = UserPlan(planOwnerUid, listOf(plan))
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

    suspend fun updatePlan(plan: Plan) = withContext(Dispatchers.IO) {
        try {
            val planOwnerUid = plan.group?.get(0) ?: ""
            val result = plansRef.whereEqualTo("uid", planOwnerUid).get().await()

            if (result.documents.size == 0) {
                val userPlan = UserPlan(planOwnerUid, listOf(plan))
                plansRef.document().set(userPlan).await()
            } else {
                val userPlan = result.documents[0].toObject(UserPlan::class.java)
                val planList = userPlan?.planList?.toMutableList() ?: mutableListOf()

                val idx = planList.indexOfFirst {
                    it.title == plan.title
                }
                Log.d("planIdx", idx.toString())

                if (idx > -1) {

                    planList[idx] = plan
                    val newPlan = userPlan?.copy(planList = planList) ?: UserPlan()
                    plansRef.document(result.documents[0].id).set(newPlan).await()
                } else {
                    null
                }
            }

        } catch (e: Exception) {
            Log.d("fbadd", e.message ?: "unknown error")
            null
        }
    }

    suspend fun deletePlan(plan: Plan) = withContext(Dispatchers.IO) {
        try {
            val planOwnerUid = plan.group?.get(0) ?: ""
            val result = plansRef.whereEqualTo("uid", planOwnerUid).get().await()

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