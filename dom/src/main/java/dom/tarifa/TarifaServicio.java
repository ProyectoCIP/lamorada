package dom.tarifa;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
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
		
		if(cantidad < 5) {
		
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
		else
		{
			getContainer().informUser("El máximo permitido es cuadruple");
			return null;
		}
		
	}
	
	@MemberOrder(sequence="2")
	@Named("Listar")
	public List<Tarifa> listarTarifa() {
		QueryDefault<Tarifa> query = QueryDefault.create(Tarifa.class,"traerTodosPax");
	    return allMatches(query);
	}

}
