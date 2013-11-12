package dom.huesped;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.contacto.Contacto;
import dom.empresa.Empresa;

@Named("Huesped")
public class HuespedServicio extends AbstractFactoryAndRepository{

	// {{ Id, iconName
    @Override
    public String getId() {
        return "Huesped";
    }

    public String iconName() {
        return "Huesped";
    }
    // }}
    
    @Named("Crear")
	@MemberOrder(sequence = "1")
	public Huesped nuevoHuesped(	
			@MaxLength(value=30)
			@RegEx(validation="[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
			@Named("Nombre") String nombre,
			@MaxLength(value=30)
			@RegEx(validation="[a-zA-Záéíóú]{2,15}(\\s[a-zA-Záéíóú]{2,15})*")
			@Named("Apellido") String apellido,
			@RegEx(validation="\\d{1,2}")
			@Named("Edad") String edad,
			@RegEx(validation="\\d{6,8}")
			@Named("Dni") String dni,
			@RegEx(validation="[\\w\\s]+")
			@Named("Dirección") String direccion,
			@Optional
			@RegEx(validation="\\d{7,10}")
			@Named("Télefono") String telefono,
			@Optional
			@RegEx(validation="\\d{3,7}(-)?\\d{6}")
			@Named("Celular") String celular,
			@Optional
			@RegEx(validation="(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+")
			@Named("E-mail") String mail,
			@Optional
			@Named("Empresa") Empresa empresa) {
		
    	Contacto contacto = newTransientInstance(Contacto.class);
		
		contacto.setDomicilio(direccion);
		contacto.setTelefono(telefono);
		contacto.setCelular(celular);
		contacto.setEmail(mail);
		persistIfNotAlready(contacto);
    	
    	return nHuesped(nombre, apellido, edad, dni,contacto,empresa);
	}
	
	@Hidden
	public Huesped nHuesped(
			final String nombre,						
			final String apellido,
			final String edad,
			final String dni,
			final Contacto contacto,
			final Empresa empresa) {
		final Huesped huesped = newTransientInstance(Huesped.class);		
		huesped.setNombre(nombre);
		huesped.setApellido(apellido);
		huesped.setEdad(edad);
		huesped.setDni(dni);
		huesped.setEstado(true);
		huesped.setContacto(contacto);
		
		contacto.addToContacto(huesped);
		
		if(empresa != null) {
			huesped.setEmpresa(empresa);
			empresa.addToHuesped(huesped);
		}
		
		persistIfNotAlready(huesped);
		
		return huesped;
	}
	
	/*
	 * Repositorio: trae los huespedes cargados en el sistema que estan habilitados
	 */
	
	@Named("Listar")
	@MemberOrder(sequence="2")
	public List<Huesped> listaHuespedes() {
	    
	    return allMatches(Huesped.class, 
		         
	       	new Filter<Huesped>() {
	       			@Override
	           		public boolean accept(final Huesped h) {
	           			return Objects.equal(h.isEstado(), true);
	           		}
	           }
	    	);    
    }	
		
    /*
     * Método para llenar el DropDownList de huespedes, con la posibilidad de que te autocompleta las coincidencias al ir tipeando
     */
    @Hidden
    public List<Huesped> completaHuesped(final String nombre) {
        return allMatches(Huesped.class, new Filter<Huesped>() {
        	@Override
            public boolean accept(final Huesped h) {
                return h.getNombre().contains(nombre) && h.isEstado();
            }
        });
    }
    
    @Programmatic
    public Huesped huespedContacto(final Contacto contacto) {
    	return uniqueMatch(Huesped.class, new Filter<Huesped>() {
			@Override
			public boolean accept(Huesped huesped) {
				// TODO Auto-generated method stub
				return huesped.getContacto().equals(contacto);
			}    		
    	});
    }
    	
    protected boolean creadoPorActualUsuario(final Huesped h) {
        return Objects.equal(h.getUsuario(), usuarioActual());
    }
    protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	
}
