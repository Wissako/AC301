package com.luis.interfaces;

import com.luis.modelo.Venta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IVentaDAO extends ICrudDAO<Venta> {
    List<Venta> obtenerPorCliente(Long clienteId);
    Optional<List<Venta>> ventasMayores();
    BigDecimal calcularTotalVentasDia(LocalDate fecha);
}
