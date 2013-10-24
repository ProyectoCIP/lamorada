package dom.tarifa;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

@Named("Tarifas")
public class TarifaServicio extends AbstractFactoryAndRepository {
	
	private Tarifa tarifa;
	
	@MemberOrder(sequence="1")
	@Named("Nueva")
	public Tarifa nueva(
			@Named("Cantidad de Personas") int cantidad,
			@Named("Precio") float precio
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
				persistIfNotAlready(tarifa);
			}
			
			return tarifa;
			
	}
	
	public String validateNueva(int cantidad, float precio) {
		return (cantidad > 4) ? "Se alojan como máximo 4 personas" : null;
	}
	
	@MemberOrder(sequence="2")
	@Named("Listar")
	public List<Tarifa> listarTarifa() {
		QueryDefault<Tarifa> query = QueryDefault.create(Tarifa.class,"traerTodosPax");
	    return allMatches(query);
	}
	
	@Programmatic
	public float tarifa(int pax) {		
		Tarifa tarifa = uniqueMatch(QueryDefault.create(Tarifa.class, "traerPax", "pax", pax));
		return tarifa.getPrecio();
	}

}
