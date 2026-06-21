package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.EntidadNoEncontradaException;

import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    // La lista que simula la base de datos en memoria para las categorías
    private final List<Categoria> categorias = new ArrayList<>();
    private Long ultimoId = 0L; // Generador de IDs autoincrementales

    // 1. HU-CAT-02: Crear categoría
    public Categoria crear(String nombre, String descripcion) throws Exception {
        // Validación de no vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }

        // Validar unicidad del nombre recorriendo la colección
        for (Categoria cat : categorias) {
            if (cat.getNombre().equalsIgnoreCase(nombre.trim()) && !cat.isEliminado()) {
                throw new Exception("Ya existe una categoría activa con el nombre: " + nombre);
            }
        }

        ultimoId++; // Incrementamos el ID
        Categoria nueva = new Categoria(ultimoId, nombre.trim(), descripcion.trim());
        categorias.add(nueva);
        return nueva;
    }

    // 2. HU-CAT-01: Listar categorías activas (no eliminadas)
    public List<Categoria> listarActivas() {
        List<Categoria> activas = new ArrayList<>();
        for (Categoria cat : categorias) {
            if (!cat.isEliminado()) { // Solo las que eliminado == false
                activas.add(cat);
            }
        }
        return activas;
    }

    // Método auxiliar para buscar por ID (sirve para editar, eliminar o asociar a productos)
    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Categoria cat : categorias) {
            if (cat.getId().equals(id) && !cat.isEliminado()) {
                return cat;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ninguna categoría activa con el ID: " + id);
    }

    // 3. HU-CAT-03: Editar categoría
    public void editar(Long id, String nuevoNombre, String nuevaDescripcion) throws Exception {
        Categoria cat = buscarPorId(id); // Si no existe, lanza la excepción anterior

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            // Validar que el nuevo nombre no lo tenga OTRA categoría distinta
            for (Categoria c : categorias) {
                if (!c.getId().equals(id) && c.getNombre().equalsIgnoreCase(nuevoNombre.trim()) && !c.isEliminado()) {
                    throw new Exception("Ya existe otra categoría activa con el nombre: " + nuevoNombre);
                }
            }
            cat.setNombre(nuevoNombre.trim());
        }

        if (nuevaDescripcion != null) {
            cat.setDescripcion(nuevaDescripcion.trim());
        }
    }

    // 4. HU-CAT-04: Eliminar categoría (Baja Lógica / Soft Delete)
    public void eliminarLogico(Long id) throws EntidadNoEncontradaException {
        Categoria cat = buscarPorId(id);
        cat.setEliminado(true); // Marcamos eliminado = true en vez de remover físicamente
    }
}