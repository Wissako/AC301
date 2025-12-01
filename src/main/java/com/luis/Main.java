package com.luis;

import com.luis.dao.ClienteDaoImpl;
import com.luis.dao.ProductoDaoImpl;
import com.luis.dao.VentaDaoImpl;
import com.luis.modelo.Cliente;
import com.luis.modelo.DetalleVenta;
import com.luis.modelo.Producto;
import com.luis.modelo.Venta;
import com.luis.service.VentaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("VentasUnidadPersistencia");
    private static ClienteDaoImpl clienteDao = new ClienteDaoImpl();
    private static ProductoDaoImpl productoDao = new ProductoDaoImpl();
    private static VentaDaoImpl ventaDao = new VentaDaoImpl();
    private static VentaService ventaService = new VentaService();

    public static void main(String[] args) {
        System.out.println("\n" +
                "╔════════════════════════════════════════════════════════════╗\n" +
                "║   SISTEMA DE GESTIÓN DE VENTAS - JPA/HIBERNATE           ║\n" +
                "║   RA3: Gestiona la persistencia de datos con ORM          ║\n" +
                "╚════════════════════════════════════════════════════════════╝");

        // LIMPIEZA INICIAL (opcional)
        // limpiarDatos();

        // Tarea 1.1 & 1.2: Crear entidades
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "TAREA 1: CONFIGURACIÓN Y MODELO JPA\n" +
                "═══════════════════════════════════════════════════════════");
       // crearDatos();

        // Tarea 2.1: Búsquedas fundamentales
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "TAREA 2.1: BÚSQUEDAS FUNDAMENTALES\n" +
                "═══════════════════════════════════════════════════════════");
        busquedas();

        // Tarea 2.2: JOIN FETCH
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "TAREA 2.2: CONSULTAS OPTIMIZADAS (JOIN FETCH)\n" +
                "═══════════════════════════════════════════════════════════");
        consultasJoinFetch();

        // Tarea 2.3: Consultas de agregación
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "TAREA 2.3: CONSULTAS DE LÓGICA Y REPORTE\n" +
                "═══════════════════════════════════════════════════════════");
        consultasAgregacion();

        // Tarea 3.1 & 3.2: Transacción atómica + Atomicidad
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "TAREA 3.1 & 3.2: TRANSACCIÓN ATÓMICA (RN-01, RN-03)\n" +
                "═══════════════════════════════════════════════════════════");
        transaccionesAtomicas();

        // Tarea 3.3: Cascada y orphanRemoval
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "TAREA 3.3: GESTIÓN DE CASCADA Y HUÉRFANOS\n" +
                "═══════════════════════════════════════════════════════════");
        cascadaYHuerfanos();

        // Tarea 4.0: Reporte consolidado
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "TAREA 4.0: REPORTE DE VENTAS CONSOLIDADO\n" +
                "═══════════════════════════════════════════════════════════");
        reporteVentas();

        System.out.println("\n✅ PROGRAMA FINALIZADO\n");
    }

   private static void crearDatos() {
        System.out.println("\n1️⃣  Creando Cliente...");
        Cliente cliente = Cliente.builder()
                .dni("12345678A")
                .nombre("Juan")
                .apellidos("Pérez García")
                .telefono("666888999")
                .direccionHabitual("Calle Principal 123")
                .email("juan@email.com")
                .build();
        clienteDao.create(cliente);

        System.out.println("\n2️⃣  Creando Productos...");
        Producto p1 = Producto.builder()
                .codigo("PROD001")
                .descripcion("Laptop Dell XPS 15")
                .precioRecomendado(new BigDecimal("1299.99"))
                .existencias(50)
                .stockMinimo(10)
                .build();
        productoDao.create(p1);

        Producto p2 = Producto.builder()
                .codigo("PROD002")
                .descripcion("Mouse Logitech")
                .precioRecomendado(new BigDecimal("29.99"))
                .existencias(3)
                .stockMinimo(20)
                .build();
        productoDao.create(p2);

        System.out.println("\n3️⃣  Creando Venta PENDIENTE con 3 líneas...");
        Venta venta = Venta.builder()
                .cliente(cliente)
                .fechaVenta(Instant.now())
                .valorTotal(BigDecimal.ZERO)
                .estado("PENDIENTE")
                .build();

        DetalleVenta d1 = DetalleVenta.builder()
                .producto(p1)
                .cantidad(2)
                .precioVenta(new BigDecimal("1299.99"))
                .descuento(BigDecimal.ZERO)
                .subtotal(new BigDecimal("2599.98"))
                .build();
        venta.addDetalle(d1);

        DetalleVenta d2 = DetalleVenta.builder()
                .producto(p2)
                .cantidad(1)
                .precioVenta(new BigDecimal("29.99"))
                .descuento(BigDecimal.ZERO)
                .subtotal(new BigDecimal("29.99"))
                .build();
        venta.addDetalle(d2);

        DetalleVenta d3 = DetalleVenta.builder()
                .producto(p1)
                .cantidad(1)
                .precioVenta(new BigDecimal("1299.99"))
                .descuento(new BigDecimal("129.99"))
                .subtotal(new BigDecimal("1170.00"))
                .build();
        venta.addDetalle(d3);

        venta.setValorTotal(new BigDecimal("3799.97"));
        ventaDao.create(venta);
    }

    private static void busquedas() {
        try {
            System.out.println(" Búsqueda por DNI (12345678A):");
            Cliente clienteDni = clienteDao.getClienteByDni("12345678A");
            System.out.println("  ✓ " + clienteDni.getNombre() + " " + clienteDni.getApellidos());
        } catch (Exception e) {
            System.out.println("  ✗ Error: " + e.getMessage());
        }

        try {
            System.out.println("\n Búsqueda por Nombre (Juan):");
            Cliente clienteNombre = clienteDao.getClienteByName("Juan");
            System.out.println("  ✓ " + clienteNombre);
        } catch (Exception e) {
            System.out.println("  ✗ Error: " + e.getMessage());
        }

        System.out.println("\n Todos los clientes:");
        clienteDao.getAll().forEach(c -> System.out.println("  • " + c.getNombre()));
    }

    private static void consultasJoinFetch() {
        System.out.println("⭐ Obteniendo ventas del cliente 1 con JOIN FETCH...");
        System.out.println("   (Verás UNA ÚNICA consulta SELECT con JOINs en la consola)");
        List<Venta> ventas = ventaDao.obtenerPorCliente(1L);
        ventas.forEach(v -> {
            System.out.println("  Venta ID: " + v.getId() + " | Estado: " + v.getEstado());
            v.getDetalleVentas().forEach(d ->
                    System.out.println("    └─ " + d.getProducto().getCodigo() + " x" + d.getCantidad())
            );
        });
    }

    private static void consultasAgregacion() {
        System.out.println(" Productos con stock bajo (< stockMinimo):");
        productoDao.obtenerConStockBajo().forEach(p ->
                System.out.println("  ⚠️  " + p.getCodigo() + " | Stock: " + p.getExistencias() +
                        " | Mínimo: " + p.getStockMinimo())
        );

        System.out.println("\n Total de ventas confirmadas HOY:");
        BigDecimal total = ventaDao.calcularTotalVentasDia(LocalDate.now());
        System.out.println("  $ " + total);
    }

    private static void transaccionesAtomicas() {
        System.out.println("\n CASO 1: CONFIRMACIÓN EXITOSA (stock suficiente)");
        ventaService.confirmarVenta(3L);

        System.out.println("\n CASO 2: INTENTO DE CONFIRMACIÓN FALLIDA (stock insuficiente)");
        ventaService.confirmarVenta(3L); // Intento 2 debería fallar
    }

    private static void cascadaYHuerfanos() {
        System.out.println("🗑️  Eliminando línea de venta (orphanRemoval):");
        // Primero obtén una venta
        List<Venta> ventas = ventaDao.getAll();
        if (!ventas.isEmpty() && !ventas.get(0).getDetalleVentas().isEmpty()) {
            Long ventaId = ventas.get(0).getId();
            Long lineaId = ventas.get(0).getDetalleVentas().get(0).getId();
            ventaService.eliminarLineaVenta(ventaId, lineaId);
        } else {
            System.out.println("  ️  No hay detalles de venta disponibles");
        }

        System.out.println("\n🗑️  Eliminando venta completa (CascadeType.ALL):");
        ventas = ventaDao.getAll(); // Recargar ventas después de eliminaciones
        if (!ventas.isEmpty()) {
            ventaService.eliminarVenta(ventas.get(0).getId());
        } else {
            System.out.println("  ⚠️  No hay ventas disponibles para eliminar");
        }
    }

    private static void reporteVentas() {
        System.out.println("📋 REPORTE DE VENTAS:");
        List<Venta> ventas = ventaDao.getAll();

        if (ventas.isEmpty()) {
            System.out.println("  (No hay ventas en el sistema)");
            return;
        }

        for (Venta v : ventas) {
            System.out.println("\n┌─ CABECERA ─────────────────────────────");
            System.out.println("│ Venta ID: " + v.getId());
            System.out.println("│ Cliente: " + v.getCliente().getNombre() + " " + v.getCliente().getApellidos());
            System.out.println("│ Estado: " + v.getEstado());
            System.out.println("│ Fecha: " + v.getFechaVenta());

            System.out.println("├─ DETALLES ─────────────────────────────");
            BigDecimal totalDetalle = BigDecimal.ZERO;
            for (DetalleVenta d : v.getDetalleVentas()) {
                BigDecimal importe = d.getPrecioVenta()
                        .multiply(new BigDecimal(d.getCantidad()))
                        .subtract(d.getDescuento());
                totalDetalle = totalDetalle.add(importe);

                System.out.println("│ • " + d.getProducto().getCodigo() +
                        " | Qty: " + d.getCantidad() +
                        " | Precio: $" + d.getPrecioVenta() +
                        " | Desc: $" + d.getDescuento() +
                        " | Importe: $" + importe);
            }

            System.out.println("├─ PIE ─────────────────────────────────");
            System.out.println("│ Total: $" + v.getValorTotal());
            System.out.println("└────────────────────────────────────────");
        }
    }

    private static void limpiarDatos() {
        System.out.println("🧹 Limpiando datos previos...");
        ventaDao.getAll().forEach(v -> ventaDao.delete(Math.toIntExact(v.getId())));
        productoDao.getAll().forEach(p -> productoDao.delete(p.getId()));
        clienteDao.getAll().forEach(c -> clienteDao.delete(c.getId()));
        System.out.println("✓ Limpieza completada\n");
    }
}