package com.luis.service;

import com.luis.Main;
import com.luis.modelo.DetalleVenta;
import com.luis.modelo.Producto;
import com.luis.modelo.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;



public class VentaService {
    EntityManagerFactory emf = Main.emf;
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

    public void eliminarLineaVenta(Long ventaId, Long lineaId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();


            Venta venta = em.find(Venta.class, ventaId);
            if (venta == null) {
                throw new Exception("Venta no encontrada");
            }


            DetalleVenta detalleAEliminar = null;
            for (DetalleVenta detalle : venta.getDetalleVentas()) {
                if (detalle.getId().equals(lineaId)) {
                    detalleAEliminar = detalle;
                    break;
                }
            }

            if (detalleAEliminar == null) {
                throw new Exception("Detalle de venta no encontrado");
            }


            venta.getDetalleVentas().remove(detalleAEliminar);


            venta.setValorTotal(
                    venta.getDetalleVentas().stream()
                            .map(DetalleVenta::getSubtotal)
                            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
            );

            em.merge(venta);
            tx.commit();

            System.out.println("COMMIT: Línea de venta eliminada (orphanRemoval)");
            System.out.println("└─ DetalleVenta ID " + lineaId + " eliminado automáticamente de la BD");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("ROLLBACK: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    public void eliminarVenta(Long ventaId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Venta venta = em.find(Venta.class, ventaId);
            if (venta == null) {
                throw new Exception("Venta no encontrada");
            }

            int numDetalles = venta.getDetalleVentas().size();


            em.remove(venta);

            tx.commit();

            System.out.println("COMMIT: Venta eliminada (CascadeType.ALL)");
            System.out.println("   └─ Venta ID " + ventaId + " y sus " + numDetalles +
                    " detalles eliminados en cascada");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("ROLLBACK: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
