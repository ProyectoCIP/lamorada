package dom.disponibilidad;


import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import dom.habitacion.Habitacion;
import dom.huesped.Huesped;
import dom.reserva.Reserva;

@Named("Disponibilidad")
public class HabitacionFechaServicio extends AbstractFactoryAndRepository {
		
	@MemberOrder(sequence = "1")
	@Named("Por Fechas")
	public List<Disponibilidad> porFechas(
	            @Named("Fecha desde:") final LocalDate desde,
	            @Optional
	            @Named("Fecha hasta:") LocalDate hasta
	        ){
			
	    	List<Disponibilidad> listaDeHabitaciones = new ArrayList<Disponibilidad>();
	    	final List<Habitacion> habitaciones = listaHabitaciones();
	    	
	    	LocalDate fechaAuxiliar = desde;
	    	if(hasta == null) 
	    		hasta = desde;
	    	
	    	for(int i=0; i <= getDiferenciaDesdeHasta(desde, hasta); i++) {
	    	
	    			for(Habitacion habitacion : habitaciones) {
	    				
	    				HabitacionFecha hf;
	    				
	    				if(existeReserva(fechaAuxiliar,habitacion.getNombre()) != null) {
	    					hf = existeReserva(fechaAuxiliar,habitacion.getNombre());
	    				}
	    				else {
	    					hf = newTransientInstance(HabitacionFecha.class);
	    					hf.setNombreHabitacion(habitacion.getNombre());
	    					hf.setParaReservar(false);
			    		}	    				
	    				
	    				hf.setFecha(fechaAuxiliar.toDate());
	    				Disponibilidad d = newTransientInstance(Disponibilidad.class);
	    				d.setFecha(fechaAuxiliar.toDate());
	    				d.setHabitacion(hf);
	    				listaDeHabitaciones.add(d);

		    		}

    				fechaAuxiliar = desde.plusDays(i+1);
			}
	    	
	    	return listaDeHabitaciones;
	    	
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
	  

/*	    @Named("Listado")
	    @ActionSemantics(Of.SAFE)
	    public List<HabitacionFecha> listaHabitacionesReservas() {
	    	
	    	return porFechas(null, null);
	    	//return allMatches(QueryDefault.create(HabitacionFecha.class, "habitacion_para_reservar"));
	    	
	    }
	*/    
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
	    
	}