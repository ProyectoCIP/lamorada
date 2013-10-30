package dom.estadisticas;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Named;

@Named("Estadisticas")
public class EstadisticasServicio extends AbstractFactoryAndRepository {
	
	
	
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
