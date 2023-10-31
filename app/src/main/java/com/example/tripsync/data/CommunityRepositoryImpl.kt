package com.example.tripsync.data

import com.example.tripsync.model.Post
import com.example.tripsync.model.UserPost
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CommunityRepositoryImpl {
    private val postsRef = FirebaseFirestore.getInstance().collection("posts")

    suspend fun addPost(post: Post) = withContext(Dispatchers.IO) {
        try {
            val postOwnerUid = post.user?.get(0) ?: ""
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
            }
        } catch (e: Exception) {
            null
        }
    }



}