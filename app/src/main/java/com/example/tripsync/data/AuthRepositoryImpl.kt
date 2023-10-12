package com.example.tripsync.data

import com.example.tripsync.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl {
    private val auth = FirebaseAuth.getInstance()
    private val usersRef = FirebaseFirestore.getInstance().collection("users")

    suspend fun register(
        email: String,
        password: String
    ): AuthResult? =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid!!
                val user = User(email, "test", listOf())
                usersRef.document(uid).set(user).await()
                result
            } catch (e: Exception) {
                null
            }
        }

    suspend fun login(email: String, password: String): AuthResult? =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result
            } catch (e: Exception) {
                null
            }
        }
}