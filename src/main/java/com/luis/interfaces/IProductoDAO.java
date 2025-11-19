package com.luis.interfaces;

import com.luis.modelo.Producto;

import java.util.List;

public interface IProductoDAO extends ICrudDAO<Producto> {
    List<Producto> obtenerConStockBajo();
}
