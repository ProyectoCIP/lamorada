package dom.tarifa;

import java.math.BigDecimal;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

/**
 * 
 * La tarifa que se cobra por cantidad de personas 
 * 
 * @author ProyectoCIP
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries({
	@javax.jdo.annotations.Query(name="traerPax", language="JDOQL",value="SELECT FROM dom.tarifa.Tarifa WHERE pax == :pax"),
	@javax.jdo.annotations.Query(name="traerTodosPax", language="JDOQL",value="SELECT FROM dom.tarifa.Tarifa")
})
@ObjectType("TARIFA")
@Audited
public class Tarifa {
	
	/**
	 * Muestra el nombre del &iacute;cono de la tarifa.
	 * @return
	 */
	public String iconName() {
		return "tarifa";
	}
		
	/**
	 * Retorna la leyenda Personas y la cantidad de persona que corresponden a la tarifa ingresada.
	 * @return
	 */
	public String title() {
		return "Personas: "+Integer.toString(getPax());
	}
	
	private int pax;
	
	/**
	 * Retorna la cantidad de hu&eacute;spedes ingresados.
	 * @return
	 */
	@Hidden(where=Where.ALL_TABLES)
	public int getPax() {
		return pax;
	}

	/**
	 * Setea la cantidad de personas de acuerdo al precio ingresado.
	 * @param pax
	 */
	public void setPax(final int pax) {
		this.pax = pax;
	}
	
	/**
	 * Valida que no se ingresen m&aacute;s de cuatro hu&eacute;spedes.
	 * @param pax
	 * @return
	 */
	public String validatePax(final int pax) {
        return (pax>4)? "Cuadruple es lo máximo":null;
    }
	
	private BigDecimal precio;

	/**
	 * Retorna el precio que corresponde a la cantidad de hu&eacute;spedes ingresados.
	 * @return
	 */
	public BigDecimal getPrecio() {
		return precio;
	}

	/**
	 * Setea el precio de acuerdo a la cantidad de Hu&eacute;spedes ingresados.
	 * @param precio
	 */
	public void setPrecio(final BigDecimal precio) {
		this.precio = precio;
	}
	
	@SuppressWarnings("unused")
	private DomainObjectContainer container;
	
	public void setContainer(final DomainObjectContainer container) {
		this.container = container;
	}   
	
	//{{inyeccion TarifaServicio
	@SuppressWarnings("unused")
	private TarifaServicio tarifaServicio;
	
	public void injectReservaServicio(final TarifaServicio tarifaServicio) {
		this.tarifaServicio = tarifaServicio;
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
		
		public static Filter<Tarifa> creadoPor(final String usuarioActual) {
	        return new Filter<Tarifa>() {
	            @Override
	            public boolean accept(final Tarifa tarifa) {
	                return Objects.equal(tarifa.getUsuario(), usuarioActual);
	            }
	        };
	    }
}
