package dom.abm;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

import dom.todo.ToDoItem;

@Named("ABM")
public class ABM extends AbstractFactoryAndRepository{

	// {{ Id, iconName
    @Override
    public String getId() {
        return "ABM";
    }

    public String iconName() {
        return "ABM";
    }
    // }}
    
	@MemberOrder(sequence = "1")
	public Huesped NuevoHuesped(			
			@Named("Nombre") String nombre,
			@Named("Apellido") String apellido,
			@Named("Edad") int edad,
			@Named("Dni") String dni,
			@Named("Estado") boolean estado,
			@Named("Direccion") String direccion	) {
		return nuevoHuesped(nombre, apellido, edad, dni,estado, direccion);
	}
	
	@Hidden
	public Huesped nuevoHuesped(
			final String nombre,						
			final String apellido,
			final int edad,
			final String dni,
			final boolean estado,
			final String direccion) {
		final Huesped huesped = newTransientInstance(Huesped.class);		
		huesped.setNombre(nombre);
		huesped.setApellido(apellido);
		huesped.setEdad(edad);
		huesped.setDni(dni);
		huesped.setEstado(estado);
		huesped.setDireccion(direccion);
		
		persist(huesped);
		
		return huesped;
	}
		
	@MemberOrder(sequence = "2")
	public Habitacion NuevaHabitacion(			
			@Named("Nombre") String nombre,
			@Named("Capacidad") int capacidad) {
		return nuevoHabitacion(nombre, capacidad);
	}
	
	@Hidden
	public Habitacion nuevoHabitacion(
			final String nombre,						
			final int capacidad) {
		final Habitacion habitacion = newTransientInstance(Habitacion.class);		
		habitacion.setNombre(nombre);
		habitacion.setCapacidad(capacidad);
		
		persist(habitacion);
		
		return habitacion;
	}
}
