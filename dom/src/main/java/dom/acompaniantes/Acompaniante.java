package dom.acompaniantes;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.disponibilidad.HabitacionFecha;
import dom.reserva.Reserva;
import dom.reserva.ReservaServicio;

/**
 * 
 * Los acompa&ntilde;antes que puede tener el huesped
 * que registra la reserva.
 * 
 * @author ProyectoCIP
 * @see dom.reserva.Reserva
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="acompaniantes", language="JDOQL",value="SELECT FROM dom.acompaniante.Acompaniante")
@AutoComplete(repository=ReservaServicio.class,action="completaAcompaniantes")
@ObjectType("ACOMPANIANTE")
@Audited
public class Acompaniante {
	
	/**
	 * 
	 * @return Retorna el nombre del icono que va a ser usado en el viewer
	 */
	public String iconName() {
		return "acompaniantes";
	}	

	private String nombre;
	
	/**
	 * 
	 * Es la primer parte del titulo que toma el objeto en el viewer
	 * 
	 * @return El nombre del Acompa&ntilde;ante 
	 */
	@Title(sequence="1.0")
	@RegEx(validation="[a-zA-Z]{2,15}(\\s[a-zA-Z]{2,15})*")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
	
	private String apellido;
	
	/**
	 * 
	 * Es la segunda parte del titulo que toma el objeto en el viewer
	 * 
	 * @return El apellido del Acompa&ntilde;ante
	 */
	@Title(sequence="1.1")
	@RegEx(validation="[a-zA-Z]{2,15}(\\s[a-zA-Z]{2,15})*")
	public String getApellido() {
		return apellido;
	}

	public void setApellido(final String apellido) {
		this.apellido = apellido;
	}	
	
	private int edad;
	
	/**
	 * 
	 * @return La edad del Acompa&ntilde;ante
	 */
	public int getEdad() {
		return edad;
	}

	public void setEdad(final int edad) {
		this.edad = edad;
	}
	
	private String relacion;
	
	/**
	 * 
	 * @return Es la relaci&oacute;n que el acompa&ntilde;ante tiene con el huesped (Esposa, Hijo, etc)
	 */
	public String getRelacion() {
		return relacion;
	}

	public void setRelacion(final String relacion) {
		this.relacion = relacion;
	}
	
	/**
	 * 
	 * Borra el acompa&ntilde;ante del repositorio
	 * 
	 * @return Retorna null cuando se usa este m&eacute;todo
	 */
	@Named("Borrar")
	@Bulk
	public Acompaniante borrar() {		
		container.removeIfNotAlready(this);
		return this;
	}
	
	private Reserva reserva;
	
	/**
	 * 
	 * @return Es la reserva en la que se registr&oacute; ese acompa&ntilde;ante
	 */
	@Hidden(where=Where.ALL_TABLES)
	public Reserva getReserva() {
		return reserva;
	}
	
	public void setReserva(final Reserva reserva) {
		this.reserva = reserva;
	}
	
	// {{ injected: DomainObjectContainer
    private DomainObjectContainer container;

    public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    }
    
  //{{Usuario actual
  	private String usuario;

  	@Hidden
  	public String getUsuario() {
  	    return usuario;
  	}

  	public void setUsuario(final String usuario) {
  	    this.usuario = usuario;
  	}//}}
  		
  	public static Filter<Acompaniante> creadoPor(final String usuarioActual) {
  	    return new Filter<Acompaniante>() {
  	        @Override
  	        public boolean accept(final Acompaniante acompaniante) {
  	            return Objects.equal(acompaniante.getUsuario(), usuarioActual);
  	        }
  	    };
  	}	
	
}
