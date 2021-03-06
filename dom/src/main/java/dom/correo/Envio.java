package dom.correo;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.joda.time.LocalDate;

/**
 * Env&iacute;a un mail al posible hu&eacute;sped informando fechas, disponibilidad, etc.
 * @author ProyectoCIP
 *
 */
public class Envio {

	private Session session;
	private Properties propiedades = new Properties();
	
	/**
	 * Setea la sesi&oacute;n que se crea para poder enviar el mail.
	 * @param propiedades
	 */
	public void setSession(Properties propiedades) {
		
		session = Session.getInstance(propiedades);
		
		/*
		 * Información de los movimientos
		 */
		session.setDebug(true);
	}

	/**
	 * Retorna la sesion creada.
	 * @return
	 */
	public Session getSession() {
		// TODO Auto-generated method stub
		return session;
	}
	
	/**
	 * Setea propiedades como puerto, host de correo, datos de usuario, etc, para poder enviar el mail al destinatario.
	 */
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
	
	/**
	 * Retorna las propiedades seteadas para poder enviar el mail.
	 * @return
	 */
	public Properties getProperties() {
		return propiedades;
	}
	
	/**
	 * Env&iacute;a el mail al posible hu&eacute;sped.
	 * @param nombre
	 * @param apellido
	 * @param telefono
	 * @param correo
	 * @param desde
	 * @param hasta
	 * @param mensaje
	 * @param direccion
	 */
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
			
			if(!nombre.equals("La Morada Petit Hotel")) {
				message.setFrom(new InternetAddress("proyectocipifes@gmail.com"));
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
			}
			else {
				message.setFrom(new InternetAddress("proyectocipifes@gmail.com"));
				// A quien va dirigido
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(direccion));
				message.setSubject("Consulta de Disponibilidad");
			
				message.setText(mensaje);			
			}
			
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
