package dom.correo;

import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Envio implements ICorreo {

	private Session session;
	private Properties propiedades = new Properties();
	
	@Override
	public void setSession(Properties propiedades) {
		
		session = Session.getInstance(propiedades);
		
		/*
		 * Información de los movimientos
		 */
		session.setDebug(true);
	}

	@Override
	public Session getSession() {
		// TODO Auto-generated method stub
		return session;
	}
	
	@Override
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
	
	public void enviar() {
		
		MimeMessage message = new MimeMessage(getSession());
		
		try {
			message.setFrom(new InternetAddress("proyectocipifes@gmail.com"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("proyectocipifes@gmail.com"));
			message.setSubject("RESERVA-PROYECTOCIP");
			message.setText("Ezequiel," +
					"Celiz," +
					"155716726," +
					"elceliz@speedy.com.ar," +
					"17/11/2013," +
					"20/11/2013");
			
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

	@Override
	public void accion() {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Mensaje> getListaMensajes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setListaMensajes(List<Mensaje> listaMensajes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCantidadMails() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCantidadMails(int cantidadMails) {
		// TODO Auto-generated method stub
		
	}
}
