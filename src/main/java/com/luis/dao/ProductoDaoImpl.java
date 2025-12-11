package com.luis.dao;

import com.luis.Main;
import com.luis.interfaces.IProductoDAO;
import com.luis.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductoDaoImpl implements IProductoDAO {
    EntityManagerFactory emf = Main.emf;


    @Override
    public List<Producto> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em
                    .createQuery("from Producto", Producto.class)
                    .getResultList();

        }
    }

    @Override
    public Producto findById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Producto.class, id);
        }

    }

    @Override
    public void actualizar(Producto obj) {
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
            em.remove(em.find(Producto.class, id));
            em.getTransaction().commit();
        }
    }

    @Override
    public void create(Producto obj) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Producto> obtenerConStockBajo() {
        String query = "SELECT p FROM Producto p WHERE p.existencias < p.stockMinimo";
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(query, Producto.class)
                    .getResultList();
        }
    }
    @Override
    public Optional<List<Producto>> obtenerPorDescripcion(String descripcion) {
        try (EntityManager em = emf.createEntityManager()) {
            String query = "SELECT p FROM Producto p WHERE LOWER(p.descripcion) LIKE LOWER(CONCAT('%',:descripcion,'%'))";
            TypedQuery<Producto> consulta = em.createQuery(query, Producto.class);
            consulta.setParameter("descripcion", "%" + descripcion.toLowerCase() + "%");
            List<Producto> productos = consulta.getResultList();
            return Optional.ofNullable(productos);
        }
    }

    @Override
    public BigDecimal calcularPromedio () {
        String query = "SELECT AVG(p.precioRecomendado) FROM Producto p";
        try (EntityManager em = emf.createEntityManager()) {
            Double resultado = em.createQuery(query, Double.class)
                    .getSingleResult();
            return resultado != null ? BigDecimal.valueOf(resultado) : BigDecimal.ZERO;
        }
    }

    @Override
    public List<Producto> masCarosQueLaMedia() {
        String query = "SELECT p FROM Producto p " +
                "WHERE p.precioRecomendado > " +
                "(SELECT AVG(p2.precioRecomendado) FROM Producto p2)";
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(query, Producto.class)
                    .getResultList();
        }
    }


}
