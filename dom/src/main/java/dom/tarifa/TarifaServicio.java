package dom.tarifa;

import java.math.BigDecimal;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

@Named("Tarifas")
public class TarifaServicio extends AbstractFactoryAndRepository {
	
	public String iconName() {
		return "tarifa";
	}
	
	private Tarifa tarifa;
	
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
				persistIfNotAlready(tarifa);
			}
			
			return tarifa;
			
	}
	
	public String validateNueva(final int cantidad, final BigDecimal precio) {
		return (cantidad > 4) ? "Se alojan como máximo 4 personas" : null;
	}
	
	@MemberOrder(sequence="2")
	@Named("Listar")
	public List<Tarifa> listarTarifa() {
		QueryDefault<Tarifa> query = QueryDefault.create(Tarifa.class,"traerTodosPax");
	    return allMatches(query);
	}
	
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

}
