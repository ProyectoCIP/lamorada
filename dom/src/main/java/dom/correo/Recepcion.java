package dom.correo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class Recepcion implements ICorreo,Job {

	private Session session;
	private Properties propiedades = new Properties();
	private Store store;
	private Message [] mensajes;
	private List<Mensaje> listaMensajes = new ArrayList<Mensaje>();
	
	@Override
	public List<Mensaje> getListaMensajes() {
		return listaMensajes;
	}
	
	@Override
	public void setListaMensajes(List<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}
	
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
		// TODO Auto-generated method stub
		// Deshabilitamos TLS
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
		
			store.connect("pop.gmail.com","proyectocipifes@gmail.com","qwertyuio123456");

			Folder folder = store.getFolder("INBOX");
	
			folder.open(Folder.READ_ONLY);
		
			mensajes = folder.getMessages();
			
			for (Message mensaje : mensajes)
			{  
				
			    System.out.println(mensaje.getContent());
				
			    if(mensaje.getSubject().contains("RESERVA-PROYECTOCIP")) {
				   
			       String[] partes = ((String) mensaje.getContent()).split(",");
				   
			       System.out.println(
			    		   		"nombre:" + partes[0] + "\n" +
						   		"apellido:" + partes[1] + "\n" +
						   		"celular:" + partes[2] + "\n" +
						   		"correo:" + partes[3] + "\n" +
						   		"fecha desde:" + partes[4] + "\n" +
						   		"fecha hasta:" + partes[5]
						   		);
			       
			       final Mensaje actual = new Mensaje();
			       
			       actual.setNombre(partes[0]);
			       actual.setApellido(partes[1]);
			       actual.setTelefono(partes[2]);
			       actual.setCorreo(partes[3]);
			       
			       getListaMensajes().add(actual);
			       
			    }
			   
			}
			
			//Cierre de la sesión
			store.close();
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void enviar(){}
	
	private int cantidadMails;

	public int getCantidadMails() {
		return cantidadMails;
	}

	public void setCantidadMails(int cantidadMails) {
		this.cantidadMails = cantidadMails;
	}
	
    public void execute(JobExecutionContext context) {
		// TODO Auto-generated method stub
		    	
    	   setProperties();
		   
		   try {
				
			    store = session.getStore("pop3");
				store.connect("pop.gmail.com","proyectocipifes@gmail.com","qwertyuio123456");

				Folder folder = store.getFolder("INBOX");
		
				folder.open(Folder.READ_ONLY);
			
				mensajes = folder.getMessages();
				
				if(mensajes.length > 0)
				{
					JOptionPane.showMessageDialog(null, "TENES "+mensajes.length+" CONSULTAS NUEVAS!");
				}
				
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
	}

}
