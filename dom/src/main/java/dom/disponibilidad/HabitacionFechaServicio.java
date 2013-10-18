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
import dom.reserva.Reserva;

@Named("Disponibilidad")
public class HabitacionFechaServicio extends AbstractFactoryAndRepository {
		
	@MemberOrder(sequence = "1")
	@Named("Por Fechas")
	public List<HabitacionFecha> porFechas(
	            @Named("Fecha desde:") final LocalDate desde,
	            @Optional
	            @Named("Fecha hasta:") LocalDate hasta
	        ){
			
	    	List<HabitacionFecha> listaDeHabitaciones = new ArrayList<HabitacionFecha>();
	    	final List<Habitacion> habitaciones = listaHabitaciones();
	    	
	    	LocalDate fechaAuxiliar = desde;
	    	if(hasta == null) 
	    		hasta = desde;
	    	
	    	for(int i=0; i <= getDiferenciaDesdeHasta(desde, hasta); i++) {
	    	
	    			for(Habitacion habitacion : habitaciones) {
	    				
	    				//final Disponibilidad fila = newTransientInstance(Disponibilidad.class);
	    	    		HabitacionFecha hf;
	    				
	    				if(existeReserva(fechaAuxiliar,habitacion.getNombre()) != null) {
	    					hf = existeReserva(fechaAuxiliar,habitacion.getNombre());
	    				}
	    				else {
	    					hf = newTransientInstance(HabitacionFecha.class);
	    					hf.setNombreHabitacion(habitacion.getNombre());
	    				}	    				
	    				
	    				hf.setFecha(fechaAuxiliar.toDate());
	    				listaDeHabitaciones.add(hf);
	    			}

    				fechaAuxiliar = desde.plusDays(i+1);
			}
	    	
	    	return listaDeHabitaciones;
	    }
		
		private HabitacionFecha existeReserva(final LocalDate fecha,final String nombre) {
			
			//getContainer().informUser("FECHA:"+fecha+"y el Nombre:"+nombre);
			
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
		
		public Reserva traerReserva() {
			
			Reserva reserva = newTransientInstance(Reserva.class);
			
			List<HabitacionFecha> disponibilidad = listaHabitacionesReservas();
			
			if(disponibilidad.size() > 0)
			{	
				persistIfNotAlready(reserva);
				reserva.setFecha(LocalDate.now());
					
				for(HabitacionFecha h : disponibilidad) {
					persistIfNotAlready(h);
					reserva.addToHabitacion(h);
					//h.setParaReservar(false);	
				}
			}
					
			return reserva;
		}
	    
	    @Programmatic
	    public List<Habitacion> listaHabitaciones() {
	    	return allMatches(QueryDefault.create(Habitacion.class, "traerHabitaciones"));	
	    }
	  
	    @Programmatic
	    public List<HabitacionFecha> listaHabitacionesReservas() {
	    	
	    	return allMatches(HabitacionFecha.class,new Filter<HabitacionFecha>(){

				@Override
				public boolean accept(HabitacionFecha h) {
					// TODO Auto-generated method stub
					return h.isParaReservar();
				}
	    		
	    	});
	    	
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
	    
	    /*
	    @Programmatic
	    public HabitacionesSeleccionadas seleccionadas() {
	    	
	    	HabitacionesSeleccionadas hS = newTransientInstance(HabitacionesSeleccionadas.class);
	    	
	    	List<Disponibilidad> lista = listaHabitacionesReservas();
	    	
	    	for(Disponibilidad d : lista) {
	    		hS.getListaHF().add(d.getHabitacion());
	    	}
	    	
	    	return hS;
	    }*/
	    
	}