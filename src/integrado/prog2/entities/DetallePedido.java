package integrado.prog2.entities;

public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;

    // Relación N:1 -> Cada detalle se refiere a un Producto específico
    private Producto producto;

    // Constructor vacío
    public DetallePedido() {
        super();
    }

    // Constructor completo
    public DetallePedido(Long id, int cantidad, Producto producto) {
        super(id);
        this.cantidad = cantidad;
        this.producto = producto;
        // El subtotal se calcula automáticamente multiplicando cantidad por el precio del producto
        this.subtotal = (producto != null) ? cantidad * producto.getPrecio() : 0.0;
    }

    // Getters y Setters
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.actualizarSubtotal(); // Si cambia la cantidad, se actualiza el subtotal
    }

    public Double getSubtotal() {
        return subtotal;
    }

    // Quitamos el setSubtotal manual para que no se pueda modificar externamente por error
    private void actualizarSubtotal() {
        this.subtotal = (producto != null) ? this.cantidad * producto.getPrecio() : 0.0;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.actualizarSubtotal(); // Si cambia el producto, también cambia el subtotal
    }

    @Override
    public String toString() {
        String nombreProd = (producto != null) ? producto.getNombre() : "Producto inexistente";
        return "Detalle [Producto=" + nombreProd + ", Cantidad=" + cantidad + ", Subtotal=$" + subtotal + "]";
    }
}