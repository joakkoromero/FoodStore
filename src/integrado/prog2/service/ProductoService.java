package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.EntidadNoEncontradaException;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    // Lista en memoria que simula la tabla de productos
    private final List<Producto> productos = new ArrayList<>();
    private Long ultimoId = 0L; // Autoincremental para los IDs de productos

    // Necesitamos el servicio de categorías para validar que la categoría exista al crear un producto
    private final CategoriaService categoriaService;

    // Constructor que recibe el CategoriaService
    public ProductoService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // 1. HU-PROD-02: Crear producto
    public Producto crear(String nombre, String descripcion, Double precio, int stock, String imagen, Boolean disponible, Long categoriaId) throws Exception {
        // Validaciones básicas de reglas de negocio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser menor a 0.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser menor a 0.");
        }

        // Validar que la categoría exista y esté activa usando el otro servicio
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        ultimoId++;
        Producto nuevo = new Producto(ultimoId, nombre.trim(), precio, descripcion.trim(), stock, imagen, disponible, categoria);
        productos.add(nuevo);
        return nuevo;
    }

    // 2. HU-PROD-01: Listar productos activos (no eliminados)
    public List<Producto> listarActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto prod : productos) {
            if (!prod.isEliminado()) {
                activos.add(prod);
            }
        }
        return activos;
    }

    // Método auxiliar para buscar un producto por ID
    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Producto prod : productos) {
            if (prod.getId().equals(id) && !prod.isEliminado()) {
                return prod;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ningún producto activo con el ID: " + id);
    }

    // 3. HU-PROD-03: Editar producto
    public void editar(Long id, String nuevoNombre, String nuevaDescripcion, Double nuevoPrecio, Integer nuevoStock, String nuevaImagen, Boolean nuevoDisponible, Long nuevoCategoriaId) throws Exception {
        Producto prod = buscarPorId(id); // Si no existe, lanza EntidadNoEncontradaException

        // Si se cargan datos nuevos, se valida la regla de negocio antes de guardar
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            prod.setNombre(nuevoNombre.trim());
        }
        if (nuevaDescripcion != null) {
            prod.setDescripcion(nuevaDescripcion.trim());
        }
        if (nuevoPrecio != null) {
            if (nuevoPrecio < 0) throw new IllegalArgumentException("El precio no puede ser menor a 0.");
            prod.setPrecio(nuevoPrecio);
        }
        if (nuevoStock != null) {
            if (nuevoStock < 0) throw new IllegalArgumentException("El stock no puede ser menor a 0.");
            prod.setStock(nuevoStock);
        }
        if (nuevaImagen != null) {
            prod.setImagen(nuevaImagen);
        }
        if (nuevoDisponible != null) {
            prod.setDisponible(nuevoDisponible);
        }
        if (nuevoCategoriaId != null) {
            // Validamos que la nueva categoría exista
            Categoria nuevaCat = categoriaService.buscarPorId(nuevoCategoriaId);
            prod.setCategoria(nuevaCat);
        }
    }

    // 4. HU-PROD-04: Eliminar producto (Baja Lógica)
    public void eliminarLogico(Long id) throws EntidadNoEncontradaException {
        Producto prod = buscarPorId(id);
        prod.setEliminado(true); // Marcamos eliminado = true para no perder el historial de pedidos
    }
}