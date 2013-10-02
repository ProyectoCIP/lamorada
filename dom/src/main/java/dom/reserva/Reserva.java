package dom.reserva;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.PublishedAction;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.annotation.Render.Type;
import org.joda.time.LocalDate;

import dom.consumo.Consumo;
import dom.empresa.Empresa;
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
	
	public void setNumero(long numero) {
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

	public void setEstado(IEReserva estado) {
		this.estado = estado;
	}
	//}}
	
	//{{Fecha en la que se realiza la reserva
	private LocalDate fecha;
	
	@MemberOrder(sequence="5")
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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
	
	public void setConsumos(List<Consumo> consumos) {
		this.consumos = consumos;
	}

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
		getConsumos().add(reservaServicio.agregarConsumo(descripcion, cantidad, precio));
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

	//{{Comentarios - No se muestran cuando se lista la reserva
	private String comentario;
	
	@Hidden(where=Where.ALL_TABLES)
	@MultiLine(numberOfLines=3)
	@MemberOrder(sequence="3")
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	//}}
	
	//{{Huesped
	private Huesped huesped;
	
	public Huesped getHuesped() {
		return huesped;
	}

	public void setHuesped(Huesped huesped) {
		this.huesped = huesped;
	}	
	//}}
	
	//{{Accion : Reservar
	@Named("Reservar")
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
			container.informUser("Ya se encuentra reservada!");
		}
	}
	//}}
	
	private DomainObjectContainer container;
	
	public void setContainer(DomainObjectContainer container) {
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
