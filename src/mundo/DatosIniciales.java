package mundo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DatosIniciales {

    private static boolean cargado = false;

    public static void cargar() {
        if (cargado) {
            return;
        }
        cargado = true;

        LocalDateTime hoy = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);

        OperacionesCliente.Guardar(new Cliente(OperacionesCliente.nuevoId(), "Laura", "Martínez", 52345678, "laura.martinez@correo.com", "3001234567", "Calle 10 #5-20, Cúcuta", "Femenino"));
        OperacionesCliente.Guardar(new Cliente(OperacionesCliente.nuevoId(), "Carlos", "Rojas", 88123456, "carlos.rojas@correo.com", "3109876543", "Av. 4 #12-30, Cúcuta", "Masculino"));
        OperacionesCliente.Guardar(new Cliente(OperacionesCliente.nuevoId(), "Andrea", "Gómez", 60234891, "andrea.gomez@correo.com", "3157654321", "Carrera 7 #8-15, Pamplona", "Femenino"));
        OperacionesCliente.Guardar(new Cliente(OperacionesCliente.nuevoId(), "Julián", "Pabón", 13456789, "julian.pabon@correo.com", "3201112233", "Calle 3 #2-10, Ocaña", "Masculino"));
        OperacionesCliente.Guardar(new Cliente(OperacionesCliente.nuevoId(), "Sofía", "Contreras", 37890123, "sofia.contreras@correo.com", "3014445566", "Av. 0 #20-45, Cúcuta", "Femenino"));

        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "101", "Disponible", "Sencilla", new BigDecimal("80000"), 1, 0));
        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "102", "Disponible", "Sencilla", new BigDecimal("80000"), 1, 0));
        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "103", "Disponible", "Doble", new BigDecimal("120000"), 2, 0));
        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "104", "Mantenimiento", "Doble", new BigDecimal("120000"), 2, 0));
        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "201", "Disponible", "Doble", new BigDecimal("130000"), 3, 0));
        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "202", "Disponible", "Familiar", new BigDecimal("180000"), 4, 0));
        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "203", "Disponible", "Suite", new BigDecimal("250000"), 2, 0));
        OperacionesHabitacion.Guardar(new Habitacion(OperacionesHabitacion.nuevoId(), "301", "Disponible", "Suite", new BigDecimal("300000"), 4, 0));

        OperacionesReserva.Guardar(new Reserva(OperacionesReserva.nuevoId(), 2, 3,
                hoy.plusDays(2).withHour(14), hoy.plusDays(4).withHour(12), hoy.minusDays(3), "Web", "Activa"));
        OperacionesReserva.Guardar(new Reserva(OperacionesReserva.nuevoId(), 3, 6,
                hoy.plusDays(5).withHour(14), hoy.plusDays(8).withHour(12), hoy.minusDays(1), "Telefónica", "Activa"));
        OperacionesReserva.Guardar(new Reserva(OperacionesReserva.nuevoId(), 5, 5,
                hoy.minusDays(6).withHour(14), hoy.minusDays(3).withHour(12), hoy.minusDays(10), "Presencial", "Completada"));

        OperacionesCheckin.Guardar(new Checkin(OperacionesCheckin.nuevoId(),
                hoy.minusDays(1).withHour(15), hoy.plusDays(2).withHour(12), 3, 1, 1,
                "Bus", "Turismo", "Bogotá", 0));

        OperacionesCategoria.Guardar(new Categoria(OperacionesCategoria.nuevoCod(), "Bebidas"));
        OperacionesCategoria.Guardar(new Categoria(OperacionesCategoria.nuevoCod(), "Snacks"));
        OperacionesCategoria.Guardar(new Categoria(OperacionesCategoria.nuevoCod(), "Aseo"));
        OperacionesCategoria.Guardar(new Categoria(OperacionesCategoria.nuevoCod(), "Licores"));

        LocalDate dia = LocalDate.now();
        OperacionesProducto.Guardar(new Producto(OperacionesProducto.nuevoId(), "P001", "Agua 600 ml", new BigDecimal("2500"), new BigDecimal("1200"), 0, 80, 0, 1, dia.plusDays(300)));
        OperacionesProducto.Guardar(new Producto(OperacionesProducto.nuevoId(), "P002", "Gaseosa 400 ml", new BigDecimal("3500"), new BigDecimal("1800"), 19, 60, 0, 1, dia.plusDays(180)));
        OperacionesProducto.Guardar(new Producto(OperacionesProducto.nuevoId(), "P003", "Papas fritas", new BigDecimal("3000"), new BigDecimal("1500"), 19, 45, 0, 2, dia.plusDays(120)));
        OperacionesProducto.Guardar(new Producto(OperacionesProducto.nuevoId(), "P004", "Chocolatina", new BigDecimal("2200"), new BigDecimal("1000"), 19, 50, 0, 2, dia.plusDays(200)));
        OperacionesProducto.Guardar(new Producto(OperacionesProducto.nuevoId(), "P005", "Jabón de baño", new BigDecimal("2000"), new BigDecimal("900"), 19, 30, 0, 3, dia.plusDays(700)));
        OperacionesProducto.Guardar(new Producto(OperacionesProducto.nuevoId(), "P006", "Cerveza nacional", new BigDecimal("5000"), new BigDecimal("2200"), 5, 40, 0, 4, dia.plusDays(240)));

        BigDecimal[] v1 = OperacionesVenta.calcular(new BigDecimal("60000"), "19%");
        OperacionesVenta.Guardar(new Venta(OperacionesVenta.nuevoId(), hoy.withHour(9), v1[0], v1[1], v1[2], v1[3], "Efectivo", 1));
        BigDecimal[] v2 = OperacionesVenta.calcular(new BigDecimal("150000"), "Exento");
        OperacionesVenta.Guardar(new Venta(OperacionesVenta.nuevoId(), hoy.minusDays(1).withHour(18), v2[0], v2[1], v2[2], v2[3], "Tarjeta", 2));
        BigDecimal[] v3 = OperacionesVenta.calcular(new BigDecimal("45000"), "5%");
        OperacionesVenta.Guardar(new Venta(OperacionesVenta.nuevoId(), hoy.withHour(11), v3[0], v3[1], v3[2], v3[3], "Transferencia", 1));

        Hotel.actualizarTodas();
    }
}
