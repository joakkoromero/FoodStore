package integrado.prog2;

import integrado.prog2.entities.*;
import integrado.prog2.enums.*;
import integrado.prog2.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // Instanciamos los servicios globales de la aplicación
    private static final CategoriaService categoriaService = new CategoriaService();
    private static final ProductoService productoService = new ProductoService(categoriaService);
    private static final UsuarioService usuarioService = new UsuarioService();
    private static final PedidoService pedidoService = new PedidoService(usuarioService, productoService);

    public static void main(String[] args) {
        precargarDatosPrueba();
        menuPrincipal();
    }

    // Método para tener el sistema con datos de entrada y facilitar el testeo inmediato
    private static void precargarDatosPrueba() {
        try {
            Categoria hamburguesas = categoriaService.crear("Hamburguesas", "Variedad de hamburguesas caseras");
            Categoria bebidas = categoriaService.crear("Bebidas", "Gaseosas, aguas y cervezas");

            productoService.crear("Hamburguesa Simple", "Carne, queso y aderezos", 4500.0, 20, "simple.jpg", true, hamburguesas.getId());
            productoService.crear("Hamburguesa Completa", "Carne, queso, jamón, huevo y lechuga", 5800.0, 15, "completa.jpg", true, hamburguesas.getId());
            productoService.crear("Gaseosa Cola 500ml", "Línea Coca Cola", 1500.0, 50, "coca.jpg", true, bebidas.getId());

            usuarioService.crear("Juan", "Pérez", "juan@mail.com", "2634556677", "1234", Rol.USUARIO);
            usuarioService.crear("Admin", "Coop", "admin@foodstore.com", "2634112233", "admin123", Rol.ADMIN);
        } catch (Exception ignored) {}
    }

    // === MENÚ PRINCIPAL ===
    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Gestión de Usuarios");
            System.out.println("4. Gestión de Pedidos");
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> subMenuCategorias();
                    case 2 -> subMenuProductos();
                    case 3 -> subMenuUsuarios();
                    case 4 -> subMenuPedidos();
                    case 0 -> System.out.println("\n¡Gracias por usar Food Store! Finalizando programa...");
                    default -> System.out.println("⚠️ Opción fuera de rango. Reintente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Error: Ingrese un número válido.");
                opcion = -1;
            }
        } while (opcion != 0);
    }

    // === SUBMENÚ 1: CATEGORÍAS ===
    private static void subMenuCategorias() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ CATEGORÍAS ---");
            System.out.println("1. Listar categorías");
            System.out.println("2. Crear categoría");
            System.out.println("3. Editar categoría");
            System.out.println("4. Eliminar categoría");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> {
                        var lista = categoriaService.listarActivas();
                        if (lista.isEmpty()) System.out.println("No hay categorías cargadas.");
                        else lista.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Nombre de la categoría: ");
                        String nom = scanner.nextLine();
                        System.out.print("Descripción: ");
                        String desc = scanner.nextLine();
                        Categoria c = categoriaService.crear(nom, desc);
                        System.out.println("✅ Categoría creada con éxito. ID asignado: " + c.getId());
                    }
                    case 3 -> {
                        System.out.print("Ingrese ID de la categoría a editar: ");
                        Long id = Long.parseLong(scanner.nextLine());
                        System.out.print("Nuevo Nombre (deje vacío para no modificar): ");
                        String nom = scanner.nextLine();
                        System.out.print("Nueva Descripción (deje vacío para no modificar): ");
                        String desc = scanner.nextLine();
                        categoriaService.editar(id, nom, desc);
                        System.out.println("✅ Categoría editada correctamente.");
                    }
                    case 4 -> {
                        System.out.print("Ingrese ID de la categoría a eliminar: ");
                        Long id = Long.parseLong(scanner.nextLine());
                        System.out.print("¿Confirma la baja lógica? (S/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("S")) {
                            categoriaService.eliminarLogico(id);
                            System.out.println("✅ Categoría dada de baja.");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error: " + e.getMessage());
                opcion = -1;
            }
        } while (opcion != 0);
    }

    // === SUBMENÚ 2: PRODUCTOS ===
    private static void subMenuProductos() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRODUCTOS ---");
            System.out.println("1. Listar productos");
            System.out.println("2. Crear producto");
            System.out.println("3. Editar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> {
                        var lista = productoService.listarActivos();
                        if (lista.isEmpty()) System.out.println("No hay productos cargados.");
                        else lista.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Nombre del producto: ");
                        String nom = scanner.nextLine();
                        System.out.print("Descripción: ");
                        String desc = scanner.nextLine();
                        System.out.print("Precio: ");
                        Double pre = Double.parseDouble(scanner.nextLine());
                        System.out.print("Stock inicial: ");
                        int stk = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nombre de imagen (ej: pizza.png): ");
                        String img = scanner.nextLine();
                        System.out.print("ID de su Categoría asociada: ");
                        Long catId = Long.parseLong(scanner.nextLine());

                        Producto p = productoService.crear(nom, desc, pre, stk, img, true, catId);
                        System.out.println("✅ Producto creado con éxito. ID: " + p.getId());
                    }
                    case 3 -> {
                        System.out.print("Ingrese ID del producto a editar: ");
                        Long id = Long.parseLong(scanner.nextLine());
                        System.out.print("Nuevo Nombre (vacío para omitir): ");
                        String nom = scanner.nextLine();
                        System.out.print("Nueva Descripción (vacío para omitir): ");
                        String desc = scanner.nextLine();
                        System.out.print("Nuevo Precio (o -1 para omitir): ");
                        double preInput = Double.parseDouble(scanner.nextLine());
                        Double pre = preInput == -1 ? null : preInput;
                        System.out.print("Nuevo Stock (o -1 para omitir): ");
                        int stkInput = Integer.parseInt(scanner.nextLine());
                        Integer stk = stkInput == -1 ? null : stkInput;
                        System.out.print("ID de nueva Categoría (o -1 para omitir): ");
                        long catInput = Long.parseLong(scanner.nextLine());
                        Long catId = catInput == -1 ? null : catInput;

                        productoService.editar(id, nom, desc, pre, stk, null, null, catId);
                        System.out.println("✅ Producto actualizado.");
                    }
                    case 4 -> {
                        System.out.print("Ingrese ID del producto a eliminar: ");
                        Long id = Long.parseLong(scanner.nextLine());
                        System.out.print("¿Confirma la baja lógica? (S/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("S")) {
                            productoService.eliminarLogico(id);
                            System.out.println("✅ Producto dado de baja.");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error: " + e.getMessage());
                opcion = -1;
            }
        } while (opcion != 0);
    }

    // === SUBMENÚ 3: USUARIOS ===
    private static void subMenuUsuarios() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ USUARIOS ---");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Crear usuario");
            System.out.println("3. Editar usuario");
            System.out.println("4. Eliminar usuario");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> {
                        var lista = usuarioService.listarActivos();
                        if (lista.isEmpty()) System.out.println("No hay usuarios registrados.");
                        else lista.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Nombre: "); String n = scanner.nextLine();
                        System.out.print("Apellido: "); String a = scanner.nextLine();
                        System.out.print("Email: "); String m = scanner.nextLine();
                        System.out.print("Celular: "); String c = scanner.nextLine();
                        System.out.print("Contraseña: "); String pass = scanner.nextLine();
                        System.out.println("Rol (1: ADMIN, 2: USUARIO): ");
                        Rol r = Integer.parseInt(scanner.nextLine()) == 1 ? Rol.ADMIN : Rol.USUARIO;

                        Usuario u = usuarioService.crear(n, a, m, c, pass, r);
                        System.out.println("✅ Usuario creado. ID: " + u.getId());
                    }
                    case 3 -> {
                        System.out.print("Ingrese ID del usuario a editar: ");
                        Long id = Long.parseLong(scanner.nextLine());
                        System.out.print("Nuevo Nombre (vacío para omitir): "); String n = scanner.nextLine();
                        System.out.print("Nuevo Apellido (vacío para omitir): "); String a = scanner.nextLine();
                        System.out.print("Nuevo Email (vacío para omitir): "); String m = scanner.nextLine();

                        usuarioService.editar(id, n, a, m, null, null, null);
                        System.out.println("✅ Datos del usuario modificados.");
                    }
                    case 4 -> {
                        System.out.print("ID del usuario a dar de baja: ");
                        Long id = Long.parseLong(scanner.nextLine());
                        System.out.print("¿Confirma baja lógica? (S/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("S")) {
                            usuarioService.eliminarLogico(id);
                            System.out.println("✅ Usuario desactivado.");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error: " + e.getMessage());
                opcion = -1;
            }
        } while (opcion != 0);
    }

    // === SUBMENÚ 4: PEDIDOS ===
    private static void subMenuPedidos() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PEDIDOS ---");
            System.out.println("1. Listar pedidos");
            System.out.println("2. Crear nuevo pedido (Cargar Carrito)");
            System.out.println("3. Cambiar estado/forma de pago de un pedido");
            System.out.println("4. Eliminar pedido");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> {
                        var lista = pedidoService.listarActivos();
                        if (lista.isEmpty()) System.out.println("No hay pedidos cargados en el historial.");
                        else {
                            for (Pedido p : lista) {
                                System.out.println(p);
                                System.out.println("   -> Detalles del Pedido:");
                                p.getDetalles().forEach(d -> System.out.println("      " + d));
                            }
                        }
                    }
                    case 2 -> {
                        System.out.print("Ingrese ID del Usuario que realiza el pedido: ");
                        Long usuarioId = Long.parseLong(scanner.nextLine());

                        System.out.println("Seleccione Forma de Pago (1: TARJETA, 2: TRANSFERENCIA, 3: EFECTIVO): ");
                        int fpSelect = Integer.parseInt(scanner.nextLine());
                        FormaPago fp = fpSelect == 1 ? FormaPago.TARJETA : (fpSelect == 2 ? FormaPago.TRANSFERENCIA : FormaPago.EFECTIVO);

                        List<int[]> itemsAPedir = new ArrayList<>();
                        String continuar;
                        do {
                            System.out.print("ID del Producto a agregar al carrito: ");
                            int prodId = Integer.parseInt(scanner.nextLine());
                            System.out.print("Cantidad: ");
                            int cant = Integer.parseInt(scanner.nextLine());

                            itemsAPedir.add(new int[]{prodId, cant});

                            System.out.print("¿Desea agregar otro producto al pedido? (S/N): ");
                            continuar = scanner.nextLine();
                        } while (continuar.equalsIgnoreCase("S"));

                        Pedido p = pedidoService.crearPedido(usuarioId, fp, itemsAPedir);
                        System.out.println("✅ ¡Pedido Creado con Éxito! ID: " + p.getId() + " - Total final: $" + p.getTotal());
                    }
                    case 3 -> {
                        System.out.print("Ingrese ID del pedido a modificar: ");
                        Long id = Long.parseLong(scanner.nextLine());

                        System.out.println("Nuevo Estado (1: PENDIENTE, 2: CONFIRMADO, 3: TERMINADO, 4: CANCELADO, 0: Omitir): ");
                        int estSel = Integer.parseInt(scanner.nextLine());
                        Estado est = estSel == 1 ? Estado.PENDIENTE : (estSel == 2 ? Estado.CONFIRMADO : (estSel == 3 ? Estado.TERMINADO : (estSel == 4 ? Estado.CANCELADO : null)));

                        pedidoService.actualizarEstadoYPago(id, est, null);
                        System.out.println("✅ Pedido actualizado correctamente.");
                    }
                    case 4 -> {
                        System.out.print("ID del pedido a eliminar: ");
                        Long id = Long.parseLong(scanner.nextLine());
                        System.out.print("¿Confirma la eliminación lógica? (S/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("S")) {
                            pedidoService.eliminarLogico(id);
                            System.out.println("✅ Pedido archivado del sistema.");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error en Pedido: " + e.getMessage());
                opcion = -1;
            }
        } while (opcion != 0);
    }
}