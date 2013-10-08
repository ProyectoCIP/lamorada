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

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("HABITACION")
@AutoComplete(repository=HabitacionServicio.class,action="completaHabitaciones")
@Audited
public class Habitacion {
		
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
	
	//{{Cuanto cuesta la habitación por noche en base a su capacidad
	/*
	private float costo;
	
	public float getCosto() {
		
	}
	
	public void setCosto() {
		
	}
	*/
	//}}
	
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
	
	//{{Fecha
	private LocalDate fecha;

	@Hidden
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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
	public void borrar() {		
		if(isEstado()) {
			setEstado(false);
		}
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
