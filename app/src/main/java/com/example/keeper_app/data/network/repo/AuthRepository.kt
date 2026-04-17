package com.example.keeper_app.data.network.repo

import android.content.Context
import android.util.Log
import com.example.keeper_app.data.network.util.FirebaseMessageTranslator
import com.example.keeper_app.data.storage.dao.UserDao
import com.example.keeper_app.data.storage.entities.UserDb
import com.example.keeper_app.presentation.auth.viewmodel.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository{
    val authState: Flow<AuthState>

    suspend fun login(email:String, password:String) : Result<UserDb>
    suspend fun register(email: String, password: String) : Result<UserDb>
    suspend fun logout()
    suspend fun changePassword(newPassword: String) : Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>

    suspend fun isUserLoggedIn() : Boolean
    suspend fun getCurrentUser() : UserDb?
    suspend fun deleteUser() : Result<Unit>
    suspend fun updateLastLogin(firebaseId: String)

    suspend fun checkAuthState()
    fun setAuthState(state: AuthState)
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userDao: UserDao,
    @ApplicationContext private val context: Context
) : AuthRepository{
    private companion object{
        const val TAG = "AuthRepository"
    }

    //для кэша
    private var _currentUser: UserDb? = null

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    //Вход
    override suspend fun login(
        email: String,
        password: String
    ): Result<UserDb> {
        return try {
            _authState.value = AuthState.Loading

            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser  = authResult.user ?: return Result.failure(Exception("Пользователь не найден"))

            val user = getOrCreateLocalUser(firebaseUser)
            _currentUser = user
            updateLastLogin(user.firebaseId)

            _authState.value = AuthState.Success(user)
            Result.success(user)
        }catch (e: Exception){
            val errorMessage = FirebaseMessageTranslator.translateErrorMessage(e)
            _currentUser = null
            _authState.value = AuthState.Error(errorMessage)
            Result.failure(Exception(errorMessage))
        }
    }

    //Проверка есть ли пользователь в БД при входе
    private suspend fun getOrCreateLocalUser(firebaseUser: FirebaseUser): UserDb {
        var user = userDao.getUserByFirebaseId(firebaseUser.uid)

        if (user == null) {
            // Создаём нового пользователя
            user = UserDb(
                firebaseId = firebaseUser.uid,
                email = firebaseUser.email,
                createdAt = System.currentTimeMillis(),
                lastLogin = System.currentTimeMillis()
            )
            userDao.insertUser(user)
        } else {
            // Обновляем email, если он изменился
            if (user.email != firebaseUser.email) {
                val updatedUser = user.copy(email = firebaseUser.email)
                userDao.updateUser(updatedUser)
                return updatedUser
            }
        }
        return user
    }

    //Регистрация
    override suspend fun register(
        email: String,
        password: String
    ): Result<UserDb> {
        return try {
            _authState.value = AuthState.Loading

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("Не удалось создать пользователя"))

            val newUser = UserDb(
                firebaseId = firebaseUser.uid,
                email = firebaseUser.email,
                createdAt = System.currentTimeMillis(),
                lastLogin = System.currentTimeMillis()
            )

            userDao.insertUser(newUser)
            _currentUser = newUser
            _authState.value = AuthState.Success(newUser)
            Result.success(newUser)
        }catch (e: Exception){
            val errorMessage = FirebaseMessageTranslator.translateErrorMessage(e)
            _currentUser = null
            _authState.value = AuthState.Error(errorMessage)
            Result.failure(Exception(errorMessage))
        }
    }

    override suspend fun checkAuthState() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser == null) {
            _authState.value = AuthState.Idle
        } else {
            val userDb = userDao.getUserByFirebaseId(currentUser.uid)
            if (userDb != null) {
                _authState.value = AuthState.Success(userDb)
            } else {
                // Пользователь в Firebase есть, но нет в локальной БД
                // Создаём запись в локальной БД
                val newUser = UserDb(
                    firebaseId = currentUser.uid,
                    email = currentUser.email ?: "",
                    createdAt = System.currentTimeMillis(),
                    lastLogin = System.currentTimeMillis()
                )
                userDao.insertUser(newUser)
                _authState.value = AuthState.Success(newUser)
            }
        }
    }

    override fun setAuthState(state: AuthState) {
        _authState.value = state
    }

    //Выход из ЛК
    override suspend fun logout() {
        try {
            firebaseAuth.signOut()
            _currentUser = null
            _authState.value = AuthState.Idle
        }catch (e: Exception){
            _authState.value = AuthState.Error("Ошибка при выходе ${e.message}")
        }

    }

    //Смена пароля
    override suspend fun changePassword(newPassword: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser ?: return Result.failure(Exception("Пользователь не авторизирован"))
            currentUser.updatePassword(newPassword).await()
            Result.success(Unit)
        }catch (e: Exception){
            val errorMessage = FirebaseMessageTranslator.translateErrorMessage(e)
            Result.failure(Exception(errorMessage))
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            if (email.isBlank()) {
                return Result.failure(IllegalArgumentException("Email не может быть пустым"))
            }

            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            val errorMessage = FirebaseMessageTranslator.translateErrorMessage(e)
            Result.failure(Exception(errorMessage))
        }
    }

    //Проверка есть ли пользователь в FB в сессии
    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    // Получение ID текущего пользователя
    override suspend fun getCurrentUser(): UserDb? {
        if(_currentUser != null){
            return _currentUser
        }

        val firebaseUser = firebaseAuth.currentUser ?: return null
        val userDb = userDao.getUserByFirebaseId(firebaseUser.uid)

        if (userDb != null){
            _currentUser = userDb
        }

        return userDb
    }

    //Удаление пользователя
    override suspend fun deleteUser() : Result<Unit> {
        return try {
            _authState.value = AuthState.Loading

            val currentUser = firebaseAuth.currentUser ?: return Result.failure(Exception("Пользователь не авторизирован"))
            //Удаляемп из FB
            currentUser.delete().await()
            //Удаляем из БД
            userDao.deleteUserByFirebase(currentUser.uid)
            _authState.value = AuthState.Idle
            Result.success(Unit)
        }catch (e: Exception){
            val errorMessage = FirebaseMessageTranslator.translateErrorMessage(e)
            _authState.value = AuthState.Error(errorMessage)
            Result.failure(Exception(errorMessage))
        }
    }

    //Обновление последнего входа
    override suspend fun updateLastLogin(firebaseId: String){
        userDao.updateLastLogin(firebaseId, System.currentTimeMillis())
    }
}