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
                val user = User(email, "test", listOf(), uid)
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

    fun logout() {
        auth.currentUser?.let {
            auth.signOut()
        }
    }

    suspend fun getCurrentUserInfo() = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser == null) {
                null
            } else {
                val docs = usersRef.whereEqualTo("email", auth.currentUser!!.email).get().await()
                if (docs.documents.size == 0) null else docs.documents[0].toObject(User::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getNameListByUidList(uidList: List<String>) = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser == null) {
                null
            } else {
                val docs = usersRef.whereIn("uid", uidList).get().await()
                docs.documents.map {
                    it.toObject(User::class.java)?.nickname ?: ""
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updatePassword(password: String) = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser != null) {
                auth.currentUser!!.updatePassword(password)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun unregister() = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser != null) {
                auth.currentUser!!.delete()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addFriend(user: User) = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser == null) {
                null
            } else {
                val docs = usersRef.whereEqualTo("email", auth.currentUser!!.email).get().await()

                if (docs.documents.size == 0) {
                    null
                } else {
                    val doc = docs.documents[0].toObject(User::class.java)

                    if (doc == null) {
                        null
                    }
                    else {
                        val friends = doc.friends?.toMutableList() ?: mutableListOf()

                        if (friends.none { it.nickname == user.nickname }) {
                            friends.add(user)
                            val newDoc = doc.copy(friends = friends)
                            usersRef.document(docs.documents[0].id)
                                .set(newDoc).await()
                            newDoc
                        } else {
                            null
                        }
                    }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteFriend(user: User) = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser == null) {
                null
            } else {
                val docs = usersRef.whereEqualTo("email", auth.currentUser!!.email).get().await()

                if (docs.documents.size == 0) {
                    null
                } else {
                    val doc = docs.documents[0].toObject(User::class.java)

                    if (doc == null) {
                        null
                    }
                    else {
                        val friends = doc.friends?.toMutableList()

                        if (friends?.filter { it.nickname == user.nickname }.isNullOrEmpty()) {
                            null
                        } else {
                            friends?.remove(user)
                            val newDoc = doc.copy(friends = friends)
                            usersRef.document(docs.documents[0].id).set(newDoc).await()
                            newDoc
                        }
                    }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun searchFriend(query: String) = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser == null) {
                null
            } else {
                val docs = usersRef
                    .whereGreaterThanOrEqualTo("nickname", query)
                    .whereLessThanOrEqualTo("nickname", query + "\uf8ff")
                    .get()
                    .await()

                docs.documents.map {
                    it.toObject(User::class.java)!!
                }.filter {
                    it.email != auth.currentUser!!.email
                }
            }
        } catch (e: Exception) {
            null
        }
    }


}