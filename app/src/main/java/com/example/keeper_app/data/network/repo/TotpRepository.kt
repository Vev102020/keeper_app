package com.example.keeper_app.data.network.repo

import android.util.Log
import com.example.keeper_app.data.network.session.SessionManager
import com.example.keeper_app.data.network.totp.CryptoManager
import com.example.keeper_app.data.storage.dao.ServiceDao
import com.example.keeper_app.data.storage.entities.ServiceDb
import com.example.keeper_app.data.storage.entities.Totp
import javax.inject.Inject
import javax.inject.Singleton

interface TotpRepository{
    suspend fun getKeyAlias() : String
    suspend fun encryptTotp(totp: Totp): Totp
    suspend fun decryptTotp(totp: Totp): Totp
    suspend fun saveServiceWithTotp(name: String, rawSecret: String, userId: String)
    suspend fun getServiceWithDecryptedTotp(serviceId: Int): ServiceDb?
}

@Singleton
class TotpRepositoryImpl @Inject constructor (
    private val serviceDao: ServiceDao,
    private val cryptoManager: CryptoManager,
    private val sessionManager: SessionManager
) : TotpRepository{
    override suspend fun getKeyAlias(): String {
        val userId = sessionManager.requireUserId()
        return "totp_key_$userId"
    }

    // 🔐 Зашифровать Totp перед сохранением
    override suspend fun encryptTotp(totp: Totp): Totp {
        val keyAlias = getKeyAlias()
        val encryptedSecret = cryptoManager.encrypt(totp.secretKey, keyAlias)
        return totp.copy(secretKey = encryptedSecret)
    }

    // 🔓 Расшифровать Totp после чтения
    override suspend fun decryptTotp(totp: Totp): Totp {
        val keyAlias = getKeyAlias()
        val decryptedSecret = cryptoManager.decrypt(totp.secretKey, keyAlias)
        return totp.copy(secretKey = decryptedSecret)
    }

    // Сохранить сервис с зашифрованным Totp
    override suspend fun saveServiceWithTotp(
        name: String,
        rawSecret: String,
        userId: String
    ) {
        val totp = Totp(secretKey = rawSecret)
        Log.i("saveTotp = ", rawSecret)
        val encryptedTotp = encryptTotp(totp)
        Log.i("encryptedTotp = ", "$encryptedTotp")
        val service = ServiceDb(
            id = 0,
            name = name,
            userId = userId,
            totp = encryptedTotp
        )

        serviceDao.insertService(service)
    }

    // Получить сервис и расшифровать Totp
    override suspend fun getServiceWithDecryptedTotp(serviceId: Int): ServiceDb? {
        val service = serviceDao.getServiceById(serviceId) ?: return null
        val totp = service.totp

        return try {
            val decryptedTotp = decryptTotp(totp)
            service.copy(totp = decryptedTotp)
        } catch (e: Exception) {
            throw RuntimeException("Не удалось расшифровать Totp", e)
        }
    }

}