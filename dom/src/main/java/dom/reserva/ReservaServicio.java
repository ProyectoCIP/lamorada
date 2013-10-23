package dom.reserva;

import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import dom.consumo.Consumo;
import dom.disponibilidad.Disponibilidad;
import dom.disponibilidad.HabitacionFecha;
import dom.huesped.Huesped;

public class ReservaServicio extends AbstractFactoryAndRepository {
	
	@Named("Reservar")
	public Reserva reservar(
			@Named("Huésped") Huesped huesped,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario) {
		
		Reserva reserva = newTransientInstance(Reserva.class);
		persistIfNotAlready(reserva);
		
		List<Disponibilidad> disponibilidad = listaHabitacionesReservas();
		
		return crear(reserva,disponibilidad,huesped,comentario);
		
	}

	private Reserva crear(
			final Reserva reserva,
			final List<Disponibilidad> disponibilidad,
			final Huesped huesped,
			final String comentario) {
		
		
		if(disponibilidad.size() > 0)
		{	
			reserva.setHuesped(huesped);
			reserva.setComentario(comentario);
			reserva.setFecha(LocalDate.now().toDate());
			
			for(Disponibilidad d : disponibilidad) {
				
				if(d.isParaReservar())
				{
					HabitacionFecha hF = newTransientInstance(HabitacionFecha.class);
					hF.setFecha(d.getFecha());
					hF.setNombreHabitacion(d.getNombreHabitacion());
					hF.setReserva(reserva);
					reserva.addToHabitacion(hF);
					persistIfNotAlready(hF);
				}
				
				/*
				 * se eliminan de la base de datos todos los rastros de la consulta
				 */
				getContainer().removeIfNotAlready(d);
				
				}
			
			}
			return reserva;
			
			
	}
	
	    
    private List<Disponibilidad> listaHabitacionesReservas() {
    	
		return allMatches(QueryDefault.create(Disponibilidad.class, "disponibilidad"));
    	
    } 
	
	public List<Reserva> listaReservas() {
		return allMatches(Reserva.class, new Filter<Reserva>() {
			@Override
			public boolean accept(Reserva r) {
				// TODO Auto-generated method stub
				return r.getNombreEstado().contains("Reservada");
			}
			
		});
	}
	
	@Hidden
	public List<Consumo> completaConsumicion(final String nombre) {
		return allMatches(Consumo.class, new Filter<Consumo>() {
			@Override
			public boolean accept(final Consumo c) {
				// TODO Auto-generated method stub
				return c.getDescripcion().contains(nombre);
			}			
		});
	}
	
	public List<Consumo> listaConsumos() {
		return allMatches(QueryDefault.create(Consumo.class,"consumos"));
	}
	
	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	

}
