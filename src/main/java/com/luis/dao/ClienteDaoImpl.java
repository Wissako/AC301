package com.luis.dao;

import com.luis.Main;
import com.luis.interfaces.IClienteDAO;
import com.luis.modelo.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public class ClienteDaoImpl implements IClienteDAO {
    EntityManagerFactory emf = Main.emf;

    @Override
    public Cliente getClienteByDni(String dni) {
        String query = "select c from Cliente c where c.dni = :dni";
        try (EntityManager em = emf.createEntityManager();) {

            return em.createQuery(query, Cliente.class)
                    .setParameter("dni", dni)
                    .getSingleResult();
        }
    }

    @Override
    public Cliente getClienteByName(String nombre) {
        String query = "select c from Cliente c where c.nombre LIKE :nombre";
        try (EntityManager em = emf.createEntityManager();) {
            return em.createQuery(query, Cliente.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        }
    }

    @Override
    public List<Cliente> compra2023No2024() {
        String query = "SELECT DISTINCT c FROM Cliente c " +
                "WHERE c.id IN (" +
                "    SELECT v1.cliente.id FROM Venta v1 " +
                "    WHERE YEAR(v1.fechaVenta) = 2023" +
                ") " +
                "AND c.id NOT IN (" +
                "    SELECT v2.cliente.id FROM Venta v2 " +
                "    WHERE YEAR(v2.fechaVenta) = 2024" +
                ") " +
                "AND LOWER(c.direccionHabitual) LIKE LOWER(CONCAT('%','%madrid%','%'))";

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(query, Cliente.class)
                    .getResultList();
        }
    }

    @Override
    public List<Cliente> getAll() {

        try(EntityManager em = emf.createEntityManager()) {
            return em
                    .createQuery("from Cliente", Cliente.class)
                    .getResultList();
        }
    }

    @Override
    public Cliente findById(Integer id) {
        String query = "select c from Cliente c where c.id = :id";
        try (EntityManager em = emf.createEntityManager();) {

            return em.createQuery(query, Cliente.class)
                    .setParameter("id", id)
                    .getSingleResult();

        }
    }

    @Override
    public void actualizar(Cliente obj) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.merge(obj);
            transaction.commit();
        }


    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Cliente cliente = em.find(Cliente.class, id);
            em.remove(cliente);
            tx.commit();
        }


    }

    @Override
    public void create(Cliente obj) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.persist(obj);
            transaction.commit();
        }


    }
}
