package com.trip.tripsync.data


import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.trip.tripsync.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
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

    suspend fun getUserListByUidList(uidList: List<String>) = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser == null) {
                null
            } else {
                val docs = usersRef.whereIn("uid", uidList).get().await()
                docs.documents.map {
                    it.toObject(User::class.java) ?: User()
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

    suspend fun checkNickname(nickname: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val nicknameDocuments = usersRef.whereEqualTo("nickname", nickname).get().await()
            nicknameDocuments.isEmpty
        } catch (e: Exception) {
            false
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

    fun getUserSnapshot(): Flow<User> = callbackFlow {
        var eventsCollection: DocumentReference? = null
        val curUserUid = auth.currentUser?.uid ?: ""

        try {
            eventsCollection = FirebaseFirestore.getInstance()
                .collection("users").document(curUserUid)
        } catch (e: Throwable) {
            close(e)
        }

        val subscription = eventsCollection?.addSnapshotListener { snapshot, _ ->

            if (snapshot == null) {
                trySend(User())
                return@addSnapshotListener
            }

            try {
                trySend(snapshot.toObject(User::class.java) ?: User())
            } catch (e: Throwable) {
                trySend(User())
            }
        }
        awaitClose { subscription?.remove() }
    }.buffer(Channel.UNLIMITED)

    suspend fun updateProfileImg(url: String) = withContext(Dispatchers.IO) {
        try {
            if (auth.currentUser == null) {
                null
            } else {
                try {
                    usersRef.document(auth.currentUser!!.uid).update("profileImg", url).await()
                } catch (e: Exception) {
                    null
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