package com.luis.dao;

import com.luis.interfaces.IProductoDAO;
import com.luis.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class ProductoDaoImpl implements IProductoDAO {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("VentasUnidadPersistencia");




    @Override
    public List<Producto> getAll() {
       try(EntityManager em = emf.createEntityManager()){
           return em
                   .createQuery("from Producto", Producto.class)
                   .getResultList();

       }
    }

    @Override
    public Producto findById(Integer id) {
        try(EntityManager em= emf.createEntityManager()){
           return em.find(Producto.class,id);
        }

    }

    @Override
    public void actualizar(Producto obj) {
    try(EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
    }
    }

    @Override
    public void delete(Integer id) {
    try(EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        em.remove(em.find(Producto.class, id));
        em.getTransaction().commit();
    }
    }

    @Override
    public void create(Producto obj) {
    try(EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        em.persist(obj);
        em.getTransaction().commit();
    }
    }
    @Override
    public List<Producto> obtenerConStockBajo(){
        String query = "SELECT p FROM Producto p WHERE p.existencias < p.stockMinimo";
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery(query, Producto.class)
                    .getResultList();
        }
    }
}
