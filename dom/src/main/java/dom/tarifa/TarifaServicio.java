package dom.tarifa;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;

import dom.empresa.Empresa;

@Named("Tarifas")
public class TarifaServicio extends AbstractFactoryAndRepository {
	
	private Tarifa tarifa;
	
	@Named("Nueva")
	public Tarifa nueva(
			@Named("Cantidad de Personas") int cantidad,
			@Named("Precio") float precio
			) {
		
		QueryDefault<Tarifa> query = QueryDefault.create(Tarifa.class,"traerPax","pax",cantidad);
       
		if(uniqueMatch(query) != null)
		{    
			Tarifa tarifa = uniqueMatch(query);
			tarifa.setPrecio(precio);
		}
		else
		{
			Tarifa tarifa = newTransientInstance(Tarifa.class);
			tarifa.setPax(cantidad);
			tarifa.setPrecio(precio);
			persistIfNotAlready(tarifa);
		}
		
		return tarifa;
	}
	
	@Named("Listar")
	public List<Tarifa> listarTarifa() {
		QueryDefault<Tarifa> query = QueryDefault.create(Tarifa.class,"traerTodosPax");
	    return allMatches(query);
	}

}
