package dom.correo;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Title;
import org.joda.time.LocalDate;

//import dom.abm.Habitacion;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("MENSAJE")
@Audited
@Immutable
public class Mensaje {
	
	private String nombre;
	private String apellido;
	private String telefono;
	private String correo;
	private LocalDate fechaActual;
	//private List<Habitacion> listaHabitaciones;
	
	
	
	public Mensaje () {}
	
	@Title
	@Optional
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Optional
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	@Optional
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Optional
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	/*
	public List<Habitacion> getListaHabitaciones() {
		return listaHabitaciones;
	}
	public void setListaHabitaciones(List<Habitacion> listaHabitaciones) {
		this.listaHabitaciones = listaHabitaciones;
	}*/
	
	@Named("Reservar")
	@Bulk
	public void reservar() {
		
	}
	
	@Named("Borrar")
	@Bulk
	public List<Mensaje> borrar() {
		//Borramos el/los objeto/s seleccionado/s
		container.removeIfNotAlready(this);

		//Vuelvo a la bandeja de entrada
		return bde.listaMensajesPersistidos();
	}

	public LocalDate getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(LocalDate fechaActual) {
		this.fechaActual = fechaActual;
	}
    /*
     * Usuario actual logeado
     */
    private String usuario;

    @Hidden
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(final String usuario) {
        this.usuario = usuario;
    }
    
    // {{ injected: DomainObjectContainer
    private DomainObjectContainer container;

    public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
    //}}
    
    private ServicioBandejaDeEntrada bde;

    public void injectServicioBandejaDeEntrada(final ServicioBandejaDeEntrada bde) {
        this.bde = bde;
    }
    
}
