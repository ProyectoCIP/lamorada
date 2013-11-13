package dom.consumo;

import java.math.BigDecimal;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.disponibilidad.HabitacionFecha;
import dom.reserva.Reserva;
import dom.reserva.ReservaServicio;

/**
 * El consumo de servicios extras que puede tener una reserva (frigobar, lavanderia, etc..)
 * 
 * @author ProyectoCIP
 * @see dom.reserva.Reserva
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="consumos", language="JDOQL",value="SELECT FROM dom.consumo.Consumo")
@AutoComplete(repository=ReservaServicio.class,action="completaConsumicion")
@ObjectType("CONSUMO")
@Audited
public class Consumo {
	
	/**
	 * 
	 * @return Retorna el nombre del icono que va a ser usado en el viewer
	 */
	public String iconName() {
		return "servicios";
	}
	
	private String descripcion; 
	private int cantidad;
	private BigDecimal precio; 
	
	/**
	 * 
	 * @return Devuelve la descripcion del producto consumido
	 */
	@Title 
	@RegEx(validation="[\\w\\s]+")
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}
	
	

	/**
	 * 
	 * @return Devuelve la cantidad del producto consumido
	 */
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(final int cantidad) {
		this.cantidad = cantidad;
	}


	/**
	 * 
	 * @return Devuelve el precio del producto
	 */
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(final BigDecimal precio) {
		this.precio = precio;
	}
	

	/**
	 * 
	 * @return Genera la cuenta entre la cantidad consumida y el precio del producto
	 */
	@Named("Total")
	@NotPersisted 
	public BigDecimal getPrecioTotal() {
		return getPrecio().multiply(new BigDecimal(getCantidad())); 
	}
	
	private Reserva reserva;
	

	/**
	 * 
	 * @return Es la reserva donde a la cual se le registra este consumo.
	 */
	
	@Hidden(where=Where.ALL_TABLES)
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
    
  //{{Usuario actual
  	private String usuario;

  	@Hidden
  	public String getUsuario() {
  	    return usuario;
  	}

  	public void setUsuario(final String usuario) {
  	    this.usuario = usuario;
  	}//}}
  		
  	public static Filter<Consumo> creadoPor(final String usuarioActual) {
  	    return new Filter<Consumo>() {
  	        @Override
  	        public boolean accept(final Consumo consumo) {
  	            return Objects.equal(consumo.getUsuario(), usuarioActual);
  	        }
  	    };
  	}	

}
