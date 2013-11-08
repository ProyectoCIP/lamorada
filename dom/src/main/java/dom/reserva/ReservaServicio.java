package dom.reserva;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import dom.acompaniantes.Acompaniante;
import dom.consumo.Consumo;
import dom.disponibilidad.Disponibilidad;
import dom.disponibilidad.HabitacionFecha;
import dom.enumeradores.EstadoReserva;
import dom.huesped.Huesped;
import dom.tarifa.TarifaServicio;
import dom.mensajeria.*;

@Named("Reservas")
public class ReservaServicio extends AbstractFactoryAndRepository {
	
	@Named("Reservar")
	@MemberOrder(sequence="1")
	public Reserva reservar(
			@Named("Huésped") Huesped huesped,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario
			) {
		
		List<Disponibilidad> disponibilidad = listaParaReservar();
		
		return crear(disponibilidad,huesped,comentario);
	}

	@Programmatic
	public Reserva crear(
			final List<Disponibilidad> disponibilidad,
			final Huesped huesped,
			final String comentario) {
		
		Reserva reserva = newTransientInstance(Reserva.class);
		
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
					reserva.addToHabitaciones(hF);					
					persistIfNotAlready(hF);	
					
				}
				
				persistIfNotAlready(reserva);
								
				if(huesped.getContacto().getCelular() != null) {
					SMS sms = new SMS();
					sms.enviarSMS(huesped.getNombre()+" "+huesped.getApellido(),huesped.getContacto().getCelular(),Long.toString(reserva.getNumero()));
				}
				//Este método queda comentado porque se gasta el crédito.-
				
				/*
				 * se eliminan de la base de datos todos los rastros de la consulta
				 */
				getContainer().removeIfNotAlready(d);
				
			}
		}
		else
		{
			getContainer().informUser("No hay habitaciones seleccionadas para reservar");
			reserva = null;
		}
		
			return reserva;
	}

    private List<Disponibilidad> listaParaReservar() {
    	return allMatches(Disponibilidad.class,new Filter<Disponibilidad>(){
			@Override
			public boolean accept(Disponibilidad d) {
				// TODO Auto-generated method stub
				return d.isParaReservar();
			}    		
    	});
    }

    @Named("Todas")
    @MemberOrder(sequence="5")
	public List<Reserva> listaReservas() {
		return allMatches(QueryDefault.create(Reserva.class, "reservas"));
	}
    
    @Named("Por Cliente")
    @MemberOrder(sequence="2")
	public List<Reserva> listaPorClientes(final Huesped huesped) {
    	return allMatches(Reserva.class, new Filter<Reserva>(){
			@Override
			public boolean accept(Reserva r) {
				// TODO Auto-generated method stub
				return r.getHuesped().equals(huesped);
			}    		
    	});	
    }
    
    @Named("Por Número")
    @MemberOrder(sequence="3")
    public Reserva porNumero(@Named("Número") final long numero) {
    	return uniqueMatch(Reserva.class,new Filter<Reserva>(){
			@Override
			public boolean accept(Reserva r) {
				// TODO Auto-generated method stub
				return (r.getNumero() == numero) ? true : false;
			}    		
    	});
    }
    
    @Named("Por Número Factura")
    @MemberOrder(sequence="4") 
    public Reserva porNumeroFactura(@Named("Número") final int numero) {
    	return uniqueMatch(Reserva.class, new Filter<Reserva>() {
			@Override
			public boolean accept(Reserva r) {
				// TODO Auto-generated method stub
				if(r.getNumeroFactura() != null) {
					return r.getNumeroFactura().equals(Integer.toString(numero)) ? true : false;
				}
				else
					return false;
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
	public List<Acompaniante> completaAcompaniantes(final String apellido) {
		return allMatches(Acompaniante.class, new Filter<Acompaniante>() {
			@Override
			public boolean accept(final Acompaniante a) {
				// TODO Auto-generated method stub
				return a.getApellido().contains(apellido);
			}			
		});
	}
	
	private TarifaServicio tFS;
	
	public void injectTarifaServicio(final TarifaServicio tFS) {
		this.tFS = tFS;
	}
	
	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	

}
