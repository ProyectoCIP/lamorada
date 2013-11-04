package dom.correo;

import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.joda.time.LocalDate;

public class Envio {

	private Session session;
	private Properties propiedades = new Properties();
	
	public void setSession(Properties propiedades) {
		
		session = Session.getInstance(propiedades);
		
		/*
		 * Información de los movimientos
		 */
		session.setDebug(true);
	}

	public Session getSession() {
		// TODO Auto-generated method stub
		return session;
	}
	
	public void setProperties() {		
		
		// Nombre del host de correo, es smtp.gmail.com
		propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");

		// TLS si está disponible
		propiedades.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para envio de correos
		propiedades.setProperty("mail.smtp.port","587");

		// Nombre del usuario
		propiedades.setProperty("mail.smtp.user", "proyectocipifes@gmail.com");

		// Si requiere o no usuario y password para conectarse.
		propiedades.setProperty("mail.smtp.auth", "true");
		
		setSession(propiedades);

	}
	
	public Properties getProperties() {
		return propiedades;
	}
	
	public void enviar(
						String nombre,
						String apellido,
						String telefono,
						String correo,
						LocalDate desde,
						LocalDate hasta,
						String mensaje,
						String direccion
					  ) {
		
		MimeMessage message = new MimeMessage(getSession());
		
		try {
			message.setFrom(new InternetAddress(correo));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("proyectocipifes@gmail.com"));
			message.setSubject("RESERVA-PROYECTOCIP");
			
			String desdeString = desde.toString();
			String hastaString = (hasta == null) ? "" : hasta.toString();
			 
			message.setText(
					nombre+"," +
					apellido+"," +
					telefono+"," +
					correo+"," +
					desdeString+"," +
					hastaString+","+
					mensaje);
		
			Transport t = session.getTransport("smtp");
			t.connect("proyectocipifes@gmail.com","qwertyuio123456");
			t.sendMessage(message,message.getAllRecipients());
			
			t.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
