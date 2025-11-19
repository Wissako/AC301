package com.luis.interfaces;

import com.luis.modelo.Cliente;

import java.util.Optional;

public interface IClienteDAO extends ICrudDAO<Cliente> {
Optional<Cliente> getClienteByDni(String dni);
Optional<Cliente> getClienteByName(String nombre);

}
