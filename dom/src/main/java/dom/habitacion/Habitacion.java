package dom.habitacion;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.enumeradores.TipoHabitacion;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
public class Habitacion {
	
	//{{idHabitacion
	private int idHabitacion;
				
	@Hidden
	public int getIdHabitacion() {
		return idHabitacion;
	}
	public void setIdHabitacion(final int idHabitacion) {
		this.idHabitacion = idHabitacion;
	}
	//}}
		
	//{{Nombre
	private String nombre;
	
	@Title
	@MemberOrder(sequence = "1")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
	//}}
	
	//{{Capacidad
	private int capacidad;
		
	@MemberOrder(sequence = "2")
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(final int capacidad) {
		this.capacidad = capacidad;
	}
	//}}
	
	//{{Tipo de Habitacion
	private TipoHabitacion tipoHabitacion;
		
	@MemberOrder(sequence = "3")
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	public void setTipoHabitacion(final TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	//}}
	
	//{{Usuario actual
	private String usuario;

    @Hidden
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(final String usuario) {
        this.usuario = usuario;
    }//}}
	
	public static Filter<Habitacion> creadoPor(final String usuarioActual) {
        return new Filter<Habitacion>() {
            @Override
            public boolean accept(final Habitacion habitacion) {
                return Objects.equal(habitacion.getUsuario(), usuarioActual);
            }
        };
    }
	
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.setContainer(container);
    }
	
	public DomainObjectContainer getContainer() {
		return container;
	}
	public void setContainer(DomainObjectContainer container) {
		this.container = container;
	}	
}
