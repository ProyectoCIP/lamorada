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
import dom.disponibilidad.HabitacionFecha;
import dom.huesped.Huesped;

public class ReservaServicio extends AbstractFactoryAndRepository {
	
	@Named("Reservar")
	public Reserva reservar(
			@Named("Huésped") Huesped huesped,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario) {
		
		
		List<HabitacionFecha> disponibilidad = listaHabitacionesReservas();
		
		if(disponibilidad.size() > 0)
		{	
			Reserva reserva = newTransientInstance(Reserva.class);
			
			reserva.setHuesped(huesped);
			reserva.setComentario(comentario);
			
			persistIfNotAlready(reserva);
			reserva.setFecha(LocalDate.now());
				
			for(HabitacionFecha h : disponibilidad) {
				
				getContainer().informUser(""+h.isParaReservar());
				//persistIfNotAlready(h);
				reserva.addToHabitacion(h);
				h.setParaReservar(false);	
			}
			
			/*List<HabitacionFecha> sobrantes = listaHabitacionesRelleno();
			
			for(HabitacionFecha sobrante : sobrantes) {
				getContainer().removeIfNotAlready(sobrante);
			}*/
			
			return reserva;
		}
		else
			return null;
	}
    
    private List<HabitacionFecha> listaHabitacionesReservas() {
    	
		return allMatches(QueryDefault.create(HabitacionFecha.class, "habitacion_para_reservar"));
    	
    } 
    
    private List<HabitacionFecha> listaHabitacionesRelleno() {
    	
		return allMatches(QueryDefault.create(HabitacionFecha.class, "habitacion_relleno"));
    	
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
