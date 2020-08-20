package com.example.cake.modules

//API for our cryptography module.
trait CryptogrhapyModule {
  type KeySpec <: KeySpecLike

  trait KeySpecLike {}

  def KeySpec(params: Map[String, String]): KeySpec

  val encryptor: Encryption
  val decryptor: Decryption

  trait Encryption {
    def encrypt(key: KeySpec, payload: String): String
  }

  trait Decryption {
    def decrypt(key: KeySpec, payload: String): String
  }

}

//HUGE implementation of SymetricKey based cryptography module. For now its small be think BIG.
//In another HUGE implementation of Encryption using symatric key and decrypiton. For now its one liner but you know the drill.
trait SymatricKeyCryptogrhapyModule extends CryptogrhapyModule {

  class KeySpec extends KeySpecLike

  override val encryptor = new SymatricKeyEncryption

  override val decryptor = new SymatricKeyDecryption

  class SymatricKeyEncryption extends Encryption {
    override def encrypt(key: KeySpec, payload: String): String = s"Encrypting communication using symatric key : $payload"
  }

  class SymatricKeyDecryption extends Decryption {
    override def decrypt(key: KeySpec, payload: String): String = s"Decrypting communication using symatric key : $payload"
  }

  override def KeySpec(params: Map[String, String]) = new KeySpec
}

trait OldAgentModule {
  def speak(information: String, encryptionModule: CryptogrhapyModule) = {
    println(encryptionModule.encryptor.encrypt(encryptionModule.KeySpec(Map()), information))
  }

  def listen(information: String, encryptionModule: CryptogrhapyModule) = {
    println(encryptionModule.decryptor.decrypt(encryptionModule.KeySpec(Map()), information))
  }
}

trait BetterAgentModule {
  this: CryptogrhapyModule =>
  def speak(information: String) = {
    println(encryptor.encrypt(KeySpec(Map()), information))
  }

  def listen(information: String) = {
    println(decryptor.decrypt(KeySpec(Map()), information))
  }
}

object TestModules1 {
  def main(args: Array[String]): Unit = {
    val oldAgentModule = new OldAgentModule {}
    val symatricKeyEncryptionModule = new CryptogrhapyModule with SymatricKeyCryptogrhapyModule

    oldAgentModule.speak("I'm Agent K.", symatricKeyEncryptionModule)
    oldAgentModule.listen("Hello agent K. This is Agent J", symatricKeyEncryptionModule)

    val newAgentModule = new BetterAgentModule with SymatricKeyCryptogrhapyModule
    newAgentModule.speak("I'm 007. Speaking with injected cryptography module")
  }

}