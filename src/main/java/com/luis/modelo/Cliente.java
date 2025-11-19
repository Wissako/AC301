package com.luis.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "clientes")
@ToString
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "dni", nullable = false, length = 10,unique = true)
    private String dni;

    @Size(max = 80)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Size(max = 120)
    @NotNull
    @Column(name = "apellidos", nullable = false, length = 120)
    private String apellidos;

    @Size(max = 30)
    @Column(name = "telefono", length = 30)
    private String telefono;

    @Size(max = 255)
    @Column(name = "direccion_habitual")
    private String direccionHabitual;

    @Size(max = 255)
    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Size(max = 150)
    @Column(name = "email", length = 150)
    private String email;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cliente")
    private List<Venta> ventas = new ArrayList<>();

}