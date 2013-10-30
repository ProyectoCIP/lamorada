package dom.estadisticas;

import java.text.SimpleDateFormat;
import java.util.Date;

@javax.jdo.annotations.Query(name="traerPax", language="JDOQL",value="SELECT FROM dom.disponibilidad.HabitacionFecha WHERE fecha == startsWidth ORDER BY fecha ASC")
public class Ocupacion {
	
	private String año;
	
	public String getAño() {
		return año;
	}

	public void setAño(String año) {
		this.año = año;
	}

	private String mes;
	
	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}
	
	private int pax;
	
	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}
	
	private String porcentaje;

	public String getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}

	private String nombreMes(Date fecha) {
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		
		String[] fechaFormateada = formato.format(fecha).split("/");
		
		switch(Integer.parseInt(fechaFormateada[1])) {
		
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
