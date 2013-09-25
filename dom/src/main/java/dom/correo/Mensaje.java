package dom.correo;

import java.io.IOException;
import java.util.Timer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Title;

import dom.todo.ToDoItems;

//import dom.abm.Habitacion;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")

@ObjectType("MENSAJE")
@Audited
public class Mensaje {
	
	private String nombre;
	private String apellido;
	private String telefono;
	private String correo;
	//private List<Habitacion> listaHabitaciones;
	
	
	
	public Mensaje () {}
	
	@Title
	@Disabled
	@Optional
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Disabled
	@Optional
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	@Disabled
	@Optional
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Disabled
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
		
	
	/*
	 * Estado del mensaje se usa para saber si es necesario borrarlo de la BD
	 
	private boolean estado;
	
	@Hidden
	public boolean getEstado() {
		return estado;
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
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
