package com.example.tripsync.data

import android.util.Log
import com.example.tripsync.model.Post
import com.example.tripsync.model.UserPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CommunityRepositoryImpl {
    private val postsRef = FirebaseFirestore.getInstance().collection("posts")

    suspend fun addPost(post: Post) = withContext(Dispatchers.IO) {
        try {
            val postOwnerUid = FirebaseAuth.getInstance().currentUser?.uid
            val result = postsRef.whereEqualTo("uid", postOwnerUid).get().await()

            if (result.documents.size == 0) {
                val userPost = UserPost(postOwnerUid, listOf(post))
                postsRef.document().set(userPost).await()
            } else {
                val userPost = result.documents[0].toObject(UserPost::class.java)
                val postList = userPost?.postList?.toMutableList()
                postList?.add(post)
                val newPost = userPost?.copy(postList = postList) ?: UserPost()
                postsRef.document(result.documents[0].id).set(newPost).await()
                Log.d("test1", result.documents[0].id)

            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun removePost(post: Post) = withContext(Dispatchers.IO) {
        try {
            val postOwnerUid = FirebaseAuth.getInstance().currentUser?.uid
            val result = postsRef.whereEqualTo("uid",postOwnerUid ).get().await()

            if (result.documents.size == 0) {
                null
            } else {
                val userPost = result.documents[0].toObject(UserPost::class.java)
                val postList = userPost?.postList?.toMutableList()

                postList?.removeIf { it.title == post.title}

                val new = userPost?.copy(postList = postList) ?: UserPost()
                postsRef.document(result.documents[0].id).set(new).await()
                Log.d("test", result.documents[0].id)
            }
        } catch (e: Exception) {
            null
        }
    }



}