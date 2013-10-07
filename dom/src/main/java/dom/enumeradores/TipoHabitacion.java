package dom.enumeradores;

public enum TipoHabitacion {
    Single(1), Doble(2), Triple(3), Cuadruple(4);
    
    private int capacidad;
    
    TipoHabitacion(int capacidad) {
    	this.capacidad = capacidad;
    }
    
    public int getCapacidad() {
    	return capacidad;
    }
}
