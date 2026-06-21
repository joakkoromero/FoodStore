package integrado.prog2.service;

import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;

import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private final List<Pedido> pedidos = new ArrayList<>();
    private Long ultimoId = 0L;

    // Necesitamos interactuar con los otros servicios para validar y descontar stock
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoService(UsuarioService usuarioService, ProductoService productoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    // 1. HU-PED-02: Crear pedido con detalles
    public Pedido crearPedido(Long usuarioId, FormaPago formaPago, List<int[]> itemsAPedir) throws Exception {
        // Validar que el usuario exista y esté activo
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        ultimoId++;
        Pedido nuevoPedido = new Pedido(ultimoId, usuario, formaPago);

        // Procesamos cada ítem del pedido (Cada elemento de la lista tiene: [productoId, cantidad])
        try {
            for (int[] item : itemsAPedir) {
                Long prodId = (long) item[0];
                int cantidad = item[1];

                // Regla de negocio: cantidad debe ser > 0
                if (cantidad <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
                }

                // Buscar el producto
                Producto producto = productoService.buscarPorId(prodId);

                // Regla de negocio: Control de Stock
                if (producto.getStock() < cantidad) {
                    throw new StockInvalidoException("Stock insuficiente para el producto: " + producto.getNombre() +
                            " (Disponible: " + producto.getStock() + ", Solicitado: " + cantidad + ")");
                }

                // Si hay stock, lo descontamos del producto
                producto.setStock(producto.getStock() - cantidad);

                // Agregamos el detalle al pedido usando el método obligatorio del UML
                nuevoPedido.addDetallePedido(cantidad, null, producto);
            }

            // Si todo salió bien, lo guardamos en la colección en memoria
            pedidos.add(nuevoPedido);
            return nuevoPedido;

        } catch (Exception e) {
            // Manejo de errores obligatorio: Si falla CUALQUIER detalle (ej. falta de stock),
            // debemos revertir los stocks que hayamos descontado previamente en este bucle.
            for (var detalle : nuevoPedido.getDetalles()) {
                Producto p = detalle.getProducto();
                p.setStock(p.getStock() + detalle.getCantidad()); // Devolvemos el stock
            }
            ultimoId--; // Revertimos el ID autoincremental
            throw e; // Volvemos a lanzar el error para que lo ataje la vista de consola
        }
    }

    // 2. HU-PED-01: Listar pedidos activos (no eliminados)
    public List<Pedido> listarActivos() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (!p.isEliminado()) {
                activos.add(p);
            }
        }
        return activos;
    }

    // Método auxiliar para buscar por ID
    public Pedido buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id) && !p.isEliminado()) {
                return p;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ningún pedido activo con el ID: " + id);
    }

    // 3. HU-PED-03: Actualizar estado / forma de pago del pedido
    public void actualizarEstadoYPago(Long id, Estado nuevoEstado, FormaPago nuevaFormaPago) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);

        if (nuevoEstado != null) {
            // Si se cancela el pedido, devolvemos el stock al inventario
            if (nuevoEstado == Estado.CANCELADO && p.getEstado() != Estado.CANCELADO) {
                for (var detalle : p.getDetalles()) {
                    Producto prod = detalle.getProducto();
                    prod.setStock(prod.getStock() + detalle.getCantidad());
                }
            }
            p.setEstado(nuevoEstado);
        }

        if (nuevaFormaPago != null) {
            p.setFormaPago(nuevaFormaPago);
        }
    }

    // 4. HU-PED-04: Eliminar pedido (Baja Lógica)
    public void eliminarLogico(Long id) throws EntidadNoEncontradaException {
        Pedido p = buscarPorId(id);
        p.setEliminado(true);
        // También marcamos como eliminados sus detalles internos para mantener la consistencia
        for (var detalle : p.getDetalles()) {
            detalle.setEliminado(true);
        }
    }
}