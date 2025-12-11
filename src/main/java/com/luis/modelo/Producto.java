package com.luis.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "codigo", nullable = false, length = 50,unique = true)
    private String codigo;

    @Size(max = 255)
    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Min(value=0, message = "El precio no puede ser negativo")
    @Column(name = "precio_recomendado", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioRecomendado;

    @NotNull
    @Min(value=0, message = "Las existencias no pueden ser negativas")
    @Column(name = "existencias", nullable = false)
    private Integer existencias;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @NotNull
    @ColumnDefault("15")
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "producto")
    private List<DetalleVenta> detalleVentas = new ArrayList<>();

}