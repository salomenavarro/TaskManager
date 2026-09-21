package com.example.taskmanager.data.repository

import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

// data/repository/AuthRepositoryImpl.kt
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("No se pudo crear el usuario")
            Result.success(User(uid = firebaseUser.uid, email = firebaseUser.email ?: ""))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("No se pudo iniciar sesión")
            Result.success(User(uid = firebaseUser.uid, email = firebaseUser.email ?: ""))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return User(uid = firebaseUser.uid, email = firebaseUser.email ?: "")
    }
}