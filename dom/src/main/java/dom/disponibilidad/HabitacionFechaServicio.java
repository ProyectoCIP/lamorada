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

import dom.enumeradores.EstadoHabitacion;
import dom.habitacion.Habitacion;
import dom.tarifa.TarifaServicio;

/**
 * 
 * @see dom.disponibilidad.Disponibilidad
 * @see dom.disponibilidad.HabitacionFecha
 * @see dom.enumeradores.EstadoHabitacion
 * @see dom.habitacion.Habitacion
 * @see dom.tarifa.TarifaServicio
 * 
 * @author ProyectoCIP
 *
 */
@Named("Disponibilidad")
public class HabitacionFechaServicio extends AbstractFactoryAndRepository {
	
	/**
	 * 
	 * @return Retorna el nombre del ícono que va a ser usado en el viewer
	 */
	public String iconName() {
		return "disponibilidad";
	}
		
	/**
	 * 
	 * Puede buscar disponibilidad en una fecha o en un rango de fechas
	 * 
	 * @param desde La fecha desde (si es un solo día se usa esta)
	 * @param hasta La fecha hasta
	 * @return Retorna una lista (mapa de disponibilidad) de objetos Disponibilidad
	 */
	@MemberOrder(sequence = "1")
	@Named("Por Fechas")
	public List<Disponibilidad> porFechas(
	            @Named("Fecha desde:") LocalDate desde,
	            @Optional
	            @Named("Fecha hasta:") LocalDate hasta
	        ){
		
			/*
			 * Cada vez que se hace una búsqueda primero revisa que no hayan objetos Disponibilidad
			 * persistidos, para que se cruce la información de una consulta obsoleta.
			 * 
			 */
			eliminarDisponibilidad();
			
	    	List<Disponibilidad> listaDeHabitaciones = new ArrayList<Disponibilidad>();
	    	
	    	/*
	    	 * Cargamos todas las Habitaciones del hotel que tenemos persistidas
	    	 */
	    	final List<Habitacion> habitaciones = listaHabitaciones();
	    	
	    	LocalDate fechaAuxiliar = desde;
	    	/*
	    	 * Si no existe hasta entonces se consulto por un solo día 
	    	 * y hacemos que hasta sea el mismo día que desde
	    	 */
	    	LocalDate hastaAuxiliar = (hasta!=null) ? hasta : desde;
	    	
	    	/*
	    	 * Avanzamos dia a dia
	    	 */
	    	for(int i=0; i <= getDiferenciaDesdeHasta(desde, hastaAuxiliar); i++) {
	    	
	    			/*
	    			 * Por cada habitación del hotel
	    			 */
	    			for(Habitacion habitacion : habitaciones) {
	    				
	    				/*
	    				 * Creamos el objeto de relleno (que es el que se muestra en el mapa de disponibilidad)
	    				 */
	    				Disponibilidad d = newTransientInstance(Disponibilidad.class);
	    				
	    				/*
	    				 * Si existe una reserva de esta habitación en este día
	    				 * entonces vamos a setear el objeto Disponibilidad de relleno
	    				 * con los datos que estan persistidos
	    				 */
	    				if(existeReserva(fechaAuxiliar,habitacion.getNombre()) != null) {
	    					HabitacionFecha hf = existeReserva(fechaAuxiliar,habitacion.getNombre());
	    					d.setNombreHabitacion(hf.getNombreHabitacion());
	    					d.setReserva(hf.getReserva());
	    					d.setTarifa(hf.getTarifa());
	    					d.setEstado(hf.getEstado());
	    				}
	    				/*
	    				 * Si no existe, entonces seteamos el objeto con los datos de la habitación,
	    				 * queda como disponible y la reserva la dejamos en blanco
	    				 */
	    				else {
	    					d.setNombreHabitacion(habitacion.getNombre());
	    					d.setTipoHabitacion(habitacion.getTipoHabitacion());
	    					d.setInterno(habitacion.getInterno());
	    					d.setEstado(EstadoHabitacion.DISPONIBLE);
	    				}
	    				
	    				/*
	    				 * La fecha actual del bucle
	    				 */
	    				d.setFecha(fechaAuxiliar.toDate());
	    				/*
	    				 * Se persiste el objeto relleno (luego se borra del repositorio)
	    				 */
	    				persistIfNotAlready(d);
	    				/*
	    				 * Se agrega a la lista que conforma el mapa de disponibilidad
	    				 */
	    				listaDeHabitaciones.add(d);
		    		}
	    			
	    			/*
	    			 * Pasamos al siguiente día
	    			 */
    				fechaAuxiliar = desde.plusDays(i+1);
			}
	    	
	    	return listaDeHabitaciones;
	    	
		}
	
	/**
	 * 
	 * @param desde
	 * @param hasta
	 * @return Si la fecha hasta es menor a la desde entonces devuelve la cadena.
	 */
	/*
	 * Validacion del ingreso de fechas por el UI
	 */
	public String validatePorFechas(final LocalDate desde, final LocalDate hasta) {
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
	
	/**
	 * Revisa todos los objetos disponibilidad que esten persistidos y los elimina
	 * preparando para la nueva consulta
	 */
	private void eliminarDisponibilidad() {
	
		List<Disponibilidad> d = allMatches(QueryDefault.create(Disponibilidad.class, "disponibilidad"));
		for(Disponibilidad borrar : d)
			getContainer().removeIfNotAlready(borrar);
	
	}
	
	/**
	 * 
	 * @param fecha La fecha que se quiere revisar
	 * @param nombre La habitación que se quiere revisar
	 * @return Retorna un objeto HabitacionFecha si es que existe una reserva de esta habitación en esta fecha
	 */
	@Programmatic
	public HabitacionFecha existeReserva(final LocalDate fecha,final String nombre) {
			
		return uniqueMatch(HabitacionFecha.class, new Filter<HabitacionFecha>(){

			@Override
			public boolean accept(HabitacionFecha habitacion) {
				// TODO Auto-generated method stub
				return habitacion.getFecha().equals(fecha.toDate())&&habitacion.getNombreHabitacion().equals(nombre);
			}				
		}); 			
	}
	
	/**
	 * 
	 * @param desde La fecha desde
	 * @param hasta La fecha hasta
	 * @return Retorna la cantidad de días que existen la fecha desde y la fecha hasta
	 */
	private int getDiferenciaDesdeHasta(final LocalDate desde,final LocalDate hasta) {
	   	//calcula la diferencia entre la fecha desde y hasta
	   	Days d = Days.daysBetween(desde, hasta);
	    	
	   	return d.getDays();
	}
	
	/**
	 * 
	 * @return Retorna la lista de habitaciones que tiene el hotel
	 */
	@Programmatic
	public List<Habitacion> listaHabitaciones() {
	  	return allMatches(QueryDefault.create(Habitacion.class, "traerHabitaciones"));	
	}
	
	/**
	 * 
	 * @param nombre Patron de búsqueda
	 * @return Retorna los objetos que coinciden con el patrón
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
		
	@SuppressWarnings("unused")
	private TarifaServicio tFS;
		
	public void injectTarifaServicio(final TarifaServicio tFS) {
		this.tFS = tFS;
	}
	
	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	
	    
}