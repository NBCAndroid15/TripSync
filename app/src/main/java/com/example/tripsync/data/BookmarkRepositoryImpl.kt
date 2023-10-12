package com.example.tripsync.data

import android.util.Log
import com.example.tripsync.model.Bookmark
import com.example.tripsync.model.User
import com.example.tripsync.model.UserBookmark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookmarkRepositoryImpl {

    private val bookmarksRef = FirebaseFirestore.getInstance().collection("bookmarks")

    suspend fun getBookmarkList(): List<Bookmark>? = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

            if (result.documents.size > 0) result.documents[0].toObject(UserBookmark::class.java)?.bookmarkList else null

        } catch (e: Exception) {
            null
        }
    }

    suspend fun isExistBookmark(bookmark: Bookmark): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext false
                val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

                if (result.documents.size == 0) {
                    false
                } else {
                    val userBookmark = result.documents[0].toObject(UserBookmark::class.java)
                    val bookmarkList = userBookmark?.bookmarkList?.toMutableList()
                    bookmarkList?.find {
                        it.title == bookmark.title && it.imageUrl == bookmark.imageUrl && it.content == bookmark.content
                    } != null
                }

            } catch (e: Exception) {
                Log.d("fbisexist", e.message ?: "unknown error")
                false
            }
        }


    suspend fun addBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

            if (isExistBookmark(bookmark)) return@withContext null

            if (result.documents.size == 0) {
                val userBookmark = UserBookmark(curUser.uid, listOf(bookmark))
                bookmarksRef.document().set(userBookmark).await()
            } else {
                val userBookmark = result.documents[0].toObject(UserBookmark::class.java)
                val bookmarkList = userBookmark?.bookmarkList?.toMutableList()
                bookmarkList?.add(bookmark)
                val newBookmark = userBookmark?.copy(bookmarkList = bookmarkList) ?: UserBookmark()
                bookmarksRef.document(result.documents[0].id).set(newBookmark).await()
            }

        } catch (e: Exception) {
            Log.d("fbadd", e.message ?: "unknown error")
            null
        }
    }

    suspend fun deleteBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        try {
            val curUser = FirebaseAuth.getInstance().currentUser ?: return@withContext null
            val result = bookmarksRef.whereEqualTo("uid", curUser.uid).get().await()

            if (result.documents.size == 0) {
                null
            } else {
                val userBookmark = result.documents[0].toObject(UserBookmark::class.java)
                val bookmarkList = userBookmark?.bookmarkList?.toMutableList()
                bookmarkList?.remove(bookmarkList.find {
                    it.title == bookmark.title && it.imageUrl == bookmark.imageUrl && it.content == bookmark.content
                })
                val newBookmark = userBookmark?.copy(bookmarkList = bookmarkList) ?: UserBookmark()
                bookmarksRef.document(result.documents[0].id).set(newBookmark).await()
            }


        } catch (e: Exception) {
            Log.d("fbdelete", e.message ?: "unknown error")
            null
        }
    }

}