package integrado.prog2.entities;

import java.time.LocalDateTime;

public abstract class Base {
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    // Constructor por defecto
    public Base() {
        this.eliminado = false; // Por defecto arranca no eliminado [cite: 184, 239]
        this.createdAt = LocalDateTime.now(); // Guarda la fecha y hora de creación automática
    }

    // Constructor con ID (útil para cuando modelemos o asignemos IDs manualmente)
    public Base(Long id) {
        this.id = id;
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}