package mundo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import javax.swing.JTable;

public class Reportes {

    public static final String[] NOMBRES = {
        "Estado de las habitaciones",
        "Reservas activas",
        "Huéspedes actuales",
        "Ventas de hoy",
        "Todas las ventas",
        "Resumen por cliente"
    };

    public static JTable tabla(int tipo) {
        if (tipo == 0) {
            Object[][] d = new Object[OperacionesHabitacion.lh.size()][6];
            for (int i = 0; i < d.length; i++) {
                Habitacion h = (Habitacion) OperacionesHabitacion.lh.get(i);
                d[i][0] = h.getNumero();
                d[i][1] = h.getTipoHabitacion();
                d[i][2] = h.getEstado();
                d[i][3] = Tablas.dinero(h.getPrecioNoche());
                d[i][4] = h.getCapacidad();
                d[i][5] = h.getIdCliente() == 0 ? "-" : OperacionesCliente.nombreCompleto(h.getIdCliente());
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Número", "Tipo", "Estado", "Precio por noche", "Capacidad", "Cliente"}, d));
        }
        if (tipo == 1) {
            LinkedList<Reserva> lista = new LinkedList();
            for (int i = 0; i < OperacionesReserva.lr.size(); i++) {
                Reserva r = (Reserva) OperacionesReserva.lr.get(i);
                if (r.getEstado().equals("Activa")) {
                    lista.add(r);
                }
            }
            Object[][] d = new Object[lista.size()][6];
            for (int i = 0; i < d.length; i++) {
                Reserva r = lista.get(i);
                d[i][0] = r.getIdReserva();
                d[i][1] = OperacionesCliente.nombreCompleto(r.getIdCliente());
                d[i][2] = OperacionesHabitacion.numero(r.getIdHabitacion());
                d[i][3] = Tablas.fecha(r.getFechaEntrada());
                d[i][4] = Tablas.fecha(r.getFechaSalida());
                d[i][5] = Hotel.noches(r.getFechaEntrada(), r.getFechaSalida());
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Id", "Cliente", "Habitación", "Entrada", "Salida", "Noches"}, d));
        }
        if (tipo == 2) {
            Object[][] d = new Object[OperacionesCheckin.lck.size()][7];
            for (int i = 0; i < d.length; i++) {
                Checkin c = (Checkin) OperacionesCheckin.lck.get(i);
                d[i][0] = c.getIdCheckin();
                d[i][1] = OperacionesCliente.nombreCompleto(c.getIdCliente());
                d[i][2] = OperacionesHabitacion.numero(c.getIdHabitacion());
                d[i][3] = Tablas.fecha(c.getFechaIngreso());
                d[i][4] = Tablas.fecha(c.getFechaSalida());
                d[i][5] = c.getNoches();
                d[i][6] = c.getAcompañantes();
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Id", "Cliente", "Habitación", "Ingreso", "Salida", "Noches", "Acompañantes"}, d));
        }
        if (tipo == 3 || tipo == 4) {
            LinkedList<Venta> lista = new LinkedList();
            for (int i = 0; i < OperacionesVenta.lv.size(); i++) {
                Venta v = (Venta) OperacionesVenta.lv.get(i);
                if (tipo == 4 || v.getFecha().toLocalDate().equals(LocalDate.now())) {
                    lista.add(v);
                }
            }
            Object[][] d = new Object[lista.size()][5];
            for (int i = 0; i < d.length; i++) {
                Venta v = lista.get(i);
                d[i][0] = v.getIdVenta();
                d[i][1] = Tablas.fecha(v.getFecha());
                d[i][2] = OperacionesCliente.nombreCompleto(v.getIdCliente());
                d[i][3] = v.getTipoPago();
                d[i][4] = Tablas.dinero(v.getTotal());
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Id", "Fecha", "Cliente", "Tipo de pago", "Total"}, d));
        }
        Object[][] d = new Object[OperacionesCliente.lc.size()][5];
        for (int i = 0; i < d.length; i++) {
            Cliente c = (Cliente) OperacionesCliente.lc.get(i);
            int reservas = 0;
            int checkins = 0;
            BigDecimal gastado = BigDecimal.ZERO;
            for (int j = 0; j < OperacionesReserva.lr.size(); j++) {
                Reserva r = (Reserva) OperacionesReserva.lr.get(j);
                if (r.getIdCliente() == c.getId() && r.getEstado().equals("Activa")) {
                    reservas++;
                }
            }
            for (int j = 0; j < OperacionesCheckin.lck.size(); j++) {
                Checkin k = (Checkin) OperacionesCheckin.lck.get(j);
                if (k.getIdCliente() == c.getId()) {
                    checkins++;
                }
            }
            for (int j = 0; j < OperacionesVenta.lv.size(); j++) {
                Venta v = (Venta) OperacionesVenta.lv.get(j);
                if (v.getIdCliente() == c.getId()) {
                    gastado = gastado.add(v.getTotal());
                }
            }
            d[i][0] = c.getNombre() + " " + c.getApellido();
            d[i][1] = reservas;
            d[i][2] = checkins;
            d[i][3] = Tablas.dinero(gastado);
            d[i][4] = c.getCorreo();
        }
        return Tablas.tabla(Tablas.modelo(new String[]{"Cliente", "Reservas activas", "Check-in", "Total comprado", "Correo"}, d));
    }

    public static String resumen(int tipo) {
        if (tipo == 0) {
            int dis = 0, res = 0, ocu = 0, man = 0;
            for (int i = 0; i < OperacionesHabitacion.lh.size(); i++) {
                String e = ((Habitacion) OperacionesHabitacion.lh.get(i)).getEstado();
                if (e.equals("Disponible")) {
                    dis++;
                } else if (e.equals("Reservada")) {
                    res++;
                } else if (e.equals("Ocupada")) {
                    ocu++;
                } else {
                    man++;
                }
            }
            return "Total: " + OperacionesHabitacion.lh.size() + "   |   Disponibles: " + dis + "   |   Reservadas: " + res
                    + "   |   Ocupadas: " + ocu + "   |   En mantenimiento: " + man;
        }
        if (tipo == 1) {
            return "Reservas activas: " + tabla(1).getRowCount();
        }
        if (tipo == 2) {
            int personas = 0;
            for (int i = 0; i < OperacionesCheckin.lck.size(); i++) {
                Checkin c = (Checkin) OperacionesCheckin.lck.get(i);
                personas += 1 + c.getAcompañantes();
            }
            return "Check-in activos: " + OperacionesCheckin.lck.size() + "   |   Personas alojadas: " + personas;
        }
        if (tipo == 3 || tipo == 4) {
            int n = 0;
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal iva = BigDecimal.ZERO;
            for (int i = 0; i < OperacionesVenta.lv.size(); i++) {
                Venta v = (Venta) OperacionesVenta.lv.get(i);
                if (tipo == 4 || v.getFecha().toLocalDate().equals(LocalDate.now())) {
                    n++;
                    total = total.add(v.getTotal());
                    iva = iva.add(v.getIva5()).add(v.getIva19());
                }
            }
            return "Ventas: " + n + "   |   Total vendido: " + Tablas.dinero(total) + "   |   IVA: " + Tablas.dinero(iva);
        }
        return "Clientes: " + OperacionesCliente.lc.size();
    }
}
