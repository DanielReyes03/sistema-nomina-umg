DROP DATABASE IF EXISTS db_nomina;
CREATE DATABASE IF NOT EXISTS db_nomina;
USE db_nomina;

CREATE TABLE `departamentos` (
 `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
 `nombre` VARCHAR(255) UNIQUE NOT NULL,
 `descripcion` VARCHAR(255)
);

CREATE TABLE `puestos` (
   `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
   `nombre` VARCHAR(255) NOT NULL,
   `descripcion` VARCHAR(255),
   `departamento_id` INTEGER,
   FOREIGN KEY (`departamento_id`) REFERENCES `departamentos` (`id`)
);

-- Tabla de empleados (modificada)
CREATE TABLE `empleados` (
     `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
     `nombre` VARCHAR(255),
     `apellido` VARCHAR(255),
     `dpi` VARCHAR(255) UNIQUE,
     `fecha_ingreso` DATE,
     `salario` REAL,
     `puesto_id` INTEGER,
     `estado` BOOLEAN,
     FOREIGN KEY (`puesto_id`) REFERENCES `puestos` (`id`)
);

CREATE TABLE `usuarios` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `username` varchar(255) UNIQUE NOT NULL,
    `password_hash` varchar(255) NOT NULL,
    `rol` varchar(255) NOT NULL,
    `empleado_id` integer,
    `estado` boolean
);

-- Tabla de anticipos
CREATE TABLE `anticipos` (
     `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
     `empleado_id` INTEGER,
     `monto` DECIMAL(10,2),
     `fecha` DATE,
     `motivo` VARCHAR(255),
     `saldo_pendiente` DECIMAL(10,2),
     FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`)
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

CREATE TABLE `nomina` (
    `id` integer PRIMARY KEY AUTO_INCREMENT,
    `periodo_inicio` date,
    `periodo_fin` date,
    `fecha_generacion` date
);

CREATE TABLE `nomina_detalle` (
    `id`             integer PRIMARY KEY AUTO_INCREMENT,
    `nomina_id`      integer,
    `empleado_id`    integer,
    `ausencias`     integer,
    `laborados`      integer,
    `sueldo_base`    decimal(10,2),
    `horas_extra`    decimal(10,2),
    `bonificaciones` decimal(10,2),
    `IGGS`    decimal(10,2),
    `ISR`     decimal(10,2),
    `anticipos` decimal(10,2),
    `prestamos` decimal(10,2),
    `otros_descuentos` decimal(10,2),
    `sueldo_liquido`     decimal(10,2),
    FOREIGN KEY (`nomina_id`) REFERENCES `nomina` (`id`),
    FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`)
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