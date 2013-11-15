package dom.reserva;

import java.math.BigDecimal;
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
import org.apache.isis.applib.annotation.MemberGroups;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Render.Type;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import dom.acompaniantes.Acompaniante;
import dom.consumo.Consumo;
import dom.disponibilidad.HabitacionFecha;
import dom.enumeradores.EstadoReserva;
import dom.enumeradores.FormaPago;
import dom.huesped.Huesped;
import dom.tarifa.TarifaServicio;

/**
 * La reserva
 * 
 * @see dom.acompaniantes.Acompaniante
 * @see dom.consumo.Consumo
 * @see dom.disponibilidad.HabitacionFecha
 * @see dom.enumeradores.EstadoReserva
 * @see dom.enumeradores.FormaPago
 * @see dom.tarifa.TarifaServicio
 * @author ProyectoCIP
 * 
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.APPLICATION)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="reservas", language="JDOQL",value="SELECT FROM dom.reserva.Reserva order by numero desc")
@ObjectType("RESERVA")
@AutoComplete(repository=ReservaServicio.class, action="completaReservas")
@MemberGroups({"Estados","Datos de la Reserva","Datos del Cierre"})
@Audited
public class Reserva {
	
	/**
	 * El &iacute;cono cambia dependiendo de si est&aacute; Reservada, CheckIn, CheckOut o Reservada
	 * 
	 * @return
	 */
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

	/**
	 * Retorna el nuúmero de la reserva.
	 * @return
	 */
    @Disabled
	@MemberOrder(name="Datos de la Reserva",sequence="1")    
	public long getNumero() {
		return numero;
	}
	
    /**
     * Setea el número de reserva.
     */
	public void setNumero(final long numero) {
		this.numero = numero;
	}
	//}}
	
	private String nombreEstado;
	
	/**
	 * Retorna nombre del estado de la reserva que se muestra en el viewer. Los estados pueden ser CjheckIn, CheckOut, Reservada y Cerrada.
	 * @return
	 */
	@Named("Estado")
	@NotPersisted
	@Hidden(where=Where.ALL_TABLES)
	@MemberOrder(name="Estados",sequence="1")
	public String getNombreEstado() {
		nombreEstado = getEstado().toString();
		return nombreEstado;
	}
	
	private EstadoReserva estado;
	
	/**
	 * Retorna el estado de la reserva.
	 * @return
	 */
	@Hidden
	public EstadoReserva getEstado() {
		return estado;
	}

	/**
	 * Setea el estado de la reserva.
	 * @param estado
	 */
	public void setEstado(final EstadoReserva estado) {
		this.estado = estado;
	}
	//}}
	
	/**
	 * Retorna el nombre del objeto estado que se muestra en el viewer.
	 * @return
	 */
	//{{Titulo
	public String title(){
		return getEstado().toString();	
	}
	//}}
	
	/**
	 * Retorna la fecha de la reserva en el formato disponible en el viewer.
	 * @return
	 */
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
	
	/**
	 * Retorna la fecha de la reserva.
	 * @return
	 */
	@Hidden
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Setea la fecha de la reserva.
	 * @param fecha
	 */
	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}
	//}}
	
	//{{Monto de la seña
	private float montoSena;
	
	/**
	 * Retorna el monto abonado en concepto de seña hecha para la reserva.
	 * @return
	 */
	@Named("Monto Seña")
    @Hidden(where=Where.ALL_TABLES)
    @Optional
	@MemberOrder(name="Datos de la Reserva",sequence="3")
	public float getMontoSena() {
		return montoSena;
	}

	/**
	 * Setea el monto abonado en concepto de seña de la reserva. 
	 * @param montoSena
	 */
	public void setMontoSena(final float montoSena) {
		this.montoSena = montoSena;
	}
	//}}
	
	//{{Forma en la que se hace la seña
	private FormaPago tipoSena;

	/**
	 * Retorna la forma de pago en que se abona la seña.
	 * @return
	 */
    @Hidden(where=Where.ALL_TABLES)
    @Optional
	@MemberOrder(name="Datos de la Reserva",sequence="4")
	public FormaPago getTipoSena() {
		return tipoSena;
	}
	
    
    /**
     * Setea la forma de pago en la que se abona la seña.
     * @param tipoSena
     */
	public void setTipoSena(final FormaPago tipoSena) {
		this.tipoSena = tipoSena;
	}
	//}}
	
	//{{
	@Persistent(mappedBy="reserva")
	private List<Acompaniante> acompaniantes = new ArrayList<Acompaniante>();
	
	/**
	 * Retorna la lista de acompañantes del huésped que registra la reserva.
	 * @return
	 */
	@Render(Type.EAGERLY)
	public List<Acompaniante> getAcompaniantes() {
		return acompaniantes;
	}
	
	/**
	 * Setea los acompañantes del huésped que realiza la reserva.
	 * @param acompaniantes
	 */
	public void setAcompaniantes(List<Acompaniante> acompaniantes) {
		this.acompaniantes = acompaniantes;
	}
	
	/**
	 * Agrega los datos del acompañante del huésped que realizó la reserva.
	 * @param nombre
	 * @param apellido
	 * @param edad
	 * @param relacion
	 * @return
	 */
	@Named("Agregar")
	@MemberOrder(name="acompaniantes",sequence="1")
    public Reserva add(
    		@RegEx(validation="[a-zA-Z]{2,15}(\\s[a-zA-Z]{2,15})*")
    		@Named("Nombre") String nombre,
    		@RegEx(validation="[a-zA-Z]{2,15}(\\s[a-zA-Z]{2,15})*")
    		@Named("Apellido") String apellido,
    		@Optional
    		@Named("Edad") int edad,
    		@Optional
    		@RegEx(validation="[a-zA-Z]{2,15}(\\s[a-zA-Z]{2,15})*")
    		@Named("Relación") String relacion) {
		/*
		 * Se envian los datos del formulario consumo al servicio y nos lo retorna ya persistido
		 */
		
		Acompaniante acompaniante = container.newTransientInstance(Acompaniante.class);
		acompaniante.setNombre(nombre);
		acompaniante.setApellido(apellido);
		acompaniante.setEdad(edad);
		acompaniante.setRelacion(relacion);
		acompaniante.setUsuario(container.getUser().getName());

		//dependencia
		addToAcompaniantes(acompaniante);
		container.persistIfNotAlready(acompaniante);
		return this;
	}
	
	/**
	 * Borra el acompañante del huésped que realizaó la esreva.
	 * @param acompaniante
	 * @return
	 */
	@Named("Borrar")
	@MemberOrder(name="acompaniantes",sequence="1")
	public Reserva removeFromAcompaniantes(final Acompaniante acompaniante) {
		habitaciones.remove(acompaniante);
		container.removeIfNotAlready(acompaniante);
		return this;
	}	
	
	@Hidden
	public void addToAcompaniantes(final Acompaniante acompaniante) {
	    if(acompaniante == null || acompaniantes.contains(acompaniante)) {
	    	return;
	    }
	    acompaniante.setReserva(this);
	    acompaniantes.add(acompaniante);
	}
		
	public List<Acompaniante> choices0RemoveFromAcompaniantes() {
		return Lists.newArrayList(getAcompaniantes());
	}
	//}}
		
	//{{Lista de habitaciones a reservar
	@Persistent(mappedBy="reserva")
	private List<HabitacionFecha> habitaciones = new ArrayList<HabitacionFecha>();
	
	@Render(Type.EAGERLY)
	public List<HabitacionFecha> getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(final List<HabitacionFecha> listaHabitaciones) {
		this.habitaciones = listaHabitaciones;
	}
	
	/**
	 * Borra las habitaciones seleccionadad.
	 * @param habitacion
	 * @return
	 */
	@Named("Borrar")
	@MemberOrder(name="habitaciones",sequence="1")
	public Reserva removeFromHabitaciones(final HabitacionFecha habitacion) {
		habitaciones.remove(habitacion);
		container.removeIfNotAlready(habitacion);
		return this;
	}
	@Hidden
	public void addToHabitaciones(final HabitacionFecha habitacion) {
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
	@Render(Type.EAGERLY)
	public List<Consumo> getConsumos() {
		return consumos;
	}
	
	public void setConsumos(final List<Consumo> consumos) {
		this.consumos = consumos;
	}
	//}}

	/**
	 * Agrega consumos realizados por los Huéspedes en su estadía en el hotel.
	 * @param descripcion
	 * @param cantidad
	 * @param precio
	 * @return
	 */
	//{{Agregar consumo
	@Named("Agregar")
	@MemberOrder(name="consumos",sequence="1")
    public Reserva add(
    		@RegEx(validation="[\\w\\s]+")
    		@Named("Descripcion") String descripcion,
    		@Named("Cantidad") int cantidad,
    		@Named("Precio") BigDecimal precio) {
		/*
		 * Se envian los datos del formulario consumo al servicio y nos lo retorna ya persistido
		 */
		
		Consumo consumo = container.newTransientInstance(Consumo.class);
		consumo.setDescripcion(descripcion);
		consumo.setCantidad(cantidad);
		consumo.setPrecio(precio);
		consumo.setUsuario(container.getUser().getName());

		//dependencia
		addToConsumos(consumo);
		container.persistIfNotAlready(consumo);
		return this;
	}
	//}}	
	
	/**
	 * Borra los consumos realizados por los huéspedes.
	 * @param consumo
	 * @return
	 */
	//{{Borrar consumo
	@Named("Borrar")
    @MemberOrder(name="consumos",sequence="2")
    public Reserva removeFromConsumos(final Consumo consumo) {
    	consumos.remove(consumo);
    	container.removeIfNotAlready(consumo);
    	return this;
    }

	@Hidden
	public void addToConsumos(final Consumo consumo) {
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

	public void setTarifaEmpresa(final boolean tarifaEmpresa) {
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
		
		//Se eliminan todas las habitaciones reservadas
		for(HabitacionFecha h : getHabitaciones())
			container.removeIfNotAlready(h);
				
		//Se eliminan todos los consumos de esta reserva
		for(Consumo c : getConsumos()) 
			container.removeIfNotAlready(c);
				
		container.informUser("Reserva número: "+getNumero()+" a sido borrada");
		container.removeIfNotAlready(this);
	}

	public String disableBorrarReserva() {
		return (getEstado() == EstadoReserva.Cerrada) ? "No se puede borrar una reserva que ya esta cerrada" : null;
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
 
    //{{ Muestra el total a pagar (tiene en cuenta consumos, descuentos...)
    private BigDecimal total;
    
    @Disabled
    @NotPersisted
    @MemberOrder(name="Datos del Cierre",sequence="4")
    public BigDecimal getTotal() {
    	
    	BigDecimal total = new BigDecimal(0);
    	
    	/*
    	 * El precio de las habitaciones
    	 */
    	for(HabitacionFecha h : getHabitaciones()) {
    		total.add(h.getTarifa());
    	}
    	
    	/*
    	 * El precio de las consumiciones
    	 */
    	for(Consumo c : getConsumos()) {
    		total.add(c.getPrecioTotal());
    	}
    	
    	/*
    	 * Descuentos
    	 */    	
    	if(getDescuento() != null) {
        	total.subtract(getDescuento());
    	}
    	
    	return total;
    }
    
    public void setTotal(final BigDecimal total) {
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

	public void setFormaDeCierre(final FormaPago formaDeCierre) {
		this.formaDeCierre = formaDeCierre;
	}
	
	public String disableFormaDeCierre() {
		return isCerrada() ? null : "La reserva debe estar cerrada para editar la forma de pago del cierre";
	}
	//}}
    
	// {{ Descuento	
    private BigDecimal descuento;
    
    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Datos del Cierre",sequence="2")  
    @Optional
	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(final BigDecimal descuento) {
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

	public void setNumeroFactura(final String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	
	public String disableNumeroFactura() {
		return isCerrada() ? null : "La reserva debe estar cerrada para editar el número de factura";
	}

	@Named("Fecha Fáctura")
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

	public void setFechaFactura(final Date fechaFactura) {
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
			@Named("Descuento") BigDecimal descuento,
			@Optional
			@Named("Número de Factura") String numeroFactura,
			@Optional
			@Named("Fecha de Factura") LocalDate fechaFactura
			) {
		
			setEstado(EstadoReserva.Cerrada);

			Date fecha = (fechaFactura == null) ? null : fechaFactura.toDate();
			
			setNumeroFactura(numeroFactura);
			setFechaFactura(fecha);
			//if(descuento != null) {
			setDescuento(descuento);
			//}
			setFormaDeCierre(fP);
			
			container.informUser("Cierre realizado con éxito!");
			
			return this;
	}
	
	public String disableCerrar(
			FormaPago fP,
			BigDecimal descuento,
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
	
	//{{Usuario actual
		private String usuario;

	    @Hidden
	    public String getUsuario() {
	        return usuario;
	    }

	    public void setUsuario(final String usuario) {
	        this.usuario = usuario;
	    }//}}
		
		public static Filter<Reserva> creadoPor(final String usuarioActual) {
	        return new Filter<Reserva>() {
	            @Override
	            public boolean accept(final Reserva reserva) {
	                return Objects.equal(reserva.getUsuario(), usuarioActual);
	            }
	        };
	    }
	
	
}
