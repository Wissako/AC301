package com.luis.interfaces;

import com.luis.modelo.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteDAO extends ICrudDAO<Cliente> {
Cliente getClienteByDni(String dni);
Cliente getClienteByName(String nombre);
List<Cliente> compra2023No2024();
}
