package dom.reserva;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import dom.consumo.Consumo;
import dom.disponibilidad.Disponibilidad;
import dom.disponibilidad.HabitacionFecha;
import dom.enumeradores.EstadoReserva;
import dom.huesped.Huesped;
import dom.tarifa.TarifaServicio;
import dom.mensajeria.*;

public class ReservaServicio extends AbstractFactoryAndRepository {
	
	@Named("Reservar")
	@MemberOrder(sequence="1")
	public Reserva reservar(
			@Named("Huésped") Huesped huesped,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario
			//@Optional
			//@Named("Notificar SMS") boolean sms,
			//@Optional
			//@Named("Celular") String celular,
			//@Optional
			//@Named("Notificar Em@il") boolean email,
			//@Optional
			//@Named("Emai@l") String correo
			) {
		
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
					hF.setTipoHabitacion(d.getTipoHabitacion());
					hF.setInterno(d.getInterno());
					/*
					 * Se persiste la tarifa minima de la habitacion (1 persona),
					 * luego se puede setear desde la reserva la cantidad de personas
					 */
					hF.setPax(1);
					hF.setTarifa(tFS.tarifa(1).getPrecio());
					//
					
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
		//Este método queda comentado porque se gasta el crédito.-
			//enviaSMS(huesped.getCelular());
			return reserva;
	}
	
	public String validateReservar(
			Huesped huesped,
			String comentario
			//boolean sms,
			//String celular,
			//boolean email,
			//String correo
			) {
		
		/*if(sms && celular == null) {
			return "Ingrese el número de celular para notificar";
		} */
		
		/*if(email && correo == null) {
			return "Ingrese el email para notificar";
		}*/
		return null;
		
	}
	    
    private List<Disponibilidad> listaHabitacionesReservas() {    	
		return allMatches(QueryDefault.create(Disponibilidad.class, "disponibilidad"));
    } 
	
    @MemberOrder(sequence="2")
	public List<Reserva> listaReservas() {
		return allMatches(QueryDefault.create(Reserva.class, "reservas"));
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
	
	private TarifaServicio tFS;
	
	public void injectTarifaServicio(TarifaServicio tFS) {
		this.tFS = tFS;
	}
	
	@Hidden
	public void enviaSMS(String celular) {
		SMS mensaje = new SMS();
		//mensaje.enviarSMS();
		Reserva reserva = new Reserva();
		System.out.print("Entro a sms");
		if(reserva.getEstado() == EstadoReserva.Reservada) {
			System.out.print("Estado reservada");
					mensaje.enviarSMS(celular);
		}
		
	}
	
	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	

}
