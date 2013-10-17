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
import dom.enumeradores.FormaPago;
import dom.huesped.Huesped;

public class ReservaServicio extends AbstractFactoryAndRepository {
	
	public void reserva(
			@Named("Huesped") Huesped huesped,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario
			) {
		
		
		reservar(huesped,comentario);
	}
	
	private Reserva reservar(
			final Huesped huesped,
			final String comentario) {
			final Reserva reserva = newTransientInstance(Reserva.class);
			reserva.setHuesped(huesped);
			reserva.setFecha(LocalDate.now());
			reserva.setComentario(comentario);
			reserva.setUsuario(usuarioActual());
			reserva.setCantidadDias(3);
			reserva.setMontoSena(400);
			reserva.setNumero(1);
			reserva.setTipoSena(FormaPago.Efectivo);
			
			persistIfNotAlready(reserva);
			getContainer().informUser("Reserva realizada con éxito!");
			
			return reserva;
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
	
	public List<Consumo> listaConsumos() {
		return allMatches(QueryDefault.create(Consumo.class,"consumos"));
	}
	
	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	

}
