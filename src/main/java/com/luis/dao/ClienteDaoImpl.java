package com.luis.dao;

import com.luis.interfaces.IClienteDAO;
import com.luis.modelo.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

public class ClienteDaoImpl implements IClienteDAO {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("VentasUnidadPersistencia");

    @Override
    public Optional<Cliente> getClienteByDni(String dni) {
        String query = "select c from Cliente c where c.dni = :dni";
        try (EntityManager em = emf.createEntityManager();) {

            return em.createQuery(query, Cliente.class)
                    .setParameter("dni", dni)
                    .getResultList()
                    .stream()
                    .findFirst();
        }
    }

    @Override
    public Optional<Cliente> getClienteByName(String nombre) {
        String query = "select c from Cliente c where c.nombre LIKE :nombre";
        try (EntityManager em = emf.createEntityManager();) {
            return em.createQuery(query, Cliente.class)
                    .setParameter("nombre", nombre)
                    .getResultList()
                    .stream()
                    .findFirst();
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
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
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
