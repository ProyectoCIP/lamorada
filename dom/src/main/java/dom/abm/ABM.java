package dom.abm;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.abm.Habitacion.TipoHabitacion;
import dom.abm.Empresa;
import dom.enumeradores.FormaPago;

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
    
    @Named("Huesped")
	@MemberOrder(sequence = "1")
	public Huesped nuevoHuesped(			
			@Named("Nombre") String nombre,
			@Named("Apellido") String apellido,
			@Named("Edad") int edad,
			@Named("Dni") String dni,
			@Named("Estado") boolean estado,
			@Named("Direccion") String direccion	) {
		return nHuesped(nombre, apellido, edad, dni,estado, direccion);
	}
	
	@Hidden
	public Huesped nHuesped(
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
		
	@Named("Habitacion")
	@MemberOrder(sequence = "2")
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
	
    @Named("Empresa")
    @MemberOrder(sequence = "3")
    public Empresa nuevaEmpresa(
            @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*") // words, spaces and selected punctuation
            @Named("CUIT") String cuit, 
            @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*") // words, spaces and selected punctuation
            @Named("Razón Social") String razonSocial,
            @Named("Tarifa") float tarifa,
            @Named("Forma de Pago") FormaPago fPago
            ) {
        final String creadoPor = usuarioActual();
        return nEmpresa(cuit, razonSocial, tarifa, fPago,creadoPor);
    }
    
    @Hidden
    public Empresa nEmpresa(
            final String cuit, 
            final String razonSocial, 
            final float tarifa,
            final FormaPago fPago, 
            final String usuario) {
        final Empresa empresa = newTransientInstance(Empresa.class);
        empresa.setCuit(cuit);
        empresa.setRazonSocial(razonSocial);
        empresa.setTarifa(tarifa);
        empresa.setEstado(true);
        empresa.setFormaPago(fPago);
        empresa.setUsuario(usuario);
        
        persist(empresa);
        
        return empresa;
    }
    
    @Named("Listar")
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "4")
    public List<Empresa> ListaEmpresas() {
        final String usuario = usuarioActual();
        final List<Empresa> listaEmpresas = allMatches(Empresa.class, Empresa.creadoPor(usuario));
        return listaEmpresas;
    }    

    /*
     * Método para llenar el DropDownList de empresas, con la posibilidad de que te autocompleta las coincidencias al ir tipeando
     */
    @Hidden
    public List<Empresa> autoComplete(final String nombre) {
        return allMatches(Empresa.class, new Filter<Empresa>() {
        	@Override
            public boolean accept(final Empresa e) {
                return creadoPorActualUsuario(e) && e.getRazonSocial().contains(nombre);
            }
        });
    }
    
    protected boolean creadoPorActualUsuario(final Empresa e) {
        return Objects.equal(e.getUsuario(), usuarioActual());
    }
    protected String usuarioActual() {
        return getContainer().getUser().getName();
    }

	
	
}
