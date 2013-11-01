package dom.disponibilidad;


import java.util.ArrayList;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import dom.habitacion.Habitacion;
import dom.tarifa.TarifaServicio;

@Named("Disponibilidad")
public class HabitacionFechaServicio extends AbstractFactoryAndRepository {
		
	@MemberOrder(sequence = "1")
	@Named("Por Fechas")
	public List<Disponibilidad> porFechas(
	            @Named("Fecha desde:") final LocalDate desde,
	            @Optional
	            @Named("Fecha hasta:") final LocalDate hasta
	        ){
		
			eliminarDisponibilidad();
			
	    	List<Disponibilidad> listaDeHabitaciones = new ArrayList<Disponibilidad>();
	    	final List<Habitacion> habitaciones = listaHabitaciones();
	    	
	    	LocalDate fechaAuxiliar = desde;
	    	LocalDate hastaAuxiliar = (hasta!=null) ? hasta : desde;
	    	
	    	for(int i=0; i <= getDiferenciaDesdeHasta(desde, hastaAuxiliar); i++) {
	    	
	    			for(Habitacion habitacion : habitaciones) {
	    				
	    				Disponibilidad d = newTransientInstance(Disponibilidad.class);
	    				
	    				if(existeReserva(fechaAuxiliar,habitacion.getNombre()) != null) {
	    					HabitacionFecha hf = existeReserva(fechaAuxiliar,habitacion.getNombre());
	    					d.setNombreHabitacion(hf.getNombreHabitacion());
	    					d.setReserva(hf.getReserva());
	    					d.setTarifa(hf.getTarifa());
	    				}
	    				else {
	    					d.setNombreHabitacion(habitacion.getNombre());
	    					d.setTipoHabitacion(habitacion.getTipoHabitacion());
	    					d.setInterno(habitacion.getInterno());
	    				}
	    				
	    				d.setFecha(fechaAuxiliar.toDate());
	    				persistIfNotAlready(d);
	    				listaDeHabitaciones.add(d);
		    		}

    				fechaAuxiliar = desde.plusDays(i+1);
			}
	    	
	    	return listaDeHabitaciones;
	    	
		}
	
	/*
	 * Validacion del ingreso de fechas por el UI
	 */
	public String validatePorFechas(LocalDate desde, LocalDate hasta) {
		if(hasta == null) {
			return null;
		}
		else {
			if(hasta.isBefore(desde)||hasta.isEqual(desde)) {
				return "La fecha hasta debe ser mayor a desde";
			}
			else {
				return null;
			}
		}
	}
	
	private void eliminarDisponibilidad() {
	
		List<Disponibilidad> d = allMatches(QueryDefault.create(Disponibilidad.class, "disponibilidad"));
		for(Disponibilidad borrar : d)
			getContainer().removeIfNotAlready(borrar);
	
	}
		
	private HabitacionFecha existeReserva(final LocalDate fecha,final String nombre) {
			
		return uniqueMatch(HabitacionFecha.class, new Filter<HabitacionFecha>(){

			@Override
			public boolean accept(HabitacionFecha habitacion) {
				// TODO Auto-generated method stub
				return habitacion.getFecha().equals(fecha.toDate())&&habitacion.getNombreHabitacion().equals(nombre);
			}				
		}); 			
	}
		
	private int getDiferenciaDesdeHasta(final LocalDate desde,final LocalDate hasta) {
	   	//calcula la diferencia entre la fecha desde y hasta
	   	Days d = Days.daysBetween(desde, hasta);
	    	
	   	return d.getDays();
	}
		
	@Programmatic
	public List<Habitacion> listaHabitaciones() {
	  	return allMatches(QueryDefault.create(Habitacion.class, "traerHabitaciones"));	
	}
	  
	@Hidden
	public List<HabitacionFecha> habitacionesReservadas(final String nombre) {
		return allMatches(HabitacionFecha.class,new Filter<HabitacionFecha>(){
			@Override
			public boolean accept(final HabitacionFecha habitacion) {
				// TODO Auto-generated method stub
				return habitacion.getNombreHabitacion().contains(nombre);
			}			
		});
	}
		
	private TarifaServicio tFS;
		
	public void injectTarifaServicio(TarifaServicio tFS) {
		this.tFS = tFS;
	}
	    
}