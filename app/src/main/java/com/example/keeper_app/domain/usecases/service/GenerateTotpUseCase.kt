package com.example.keeper_app.domain.usecases.service

import com.example.keeper_app.data.storage.entities.Totp
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.math.pow

class GenerateTotpUseCase @Inject constructor(){
    fun generateTotp(totp: Totp): String{
        val key = decodeBase32(totp.secretKey)
        val counter = System.currentTimeMillis() / 1000 / totp.period


        val algorithm = when(totp.algorithm.uppercase()){
            "SHA1" -> "HmacSHA1"
            "SHA256" -> "HmacSHA256"
            "SHA512" -> "HmacSHA512"
            else -> throw IllegalArgumentException("Неподдерживаемый алгоритм: ${totp.algorithm}")
        }

        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(key, algorithm))

        val counterBytes = ByteArray(8)
        for(i in 7 downTo 0){
            counterBytes[i] = (counter shr (8*(7-i))).toByte()
        }

        val hash = mac.doFinal(counterBytes)

        val offset = hash[19].toInt() and 0x0F
        var binary = ((hash[offset].toInt() and 0x7F) shl 24) or
                ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                (hash[offset + 3].toInt() and 0xFF)
        binary = binary and 0x7FFFFFFF

        val mod = 10.0.pow(totp.digits.toDouble()).toLong()
        val code = (binary % mod).toString().padStart(totp.digits, '0')
        return code

    }

    private fun decodeBase32(input: String): ByteArray {
        val normalized = input.trim().replace("-", "").replace(" ", "").uppercase()
        val bytes = mutableListOf<Byte>()
        var buffer = 0
        var bits = 0

        for (c in normalized) {
            val value = charToValue(c)
            if (value == -1) continue
            buffer = (buffer shl 5) or value
            bits += 5
            while (bits >= 8) {
                bytes.add(((buffer shr (bits - 8)) and 0xFF).toByte())
                bits -= 8
            }
        }
        return bytes.toByteArray()
    }

    private fun charToValue(c: Char): Int = when (c) {
        in 'A'..'Z' -> c - 'A'
        in '2'..'7' -> c - '2' + 26
        else -> -1
    }
}