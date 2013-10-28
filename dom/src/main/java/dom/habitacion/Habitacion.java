package dom.habitacion;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;

import dom.enumeradores.EstadoHabitacion;
import dom.enumeradores.TipoHabitacion;
import dom.reserva.Reserva;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="traerHabitaciones", language="JDOQL",value="SELECT FROM dom.habitacion.Habitacion")
@ObjectType("HABITACION")
@AutoComplete(repository=HabitacionServicio.class,action="completaHabitaciones")
@Audited
public class Habitacion {
	
	public String iconName() {
		return "habitacion";
	}
		
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
	
	//{{Tipo de Habitacion
	private TipoHabitacion tipoHabitacion;
		
	@MemberOrder(sequence = "2")
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	public void setTipoHabitacion(final TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	
	//{{Estado del objeto
	private boolean estado;
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}	
	//}}
	
	//{{Estado : Disponible / Bloqueada
	private EstadoHabitacion estadoHabitacion;
	
	@Hidden(where=Where.ALL_TABLES)
	public EstadoHabitacion getEstado() {
		return estadoHabitacion;
	}
	public void setEstado(EstadoHabitacion estadoHabitacion) {
		this.estadoHabitacion = estadoHabitacion;
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
	
	@Named("Borrar Habitación")
	@Bulk
	public Habitacion borrar() {		
		if(isEstado()) {
			setEstado(false);
		}
		return this;
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
