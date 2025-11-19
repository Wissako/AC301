
package com.luis.dao;


import com.luis.interfaces.IVentaDAO;
import com.luis.modelo.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VentaDaoImpl implements IVentaDAO {
private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("VentasUnidadPersistencia");


    @Override
    public List<Venta> obtenerPorCliente(Long clienteId) {
        return List.of();
    }

    @Override
    public BigDecimal calcularTotalVentasDia(LocalDate fecha) {
        return null;
    }

    @Override
    public List<Venta> getAll() {
        return List.of();
    }

    @Override
    public Venta findById(Integer id) {
        return null;
    }

    @Override
    public void actualizar(Venta obj) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void create(Venta obj) {

    }
}
