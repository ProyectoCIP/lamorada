package dom.tarifa;

import java.math.BigDecimal;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

/**
 * Crea tarifas nuevas de acuerdo a la cantidad de personas ingresadas y puede listar las tarifas ingresadas en el sistema.
 * @author ProyectoCIP
 *
 */
@Named("Tarifas")
public class TarifaServicio extends AbstractFactoryAndRepository {
	
	/*
	 * Retorna el nombre del icono de la tarifa.
	 */
	public String iconName() {
		return "tarifa";
	}
	
	private Tarifa tarifa;
	
	/**
	 * El m&eacute;todo puede crear tarifas de acuerdo a la cantidad de hu&eacute;spedes, valida que no se ingresen mas de 4 hu&eacute;spedes.
	 * @param cantidad
	 * @param precio
	 * @return
	 */
	@MemberOrder(sequence="1")
	@Named("Nueva")
	public Tarifa nueva(
			@Named("Cantidad de Personas") int cantidad,
			@Named("Precio") BigDecimal precio
			) {
				
			QueryDefault<Tarifa> query = QueryDefault.create(Tarifa.class,"traerPax","pax",cantidad);
	       
			if(uniqueMatch(query) != null)
			{    
				tarifa = uniqueMatch(query);
				tarifa.setPrecio(precio);
			}
			else
			{
				tarifa = newTransientInstance(Tarifa.class);
				tarifa.setPax(cantidad);
				tarifa.setPrecio(precio);
				tarifa.setUsuario(usuarioActual());
				persistIfNotAlready(tarifa);
			}
			
			return tarifa;
			
	}
	
	/**
	 * El m&eacute;todo valida que no se ingresen mas de cuatro hu&eacute;spedes, ya que es la capacidad m&aacute;xima de las habitaciones.
	 * @param cantidad
	 * @param precio
	 * @return
	 */
	public String validateNueva(final int cantidad, final BigDecimal precio) {
		return (cantidad > 4) ? "Se alojan como máximo 4 personas" : null;
	}
	
	/**
	 * Muestra un listado con las tarifas y sus correspondiente cantidad de hu&eacute;spedes.
	 * @return
	 */
	@MemberOrder(sequence="2")
	@Named("Listar")
	public List<Tarifa> listarTarifa() {
		QueryDefault<Tarifa> query = QueryDefault.create(Tarifa.class,"traerTodosPax");
	    return allMatches(query);
	}
	
	/**
	 * Este m&eacute;todo valida que no se repita la tarifa ingresada previamente.
	 * @param pax
	 * @return
	 */
	@Programmatic
	public Tarifa tarifa(final int pax) {		
		
		Tarifa t = uniqueMatch(QueryDefault.create(Tarifa.class, "traerPax", "pax", pax));
		
		if(t == null)
		{
			t = newTransientInstance(Tarifa.class);
			t.setPrecio(new BigDecimal(0));
		}
		
		return t;
	}

	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	
}
