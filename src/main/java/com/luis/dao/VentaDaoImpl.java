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
    public List<Venta> getAll() {
       try(EntityManager em= emf.createEntityManager()){
        return em
                .createQuery("FROM Venta", Venta.class)
                .getResultList();
       }
    }

    @Override
    public Venta findById(Integer id) {
    try(EntityManager em = emf.createEntityManager()){
        return em.find(Venta.class, id);
    }
}

    @Override
    public void actualizar(Venta obj) {
    try(EntityManager em= emf.createEntityManager()){
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }
    }

    @Override
    public void delete(Integer id) {
    try(EntityManager em = emf.createEntityManager()){
        em.getTransaction().begin();
        Venta venta = em.find(Venta.class, id);
        if(venta != null){
            em.remove(venta);
        }
        em.getTransaction().commit();
    }
    }

    @Override
    public void create(Venta obj) {
        try(EntityManager em= emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        }

    }

    @Override
    public List<Venta> obtenerPorCliente(Long clienteId) {
        String jpql = "SELECT DISTINCT v FROM Venta v " +
                "LEFT JOIN FETCH v.detalles d " +
                "LEFT JOIN FETCH d.producto " +
                "WHERE v.cliente.id = :clienteId";

        try(EntityManager em = emf.createEntityManager()) {
            return em.createQuery(jpql, Venta.class)
                    .setParameter("clienteId", clienteId)
                    .getResultList();
        }
    }
    @Override
    public BigDecimal calcularTotalVentasDia(LocalDate fecha){
        String query =
                "SELECT COALESCE(SUM(v.valorTotal),0) FROM Venta v"+
                "WHERE DATE(v.fechaVenta)= :fecha"+
                "AND v.estado = 'CONFIRMADA'";
        try(EntityManager em = emf.createEntityManager()){
            java.sql.Date sqlDate = java.sql.Date.valueOf(fecha);
            Object result = em.createQuery(query)
                    .setParameter("fecha", sqlDate)
                    .getSingleResult();

            return result != null ? (BigDecimal) result : BigDecimal.ZERO;
        }

    }
}
