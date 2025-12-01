package com.luis.interfaces;

import com.luis.modelo.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoDAO extends ICrudDAO<Producto> {
    List<Producto> obtenerConStockBajo();
    Optional<List<Producto>> obtenerPorDescripcion(String descripcion);
}
