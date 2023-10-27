package com.example.tripsync.data

import android.util.Log
import com.example.tripsync.model.Travel
import com.example.tripsync.model.UserBookmark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookmarkRepositoryImpl {

    private val bookmarksRef = FirebaseFirestore.getInstance().collection("bookmarks")

    suspend fun getBookmarkList(): List<Travel>? = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

            if (result.documents.size > 0) result.documents[0].toObject(UserBookmark::class.java)?.travelList else null

        } catch (e: Exception) {
            null
        }
    }

    suspend fun isExistBookmark(travel: Travel): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext false
                val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

                if (result.documents.size == 0) {
                    false
                } else {
                    val userBookmark = result.documents[0].toObject(UserBookmark::class.java)
                    val bookmarkList = userBookmark?.travelList?.toMutableList()
                    bookmarkList?.find {
                        it.title == travel.title && it.imageUrl == travel.imageUrl
                    } != null
                }

            } catch (e: Exception) {
                Log.d("fbisexist", e.message ?: "unknown error")
                false
            }
        }


    suspend fun addBookmark(travel: Travel) = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

            if (isExistBookmark(travel)) return@withContext null

            if (result.documents.size == 0) {
                val userBookmark = UserBookmark(curUser.uid, listOf(travel))
                bookmarksRef.document().set(userBookmark).await()
                listOf(travel)
            } else {
                val userBookmark = result.documents[0].toObject(UserBookmark::class.java)
                val bookmarkList = userBookmark?.travelList?.toMutableList() ?: mutableListOf()
                bookmarkList.add(travel)
                val newBookmark = userBookmark?.copy(travelList = bookmarkList) ?: UserBookmark()
                bookmarksRef.document(result.documents[0].id).set(newBookmark).await()
                bookmarkList
            }

        } catch (e: Exception) {
            Log.d("fbadd", e.message ?: "unknown error")
            null
        }
    }

    suspend fun deleteBookmark(travel: Travel) = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

            if (result.documents.size == 0) {
                null
            } else {
                val userBookmark = result.documents[0].toObject(UserBookmark::class.java)
                val bookmarkList = userBookmark?.travelList?.toMutableList()
                bookmarkList?.remove(bookmarkList.find {
                    it.title == travel.title && it.imageUrl == travel.imageUrl
                })
                val newBookmark = userBookmark?.copy(travelList = bookmarkList) ?: UserBookmark()
                bookmarksRef.document(result.documents[0].id).set(newBookmark).await()
                bookmarkList
            }


        } catch (e: Exception) {
            Log.d("fbdelete", e.message ?: "unknown error")
            null
        }
    }

}