package com.luis.dao;

import com.luis.Main;
import com.luis.interfaces.IVentaDAO;
import com.luis.modelo.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class VentaDaoImpl implements IVentaDAO {
    EntityManagerFactory emf = Main.emf;

    @Override
    public List<Venta> getAll() {

        try (EntityManager em = emf.createEntityManager()) {
            return em
                    .createQuery("FROM Venta", Venta.class)
                    .getResultList();
        }
    }

    @Override
    public Venta findById(Integer id) {

        String query = "select v FROM Venta v where v.id = :id";

        try (EntityManager em = emf.createEntityManager()) {

            return em.createQuery(query, Venta.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }


    @Override
    public void actualizar(Venta obj) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Venta venta = em.find(Venta.class, id);
            if (venta != null) {
                em.remove(venta);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public void create(Venta obj) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Venta> obtenerPorCliente(Long clienteId) {
        String query = "SELECT DISTINCT v FROM Venta v " +
                "LEFT JOIN FETCH v.detalleVentas d " +
                "LEFT JOIN FETCH d.producto " +
                "WHERE v.cliente.id = :clienteId";

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(query, Venta.class)
                    .setParameter("clienteId", clienteId)
                    .getResultList();
        }
    }

    @Override
    public BigDecimal calcularTotalVentasDia(LocalDate fecha) {
        String query =
                "SELECT COALESCE(SUM(v.valorTotal),0) FROM Venta v " +
                        "WHERE DATE(v.fechaVenta) = :fecha " +
                        "AND v.estado = 'CONFIRMADA'";
        try (EntityManager em = emf.createEntityManager()) {
            java.sql.Date sqlDate = java.sql.Date.valueOf(fecha);
            Object result = em.createQuery(query)
                    .setParameter("fecha", sqlDate)
                    .getSingleResult();

            return result != null ? (BigDecimal) result : BigDecimal.ZERO;
        }
    }

    /*
    *
    * Situación: Queremos generar un listado de todas las Ventas realizadas para
    enviarlas a contabilidad cuyo importe sea mayor a 500€.
    Requerimiento: Trae todas las ventas y sus clientes asociados en una sola
    consulta
    *
    * */
    @Override
    public Optional<List<Venta>> ventasMayores() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Venta> query = em.createQuery(
                    "SELECT v FROM Venta v " +
                            "LEFT JOIN FETCH v.cliente c  " +
                            "WHERE v.valorTotal >= 500",
                    Venta.class
            );
            List<Venta> lista = query.getResultList();
            return Optional.ofNullable(lista);
        }
    }
}