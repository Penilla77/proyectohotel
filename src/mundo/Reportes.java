package mundo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import javax.swing.JTable;

public class Reportes {

    public static final String[] NOMBRES = {
        "Estado de las habitaciones",
        "Reservas activas",
        "Huéspedes actuales",
        "Ventas de hoy",
        "Todas las ventas",
        "Resumen por cliente",
        "Cuentas abiertas",
        "Historial de facturas"
    };

    public static JTable tabla(int tipo) throws Exception {
        if (tipo == 0) {
            LinkedList<Object[]> lh = ConexionBD.filas("SELECT h.numero, h.tipoHabitacion, h.estado, h.precioNoche, h.capacidad, "
                    + "CASE WHEN h.idCliente IS NULL THEN '-' ELSE COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)') END "
                    + "FROM habitacion h LEFT JOIN cliente c ON c.id = h.idCliente ORDER BY h.id");
            Object[][] d = new Object[lh.size()][6];
            for (int i = 0; i < d.length; i++) {
                Object[] h = lh.get(i);
                d[i][0] = h[0];
                d[i][1] = h[1];
                d[i][2] = h[2];
                d[i][3] = Tablas.dinero((BigDecimal) h[3]);
                d[i][4] = h[4];
                d[i][5] = h[5];
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Número", "Tipo", "Estado", "Precio por noche", "Capacidad", "Cliente"}, d));
        }
        if (tipo == 1) {
            LinkedList<Object[]> lista = ConexionBD.filas("SELECT r.idReserva, COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)'), "
                    + "COALESCE(h.numero, '(eliminada)'), r.fechaEntrada, r.fechaSalida "
                    + "FROM reserva r LEFT JOIN cliente c ON c.id = r.idCliente LEFT JOIN habitacion h ON h.id = r.idHabitacion "
                    + "WHERE r.estado IN ('PENDIENTE', 'CONFIRMADA') ORDER BY r.idReserva");
            Object[][] d = new Object[lista.size()][6];
            for (int i = 0; i < d.length; i++) {
                Object[] r = lista.get(i);
                LocalDateTime entrada = (LocalDateTime) r[3];
                LocalDateTime salida = (LocalDateTime) r[4];
                d[i][0] = r[0];
                d[i][1] = r[1];
                d[i][2] = r[2];
                d[i][3] = Tablas.fecha(entrada);
                d[i][4] = Tablas.fecha(salida);
                d[i][5] = Hotel.noches(entrada, salida);
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Id", "Cliente", "Habitación", "Entrada", "Salida", "Noches"}, d));
        }
        if (tipo == 2) {
            LinkedList<Object[]> lck = ConexionBD.filas("SELECT k.idCheckin, COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)'), "
                    + "COALESCE(h.numero, '(eliminada)'), k.fechaIngreso, k.fechaSalida, k.noches, k.acompañantes "
                    + "FROM checkin k LEFT JOIN cliente c ON c.id = k.idCliente LEFT JOIN habitacion h ON h.id = k.idHabitacion "
                    + "ORDER BY k.idCheckin");
            Object[][] d = new Object[lck.size()][7];
            for (int i = 0; i < d.length; i++) {
                Object[] c = lck.get(i);
                d[i][0] = c[0];
                d[i][1] = c[1];
                d[i][2] = c[2];
                d[i][3] = Tablas.fecha((LocalDateTime) c[3]);
                d[i][4] = Tablas.fecha((LocalDateTime) c[4]);
                d[i][5] = c[5];
                d[i][6] = c[6];
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Id", "Cliente", "Habitación", "Ingreso", "Salida", "Noches", "Acompañantes"}, d));
        }
        if (tipo == 3 || tipo == 4) {
            LinkedList<Object[]> lv = ConexionBD.filas("SELECT v.idVenta, v.fecha, COALESCE(CONCAT(c.nombre, ' ', c.apellido), '(eliminado)'), v.tipoPago, v.total "
                    + "FROM venta v LEFT JOIN cliente c ON c.id = v.idCliente ORDER BY v.idVenta");
            LinkedList<Object[]> lista = new LinkedList();
            for (int i = 0; i < lv.size(); i++) {
                Object[] v = lv.get(i);
                if (tipo == 4 || ((LocalDateTime) v[1]).toLocalDate().equals(LocalDate.now())) {
                    lista.add(v);
                }
            }
            Object[][] d = new Object[lista.size()][5];
            for (int i = 0; i < d.length; i++) {
                Object[] v = lista.get(i);
                d[i][0] = v[0];
                d[i][1] = Tablas.fecha((LocalDateTime) v[1]);
                d[i][2] = v[2];
                d[i][3] = v[3];
                d[i][4] = Tablas.dinero((BigDecimal) v[4]);
            }
            return Tablas.tabla(Tablas.modelo(new String[]{"Id", "Fecha", "Cliente", "Tipo de pago", "Total"}, d));
        }
        if (tipo == 6) {
            return Tablas.tabla(OperacionesCuenta.modeloCuenta());
        }
        if (tipo == 7) {
            return Tablas.tabla(OperacionesFactura.modeloFactura());
        }
        LinkedList<Cliente> lc = OperacionesCliente.listar();
        LinkedList<Reserva> lr = OperacionesReserva.listar();
        LinkedList<Checkin> lck = OperacionesCheckin.listar();
        LinkedList<Venta> lv = OperacionesVenta.listar();
        Object[][] d = new Object[lc.size()][5];
        for (int i = 0; i < d.length; i++) {
            Cliente c = (Cliente) lc.get(i);
            int reservas = 0;
            int checkins = 0;
            BigDecimal gastado = BigDecimal.ZERO;
            for (int j = 0; j < lr.size(); j++) {
                Reserva r = (Reserva) lr.get(j);
                if (r.getIdCliente() == c.getId() && Hotel.vigente(r.getEstado())) {
                    reservas++;
                }
            }
            for (int j = 0; j < lck.size(); j++) {
                Checkin k = (Checkin) lck.get(j);
                if (k.getIdCliente() == c.getId()) {
                    checkins++;
                }
            }
            for (int j = 0; j < lv.size(); j++) {
                Venta v = (Venta) lv.get(j);
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

    public static String resumen(int tipo) throws Exception {
        if (tipo == 0) {
            LinkedList<Habitacion> lh = OperacionesHabitacion.listar();
            int dis = 0, res = 0, ocu = 0, man = 0;
            for (int i = 0; i < lh.size(); i++) {
                String e = ((Habitacion) lh.get(i)).getEstado();
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
            return "Total: " + lh.size() + "   |   Disponibles: " + dis + "   |   Reservadas: " + res
                    + "   |   Ocupadas: " + ocu + "   |   En mantenimiento: " + man;
        }
        if (tipo == 1) {
            return "Reservas activas: " + tabla(1).getRowCount();
        }
        if (tipo == 2) {
            LinkedList<Checkin> lck = OperacionesCheckin.listar();
            int personas = 0;
            for (int i = 0; i < lck.size(); i++) {
                Checkin c = (Checkin) lck.get(i);
                personas += 1 + c.getAcompañantes();
            }
            return "Check-in activos: " + lck.size() + "   |   Personas alojadas: " + personas;
        }
        if (tipo == 3 || tipo == 4) {
            LinkedList<Venta> lv = OperacionesVenta.listar();
            int n = 0;
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal iva = BigDecimal.ZERO;
            for (int i = 0; i < lv.size(); i++) {
                Venta v = (Venta) lv.get(i);
                if (tipo == 4 || v.getFecha().toLocalDate().equals(LocalDate.now())) {
                    n++;
                    total = total.add(v.getTotal());
                    iva = iva.add(v.getIva5()).add(v.getIva19());
                }
            }
            return "Ventas: " + n + "   |   Total vendido: " + Tablas.dinero(total) + "   |   IVA: " + Tablas.dinero(iva);
        }
        if (tipo == 6) {
            return "Cuentas abiertas: " + tabla(6).getRowCount();
        }
        if (tipo == 7) {
            LinkedList<Factura> lf = OperacionesFactura.listar();
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < lf.size(); i++) {
                Factura f = (Factura) lf.get(i);
                total = total.add(f.getTotal());
            }
            return "Facturas: " + lf.size() + "   |   Total facturado: " + Tablas.dinero(total);
        }
        return "Clientes: " + OperacionesCliente.listar().size();
    }

}
