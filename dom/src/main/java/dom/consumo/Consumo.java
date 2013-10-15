package dom.consumo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;

import dom.reserva.Reserva;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="consumiciones", language="JDOQL",value="SELECT FROM dom.consumicion.Consumicion")
@ObjectType("CONSUMO")
@AutoComplete(repository=ConsumoServicio.class, action="completaConsumiciones")
@Audited
public class Consumo {
	
	private String descripcion; 
	private int cantidad;
	private float precio; 
	
	@Title 
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
	@Named("Total")
	@NotPersisted 
	public float getPrecioTotal() {
		return getCantidad()*getPrecio(); 
	}
	
	private Reserva reserva;
	
	@Hidden(where=Where.ALL_TABLES)
	public Reserva getReserva() {
		return reserva;
	}
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

}
