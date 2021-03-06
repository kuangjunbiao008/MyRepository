/**
 * Created By: YBASE
 * Created Date: 2013-4-23 上午9:17:54
 * Author: Tom Yang
 */
package com.ybase.james.client;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Tom Yang
 * @version 1.0
 */
public class MailOperator {

	public static void getMail() {
		String host = "localhost";
		final String username = "test";
		final String password = "test";

		// 创建Properties 对象
		Properties props = new Properties();

		// 创建邮件会话
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}

		});
		
//		session.setDebug(true);

		try {
			// 获取邮箱的pop3存储
			Store store = session.getStore("pop3");
			store.connect(host, username, password);

			// 获取inbox文件
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY); // 打开，打开后才能读取邮件信息

			// 获取邮件消息
			Message message[] = folder.getMessages();

			for (int i = 0, n = message.length; i < n; i++) {
				System.out.println(i + ": " + message[i].getFrom()[0] + "\t" + message[i].getSubject());
				try {
					message[i].writeTo(System.out);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			// 关闭资源
			folder.close(false);
			store.close();

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		System.out.println("GetMail Process Over!");
	}

	public static void sendMail() {
		// String host = "192.168.1.98"; // 指定的smtp服务器，本机的局域网IP
		String host = "localhost"; // 本机smtp服务器
		// String host = "smtp.163.com"; // 163的smtp服务器
		String from = "tom_yang@ybase.com"; // 邮件发送人的邮件地址
		String to = "geven@smail.com"; // 邮件接收人的邮件地址
		final String username = "tom_yang"; // 发件人的邮件帐户
		final String password = "tom_yang"; // 发件人的邮件密码
		// 创建Properties 对象
		Properties props = System.getProperties();
		// 添加smtp服务器属性
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		// 创建邮件会话
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}

		});
		try {
			// 定义邮件信息
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// message.setSubject(transferChinese("我有自己的邮件服务器了"));
			message.setSubject("I hava my own mail server");
			message.setText("From now, you have your own mail server, congratulation!");
			session.getTransport("smtp");
			// 发送消息
			Transport.send(message);
			// Transport.send(message); //也可以这样创建Transport对象发送
			System.out.println("SendMail Process Over!");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  sendMail();
//		  getMail();
	}

}
