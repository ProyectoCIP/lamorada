package dom.disponibilidad;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.LocalDate;

import dom.enumeradores.EstadoHabitacion;
import dom.enumeradores.TipoHabitacion;
import dom.huesped.Huesped;
import dom.reserva.Reserva;
import dom.reserva.ReservaServicio;

/**
 * 
 * Es una copia de un objeto HabitacionFecha.
 * Sirve para mostrar el mapa de disponibilidad de las habitaciones 
 * en una fecha o un rango de fechas ingresadas por el usuario.
 * Los objetos de esta clase se persisten cuando se hace la consulta y luego se borran.
 * El propósito de esto es para mostrar las habitaciones reservadas 
 * y las que no, se rellenan con estos objetos. 
 * Es por eso que es innecesario mantenerlos persistidos.
 * 
 * @author ProyectoCIP
 * @see dom.reserva.Reserva
 * @see dom.reserva.ReservaServicio
 * @see dom.huesped.Huesped
 * @see dom.enumeradores.EstadoHabitacion
 * @see dom.enumeradores.TipoHabitacion
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="disponibilidad", language="JDOQL",value="SELECT FROM dom.disponibilidad.Disponibilidad")
@ObjectType("DISPONIBILIDAD")
@Audited
public class Disponibilidad {
	
	/**
	 * 
	 * El ícono cambia dependiendo de si está Disponible, Bloqueada o Reservada
	 * 
	 * @return Retorna el nombre del ícono que va a ser usado en el viewer
	 */
	public String iconName() {
		if (getEstado() == EstadoHabitacion.BLOQUEADA) { 
			return "bloqueada";
		}
		else {
			return (getReserva() == null) ? "disponibilidad" : "candado";
		}
	}
	
	private EstadoHabitacion estadoHabitacion;
		
	/**
	 * 
	 * @return Retorna el estado de la habitación que puede ser Disponible o Bloqueada
	 */
	@Hidden
	public EstadoHabitacion getEstado() {
		return estadoHabitacion;
	}
	public void setEstado(final EstadoHabitacion estadoHabitacion) {
		this.estadoHabitacion = estadoHabitacion;
	}

	/**
	 * 
	 * Para los métodos que se aplican a la lista de objetos seleccionados 
	 * 
	 * @return Retorna el objeto DISPONIBLE
	 */
	@Bulk
	@MemberOrder(name="estadoHabitacion",sequence="2")
	public Disponibilidad desbloquear() {
		if(getEstado() == EstadoHabitacion.BLOQUEADA) {
			this.estadoHabitacion = EstadoHabitacion.DISPONIBLE;
		}
		return this;
	}
	
	/**
	 * 
	 * Si la habitación que se muestra en el mapa de disponibilidad
	 * aún no esta persistida, entonces es necesario guardar el objeto HabitaciónFecha en el repositorio.
	 * 
	 * Para los metodos que se aplican a la lista de objetos seleccionados 
	 * 
	 * @return Retorna el objeto BLOQUEADO
	 */
	@Bulk
	@MemberOrder(name="estadoHabitacion",sequence="3")
	public Disponibilidad bloquear() {
		if(getEstado() == EstadoHabitacion.DISPONIBLE) {
			this.estadoHabitacion = EstadoHabitacion.BLOQUEADA;
			
			HabitacionFecha hF = hFS.existeReserva(new LocalDate(getFecha()), getNombreHabitacion());
			
			if(hF == null) {
				hF = container.newTransientInstance(HabitacionFecha.class);
				hF.setEstado(EstadoHabitacion.BLOQUEADA);
				hF.setFecha(getFecha());
				hF.setNombreHabitacion(getNombreHabitacion());
				hF.setInterno(getInterno());
				hF.setPax(0);
				hF.setTipoHabitacion(getTipoHabitacion());
				hF.setTarifa(new BigDecimal(0));
				container.persistIfNotAlready(hF);
			}
			else {
				hF.setEstado(EstadoHabitacion.BLOQUEADA);
			}
				 
		}
		
		return this;
	}
	
	/**
	 * 
	 * Desactiva el método bloquear si se cumple la condición
	 * 
	 * @return Si cumple con la condición devuelve la cadena que se muestra en el viewer
	 */
	public String disableBloquear() {
		if(getEstado() != EstadoHabitacion.BLOQUEADA) {
			return (getReserva() != null) ? "No se puede bloquear una habitación reservada" : null;
		}
		else {
			return "Ya se encuentra bloqueada!";
		}
	}
	
	/**
	 * 
	 * Desactiva el método desbloquear si se cumple la condición
	 * 
	 * @return Si cumple con la condición devuelve la cadena que se muestra en el viewer
	 */
	public String disableDesbloquear() {
		if(getEstado() != EstadoHabitacion.DISPONIBLE) {
			return (getReserva() != null) ? "Esta habitación ya está reservada" : null;
		}
		else {
			return "Ya se encuentra disponible!";
		}
	}
	//}}
	
	
	private String nombreHabitacion;

	/**
	 * 
	 * @return Es el título que toma el objeto en el viewer
	 */
	@Title
	@Hidden
	public String getNombreHabitacion() {
		return nombreHabitacion;
	}
	
	public void setNombreHabitacion(final String nombreHabitacion) {
		this.nombreHabitacion = nombreHabitacion;
	}
	
	private boolean paraReservar;
	
	/**
	 * 
	 * @return Retorna si o no el objeto está seleccionado para su posterior reserva
	 */
	@Named("Seleccionada")
	public boolean isParaReservar() {
		return paraReservar;
	}
	
	public void setParaReservar(final boolean paraReservar) {
		this.paraReservar = paraReservar;
	}
	
	/**
	 * 
	 * @return Retorna la fecha formateada que se muestra en el viewer de la forma "dd/MM/yyyy"
	 */
	@Named("Fecha")
	public String getFechaString() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(getFecha());
	}
	
	private Date fecha;
	
	/**
	 * 
	 * @return Retorna la fecha de consulta por el usuario
	 */
	@Hidden
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(final Date date) {
		this.fecha = date;
	}
	
	/**
	 * 
	 * Si la habitación que se muestra en el mapa de disponibilidad
	 * está disponible y no está reservada, aplica la selección para su posterior reserva.
	 * 
	 * Para los métodos que se aplican a la lista de objetos seleccionados 
	 * 
	 * @return Retorna el objeto BLOQUEADO
	 */
	@Named("Seleccionar")
	@Bulk
	@MemberOrder(name="paraReservar",sequence="1")
	public Disponibilidad reservar() {
		
		if(getEstado() == EstadoHabitacion.DISPONIBLE) {
			if(getReserva() == null) {			
				if(isParaReservar())
					setParaReservar(false);
				else
					setParaReservar(true);			
			}
		}
		
		return this;
	}
	
	/**
	 * 
	 * Desactiva el método reservar si la habitación está bloqueada o ya está seleccionada
	 * 
	 * @return Si cumple con la condición devuelve la cadena que se muestra en el viewer
	 */
	public String disableReservar() {
		if(getEstado() != EstadoHabitacion.BLOQUEADA) {	
			return paraReservar ? "Ya esta seleccionada!" : null;
		}
		else {
			return "No se puede reservar una habitación que está bloqueada";
		}
	}
	
	/**
	 * 
	 * Si se accede a uno y solo uno objeto Disponibilidad en el mapa de disponibilidad
	 * se puede reservar directamente este día y está habitación.
	 * 
	 * @return Retorna la reserva creada con los nuevos datos
	 */
	@Named("Reservar") 
	public Reserva reservarDisponible(
			@Named("Huésped") Huesped huesped,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario
			) {
		
		List<Disponibilidad> disponibilidad = new ArrayList<Disponibilidad>();
		setParaReservar(true);
		disponibilidad.add(this);
		return rS.crear(disponibilidad, huesped, comentario);
	}
	
	/**
	 * 
	 * Desactiva el método reservar si la habitación está bloqueada o ya está reservada
	 * 
	 * @return Si cumple con la condición devuelve la cadena que se muestra en el viewer
	 */
	public String disableReservarDisponible(final Huesped huesped,final String comentario) {

		if(getEstado() != EstadoHabitacion.BLOQUEADA) {	
			return (getReserva() != null) ? "Ya está reservada" : null;
		}
		else {
			return "No se puede reservar una habitación que está bloqueada";
		}
	}	
	
	private TipoHabitacion tipoHabitacion;
	
	/**
	 * 
	 * @return Retorna el tipo de habitación (Doble, Triple, Cuadruple)
	 */
	@Hidden
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	
	public void setTipoHabitacion(final TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	
	private BigDecimal tarifa;
	
	/**
	 * 
	 * @return Retorna la tarifa que se va a cobrar por reservar esta habitación
	 */
	@Hidden(where=Where.OBJECT_FORMS)
	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(final BigDecimal tarifa) {
		this.tarifa = tarifa;
	}
	
	
	private int interno;
	
	/**
	 * 
	 * @return Retorna el número Interno de la habitación que se registro en la central telefónica
	 */
	@Hidden
	public int getInterno(){
		return interno;
	}
		
	public void setInterno(final int interno) {
		this.interno = interno;
	}
	
	private Reserva reserva;
	
	/**
	 * 
	 * @return Retorna el objeto reserva en el que esta habitación esta registrada
	 */
	@Named("Estado")
	@Hidden(where=Where.OBJECT_FORMS)
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
	
	private ReservaServicio rS;
	
	public void injectReservaServicio(final ReservaServicio rS) {
		this.rS = rS;
	}
	
	private HabitacionFechaServicio hFS;
	
	public void injectHabitacionFechaServicio(final HabitacionFechaServicio hFS) {
		this.hFS = hFS;
	}

}
