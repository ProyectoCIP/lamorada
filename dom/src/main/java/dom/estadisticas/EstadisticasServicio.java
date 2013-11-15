package dom.estadisticas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;

import dom.disponibilidad.HabitacionFecha;
import dom.enumeradores.Años;

/**
 * 
 * @see dom.estadisticas.Ocupacion
 * @see dom.disponibilidad.HabitacionFecha
 * @see dom.enumeradores.Años
 * 
 * @author ProyectoCIP
 *
 */
@Named("Estadisticas")
public class EstadisticasServicio extends AbstractFactoryAndRepository {
	
	/**
	 * 
	 * @return Retorna el nombre del &iacute;cono que va a ser usado en el viewer
	 */
	public String iconName() {
		return "estadisticas";
	}
	
	/**
	 * 
	 * @param años El a&ntilde;o del cual se quiere saber el porcentaje de ocupaci&oacute;n
	 * @return Retorna la lista de todos los meses con sus respectivos percentajes
	 */
	@Named("Ocupacion") 
	public List<Ocupacion> listaOcupacion(@Named("Año:") Años años) {
		
		/*
		 * Sacamos el primer dígito del parametro porque es una letra
		 */
		String año = años.toString();
		año = año.substring(1);
		
		/*
		 * La lista que se retorna
		 */
		List<Ocupacion> listadoOcupacion = new ArrayList<Ocupacion>();
		
		/*
		 * Los 12 meses del año
		 */
		for(int i = 1; i < 13; i++) {
			
			/*
			 * De entrada completamos todo el año con los valores nulos
			 */
			Ocupacion ocupacion = newTransientInstance(Ocupacion.class);
			ocupacion.setAño(año);
			ocupacion.setMes(nombreMes(i));
			ocupacion.setPax(0);
			ocupacion.setPlazas(plazas(Integer.parseInt(ocupacion.getAño()),i));
			
			listadoOcupacion.add(ocupacion);
			
		}
		
		/*
		 * Desde el primer día del año hasta el último
		 */
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(año), 01, 01);
		Date inicio = cal.getTime();
		cal.set(Integer.parseInt(año), 12, 31);
		Date fin = cal.getTime();
		
		/*
		 * Buscamos todas las habitaciones reservadas del año
		 */
		List<HabitacionFecha> listadoHabitacionesOcupadas = allMatches(QueryDefault.create(HabitacionFecha.class, "traerOcupacion", "inicio", inicio, "fin", fin));
		
		/*
		 * Por cada habitación reservada 
		 */
		for(HabitacionFecha h : listadoHabitacionesOcupadas) {
			lista(listadoOcupacion,h);
		}
		
		return listadoOcupacion;		
		
	}
	
	/**
	 * 
	 * @param lista La lista de ocupación que se muestra en el viewer
	 * @param h Cada habitación reservada en el a&ntilde;o
	 */
	private void lista(final List<Ocupacion> lista,final HabitacionFecha h) {
		
		/*
		 * Se convierte la fecha en dormato dd/MM/yyyy para 
		 * posteriormente separarla en partes y quedarme con la segunda posición del array
		 * que es donde está el mes
		 */
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String[] fechaSeparada = formato.format(h.getFecha()).split("/");
		
		/*
		 * En el mes que corresponde acumulamos la cantidad de personas de esta habitación 
		 */
		for(Ocupacion o : lista) {
			if(o.getMes().equals(nombreMes(Integer.parseInt(fechaSeparada[1])))){
				o.setPax(o.getPax()+h.getPax());
			}
		}		
	}
	
	/**
	 * 
	 * @param año El año
	 * @param mes El mes
	 * @return Retorna la cantidad de d&iacute;as que tiene ese mes en ese año
	 */
	private int plazas(final int año,final int mes) {
		
		Calendar cal = null;
		
		switch(mes) {
			case 1 : cal = new GregorianCalendar(año,Calendar.JANUARY,1); break; 
			case 2 : cal = new GregorianCalendar(año,Calendar.FEBRUARY,1); break; 
			case 3 : cal = new GregorianCalendar(año,Calendar.MARCH,1); break; 
			case 4 : cal = new GregorianCalendar(año,Calendar.APRIL,1); break;
			case 5 : cal = new GregorianCalendar(año,Calendar.MAY,1); break;
			case 6 : cal = new GregorianCalendar(año,Calendar.JUNE,1); break; 
			case 7 : cal = new GregorianCalendar(año,Calendar.JULY,1); break; 
			case 8 : cal = new GregorianCalendar(año,Calendar.AUGUST,1); break;
			case 9 : cal = new GregorianCalendar(año,Calendar.SEPTEMBER,1); break;
			case 10 : cal = new GregorianCalendar(año,Calendar.OCTOBER,1); break;
			case 11 : cal = new GregorianCalendar(año,Calendar.NOVEMBER,1); break;
			case 12 : cal = new GregorianCalendar(año,Calendar.DECEMBER,1); break;
		}
		
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 
	 * @param mes El mes
	 * @return Retorna el nombre del mes
	 */
	private String nombreMes(int mes) {
			
		switch(mes) {
			
			case 1 : return "Enero"; 
			case 2 : return "Febrero"; 
			case 3 : return "Marzo"; 
			case 4 : return "Abril"; 
			case 5 : return "Mayo"; 
			case 6 : return "Junio"; 
			case 7 : return "Julio"; 
			case 8 : return "Agosto"; 
			case 9 : return "Septiembre"; 
			case 10 : return "Octubre"; 
			case 11 : return "Noviembre"; 
			case 12 : return "Diciembre";		
			
		}			
		return null;
	}

}
