package dom.correo;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class Recepcion implements ICorreo {

	private Session session;
	private Properties propiedades = new Properties();
	private Store store;
	private Message [] mensajes;
	
	@Override
	public void setSession(Properties propiedades) {
		// TODO Auto-generated method stub
		session = Session.getInstance(propiedades);
		session.setDebug(true);
	}

	@Override
	public Session getSession() {
		// TODO Auto-generated method stub
		return session;
	}

	@Override
	public void setProperties() {
		// TODO Auto-generated method stub// Deshabilitamos TLS
    	propiedades.setProperty("mail.pop3.starttls.enable", "false");

    	// Hay que usar SSL
    	propiedades.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory" );
    	propiedades.setProperty("mail.pop3.socketFactory.fallback", "false");

    	// Puerto 995 para conectarse.
    	propiedades.setProperty("mail.pop3.port","995");
    	propiedades.setProperty("mail.pop3.socketFactory.port", "995");
    	
		setSession(propiedades);
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return propiedades;
	}

	@Override
	public void accion() {
		
		try {
			store = session.getStore("pop3");
		
			store.connect("pop.gmail.com","usuario@gmail.com","---");

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
		
			mensajes = folder.getMessages();
			
			for (int i=0;i<mensajes.length;i++)
			{
			   System.out.println("From:"+mensajes[i].getFrom()[0].toString());
			   System.out.println("Subject:"+mensajes[i].getSubject());
			   
			   /*
			    * Solo recibimos texto plano en los correos enviados desde el formulario
				*/
			   
			   System.out.println(mensajes[i].getContent());
			}
		
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
