package com.luis;

import com.luis.dao.ClienteDaoImpl;
import com.luis.dao.ProductoDaoImpl;
import com.luis.modelo.Cliente;
import com.luis.modelo.Producto;

import java.util.List;


public class Main {

    public static void main(String[] args) {
    ClienteDaoImpl clienteDao = new ClienteDaoImpl();
    List<Cliente> clientes = clienteDao.getAll();
    clientes.forEach(System.out::println);

    ProductoDaoImpl productoDao = new ProductoDaoImpl();
    List<Producto> productos = productoDao.getAll();
    productos.forEach(System.out::println);
    }
}