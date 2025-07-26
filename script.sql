DROP DATABASE IF EXISTS db_nomina;
CREATE DATABASE IF NOT EXISTS db_nomina;
USE db_nomina;


CREATE TABLE `departamentos` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `nombre` varchar(255) UNIQUE NOT NULL,
    `descripcion` varchar(255)
);

CREATE TABLE `puestos` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `nombre` varchar(255) NOT NULL,
    `descripcion` varchar(255),
    `departamento_id` int
);

CREATE TABLE `empleados` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `nombre` varchar(255),
    `apellido` varchar(255),
    `dpi` varchar(255) UNIQUE,
    `fecha_ingreso` date,
    `salario` decimal(10,2),
    `puesto_id` int,
    `estado` boolean
);

CREATE TABLE `usuarios` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `username` varchar(255) UNIQUE NOT NULL,
    `password_hash` varchar(255) NOT NULL,
    `rol` varchar(255) NOT NULL,
    `empleado_id` int,
    `estado` boolean
);

CREATE TABLE `anticipos` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` int,
    `monto` decimal(10,2),
    `fecha` date,
    `motivo` varchar(255),
    `saldo_pendiente` decimal(10,2)
);

CREATE TABLE `asistencias` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` int,
    `fecha` date,
    `hora_entrada` time,
    `hora_salida` time
);

CREATE TABLE `horas_extra` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` int,
    `fecha` date,
    `horas` int,
    `motivo` varchar(255),
    `aprobado` boolean
);

CREATE TABLE `ausencias_permisos` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` int,
    `fecha` date,
    `tipo` varchar(255),
    `justificada` boolean,
    `motivo` varchar(255),
    `observacion` varchar(255)
);

CREATE TABLE `vacaciones` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` int,
    `fecha_inicio` date,
    `fecha_fin` date,
    `dias` int,
    `aprobada` boolean
);

CREATE TABLE `adelantos` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `empleado_id` int,
    `monto` decimal(10,2),
    `fecha` date,
    `saldo_pendiente` decimal(10,2)
);

CREATE TABLE `nomina` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `periodo_inicio` date,
    `periodo_fin` date,
    `fecha_generacion` date,
    `tipo` enum('Quincenal','Mensual')
);

CREATE TABLE `detalles_nomina` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `nomina_id` int,
    `empleado_id` int,
    `ausencias` int,
    `dias_laborados` int,
    `percepciones` decimal(10,2),
    `deducciones` decimal(10,2),
    `sueldo_liquido` decimal(10,2)
);

CREATE TABLE `conceptos_nomina` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `nombre` varchar(100) NOT NULL,
    `descripcion` text,
    `tipo` enum('PERCEPCION','DEDUCCION'),
    `tipo_calculo` enum('FIJO','PORCENTAJE','MULTIPLICACION','DIVISION') DEFAULT 'MULTIPLICACION',
    `valor` decimal(10,2),
    `aplica_automatico` boolean DEFAULT false
);

CREATE TABLE `movimientos_nomina` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `detalle_nomina_id` int,
    `concepto_id` int,
    `monto` decimal(10,2)
);

ALTER TABLE `puestos` ADD FOREIGN KEY (`departamento_id`) REFERENCES `departamentos` (`id`);
ALTER TABLE `empleados` ADD FOREIGN KEY (`puesto_id`) REFERENCES `puestos` (`id`);
ALTER TABLE `usuarios` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `anticipos` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `asistencias` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `horas_extra` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `ausencias_permisos` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `vacaciones` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `adelantos` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `detalles_nomina` ADD FOREIGN KEY (`nomina_id`) REFERENCES `nomina` (`id`);
ALTER TABLE `detalles_nomina` ADD FOREIGN KEY (`empleado_id`) REFERENCES `empleados` (`id`);
ALTER TABLE `movimientos_nomina` ADD FOREIGN KEY (`detalle_nomina_id`) REFERENCES `detalles_nomina` (`id`);
ALTER TABLE `movimientos_nomina` ADD FOREIGN KEY (`concepto_id`) REFERENCES `conceptos_nomina` (`id`);
