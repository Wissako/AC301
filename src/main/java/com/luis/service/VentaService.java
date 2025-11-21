package com.luis.service;

import com.luis.modelo.DetalleVenta;
import com.luis.modelo.Producto;
import com.luis.modelo.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import static com.luis.dao.VentaDaoImpl.emf;

public class VentaService {
    public void confirmarVenta(Long ventaId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

    try{
        Venta venta = em.find(Venta.class, ventaId);
        if(venta == null ) throw new Exception("venta no encontrada");
        for(DetalleVenta detalle : venta.getDetalleVentas()){
            Producto p = detalle.getProducto();
            if(p.getExistencias() < detalle.getCantidad()){
                throw new Exception("Stock insuficiente en:"+p.getCodigo());
            }
        }
        for(DetalleVenta detalle: venta.getDetalleVentas()){
            Producto p = detalle.getProducto();
            p.setExistencias(p.getExistencias() - detalle.getCantidad());
            em.merge(p);
        }
        venta.setEstado("'CONFIRMADA'");
        em.merge(venta);
        tx.commit();
        System.out.println("COMMIT: Venta confirmada");
    }catch (Exception e){
        tx.rollback();
        System.out.println("ROLLBACK:"+ e.getMessage());
    } finally {
        em.close();

    }

    }
}
