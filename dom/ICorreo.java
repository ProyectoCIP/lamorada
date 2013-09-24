package dom.correo;

import java.util.List;
import java.util.Properties;
import javax.mail.Session;

public interface ICorreo {
	
	void setSession(Properties propiedades);
	
	Session getSession();
	
	/*
	 * Propiedades para iniciar la sesión
	 * en donde por ejemplo se puede setear los puertos de los servidores
	 * POP y SMTP -> getProperties("mail.smtp.port","587");
	 */
	void setProperties();
	
	Properties getProperties();
	
	List<Mensaje> getListaMensajes();
	
	public int getCantidadMails();

	public void setCantidadMails(int cantidadMails);
	
	void setListaMensajes(List<Mensaje> listaMensajes);
	
	void enviar();
	
	void accion();

}
