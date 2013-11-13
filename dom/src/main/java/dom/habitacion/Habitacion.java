package dom.habitacion;

import java.util.List;

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
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;

import dom.disponibilidad.HabitacionFecha;
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
	@RegEx(validation="[\\w\\s]+")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}
	//}}
	
	//{{ Interno de la habitación para llamadas con la central asterisk
	private int interno;
	
	public int getInterno(){
		return interno;
	}
	
	public void setInterno(final int interno) {
		actualizarInternos(interno);
		this.interno = interno;
	}
	
	private void actualizarInternos(final int nuevoInterno) {
		
		List<HabitacionFecha> habitacionesReservadas = container.allMatches(HabitacionFecha.class, new Filter<HabitacionFecha>(){
			@Override
			public boolean accept(HabitacionFecha habitacion) {
				// TODO Auto-generated method stub
				return habitacion.getNombreHabitacion().equals(getNombre());
			}				
		});
		
		for(HabitacionFecha hF : habitacionesReservadas){
			hF.setInterno(nuevoInterno);
		}
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
	public void setEstado(final boolean estado) {
		this.estado = estado;
	}	
	//}}
	
	@Named("Borrar")
	@Bulk
	public List<Habitacion> borrar() {		
		container.removeIfNotAlready(this);
		return habitacionServicio.Listahabitacion();
	}
	
	private DomainObjectContainer container;
	
	public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.setContainer(container);
    }
	
	public DomainObjectContainer getContainer() {
		return container;
	}
	public void setContainer(final DomainObjectContainer container) {
		this.container = container;
	}
	
	private HabitacionServicio habitacionServicio;
	
	public void injectHabitacionServicio(HabitacionServicio habitacionServicio) {
		this.habitacionServicio = habitacionServicio;
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
		
		public static Filter<Habitacion> creadoPor(final String usuarioActual) {
	        return new Filter<Habitacion>() {
	            @Override
	            public boolean accept(final Habitacion habitacion) {
	                return Objects.equal(habitacion.getUsuario(), usuarioActual);
	            }
	        };
	    }
		
}
