package com.luis;

import com.luis.dao.ClienteDaoImpl;
import com.luis.dao.ProductoDaoImpl;
import com.luis.dao.VentaDaoImpl;
import com.luis.service.VentaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

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

    }
}