-- Base de datos del sistema de hotel (MySQL / MariaDB)
-- Cada tabla y cada columna se llama igual que la clase y el atributo de src/mundo.
-- Ejecutar con:  mysql -uroot --default-character-set=utf8mb4 < sql/hotel.sql

DROP DATABASE IF EXISTS hotel;
CREATE DATABASE hotel CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel;

-- Cliente.java
CREATE TABLE cliente (
    id        INT          NOT NULL AUTO_INCREMENT,
    nombre    VARCHAR(60)  NOT NULL,
    apellido  VARCHAR(60)  NOT NULL,
    documento INT          NOT NULL,
    correo    VARCHAR(100) NOT NULL,
    telefono  VARCHAR(20)  NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    sexo      VARCHAR(15)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_cliente_documento (documento)
) ENGINE=InnoDB;

-- Habitacion.java  (idCliente es NULL cuando la habitacion no tiene cliente; en Java es 0)
CREATE TABLE habitacion (
    id             INT           NOT NULL AUTO_INCREMENT,
    numero         VARCHAR(10)   NOT NULL,
    estado         VARCHAR(20)   NOT NULL,
    tipoHabitacion VARCHAR(30)   NOT NULL,
    precioNoche    DECIMAL(12,2) NOT NULL,
    capacidad      INT           NOT NULL,
    idCliente      INT           NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_habitacion_numero (numero),
    CONSTRAINT fk_habitacion_cliente FOREIGN KEY (idCliente) REFERENCES cliente (id)
) ENGINE=InnoDB;

-- Reserva.java
CREATE TABLE reserva (
    idReserva    INT         NOT NULL AUTO_INCREMENT,
    idCliente    INT         NOT NULL,
    idHabitacion INT         NOT NULL,
    fechaEntrada DATETIME    NOT NULL,
    fechaSalida  DATETIME    NOT NULL,
    createdAt    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipoReserva  ENUM('AIRBNB', 'OTRO') NOT NULL,
    estado       ENUM('PENDIENTE', 'CONFIRMADA', 'CANCELADA', 'FINALIZADA') NOT NULL,
    PRIMARY KEY (idReserva),
    CONSTRAINT fk_reserva_cliente    FOREIGN KEY (idCliente)    REFERENCES cliente (id),
    CONSTRAINT fk_reserva_habitacion FOREIGN KEY (idHabitacion) REFERENCES habitacion (id)
) ENGINE=InnoDB;

-- Checkin.java
CREATE TABLE checkin (
    idCheckin    INT          NOT NULL AUTO_INCREMENT,
    fechaIngreso DATETIME     NOT NULL,
    fechaSalida  DATETIME     NOT NULL,
    noches       INT          NOT NULL,
    idHabitacion INT          NOT NULL,
    idCliente    INT          NOT NULL,
    transporte   VARCHAR(30)  NOT NULL,
    motivoViaje  VARCHAR(60)  NOT NULL,
    procedencia  VARCHAR(80)  NOT NULL,
    `acompañantes` INT        NOT NULL DEFAULT 0,
    PRIMARY KEY (idCheckin),
    CONSTRAINT fk_checkin_habitacion FOREIGN KEY (idHabitacion) REFERENCES habitacion (id),
    CONSTRAINT fk_checkin_cliente    FOREIGN KEY (idCliente)    REFERENCES cliente (id)
) ENGINE=InnoDB;

-- Categoria.java
CREATE TABLE categoria (
    codCategoria INT         NOT NULL AUTO_INCREMENT,
    detalle      VARCHAR(60) NOT NULL,
    PRIMARY KEY (codCategoria)
) ENGINE=InnoDB;

-- Producto.java  (idUsuario no tiene tabla en el proyecto, por eso no lleva llave foranea)
CREATE TABLE producto (
    idProducto   BIGINT        NOT NULL AUTO_INCREMENT,
    codigo       VARCHAR(20)   NOT NULL,
    descripcion  VARCHAR(100)  NOT NULL,
    precioVenta  DECIMAL(12,2) NOT NULL,
    precioCompra DECIMAL(12,2) NOT NULL,
    iva          INT           NOT NULL DEFAULT 0,
    existencia   INT           NOT NULL DEFAULT 0,
    idUsuario    BIGINT        NOT NULL DEFAULT 0,
    codCategoria INT           NOT NULL,
    vencimiento  DATE          NULL,
    PRIMARY KEY (idProducto),
    UNIQUE KEY uq_producto_codigo (codigo),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (codCategoria) REFERENCES categoria (codCategoria)
) ENGINE=InnoDB;

-- Venta.java
CREATE TABLE venta (
    idVenta   BIGINT        NOT NULL AUTO_INCREMENT,
    fecha     DATETIME      NOT NULL,
    total     DECIMAL(12,2) NOT NULL,
    iva5      DECIMAL(12,2) NOT NULL DEFAULT 0,
    iva19     DECIMAL(12,2) NOT NULL DEFAULT 0,
    exento    DECIMAL(12,2) NOT NULL DEFAULT 0,
    tipoPago  VARCHAR(20)   NOT NULL,
    idCliente INT           NOT NULL,
    PRIMARY KEY (idVenta),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (idCliente) REFERENCES cliente (id)
) ENGINE=InnoDB;

-- ProductoVendido.java
CREATE TABLE productovendido (
    idProductoV    BIGINT        NOT NULL AUTO_INCREMENT,
    idProducto     BIGINT        NOT NULL,
    idVenta        BIGINT        NOT NULL,
    cantidad       BIGINT        NOT NULL,
    precioUnitario DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (idProductoV),
    CONSTRAINT fk_pv_producto FOREIGN KEY (idProducto) REFERENCES producto (idProducto),
    CONSTRAINT fk_pv_venta    FOREIGN KEY (idVenta)    REFERENCES venta (idVenta)
) ENGINE=InnoDB;

-- Cuenta.java: se abre sola al hacer check-in y se cierra con el checkout (que genera la venta)
CREATE TABLE cuenta (
    idCuenta      INT         NOT NULL AUTO_INCREMENT,
    idCheckin     INT         NULL,
    idCliente     INT         NOT NULL,
    idHabitacion  INT         NOT NULL,
    fechaApertura DATETIME    NOT NULL,
    estado        VARCHAR(10) NOT NULL DEFAULT 'Abierta',
    idVenta       BIGINT      NULL,
    PRIMARY KEY (idCuenta),
    CONSTRAINT fk_cuenta_checkin    FOREIGN KEY (idCheckin)    REFERENCES checkin (idCheckin) ON DELETE SET NULL,
    CONSTRAINT fk_cuenta_cliente    FOREIGN KEY (idCliente)    REFERENCES cliente (id),
    CONSTRAINT fk_cuenta_habitacion FOREIGN KEY (idHabitacion) REFERENCES habitacion (id),
    CONSTRAINT fk_cuenta_venta      FOREIGN KEY (idVenta)      REFERENCES venta (idVenta) ON DELETE SET NULL
) ENGINE=InnoDB;

-- CuentaProducto.java: consumos que el huesped va cargando a su cuenta
CREATE TABLE cuentaproducto (
    idCuentaProducto BIGINT        NOT NULL AUTO_INCREMENT,
    idCuenta         INT           NOT NULL,
    idProducto       BIGINT        NOT NULL,
    cantidad         BIGINT        NOT NULL,
    precioUnitario   DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (idCuentaProducto),
    CONSTRAINT fk_cp_cuenta   FOREIGN KEY (idCuenta)   REFERENCES cuenta (idCuenta),
    CONSTRAINT fk_cp_producto FOREIGN KEY (idProducto) REFERENCES producto (idProducto)
) ENGINE=InnoDB;

-- Factura.java: historico de facturas. Guarda una copia del texto tal como se emitio,
-- por eso no cambia aunque despues cambien precios, nombres o se borre la venta.
CREATE TABLE factura (
    idFactura BIGINT        NOT NULL AUTO_INCREMENT,
    idVenta   BIGINT        NULL,
    fecha     DATETIME      NOT NULL,
    cliente   VARCHAR(130)  NOT NULL,
    total     DECIMAL(12,2) NOT NULL,
    detalle   TEXT          NOT NULL,
    PRIMARY KEY (idFactura),
    CONSTRAINT fk_factura_venta FOREIGN KEY (idVenta) REFERENCES venta (idVenta) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Datos iniciales (antes estaban en DatosIniciales.java). Las fechas se calculan a partir del dia en que se ejecuta el script.
SET @hoy = DATE_FORMAT(NOW(), '%Y-%m-%d %H:00:00');

INSERT INTO cliente (id, nombre, apellido, documento, correo, telefono, direccion, sexo) VALUES
(1, 'Laura',  'Martínez',  52345678, 'laura.martinez@correo.com',  '3001234567', 'Calle 10 #5-20, Cúcuta',     'Femenino'),
(2, 'Carlos', 'Rojas',     88123456, 'carlos.rojas@correo.com',    '3109876543', 'Av. 4 #12-30, Cúcuta',       'Masculino'),
(3, 'Andrea', 'Gómez',     60234891, 'andrea.gomez@correo.com',    '3157654321', 'Carrera 7 #8-15, Pamplona',  'Femenino'),
(4, 'Julián', 'Pabón',     13456789, 'julian.pabon@correo.com',    '3201112233', 'Calle 3 #2-10, Ocaña',       'Masculino'),
(5, 'Sofía',  'Contreras', 37890123, 'sofia.contreras@correo.com', '3014445566', 'Av. 0 #20-45, Cúcuta',       'Femenino');

INSERT INTO habitacion (id, numero, estado, tipoHabitacion, precioNoche, capacidad, idCliente) VALUES
(1, '101', 'Ocupada',       'Sencilla', 80000,  1, 1),
(2, '102', 'Disponible',    'Sencilla', 80000,  1, NULL),
(3, '103', 'Reservada',     'Doble',    120000, 2, 2),
(4, '104', 'Mantenimiento', 'Doble',    120000, 2, NULL),
(5, '201', 'Disponible',    'Doble',    130000, 3, NULL),
(6, '202', 'Reservada',     'Familiar', 180000, 4, 3),
(7, '203', 'Disponible',    'Suite',    250000, 2, NULL),
(8, '301', 'Disponible',    'Suite',    300000, 4, NULL);

INSERT INTO reserva (idReserva, idCliente, idHabitacion, fechaEntrada, fechaSalida, createdAt, tipoReserva, estado) VALUES
(1, 2, 3, TIMESTAMP(DATE(@hoy + INTERVAL 2 DAY), '14:00:00'), TIMESTAMP(DATE(@hoy + INTERVAL 4 DAY), '12:00:00'), @hoy - INTERVAL 3 DAY,  'AIRBNB', 'CONFIRMADA'),
(2, 3, 6, TIMESTAMP(DATE(@hoy + INTERVAL 5 DAY), '14:00:00'), TIMESTAMP(DATE(@hoy + INTERVAL 8 DAY), '12:00:00'), @hoy - INTERVAL 1 DAY,  'OTRO',   'PENDIENTE'),
(3, 5, 5, TIMESTAMP(DATE(@hoy - INTERVAL 6 DAY), '14:00:00'), TIMESTAMP(DATE(@hoy - INTERVAL 3 DAY), '12:00:00'), @hoy - INTERVAL 10 DAY, 'OTRO',   'FINALIZADA');

INSERT INTO checkin (idCheckin, fechaIngreso, fechaSalida, noches, idHabitacion, idCliente, transporte, motivoViaje, procedencia, `acompañantes`) VALUES
(1, TIMESTAMP(DATE(@hoy - INTERVAL 1 DAY), '15:00:00'), TIMESTAMP(DATE(@hoy + INTERVAL 2 DAY), '12:00:00'), 3, 1, 1, 'Bus', 'Turismo', 'Bogotá', 0);

INSERT INTO cuenta (idCuenta, idCheckin, idCliente, idHabitacion, fechaApertura, estado, idVenta) VALUES
(1, 1, 1, 1, TIMESTAMP(DATE(@hoy - INTERVAL 1 DAY), '15:00:00'), 'Abierta', NULL);

INSERT INTO categoria (codCategoria, detalle) VALUES
(1, 'Bebidas'), (2, 'Snacks'), (3, 'Aseo'), (4, 'Licores'), (5, 'Habitación');

-- existencia ya descontada por las 3 ventas de ejemplo
INSERT INTO producto (idProducto, codigo, descripcion, precioVenta, precioCompra, iva, existencia, idUsuario, codCategoria, vencimiento) VALUES
(1, 'P001', 'Agua 600 ml',      2500, 1200,  0, 74, 0, 1, CURDATE() + INTERVAL 300 DAY),
(2, 'P002', 'Gaseosa 400 ml',   3500, 1800, 19, 59, 0, 1, CURDATE() + INTERVAL 180 DAY),
(3, 'P003', 'Papas fritas',     3000, 1500, 19, 43, 0, 2, CURDATE() + INTERVAL 120 DAY),
(4, 'P004', 'Chocolatina',      2200, 1000, 19, 48, 0, 2, CURDATE() + INTERVAL 200 DAY),
(5, 'P005', 'Jabón de baño',    2000,  900, 19, 30, 0, 3, CURDATE() + INTERVAL 700 DAY),
(6, 'P006', 'Cerveza nacional', 5000, 2200,  5, 34, 0, 4, CURDATE() + INTERVAL 240 DAY);

-- Producto de la categoria Habitacion: la venta lo usa para cobrar el alojamiento (cantidad = noches, precio = precioNoche).
-- El IVA (19) es un supuesto: se cambia desde el formulario de Productos. No lleva inventario.
INSERT INTO producto (idProducto, codigo, descripcion, precioVenta, precioCompra, iva, existencia, idUsuario, codCategoria, vencimiento) VALUES
(7, 'HAB', 'Habitación (noches)', 0, 0, 19, 0, 0, 5, '2099-12-31');

INSERT INTO venta (idVenta, fecha, total, iva5, iva19, exento, tipoPago, idCliente) VALUES
(1, TIMESTAMP(DATE(@hoy), '09:00:00'),                       9165,     0,  665,  5000, 'Efectivo',      1),
(2, TIMESTAMP(DATE(@hoy - INTERVAL 1 DAY), '18:00:00'),     17140,     0, 1140, 10000, 'Tarjeta',       2),
(3, TIMESTAMP(DATE(@hoy), '11:00:00'),                      36736,  1500,  836,     0, 'Transferencia', 1);

INSERT INTO productovendido (idProductoV, idProducto, idVenta, cantidad, precioUnitario) VALUES
(1, 1, 1, 2, 2500),
(2, 2, 1, 1, 3500),
(3, 1, 2, 4, 2500),
(4, 3, 2, 2, 3000),
(5, 6, 3, 6, 5000),
(6, 4, 3, 2, 2200);