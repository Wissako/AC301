package com.luis.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente", nullable = false)
    private Cliente cliente;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private Instant fechaVenta;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "valor_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorTotal;

    @Size(max = 500)
    @Column(name = "notas", length = 500)
    private String notas;

    @NotNull
    @Column(name = "estado",nullable = false)
    @ColumnDefault("'PENDIENTE'")
    private String estado;

    @Builder.Default
    @OneToMany(mappedBy = "venta",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DetalleVenta> detalleVentas = new ArrayList<>();

    public void addDetalle(DetalleVenta detalles) {
        if(detalleVentas == null){
            detalleVentas = new ArrayList<>();
        }
        detalleVentas.add(detalles);
        detalles.setVenta(this);
    }
}