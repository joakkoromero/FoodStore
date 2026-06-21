package integrado.prog2.service;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.EntidadNoEncontradaException;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    // Lista en memoria que simula la tabla de usuarios
    private final List<Usuario> usuarios = new ArrayList<>();
    private Long ultimoId = 0L; // Autoincremental para los IDs de usuarios

    // 1. HU-USR-02: Crear usuario
    public Usuario crear(String nombre, String apellido, String mail, String celular, String contraseña, Rol rol) throws Exception {
        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre y apellido no pueden estar vacíos.");
        }
        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }

        // REGLA DE NEGOCIO: Validar la unicidad del mail recorriendo la colección
        for (Usuario user : usuarios) {
            if (user.getMail().equalsIgnoreCase(mail.trim()) && !user.isEliminado()) {
                throw new Exception("Error: Ya existe un usuario activo registrado con el email: " + mail);
            }
        }

        ultimoId++;
        Usuario nuevo = new Usuario(ultimoId, nombre.trim(), apellido.trim(), mail.trim().toLowerCase(), celular.trim(), contraseña, rol);
        usuarios.add(nuevo);
        return nuevo;
    }

    // 2. HU-USR-01: Listar usuarios activos (no eliminados)
    public List<Usuario> listarActivos() {
        List<Usuario> activos = new ArrayList<>();
        for (Usuario user : usuarios) {
            if (!user.isEliminado()) {
                activos.add(user);
            }
        }
        return activos;
    }

    // Método auxiliar para buscar un usuario por ID
    public Usuario buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Usuario user : usuarios) {
            if (user.getId().equals(id) && !user.isEliminado()) {
                return user;
            }
        }
        throw new EntidadNoEncontradaException("No se encontró ningún usuario activo con el ID: " + id);
    }

    // 3. HU-USR-03: Editar usuario
    public void editar(Long id, String nuevoNombre, String nuevoApellido, String nuevoMail, String nuevoCelular, String nuevaContraseña, Rol nuevoRol) throws Exception {
        Usuario user = buscarPorId(id); // Si no existe, lanza EntidadNoEncontradaException

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            user.setNombre(nuevoNombre.trim());
        }
        if (nuevoApellido != null && !nuevoApellido.trim().isEmpty()) {
            user.setApellido(nuevoApellido.trim());
        }

        // Si se modifica el mail, volvemos a validar que siga siendo único
        if (nuevoMail != null && !nuevoMail.trim().isEmpty()) {
            String mailFormateado = nuevoMail.trim().toLowerCase();
            for (Usuario u : usuarios) {
                if (!u.getId().equals(id) && u.getMail().equalsIgnoreCase(mailFormateado) && !u.isEliminado()) {
                    throw new Exception("Error: El email '" + nuevoMail + "' ya está siendo usado por otro usuario.");
                }
            }
            user.setMail(mailFormateado);
        }

        if (nuevoCelular != null) {
            user.setCelular(nuevoCelular.trim());
        }
        if (nuevaContraseña != null && !nuevaContraseña.isEmpty()) {
            user.setContraseña(nuevaContraseña);
        }
        if (nuevoRol != null) {
            user.setRol(nuevoRol);
        }
    }

    // 4. HU-USR-04: Eliminar usuario (Baja Lógica / Soft Delete)
    public void eliminarLogico(Long id) throws EntidadNoEncontradaException {
        Usuario user = buscarPorId(id);
        user.setEliminado(true); // Se marca como eliminado para resguardar el historial de sus pedidos anteriores
    }
}