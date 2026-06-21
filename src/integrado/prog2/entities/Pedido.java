package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;

    // Relación 1:N (Composición) -> Un pedido contiene muchos detalles en memoria
    private List<DetallePedido> detalles;

    // Relación N:1 -> Un pedido pertenece obligatoriamente a un Usuario
    private Usuario usuario;

    // Constructor vacío
    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE; // Todo pedido arranca PENDIENTE
        this.total = 0.0;
        this.detalles = new ArrayList<>(); // Inicializamos la colección en memoria
    }

    // Constructor completo
    public Pedido(Long id, Usuario usuario, FormaPago formaPago) {
        super(id);
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }

    // === MÉTODOS PROPIOS EXIGIDOS POR EL UML ===

    // 1. Añadir detalle al pedido
    public void addDetallePedido(int cantidad, Double subtotal, Producto producto) {
        // Creamos el objeto detalle (usamos un ID temporal o autoincremental luego)
        DetallePedido nuevoDetalle = new DetallePedido(null, cantidad, producto);
        this.detalles.add(nuevoDetalle);

        // Cada vez que agregamos un detalle, recalculamos el total general del pedido
        this.calcularTotal();
    }

    // 2. Buscar un detalle específico según el producto
    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
        for (DetallePedido detalle : detalles) {
            if (detalle.getProducto().getId().equals(producto.getId())) {
                return detalle;
            }
        }
        return null; // Si no lo encuentra
    }

    // 3. Eliminar un detalle del pedido según el producto
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido aEliminar = findeDetallePedidoByProducto(producto);
        if (aEliminar != null) {
            this.detalles.remove(aEliminar);
            this.calcularTotal(); // Recalculamos el total tras removerlo
        }
    }

    // === IMPLEMENTACIÓN DE LA INTERFAZ CALCULABLE ===
    @Override
    public void calcularTotal() {
        double suma = 0.0;
        for (DetallePedido detalle : detalles) {
            suma += detalle.getSubtotal();
        }
        this.total = suma;
    }

    // === GETTERS Y SETTERS ===
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        String cliente = (usuario != null) ? usuario.getNombre() + " " + usuario.getApellido() : "Sin Usuario";
        return "Pedido [ID=" + getId() + ", Fecha=" + fecha + ", Cliente=" + cliente +
                ", Estado=" + estado + ", Pago=" + formaPago + ", TOTAL=$" + total + "]";
    }
}