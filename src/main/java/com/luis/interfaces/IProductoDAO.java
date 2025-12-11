package com.luis.interfaces;

import com.luis.modelo.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProductoDAO extends ICrudDAO<Producto> {
    List<Producto> obtenerConStockBajo();
    Optional<List<Producto>> obtenerPorDescripcion(String descripcion);
    BigDecimal calcularPromedio();
    List<Producto>masCarosQueLaMedia();
}
