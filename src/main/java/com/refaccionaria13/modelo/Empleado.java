package com.refaccionaria13.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "paterno")
    private String paterno;

    @Column(name = "materno")
    private String materno;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "contrasena")
    private String contrasena; // Recuerda que evitamos la 'ñ'

    @Column(name = "estatus")
    private Boolean estatus;

    @Column(name = "rol_empleado")
    private Integer rolEmpleado; 

    // El tipo 'date' de PostgreSQL se traduce a LocalDate
    @Column(name = "fecha_contrato")
    private LocalDate fechaContrato;

    // -- GET & SET

    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPaterno() { return paterno; }
    public void setPaterno(String paterno) { this.paterno = paterno; }

    public String getMaterno() { return materno; }
    public void setMaterno(String materno) { this.materno = materno; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Boolean getEstatus() { return estatus; }
    public void setEstatus(Boolean estatus) { this.estatus = estatus; }

    public Integer getRolEmpleado() { return rolEmpleado; }
    public void setRolEmpleado(Integer rolEmpleado) { this.rolEmpleado = rolEmpleado; }

    public LocalDate getFechaContrato() { return fechaContrato; }
    public void setFechaContrato(LocalDate fechaContrato) { this.fechaContrato = fechaContrato; }
}
