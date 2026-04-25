package com.example.keeper_app.data.network.totp

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val keySize = 256
    private val transformation = "AES/GCM/NoPadding"

    //Создаёт или получает ключ из Android Keystore. Ключ привязан к пользователю через [keyAlias].

    private fun getOrCreateKey(keyAlias: String): SecretKey {
        if (keyStore.containsAlias(keyAlias)) {
            return (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val builder = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(keySize)

        // Добавляем setUnlockedDeviceRequired только на API 28+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            builder.setUnlockedDeviceRequired(true)
        }

        val spec = builder.build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    //Шифрование: возвращает Base64-encoded строку (IV + ciphertext)

    fun encrypt(plainText: String, keyAlias: String): String {
        val secretKey = getOrCreateKey(keyAlias)
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // IV (12 байт для GCM) + зашифрованные данные
        val combined = iv + encryptedBytes
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    //Дешифрование: принимает Base64-encoded строку (IV + ciphertext)
    fun decrypt(encryptedData: String, keyAlias: String): String {
        val secretKey = getOrCreateKey(keyAlias)
        val combined = Base64.decode(encryptedData, Base64.NO_WRAP)

        // GCM IV — первые 12 байт
        val iv = combined.copyOfRange(0, 12)
        val encryptedBytes = combined.copyOfRange(12, combined.size)

        val cipher = Cipher.getInstance(transformation)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}