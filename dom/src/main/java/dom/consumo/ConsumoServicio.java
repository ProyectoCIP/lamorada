package dom.consumo;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;

public class ConsumoServicio extends AbstractFactoryAndRepository {
	
	@Hidden
	public List<Consumo> completaConsumiciones() {
		return allMatches(QueryDefault.create(Consumo.class, "consumiciones"));
	}	

	@Hidden
	public List<Consumo> completaConsumicion(final String nombre) {
		return allMatches(Consumo.class, new Filter<Consumo>() {
			@Override
			public boolean accept(Consumo c) {
				// TODO Auto-generated method stub
				return c.getDescripcion().contains(nombre);
			}			
		});
	}	
	
}
