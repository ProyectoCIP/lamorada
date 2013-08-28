package dom.abm;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;

import dom.todo.ToDoItem;

@Named("ABM")
public class Huespedes extends AbstractFactoryAndRepository{

	// {{ Id, iconName
    @Override
    public String getId() {
        return "ABM";
    }

    public String iconName() {
        return "Huesped";
    }
    // }}
    
	@MemberOrder(sequence = "1")
	public Huesped NuevoHuesped(@Named("Description") String nombre,@Named("Apellido") String apellido,	@Named("Edad") int edad,	@Named("Direccion") String direccion	) {
		return nuevoHuesped(nombre, apellido, edad, direccion);
	}
	
	@Hidden
	public Huesped nuevoHuesped(final String nombre,final String apellido,final int edad,final String direccion) {
		final Huesped huesped = newTransientInstance(Huesped.class);
		huesped.setNombre(nombre);
		huesped.setApellido(apellido);
		huesped.setEdad(edad);
		huesped.setDireccion(direccion);
		
		persist(huesped);
		
		return huesped;
	}
		
	
}
