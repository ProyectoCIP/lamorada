package dom.reserva;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberGroups;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Render.Type;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.LocalDate;

import com.google.common.collect.Lists;

import dom.consumo.Consumo;
import dom.disponibilidad.HabitacionFecha;
import dom.enumeradores.EstadoReserva;
import dom.enumeradores.FormaPago;
import dom.huesped.Huesped;
import dom.tarifa.TarifaServicio;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.APPLICATION)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="reservas", language="JDOQL",value="SELECT FROM dom.reserva.Reserva order by numero desc")
@ObjectType("RESERVA")
@AutoComplete(repository=ReservaServicio.class, action="completaReservas")
@MemberGroups({"Estados","Datos de la Reserva","Datos del Cierre"})
@Audited
public class Reserva {
	
	public String iconName() {
		if(getEstado() == EstadoReserva.Reservada) {
			return "Reservada";	
		}
		if(getEstado() == EstadoReserva.CheckIN) {
			return "CheckIN";	
		}
		if(getEstado() == EstadoReserva.CheckOUT) {
			return "CheckOUT";	
		}
		if(getEstado() == EstadoReserva.Cerrada) {
			return "Facturada";	
		}
		return null;
	}
	
	//{{Numero de la reserva, autoincremental. Responsabilidad del ORM
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private long numero;

    @Disabled
	@MemberOrder(name="Datos de la Reserva",sequence="1")    
	public long getNumero() {
		return numero;
	}
	
	public void setNumero(final long numero) {
		this.numero = numero;
	}
	//}}
	
	private String nombreEstado;
	
	@Named("Estado")
	@NotPersisted
	@Hidden(where=Where.ALL_TABLES)
	@MemberOrder(name="Estados",sequence="1")
	public String getNombreEstado() {
		nombreEstado = getEstado().toString();
		return nombreEstado;
	}
	
	private EstadoReserva estado;
	
	@Hidden
	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(final EstadoReserva estado) {
		this.estado = estado;
	}
	//}}
	
	//{{Titulo
	public String title(){
		return getEstado().toString();	
	}
	//}}
	
	//{{Fecha en la que se realiza la reserva
	@Named("Fecha")
	@MemberOrder(name="Datos de la Reserva",sequence="2")
	public String getFechaString() {
		if(getFecha() != null) {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			return formato.format(getFecha());
		}
		return null;
	}
	
	private Date fecha;
	
	@Hidden
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}
	//}}
	
	//{{Monto de la seña
	private float montoSena;
	
	@Named("Monto Seña")
    @Hidden(where=Where.ALL_TABLES)
    @Optional
	@MemberOrder(name="Datos de la Reserva",sequence="3")
	public float getMontoSena() {
		return montoSena;
	}

	public void setMontoSena(float montoSena) {
		this.montoSena = montoSena;
	}
	//}}
	
	//{{Forma en la que se hace la seña
	private FormaPago tipoSena;

    @Hidden(where=Where.ALL_TABLES)
    @Optional
	@MemberOrder(name="Datos de la Reserva",sequence="4")
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

	@Render(Type.EAGERLY)
	public List<HabitacionFecha> getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(List<HabitacionFecha> listaHabitaciones) {
		this.habitaciones = listaHabitaciones;
	}
	
	
	@Named("Borrar")
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
	
	@Named("Consumiciónes/Extras")
	public List<Consumo> getConsumos() {
		return consumos;
	}
	
	public void setConsumos(final List<Consumo> consumos) {
		this.consumos = consumos;
	}
	//}}

	//{{Agregar consumo
	@Named("Agregar")
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
	@Named("Borrar")
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
	
	//{{Comentarios - No se muestran cuando se lista la reserva
	private String comentario;
	
	@MultiLine(numberOfLines=3)
	@MemberOrder(name="Datos de la Reserva",sequence="5")
	@Hidden(where=Where.ALL_TABLES)
	public String getComentario() {
		return comentario;
	}

	public void setComentario(final String comentario) {
		this.comentario = comentario;
	}
	//}}
	
	//{{Huesped
	private Huesped huesped;

	@MemberOrder(name="Datos de la Reserva",sequence="6")
	public Huesped getHuesped() {
		return huesped;
	}

	public void setHuesped(final Huesped huesped) {
		this.huesped = huesped;
	}	
	//}}
	
	//{{Si el huesped viene con empresa esta la opcion de cobrar la tarifa por convenio
	private boolean tarifaEmpresa;

	@Disabled
	@MemberOrder(name="Datos de la Reserva", sequence="7")
	public boolean isTarifaEmpresa() {
		return tarifaEmpresa;
	}

	public void setTarifaEmpresa(boolean tarifaEmpresa) {
		this.tarifaEmpresa = tarifaEmpresa;
	}
	
	@Named("On/Off")
	@MemberOrder(name="tarifaEmpresa",sequence="1")
	public Reserva tarifaEmpresa() {
		tarifaEmpresa = isTarifaEmpresa() ? false : true;
		actualizarTarifas();
		return this;
	}
	
	private void actualizarTarifas() {
		for(HabitacionFecha h : getHabitaciones()) {
			if(isTarifaEmpresa()) {			
				h.setTarifa(getHuesped().getEmpresa().getTarifa());
			}
			else {
				h.setTarifa(tFS.tarifa(h.getPax()).getPrecio());
			}
		}
	}
	
	public String disableTarifaEmpresa() {
		return (getHuesped().getEmpresa() == null) ? "Este cliente no es miembro de ninguna empresa" : null;
	}
	//}}
	
	//{{Accion
	@Named("Borrar")
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
 
    //{{ Muestra el total a pagar (tiene en cuenta consumos, descuentos...)
    private float total;
    
    @Disabled
    @NotPersisted
    @MemberOrder(name="Datos del Cierre",sequence="4")
    public float getTotal() {
    	
    	float total = 0;
    	
    	/*
    	 * El precio de las habitaciones
    	 */
    	for(HabitacionFecha h : getHabitaciones()) {
    		total += h.getTarifa();
    	}
    	
    	/*
    	 * El precio de las consumiciones
    	 */
    	for(Consumo c : getConsumos()) {
    		total += c.getPrecioTotal();
    	}
    	
    	/*
    	 * Descuentos
    	 */    	
    	total -= getDescuento();
    	
    	return total;
    }
    
    public void setTotal(float total) {
    	this.total = total;
    }
    //}}

    //{{ La forma en que se paga la estadía
    private FormaPago formaDeCierre;

    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Datos del Cierre",sequence="1")
    @Named("Paga con")
    public FormaPago getFormaDeCierre() {
		return formaDeCierre;
	}

	public void setFormaDeCierre(FormaPago formaDeCierre) {
		this.formaDeCierre = formaDeCierre;
	}
	
	public String disableFormaDeCierre() {
		return isCerrada() ? null : "La reserva debe estar cerrada para editar la forma de pago del cierre";
	}
	//}}
    
	// {{ Descuento	
    private float descuento;
    
    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Datos del Cierre",sequence="2")  
    @Optional
	public float getDescuento() {
		return descuento;
	}

	public void setDescuento(float descuento) {
		this.descuento = descuento;
	}
			
	public String disableDescuento() {
		return isCerrada() ? null : "La reserva debe estar cerrada para editar el descuento";
	}	
	//}}
	
    
	private String numeroFactura;
	
    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Datos del Cierre",sequence="3")
    @Optional
    public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	
	public String disableNumeroFactura() {
		return isCerrada() ? null : "La reserva debe estar cerrada para editar el número de factura";
	}

    @MemberOrder(name="Datos del Cierre",sequence="4")
	public String getFechaFacturaString() {
		if(getFechaFactura() != null) {
	    	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			return (getFechaFactura() != null) ? formato.format(getFechaFactura()) : "";
		}
		return null;
	}
	
	private Date fechaFactura;

    @Hidden    
    @Optional
	public Date getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	
	public String disableFechaFactura() {
		return isCerrada() ? null : "La reserva debe estar cerrada para editar la fecha de la factura";
	}
	
	@MemberOrder(name="nombreEstado",sequence="1")
	public Reserva checkIn() {
		setEstado(EstadoReserva.CheckIN);
		return this;
	}
	
	public String disableCheckIn() {
		if(getEstado() == EstadoReserva.Reservada) {
			return null;
		}
		else {
			if(getEstado() == EstadoReserva.CheckIN) {
				return "La reserva ya se encuentra CheckIN";
			}
			else {
				return "Tiene que estar Reservada para realizar el CheckIN";
			}
		}
	}
	
	@MemberOrder(name="nombreEstado",sequence="2")
	public Reserva checkOut() {
		setEstado(EstadoReserva.CheckOUT);
		return this;
	}
	
	public String disableCheckOut() {
		if(getEstado() == EstadoReserva.CheckIN) {
			return null;
		}
		else {
			if(getEstado() == EstadoReserva.CheckOUT) {
				return "La reserva ya se encuentra CheckOUT";
			}
			else {
				return "Tiene que estar CheckIN para realizar el CheckOUT";
			}
		}
	}
	
	@MemberOrder(name="nombreEstado",sequence="3")
	public Reserva cerrar(
			@Named("Forma de Pago") FormaPago fP,
			@Optional
			@Named("Descuento") float descuento,
			@Optional
			@Named("Número de Factura") String numeroFactura,
			@Optional
			@Named("Fecha de Factura") LocalDate fechaFactura
			) {
		
			setEstado(EstadoReserva.Cerrada);

			Date fecha = (fechaFactura == null) ? null : fechaFactura.toDate();
			
			setNumeroFactura(numeroFactura);
			setFechaFactura(fecha);
			setDescuento(descuento);
			setFormaDeCierre(fP);
			
			container.informUser("Cierre realizado con éxito!");
			
			return this;
	}
	
	
	public String disableCerrar(
			FormaPago fP,
			float descuento,
			String numeroFactura,
			LocalDate fechaFactura
			) {
		if(getEstado() == EstadoReserva.CheckOUT) {
			return null;
		}
		else {
			if(getEstado() == EstadoReserva.Cerrada) {
				return "La reserva ya se encuentra Cerrada";
			}
			else {
				return "Tiene que estar CheckOUT para realizar el Cierre";
			}
		}
	}	
	
	@Hidden
    public boolean isCerrada() {
    	return (getEstado() == EstadoReserva.Cerrada) ? true : false; 
    }
	
	private TarifaServicio tFS;
	
	public void injectTarifaServicio(TarifaServicio tFS) {
		this.tFS = tFS;
	}
	
	
}
