package dom.habitacion;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ActionSemantics.Of;

import com.google.common.base.Objects;

import dom.enumeradores.TipoHabitacion;
import dom.habitacion.Habitacion;

@Named("Habitacion")
public class HabitacionServicio extends AbstractFactoryAndRepository{

	// {{ Id, iconName
    @Override
    public String getId() {
        return "ABM";
    }

    public String iconName() {
        return "ABM";
    }
    // }}
    	
	@Named("Crear")
	@MemberOrder(sequence = "1")
	public Habitacion nuevaHabitacion(			
			@Named("Nombre") String nombre,
			@Named("Capacidad") int capacidad,
			@Named("Tipo de Habitación") TipoHabitacion tipoHabitacion) {
		return nuevoHabitacion(nombre, capacidad, tipoHabitacion);
	}
	
	@Hidden
	public Habitacion nuevoHabitacion(
			final String nombre,						
			final int capacidad,
			final TipoHabitacion tipoHabitacion) {
		final Habitacion habitacion = newTransientInstance(Habitacion.class);		
		habitacion.setNombre(nombre);
		habitacion.setCapacidad(capacidad);
		habitacion.setTipoHabitacion(tipoHabitacion);
		
		persist(habitacion);
		
		return habitacion;
	}
	
	@Named("Listar")
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    public List<Habitacion> Listahabitacion() {
        final String usuario = usuarioActual();
        final List<Habitacion> listaHabitacion = allMatches(Habitacion.class, Habitacion.creadoPor(usuario));
        return listaHabitacion;
    }    

	
    protected boolean creadoPorActualUsuario(final Habitacion h) {
        return Objects.equal(h.getUsuario(), usuarioActual());
    }
    protected String usuarioActual() {
        return getContainer().getUser().getName();
    }

	
	
}