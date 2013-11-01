package dom.correo;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;

@Named("Em@ils")
public class ServicioBandejaDeEntrada extends AbstractFactoryAndRepository {
	
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
	    		mensajeTransient.setUsuario(usuarioActual());
	    		mensajeTransient.setFechaActual(LocalDate.now());
	    		persistIfNotAlready(mensajeTransient);
	    		
	    	}
		
		}
		return listaMensajesPersistidos();
	}
	
	/*
	 * Repositorio: trae los emails guardados por el usuario registrado
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
	
	@MemberOrder(sequence = "2")
	@Named("Enviar") 
	public void enviarCorreo() {
		/*Envio enviar = new Envio();
		enviar.setProperties();
		enviar.enviar();*/
	}
	
	
	protected boolean creadoPorActualUsuario(final Mensaje m) {
	    return Objects.equal(m.getUsuario(), usuarioActual());
	}
	
	protected String usuarioActual() {
	    return getContainer().getUser().getName();
	}
}
