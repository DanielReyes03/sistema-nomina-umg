CREATE DATABASE IF NOT EXISTS db_nomina;
USE db_nomina;

CREATE TABLE `empleados` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `nombre` varchar(255),
    `apellido` varchar(255),
    `dpi` varchar(255) UNIQUE,
    `fecha_ingreso` date,
    `salario` real,
    `puesto` varchar(255),
    `departamento` varchar(255),
    `estado` boolean
);

CREATE TABLE `usuarios` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `username` varchar(255) UNIQUE NOT NULL,
    `password_hash` varchar(255) NOT NULL,
    `rol` varchar(255) NOT NULL,
    `empleado_id` integer,
    `estado` boolean
);

CREATE TABLE `asistencias` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` integer,
    `fecha` date,
    `hora_entrada` time,
    `hora_salida` time
);

CREATE TABLE `horas_extra` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` integer,
    `fecha` date,
    `horas` integer,
    `motivo` varchar(255),
    `aprobado` boolean
);

CREATE TABLE `ausencias_permisos` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` integer,
    `fecha` date,
    `tipo` enum('ausencia', 'permiso'),
    `justificada` boolean,
    `motivo` varchar(255),
    `observacion` varchar(255)
);

CREATE TABLE `vacaciones` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` integer,
    `fecha_inicio` date,
    `fecha_fin` date,
    `dias` integer,
    `aprobada` boolean
);

CREATE TABLE `prestamos` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` integer,
    `monto` decimal,
    `fecha` date,
    `saldo_pendiente` decimal
);

CREATE TABLE `nominas` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `periodo_inicio` date,
    `periodo_fin` date,
    `fecha_generacion` date,
    `tipo` varchar(255)
);

CREATE TABLE `recibos` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `nomina_id` integer,
    `empleado_id` integer,
    `sueldo_base` decimal,
    `horas_extra` decimal,
    `deducciones` decimal,
    `bonificaciones` decimal,
    `neto_pagar` decimal
);

ALTER TABLE `usuarios`
ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `asistencias`
ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `horas_extra`
ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `ausencias_permisos`
ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `vacaciones`
ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `prestamos`
ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);

ALTER TABLE `recibos`
ADD FOREIGN KEY (`nomina_id`) REFERENCES `nominas` (`id`);

ALTER TABLE `recibos`
ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);