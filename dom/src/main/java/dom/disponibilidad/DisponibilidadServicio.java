package dom.disponibilidad;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import dom.habitacion.Habitacion;
import dom.reserva.HabitacionFecha;
import dom.reserva.Reserva;

	@Named("Disponibilidad")
	public class DisponibilidadServicio extends AbstractFactoryAndRepository {
		
		@MemberOrder(sequence = "1")
		@Named("Por Fechas")
	    public List<Disponibilidad> porFechas(
	            @Named("Fecha desde:") LocalDate desde,
	            @Named("Fecha hasta:") LocalDate hasta
	        ){
			
	    	List<Disponibilidad> listaDeHabitaciones = new ArrayList<Disponibilidad>();
	    	final List<Habitacion> habitaciones = listaHabitaciones();
	    	
	    	LocalDate fechaAuxiliar = desde;	    	
	    	
	    	for(int i=0; i <= getDiferenciaDesdeHasta(desde, hasta); i++) {

	    			for(Habitacion habitacion : habitaciones) {
	    				
	    				final HabitacionFecha hf = newTransientInstance(HabitacionFecha.class);
	    				
	    				hf.setNombreHabitacion(habitacion.getNombre());
	    				hf.setFecha(fechaAuxiliar);
	    				
	    				Disponibilidad fila = newTransientInstance(Disponibilidad.class);
	    				fila.setHabitacion(hf);
	    				fila.setFecha(fechaAuxiliar);
	    				listaDeHabitaciones.add(fila);
	    			}

    				fechaAuxiliar = desde.plusDays(i+1);
			}
	    	
	    	return listaDeHabitaciones;
	    }
		
		private int getDiferenciaDesdeHasta(final LocalDate desde,final LocalDate hasta) {
	      	//calcula la diferencia entre la fecha desde y hasta
	    	Days d = Days.daysBetween(desde, hasta);
	    	
	    	return d.getDays();
	    }
		
		public Reserva traerReserva() {
			
			Reserva reserva = newTransientInstance(Reserva.class);
			
			List<Disponibilidad> disponibilidad = listaHabitacionesReservas();
			
			if(disponibilidad.size() > 0)
			{	
				persistIfNotAlready(reserva);
				reserva.setFecha(LocalDate.now());
					
				for(Disponibilidad d : disponibilidad) {
					persistIfNotAlready(d.getHabitacion());
					d.getHabitacion().setReserva(reserva);
					reserva.addToHabitacion(d.getHabitacion());
						
					getContainer().removeIfNotAlready(d);
				}
			}
					
			return reserva;
		}
	    
		/*
	    private List<Disponibilidad> consultar(final LocalDate fechaDesde, final LocalDate hasta) {
			
	    	final List<Disponibilidad> reservar = new ArrayList<Disponibilidad>();
			
	    	LocalDate fechaAuxiliar = fechaDesde;
	    	final List<Habitacion> habitaciones = listaHabitaciones();
	    	
	    	for(int i=0; i <= getDiferenciaDesdeHasta(fechaDesde, hasta); i++) {

	    			for(Habitacion habitacion : habitaciones) {
	    				final HabitacionFecha hf = newTransientInstance(HabitacionFecha.class);
	    				
	    				hf.setNombreHabitacion(habitacion.getNombre());
	    				hf.setFecha(fechaAuxiliar);
	    				//poner en disponibilidad la hf
	    				
	    				Disponibilidad reservaRelleno = newTransientInstance(Disponibilidad.class);
	    				reservaRelleno.setHabitacion(hf);
	    				reservaRelleno.setFecha(fechaAuxiliar);
	    				reservar.add(reservaRelleno);
	    				fechaAuxiliar = fechaDesde.plusDays(i+1);
	    			}
			}
	    	
	    	return reservar;
	    }*/
	    
	    @Programmatic
	    public List<Habitacion> listaHabitaciones() {
	    	return allMatches(QueryDefault.create(Habitacion.class, "traerHabitaciones"));	
	    }
	    @Programmatic
	    public List<Disponibilidad> listaHabitacionesReservas() {
	    	return allMatches(QueryDefault.create(Disponibilidad.class,"traerLosQueSeReservan"));
		}
	    
	    @Programmatic
	    public HabitacionesSeleccionadas seleccionadas() {
	    	
	    	HabitacionesSeleccionadas hS = newTransientInstance(HabitacionesSeleccionadas.class);
	    	
	    	List<Disponibilidad> lista = listaHabitacionesReservas();
	    	
	    	for(Disponibilidad d : lista) {
	    		hS.getListaHF().add(d.getHabitacion());
	    		
	    		System.out.println("HABITACION"+d.getHabitacion().getNombreHabitacion());
	    	}
	    	
	    	return hS;
	    }
	    
	}