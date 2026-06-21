package integrado.prog2.entities;

public class Categoria extends Base {
    private String nombre;
    private String descripcion;

    // Constructor vacío
    public Categoria() {
        super(); // Llama al constructor de Base
    }

    // Constructor completo
    public Categoria(Long id, String nombre, String descripcion) {
        super(id); // Pasa el ID a la clase Base
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // El profesor pide un toString() claro para mostrar en consola [cite: 178, 190]
    @Override
    public String toString() {
        return "Categoría [ID=" + getId() + ", Nombre=" + nombre + ", Descripción=" + descripcion + "]";
    }
}