package dom.huesped;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.contacto.Contacto;
import dom.empresa.Empresa;

@Named("huesped")
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
			@Named("Nombre") String nombre,
			@Named("Apellido") String apellido,
			@Named("Edad") int edad,
			@Named("Dni") String dni,
			@Named("Dirección") String direccion,
			@Optional
			@Named("Télefono") String telefono,
			@Optional
			@Named("Celular") String celular,
			@Optional
			@Named("E-mail") String mail,
			@Optional
			@Named("Empresa") Empresa empresa) {
		return nHuesped(nombre, apellido, edad, dni,direccion,telefono,celular,mail,empresa);
	}
	
	@Hidden
	public Huesped nHuesped(
			final String nombre,						
			final String apellido,
			final int edad,
			final String dni,
			final String direccion,
			final String telefono,
			final String celular,
			final String mail,
			final Empresa empresa) {
		final Huesped huesped = newTransientInstance(Huesped.class);		
		huesped.setNombre(nombre);
		huesped.setApellido(apellido);
		huesped.setEdad(edad);
		huesped.setDni(dni);
		//huesped.setCelular(celular);
		//huesped.setMail(mail);
		huesped.setEstado(true);
		
		Contacto contacto = newTransientInstance(Contacto.class);
		
		contacto.setDomicilio(direccion);
		contacto.setTelefono(telefono);
		contacto.setCelular(celular);
		contacto.setEmail(mail);
		
		persistIfNotAlready(contacto);
		
		huesped.setContacto(contacto);		
		
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
    
    protected boolean creadoPorActualUsuario(final Huesped h) {
        return Objects.equal(h.getUsuario(), usuarioActual());
    }
    protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	
}
