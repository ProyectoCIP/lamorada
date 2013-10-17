package dom.reserva;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.When;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.annotation.Render.Type;
import org.joda.time.LocalDate;

import com.google.common.collect.Lists;

import dom.consumo.Consumo;
import dom.enumeradores.FormaPago;
import dom.huesped.Huesped;
import dom.todo.ToDoItem;
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column="numero")
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("RESERVA")
@AutoComplete(repository=ReservaServicio.class, action="completaReservas")
@Audited
public class Reserva {
	
	//{{Numero de la reserva, autoincremental. Responsabilidad del ORM
	private long numero;

	@NotPersisted
	public long getNumero() {
		return numero;
	}
	
	public void setNumero(final long numero) {
		this.numero = numero;
	}
	//}}
	
	//{{Estado actual de la reserva
	@Persistent
	private EReservada eReservada;
	//{{Estado actual de la reserva
	@Persistent
	private ECheckIN eCheckin;
	//{{Estado actual de la reserva
	@Persistent
	private ECheckOUT eCheckout;
	//{{Estado actual de la reserva
	@Persistent
	private ECerrada eCerrada;
	
	private IEReserva estado = new EReservada();

	@Hidden
	public IEReserva getEstado() {
		return estado;
	}

	public void setEstado(final IEReserva estado) {
		this.estado = estado;
	}
	//}}
	
	@Title
	public String getNombreEstado() {
		return (estado == null) ? "Disponible" : getEstado().getNombre();		
		//return "Reservada";
	}
	
	//}}
	
	//{{Fecha en la que se realiza la reserva
	private LocalDate fecha;
	
	@MemberOrder(sequence="5")
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(final LocalDate fecha) {
		this.fecha = fecha;
	}
	//}}
	
	//{{Monto de la seña
	private float montoSena;

	public float getMontoSena() {
		return montoSena;
	}

	public void setMontoSena(float montoSena) {
		this.montoSena = montoSena;
	}
	//}}
	
	//{{Forma en la que se hace la seña
	private FormaPago tipoSena;
	
	public FormaPago getTipoSena() {
		return tipoSena;
	}
	
	public void setTipoSena(FormaPago tipoSena) {
		this.tipoSena = tipoSena;
	}
	//}}
	
	//{{Lista de habitaciones a reservar
	@Persistent(mappedBy="reserva")
	private List<HabitacionFecha> habitaciones = new ArrayList<HabitacionFecha>();
	
	public List<HabitacionFecha> getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(List<HabitacionFecha> listaHabitaciones) {
		this.habitaciones = listaHabitaciones;
	}
	
	
	@Named("Borrar Habitación")
	@MemberOrder(name="habitaciones",sequence="1")
	public Reserva removeFromHabitaciones(final HabitacionFecha habitacion) {
		habitaciones.remove(habitacion);
		container.removeIfNotAlready(habitacion);
		return this;
	}
	@Hidden
	public void addToHabitacion(HabitacionFecha habitacion) {
	    if(habitacion == null || habitaciones.contains(habitacion)) {
	    	return;
	    }
	    habitacion.setReserva(this);
	    habitaciones.add(habitacion);
	}	
	
	
    // provide a drop-down
    public List<HabitacionFecha> choices0RemoveFromHabitaciones() {
        return Lists.newArrayList(getHabitaciones());
    }
	//}}
	
	//{{Consumos
	@Persistent(mappedBy="reserva")
	private List<Consumo> consumos = new ArrayList<Consumo>();
	
	@Named("Consumición en esta reserva")
	@Render(Type.EAGERLY)
	public List<Consumo> getConsumos() {
		return consumos;
	}
	
	public void setConsumos(final List<Consumo> consumos) {
		this.consumos = consumos;
	}
	//}}

	//{{Agregar consumo
	@Named("Agregar Consumo")
	@MemberOrder(name="consumos",sequence="1")
    public Reserva add(
    		@Named("Descripcion") String descripcion,
    		@Named("Cantidad") int cantidad,
    		@Named("Precio") float precio) {
		/*
		 * Se envian los datos del formulario consumo al servicio y nos lo retorna ya persistido
		 */
		
		Consumo consumo = container.newTransientInstance(Consumo.class);
		consumo.setDescripcion(descripcion);
		consumo.setCantidad(cantidad);
		consumo.setPrecio(precio);

		//dependencia
		addToConsumo(consumo);
		container.persistIfNotAlready(consumo);
		return this;
	}
	//}}	
	
	//{{Borrar consumo
	@Named("Borrar Consumo")
    @MemberOrder(name="consumos",sequence="2")
    public Reserva removeFromConsumos(final Consumo consumo) {
    	consumos.remove(consumo);
    	container.removeIfNotAlready(consumo);
    	return this;
    }

	@Hidden
	public void addToConsumo(Consumo consumo) {
	    if(consumo == null || consumos.contains(consumo)) {
	    	return;
	    }
	    consumo.setReserva(this);
	    consumos.add(consumo);
	}
	
	public List<Consumo> choices0RemoveFromConsumos() {
		return Lists.newArrayList(getConsumos());
	}
	//}}
	
	private int cantidadDias;
	
	public int getCantidadDias() {
		return cantidadDias;
	}
	
	public void setCantidadDias(final int cantidadDias) {
		this.cantidadDias = cantidadDias;
	}
	
	//{{Comentarios - No se muestran cuando se lista la reserva
	private String comentario;
	
	@Hidden(where=Where.ALL_TABLES)
	@MultiLine(numberOfLines=3)
	@MemberOrder(sequence="3")
	public String getComentario() {
		return comentario;
	}

	public void setComentario(final String comentario) {
		this.comentario = comentario;
	}
	//}}
	
	//{{Huesped
	private Huesped huesped;
	
	public Huesped getHuesped() {
		return huesped;
	}

	public void setHuesped(final Huesped huesped) {
		this.huesped = huesped;
	}	
	//}}
	
	//{{Accion : Reservar / Desactivada cuando el objeto ya está persistido (ya se encuentra reservada)
	@Named("Reservar")
	@Hidden(when=When.ONCE_PERSISTED)
	@Bulk
	public void reservar() {
		/*
		 * Se puede reservar
		 */
		/*if(getEstado()==null) {
			eReservada = container.newTransientInstance(EReservada.class);
			setEstado(eReservada);
			getEstado().accion(this);
		}
		else {
		*/
			container.informUser("No se puede realizar la Reserva solicitada");
		//}
	}
	//}}
	
	//{{Accion : Borrar Reserva / desactivada cuando el objeto aún no se ha persistido (no es reserva)
	@Named("Borrar Reserva")
	@Disabled(when=When.UNTIL_PERSISTED)
	public void borrarReserva() {
		container.informUser("Reserva número: "+getNumero()+" a sido borrada");
		container.removeIfNotAlready(this);
	}
	//}}
	
	private DomainObjectContainer container;
	
	public void setContainer(final DomainObjectContainer container) {
		this.container = container;
	}   
	
	//{{inyeccion ReservaServicio
	private ReservaServicio reservaServicio;
	
	public void injectReservaServicio(final ReservaServicio reservaServicio) {
		this.reservaServicio = reservaServicio;
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
    

	
}
