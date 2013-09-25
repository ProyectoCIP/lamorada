package dom.empresa;


import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.jdo.annotations.Persistent;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.Render.Type;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;

import dom.huesped.Huesped;
import dom.contacto.ContactoVO;
import dom.correo.ServicioBandejaDeEntrada;
import dom.enumeradores.FormaPago;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("EMPRESA")
@AutoComplete(repository=EmpresaServicio.class, action="completaEmpresas")
@Audited
public class Empresa {
		
	/*
	 * Razon Social de la empresa
	 */
	private String razonSocial;

	@Title
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	/*
	 * Cuit de la empresa
	 */
	private String cuit;

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	/*
	 * Tarifa especial por convenio
	 */
	private float tarifa;	

	public float getTarifa() {
		return tarifa;
	}

	public void setTarifa(float tarifa) {
		this.tarifa = tarifa;
	}
	
	/*
	 * Datos de las distintas maneras
	 * de contactarnos con la empresa.
	 * Contacto -> Value Object
	 */
	
	private ContactoVO contacto;
	
	public ContactoVO getContacto() {
		return contacto;
	}
	
	public void setContacto(ContactoVO contacto) {
		this.contacto = contacto;
	}
	
	/*
	 * Forma en que la empresa le paga al hotel
	 * Crédito, Débito, Efectivo, Transferencia
	 */
	private FormaPago formaPago;
	
    public FormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}
		
	/*
	 * Estado : false (baja) - true (activo)
	 */
	private boolean estado;
	
	/*
	 * No se publica el estado del objeto
	 */
	@Hidden
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
    public static Filter<Empresa> creadoPor(final String usuarioActual) {
        return new Filter<Empresa>() {
            @Override
            public boolean accept(final Empresa empresa) {
                return Objects.equal(empresa.getUsuario(), usuarioActual);
            }
        };
    }   
    
    @Named("Borrar")
    @Bulk
    @MemberOrder(name="accionesEmpresa", sequence = "1")
    public List<Empresa> baja() {
    	setEstado(false);
    	return empresaServicio.listaEmpresas();
    }

    /*
     * Usuario actual logeado
     */
    private String usuario;

    @Hidden
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(final String usuario) {
        this.usuario = usuario;
    }
	
 // {{ injected: DomainObjectContainer
    private DomainObjectContainer container;

    public void injectDomainObjectContainer(final DomainObjectContainer container) {
        this.setContainer(container);
    }

	public DomainObjectContainer getContainer() {
		return container;
	}

	public void setContainer(DomainObjectContainer container) {
		this.container = container;
	}
	
	/*
	 * Inyección del servicio
	 */
	
	private EmpresaServicio empresaServicio;
	
	public void injectEmpresaServicio(final EmpresaServicio serviceEnterprise) {
        this.empresaServicio = serviceEnterprise;
    }
    
    /*
     * 
     * Relacion bidireccional entre la empresa y los huespedes
     */
    @Persistent(mappedBy="empresa")
    private List<Huesped> huespedes = new ArrayList<Huesped>();
    
    @Named("Listado de huéspedes")
    @Render(Type.EAGERLY)
    public List<Huesped> getHuespedes() { return huespedes; }
    
    @Hidden
    public void addToHuesped(Huesped huesped) {
    	if(huesped == null || huespedes.contains(huesped)) {
    		return;
    	}
    	huesped.setEmpresa(this);
    	huespedes.add(huesped);
    }
}
