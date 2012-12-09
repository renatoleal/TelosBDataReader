import java.util.Date;
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SendEmail {
 
	public static void sendWarningMail(String destination, int idSensor, int valor, long time) {
 
		final String username = "ecorssf2012";
		final String password = "ecorssfmail";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ecorssf2012@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(destination));
			message.setSubject("PERIGO: Nivel de sensor fora do limite!");
			message.setContent("Caro t�cnico,"
				+ "\n\nIdentificamos n�veis anormais na coleta de um sensor. \nFavor verificar:" +
				"\n\n Sensor: " + idSensor +
				"\n\n Valor: " + valor +
				"\n\n Hor�rio: " + new Date(time), "text/plain; charset=UTF-8");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}