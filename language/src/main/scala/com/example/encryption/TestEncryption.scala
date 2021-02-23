package com.example.encryption

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.{Provider, SecureRandom}
import java.util
import java.util.{Base64, UUID}

import javax.crypto.{Cipher, SecretKeyFactory}
import javax.crypto.spec.{IvParameterSpec, PBEKeySpec, SecretKeySpec}

import scala.util.Try

object TestEncryption {
  private val cipherInstance = "AES/CBC/PKCS5Padding"

  def getRandomNonce(numBytes: Int): Array[Byte] = {
    val nonce = new Array[Byte](numBytes);
    new SecureRandom().nextBytes(nonce);
    nonce
  }

  private def setAESKeyLength(secret: String): Array[Byte] = {
    val key = secret.getBytes(StandardCharsets.UTF_8)
    util.Arrays.copyOf(key, 16);
  }


  private def setKey(secret: String): SecretKeySpec = {
    val cipherAlgorithm = "AES" // Only one supported by S3
    val key = setAESKeyLength(secret)
    new SecretKeySpec(key, cipherAlgorithm)
  }


  def encrypt(payload: Array[Byte], secret: String): Try[Array[Byte]] = {
    Try {
      val secretKey = setKey(secret)
      val sr = new SecureRandom()
      val iv = new Array[Byte](16)
      sr.nextBytes(iv)
      val ivspec = new IvParameterSpec(iv)
      val cipher = Cipher.getInstance(cipherInstance)
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(setAESKeyLength(secret)))
      //      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
      Base64.getEncoder.encode(cipher.doFinal(payload))
    }
  }

  def getKeyFromPass(password: Array[Char], salt: Array[Byte]) = {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = new PBEKeySpec(password, salt, 65536, 256);
    val secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    secret;
  }

  def encrypt1(pText: Array[Byte], password: String) = {
    // 16 bytes salt
    val salt = getRandomNonce(16);
    val iv = getRandomNonce(12);
    val aesKeyFromPassword = getKeyFromPass(password.toCharArray(), salt);
    val cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword);
    val cipherText = cipher.doFinal(pText);
    val cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length).put(iv).put(salt).put(cipherText).array();
    Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
  }

  def decrypt(bytes:Array[Byte],password:String) = {
    val iv = new Array[Byte](12);
    val keySpec = new SecretKeySpec(password.getBytes(),cipherInstance)
  }

  // we need the same password, salt and iv to decrypt it
  def decrypt1(cText: String, password: String): String = {
    val decode = Base64.getDecoder().decode(cText.getBytes("UTF-8"));
    val bb = ByteBuffer.wrap(decode);
    val iv = new Array[Byte](12);
    val salt = new Array[Byte](16);
    //it seems iv/salt is in the begining
    bb.get(iv)
    bb.get(salt)
    val cipherText = new Array[Byte](bb.remaining());
    bb.get(cipherText);
    val aesKeyFromPassword = getKeyFromPass(password.toCharArray(), salt);
    val cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword);
    val plainText = cipher.doFinal(cipherText);
    new String(plainText, "utf-8");
  }

  def main(args: Array[String]): Unit = {
    println("---------TEST 1 Correct way-----------")
    val encryptedData = encrypt1("Hello".getBytes(), "password")
    println(encryptedData)
    println(encrypt1("Hello".getBytes(), "password"))

    val x = encrypt("Hello".getBytes(), "password").get
    val y = encrypt("Hello".getBytes(), "password").get

    println(Base64.getEncoder().encodeToString(x))
    println(Base64.getEncoder().encodeToString(y))

    val decrptedData = decrypt(x,"password")
    println(decrptedData)
    UUID.fromString("18111689-98d6-497b-9ee3-f7a85ab2f52e")


    val seed = new Array[Byte](1)
    seed(0) = 1
    println(new SecureRandom(seed).nextLong())
    println(new SecureRandom(seed).nextLong())

    val l = Long.MaxValue
  }
}
