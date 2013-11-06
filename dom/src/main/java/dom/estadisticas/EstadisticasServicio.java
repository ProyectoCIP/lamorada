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

@Named("Estadisticas")
public class EstadisticasServicio extends AbstractFactoryAndRepository {
	
	public String iconName() {
		return "estadisticas";
	}
	
	@Named("Ocupacion") 
	public List<Ocupacion> listaOcupacion(@Named("Año:") Años años) {
		
		String año = años.toString();
		año = año.substring(1);
		
		List<Ocupacion> listadoOcupacion = new ArrayList<Ocupacion>();
		
		for(int i = 1; i < 13; i++) {
			Ocupacion ocupacion = newTransientInstance(Ocupacion.class);
			ocupacion.setAño(año);
			ocupacion.setMes(nombreMes(i));
			ocupacion.setPax(0);
			ocupacion.setPlazas(plazas(Integer.parseInt(ocupacion.getAño()),i));
			
			listadoOcupacion.add(ocupacion);
			
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(año), 01, 01);
		Date inicio = cal.getTime();
		cal.set(Integer.parseInt(año), 12, 31);
		Date fin = cal.getTime();
		
		List<HabitacionFecha> listadoHabitacionesOcupadas = allMatches(QueryDefault.create(HabitacionFecha.class, "traerOcupacion", "inicio", inicio, "fin", fin));
		
		for(HabitacionFecha h : listadoHabitacionesOcupadas) {
			lista(listadoOcupacion,h);
		}
		
		return listadoOcupacion;		
		
	}
	
	private void lista(List<Ocupacion> lista,HabitacionFecha h) {
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String[] fechaSeparada = formato.format(h.getFecha()).split("/");
		for(Ocupacion o : lista) {
			if(o.getMes().equals(nombreMes(Integer.parseInt(fechaSeparada[1])))){
				o.setPax(o.getPax()+h.getPax());
			}
		}		
		
	}
	
	private int plazas(int año,int mes) {
		
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
