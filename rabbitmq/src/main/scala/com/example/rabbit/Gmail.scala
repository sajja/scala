package com.example.rabbit

import java.io.IOException;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import com.sun.mail.pop3.POP3Store;

object Gmail {
  def receive() = {
    val props = new Properties()
    props.put("imap.gmail.com", "imaps")
    props.setProperty("mail.store.protocol", "imaps");
    props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.setProperty("mail.imap.socketFactory.fallback", "false");

    val session = Session.getDefaultInstance(props)
    val store = session.getStore("imaps")
    store.connect("imap.gmail.com", "", "")
    val inbox = store.getFolder("INBOX")
    val msgs = inbox.getMessages();
    println(msgs.length)
    inbox.close(false)
    store.close()
  }

  def main(args: Array[String]): Unit = {
    receive()
  }


}
