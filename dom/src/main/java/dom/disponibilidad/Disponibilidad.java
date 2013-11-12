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

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="disponibilidad", language="JDOQL",value="SELECT FROM dom.disponibilidad.Disponibilidad")
@ObjectType("DISPONIBILIDAD")
@Audited
public class Disponibilidad {
	
	public String iconName() {
		if (getEstado() == EstadoHabitacion.BLOQUEADA) { 
			return "bloqueada";
		}
		else {
			return (getReserva() == null) ? "disponibilidad" : "candado";
		}
	}
	
	//{{Estado : Disponible / Bloqueada
	private EstadoHabitacion estadoHabitacion;
		
	@Hidden
	public EstadoHabitacion getEstado() {
		return estadoHabitacion;
	}
	public void setEstado(final EstadoHabitacion estadoHabitacion) {
		this.estadoHabitacion = estadoHabitacion;
	}
	
	@Bulk
	@MemberOrder(name="estadoHabitacion",sequence="2")
	public Disponibilidad desbloquear() {
		if(getEstado() == EstadoHabitacion.BLOQUEADA) {
			this.estadoHabitacion = EstadoHabitacion.DISPONIBLE;
		}
		return this;
	}
	
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
	
	public String disableBloquear() {
		if(getEstado() != EstadoHabitacion.BLOQUEADA) {
			return (getReserva() != null) ? "No se puede bloquear una habitación reservada" : null;
		}
		else {
			return "Ya se encuentra bloqueada!";
		}
	}
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

	@Title
	@Hidden
	public String getNombreHabitacion() {
		return nombreHabitacion;
	}
	
	public void setNombreHabitacion(final String nombreHabitacion) {
		this.nombreHabitacion = nombreHabitacion;
	}
	
	private boolean paraReservar;
	
	@Named("Seleccionada")
	public boolean isParaReservar() {
		return paraReservar;
	}
	
	public void setParaReservar(final boolean paraReservar) {
		this.paraReservar = paraReservar;
	}
	
	@Named("Fecha")
	public String getFechaString() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(getFecha());
	}
	
	private Date fecha;
	
	@Hidden
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(final Date date) {
		this.fecha = date;
	}
	
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
	
	public String disableReservar() {
		if(getEstado() != EstadoHabitacion.BLOQUEADA) {	
			return paraReservar ? "Ya esta seleccionada!" : null;
		}
		else {
			return "No se puede reservar una habitación que está bloqueada";
		}
	}
	
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
	
	public String disableReservarDisponible(final Huesped huesped,final String comentario) {

		if(getEstado() != EstadoHabitacion.BLOQUEADA) {	
			return (getReserva() != null) ? "Ya está reservada" : null;
		}
		else {
			return "No se puede reservar una habitación que está bloqueada";
		}
	}	
	
	private TipoHabitacion tipoHabitacion;
	
	@Hidden
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	
	public void setTipoHabitacion(final TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	
	private BigDecimal tarifa;
	
	@Hidden(where=Where.OBJECT_FORMS)
	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(final BigDecimal tarifa) {
		this.tarifa = tarifa;
	}
	
	//{{ Interno de la habitación para llamadas con la central asterisk
	private int interno;
		
	@Hidden
	public int getInterno(){
		return interno;
	}
		
	public void setInterno(final int interno) {
		this.interno = interno;
	}
	//}}
	
	private Reserva reserva;
	
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
