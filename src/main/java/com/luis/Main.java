package com.luis;

import com.luis.dao.ClienteDaoImpl;
import com.luis.dao.ProductoDaoImpl;
import com.luis.dao.VentaDaoImpl;
import com.luis.modelo.Cliente;
import com.luis.modelo.Producto;
import com.luis.service.VentaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("VentasUnidadPersistencia");
    private static ClienteDaoImpl clienteDao = new ClienteDaoImpl();
    private static ProductoDaoImpl productoDao = new ProductoDaoImpl();
    private static VentaDaoImpl ventaDao = new VentaDaoImpl();
    private static VentaService ventaService = new VentaService();

    public static void main(String[] args) {
        productoDao.obtenerPorDescripcion("gam")
                .ifPresent(lista -> lista.forEach(System.out::println));
        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");
        ventaDao.ventasMayores()
                .ifPresent(lista -> lista.forEach(System.out::println));

        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");

        System.out.println("\n=== RETO 4: PRECIO MEDIO DE PRODUCTOS ===");
        BigDecimal precioMedio = productoDao.calcularPromedio();
        System.out.println("Precio medio: " + precioMedio + "€");

        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");

        System.out.println("\n=== RETOS 5 y 6: PRODUCTOS MÁS CAROS QUE LA MEDIA ===");
        List<Producto> productosCasos = productoDao.masCarosQueLaMedia();
        productosCasos.forEach(p ->
                System.out.println(p.getDescripcion() + " - Precio: " + p.getPrecioRecomendado() + "€")
        );

        System.out.println("-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-");

        System.out.println("\n=== RETO 7: CLIENTES 2023 NO 2024 DE MADRID ===");
        List<Cliente> clientesFiltrados = clienteDao.compra2023No2024();
        clientesFiltrados.forEach(c ->
                System.out.println(c.getNombre() + " " + c.getApellidos() +
                        " - Dirección: " + c.getDireccionHabitual())
        );
    }
}