package dom.contacto;

import org.apache.isis.applib.adapters.AbstractValueSemanticsProvider;

public final class ContactoValueSemanticsProvider extends AbstractValueSemanticsProvider <Contacto> {
	
	public void getParsed() {
		// TODO Auto-generated method stub
		
	}
	
	protected String doEncode(final Object object) {
		final Contacto contacto = (Contacto) object;
		final String valor = String.valueOf(contacto.getDomicilio() + "-" + contacto.getEmail() + "-" + contacto.getTelefono());
		return valor;
	}
	
	protected Contacto doRestore(final String data) {
		final String[] partes = data.split("-");
		final String domicilio = partes[0];
		final String correo = partes[1];
		final String telefono = partes[2];
		return new Contacto(domicilio, correo, telefono);		
	}
	
}
