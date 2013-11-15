package dom.correo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
//import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
//import javax.swing.JOptionPane;

import org.apache.isis.applib.DomainObjectContainer;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;

/**
 * 
 * Clase que descarga todos los correos de la bandeja de entrada 
 * y los convierte en objetos de dominio
 * 
 * @author ProyectoCIP
 *
 */
public class Recepcion {

	private Session session;
	private Properties propiedades = new Properties();
	private Store store;
	private Message [] mensajes;
	private List<Mensaje> listaMensajes = new ArrayList<Mensaje>();
	
	/**
	 * 
	 * @return Retorna la lista con los correos electrónicos nuevos
	 */
	public List<Mensaje> getListaMensajes() {
		return listaMensajes;
	}
	
	/**
	 * Setea la lista de correos electrónicos nuevos
	 * @param listaMensajes 
	 */
	public void setListaMensajes(List<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}
	
	/**
	 * A la sesión actual le aplica las propiedades de conexión
	 * @param propiedades 
	 */
	public void setSession(Properties propiedades) {
		// TODO Auto-generated method stub
		session = Session.getInstance(propiedades);
		session.setDebug(true);
	}

	/**
	 * 
	 * @return Retorna la sesión actual
	 */
	public Session getSession() {
		// TODO Auto-generated method stub
		return session;
	}

	/**
	 * Setea las propiedades para crear la sesión de usuario
	 */
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

	/**
	 * 
	 * @return Retorna las propiedades para crear la sesión de usuario
	 */
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return propiedades;
	}

	/**
	 * 
	 * Se encarga de conectarse al buzon del correo y bajar todos los correos,
	 * filtra por asunto y setea el futuro objeto de dominio (Correo).
	 * 
	 */
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
			       
			       final Mensaje actual = new Mensaje();
			       
			       actual.setNombre(partes[0]);
			       actual.setApellido(partes[1]);
			       actual.setTelefono(partes[2]);
			       actual.setCorreo(partes[3]);
			       actual.setDesde(partes[4]);
			       actual.setHasta(partes[5]);
			       actual.setMensaje(partes[6]);
			       actual.setUsuario(container.getUser().getName());
			       
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
		
	/*
	 * Si es necesario utilizar Quartz para revisar la bandeja de entrada cada x tiempo
	 * 
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
	*/

    private DomainObjectContainer container;
    
    public void injectDomainObjectContainer(DomainObjectContainer container) {
    	this.container = container;
    }
    
}
