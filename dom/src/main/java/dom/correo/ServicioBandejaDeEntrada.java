package dom.correo;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;

/**
 * Men&uacute; de la bandeja de entrada
 * @author ProyectoCIP
 *
 */
@Named("Em@ils")
public class ServicioBandejaDeEntrada extends AbstractFactoryAndRepository {
	
	 /**
     * 
     * @return Retorna el nombre del icon ServicioBandejaDeEntrada que va a ser usado en el viewer.
     */
	public String iconName() {
		return "email";
	}
	
	/**
	 * 
	 * @return Retorna la lista de correos persistidos y los nuevos
	 */
	@Named("Bandeja de Entrada")
	@MemberOrder(sequence = "1")
	public List<Mensaje> bde() {

		Recepcion recepcion = new Recepcion();
		recepcion.setProperties();
		
		recepcion.accion();
		
		final List<Mensaje> listaJavaMail = recepcion.getListaMensajes();
		
	    String mensajeNuevos = listaJavaMail.size() == 1 ? "TENES UNA NUEVA CONSULTA!" : "TENES "+listaJavaMail.size()+" CONSULTAS NUEVAS!"; 
	    
		if(listaJavaMail.size() > 0) {
		
			getContainer().informUser(mensajeNuevos);
	    	
	    	for(Mensaje mensaje : listaJavaMail) {
	    		
	    		final Mensaje mensajeTransient = newTransientInstance(Mensaje.class);
	    		
	    		mensajeTransient.setNombre(mensaje.getNombre());
	    		mensajeTransient.setApellido(mensaje.getApellido());
	    		mensajeTransient.setCorreo(mensaje.getCorreo());
	    		mensajeTransient.setTelefono(mensaje.getTelefono());
	    		mensajeTransient.setDesde(mensaje.getDesde());
	    		mensajeTransient.setHasta(mensaje.getHasta());
	    		mensajeTransient.setMensaje(mensaje.getMensaje());
	    		mensajeTransient.setUsuario(usuarioActual());
	    		mensajeTransient.setFechaActual(LocalDate.now());
	    		persistIfNotAlready(mensajeTransient);
	    		
	    	}
		
		}
		return listaMensajesPersistidos();
	}
	
	/**
	 * 
	 * @return Retorna los emails guardados por el usuario registrado
	 */
	@Programmatic
    public List<Mensaje> listaMensajesPersistidos() {
    
	    return allMatches(Mensaje.class, 
		         
	       	new Filter<Mensaje>() {
	       			@Override
	           		public boolean accept(final Mensaje mensaje) {
	           			return Objects.equal(mensaje.getUsuario(), usuarioActual());
	           		}
	           }
	    	);    
    }	
	
	protected boolean creadoPorActualUsuario(final Mensaje m) {
	    return Objects.equal(m.getUsuario(), usuarioActual());
	}
	
	protected String usuarioActual() {
	    return getContainer().getUser().getName();
	}
}
