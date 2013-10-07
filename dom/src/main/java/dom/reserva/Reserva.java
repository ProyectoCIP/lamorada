package dom.reserva;

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

import dom.consumo.Consumo;
import dom.enumeradores.FormaPago;
import dom.habitacion.Habitacion;
import dom.huesped.Huesped;


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
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
	private IEReserva estado = null;

	@MemberOrder(sequence="1")
	public IEReserva getEstado() {
		return estado;
	}

	public void setEstado(final IEReserva estado) {
		this.estado = estado;
	}
	//}}
	
	//{{Nombre del estado actual que aparece en el viewer : Disponible, Reservada, CheckIN, CheckOUT, Cerrada
	@Title
	public String getNombreEstado() {
		return (estado == null) ? "Disponible" : getEstado().getNombre();		
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
	private float montoSeña;

	public float getMontoSeña() {
		return montoSeña;
	}

	public void setMontoSeña(float montoSeña) {
		this.montoSeña = montoSeña;
	}
	//}}
	
	//{{Forma en la que se hace la seña
	private FormaPago tipoSeña;
	
	public FormaPago getTipoSeña() {
		return tipoSeña;
	}
	
	public void setTipoSeña(FormaPago tipoSeña) {
		this.tipoSeña = tipoSeña;
	}
	//}}
	
	//{{Lista de habitaciones a reservar
	private List<Habitacion> listaHabitaciones;
	
	public List<Habitacion> getListaHabitaciones() {
		return listaHabitaciones;
	}

	public void setListaHabitaciones(List<Habitacion> listaHabitaciones) {
		this.listaHabitaciones = listaHabitaciones;
	}
	//}}
	
	//{{Consumos
	@Persistent(mappedBy="reserva")
	private List<Consumo> consumos;
	
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
		getConsumos().add(reservaServicio.agregarConsumo(this, descripcion, cantidad, precio));
		return this;
	}
	//}}
	
	//{{Borrar consumo
	@Named("Borrar Consumo")
    @MemberOrder(name="consumos",sequence="2")
    public Reserva remove(final Consumo consumo) {
    	getConsumos().remove(consumos);
    	return this;
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
	@Disabled(when=When.ONCE_PERSISTED)
	@Bulk
	public void reservar() {
		/*
		 * Se puede reservar
		 */
		if(getEstado()==null) {
			setEstado(new EReservada());
			getEstado().accion(this);
		}
		else {
			container.informUser("No se puede realizar la Reserva solicitada");
		}
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
    
    /*
     * Los datos del CheckIN
     */
    
    //{{Acción : CheckIN / desactivada cuando el objeto aún no se ha persistido (no es reserva)
    @Named("CheckIN")
	@Disabled(when=When.UNTIL_PERSISTED)
    public void checkIn() {
		/*
		 * Se puede checkIn
		 */
		if(getEstado() instanceof EReservada) {
			setEstado(new ECheckIN());
			getEstado().accion(this);
		}
		else {
			container.informUser("No se puede realizar el CheckIN solicitado");
		}
	}
    //}}
    
    /*
     * Los datos del CheckOUT
     */
    
    //{{Acción : CheckOUT / desactivada cuando el objeto aún no se ha persistido (no es reserva)
    @Named("CheckOUT")
    @Disabled(when=When.UNTIL_PERSISTED)
	public void checkOut() {
		/*
		 * Se puede checkIn
		 */
		if(getEstado() instanceof ECheckIN) {
			setEstado(new ECheckOUT());
			getEstado().accion(this);
		}
		else {
			container.informUser("No se puede realizar el CheckOUT solicitado");
		}
	}
    //}}
    
    /*
     * Los datos del Cierre
     */
    
    //{{Número de la factura impresa
    private String numeroFactura;

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	//}}
	
	//{{Fecha de la factura impresa
	private LocalDate fechaFactura;
	
	public LocalDate getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(LocalDate fechaFactura) {
		this.fechaFactura = fechaFactura;
	}	
	//}}
	
	//{{El monto total que se debe pagar
    private float total;
    
    public float getTotal() {
    	return total;
    }
    
    public void setTotal(final float total) {
    	this.total = total;
    }
    //}}
    
    //{{Acción : Cerrar / desactivada cuando el objeto aún no se ha persistido (no es reserva)
    @Named("Cerrar")
    @Disabled(when=When.UNTIL_PERSISTED)
	public void cerrar() {
		/*
		 * Se puede checkIn
		 */
		if(getEstado() instanceof ECheckOUT) {
			setEstado(new ECerrada());
			getEstado().accion(this);
		}
		else {
			container.informUser("No se puede realizar el Cierre solicitado");
		}
	}
    //}}
	
}
