package mundo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

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

        LinkedList<ProductoVendido> items = new LinkedList();
        items.add(linea(1, 2));
        items.add(linea(2, 1));
        venta(hoy.withHour(9), "Efectivo", 1, items);

        items = new LinkedList();
        items.add(linea(1, 4));
        items.add(linea(3, 2));
        venta(hoy.minusDays(1).withHour(18), "Tarjeta", 2, items);

        items = new LinkedList();
        items.add(linea(6, 6));
        items.add(linea(4, 2));
        venta(hoy.withHour(11), "Transferencia", 1, items);

        Hotel.actualizarTodas();
    }

    private static ProductoVendido linea(long idProducto, long cantidad) {
        Producto p = OperacionesProducto.buscar(idProducto);
        return new ProductoVendido(0, idProducto, 0, cantidad, p.getPrecioVenta());
    }

    private static void venta(LocalDateTime fecha, String pago, int idCliente, LinkedList<ProductoVendido> items) {
        BigDecimal[] r = Hotel.totalesVenta(items);
        long id = OperacionesVenta.nuevoId();
        OperacionesVenta.Guardar(new Venta(id, fecha, r[0], r[1], r[2], r[3], pago, idCliente));
        Hotel.guardarProductos(id, items);
    }
}
