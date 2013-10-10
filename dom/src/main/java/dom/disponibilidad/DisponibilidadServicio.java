package dom.disponibilidad;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.clock.Clock;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import com.google.common.base.Objects;

import dom.habitacion.Habitacion;
import dom.reserva.Reserva;
import dom.todo.ToDoItem;

	@Named("Disponibilidad")
	public class DisponibilidadServicio extends AbstractFactoryAndRepository {
		
		@MemberOrder(sequence = "1")
		@Named("Por Fechas")
	    public List<Disponibilidad> porFechas(
	            @Named("Fecha desde:") LocalDate desde,
	            @Named("Fecha hasta:") LocalDate hasta
	        ){
			
	        return consultar(desde,hasta);
	    }
		
		private int getDiferenciaDesdeHasta(LocalDate desde, LocalDate hasta) {
	      	//calcula la diferencia entre la fecha desde y hasta
	    	Days d = Days.daysBetween(desde, hasta);
	    	
	    	return d.getDays();
	    }
		
		public Reserva traerReserva() {
			
			List<Disponibilidad> disponibilidad = allMatches(QueryDefault.create(Disponibilidad.class,"traerLosQueSeReservan"));
			
			List<Habitacion> habitaciones = new ArrayList<Habitacion>();
			
			for(Disponibilidad d : disponibilidad) {

				habitaciones.add(d.getHabitacion());
				//getContainer().remove(d);
			}
			
			Reserva reserva = newTransientInstance(Reserva.class);
			persistIfNotAlready(reserva);
			
			reserva.setListaHabitaciones(habitaciones);
			
			return reserva;
		}
		
		
	    
	    private List<Disponibilidad> consultar(LocalDate fechaDesde, LocalDate hasta) {
			
	    	List<Disponibilidad> reservar = new ArrayList<Disponibilidad>();
			
	    	Habitacion habitacion = newTransientInstance(Habitacion.class);
	    	habitacion.setNombre("Dorada");
	    	LocalDate fechaAuxiliar = fechaDesde;
	    	
	    	for(int i=0; i <= getDiferenciaDesdeHasta(fechaDesde, hasta); i++) {

		    		Disponibilidad reservaRelleno = newTransientInstance(Disponibilidad.class);
		    		reservaRelleno.setFecha(fechaAuxiliar);reservar.add(reservaRelleno);
		    		fechaAuxiliar = fechaDesde.plusDays(i+1);
		    	
			    	reservar.add(reservaRelleno);
			    	
			}
	    	
	    	return reservar;
	    }
	}