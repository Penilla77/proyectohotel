package mundo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Hotel {

    public static boolean cruce(LocalDateTime ent1, LocalDateTime sal1, LocalDateTime ent2, LocalDateTime sal2) {
        return ent1.isBefore(sal2) && ent2.isBefore(sal1);
    }

    public static boolean habitacionLibre(int idHab, LocalDateTime ent, LocalDateTime sal, int ignorarReserva, int ignorarCheckin) {
        for (int i = 0; i < OperacionesReserva.lr.size(); i++) {
            Reserva r = (Reserva) OperacionesReserva.lr.get(i);
            if (r.getIdHabitacion() == idHab && r.getEstado().equals("Activa")
                    && r.getIdReserva() != ignorarReserva
                    && cruce(ent, sal, r.getFechaEntrada(), r.getFechaSalida())) {
                return false;
            }
        }
        for (int i = 0; i < OperacionesCheckin.lck.size(); i++) {
            Checkin c = (Checkin) OperacionesCheckin.lck.get(i);
            if (c.getIdHabitacion() == idHab && c.getIdCheckin() != ignorarCheckin
                    && cruce(ent, sal, c.getFechaIngreso(), c.getFechaSalida())) {
                return false;
            }
        }
        return true;
    }

    public static void actualizarHabitacion(int idHab) {
        Habitacion h = OperacionesHabitacion.buscar(idHab);
        if (h == null) {
            return;
        }
        if (h.getEstado().equals("Mantenimiento")) {
            h.setIdCliente(0);
            return;
        }
        for (int i = 0; i < OperacionesCheckin.lck.size(); i++) {
            Checkin c = (Checkin) OperacionesCheckin.lck.get(i);
            if (c.getIdHabitacion() == idHab) {
                h.setEstado("Ocupada");
                h.setIdCliente(c.getIdCliente());
                return;
            }
        }
        Reserva proxima = null;
        for (int i = 0; i < OperacionesReserva.lr.size(); i++) {
            Reserva r = (Reserva) OperacionesReserva.lr.get(i);
            if (r.getIdHabitacion() == idHab && r.getEstado().equals("Activa")) {
                if (proxima == null || r.getFechaEntrada().isBefore(proxima.getFechaEntrada())) {
                    proxima = r;
                }
            }
        }
        if (proxima != null) {
            h.setEstado("Reservada");
            h.setIdCliente(proxima.getIdCliente());
            return;
        }
        h.setEstado("Disponible");
        h.setIdCliente(0);
    }

    public static void actualizarTodas() {
        for (int i = 0; i < OperacionesHabitacion.lh.size(); i++) {
            Habitacion h = (Habitacion) OperacionesHabitacion.lh.get(i);
            actualizarHabitacion(h.getId());
        }
    }

    public static boolean clienteTieneRegistros(int idCliente) {
        for (int i = 0; i < OperacionesReserva.lr.size(); i++) {
            Reserva r = (Reserva) OperacionesReserva.lr.get(i);
            if (r.getIdCliente() == idCliente && r.getEstado().equals("Activa")) {
                return true;
            }
        }
        for (int i = 0; i < OperacionesCheckin.lck.size(); i++) {
            Checkin c = (Checkin) OperacionesCheckin.lck.get(i);
            if (c.getIdCliente() == idCliente) {
                return true;
            }
        }
        for (int i = 0; i < OperacionesVenta.lv.size(); i++) {
            Venta v = (Venta) OperacionesVenta.lv.get(i);
            if (v.getIdCliente() == idCliente) {
                return true;
            }
        }
        return false;
    }

    public static boolean habitacionTieneRegistros(int idHab) {
        for (int i = 0; i < OperacionesReserva.lr.size(); i++) {
            Reserva r = (Reserva) OperacionesReserva.lr.get(i);
            if (r.getIdHabitacion() == idHab && r.getEstado().equals("Activa")) {
                return true;
            }
        }
        for (int i = 0; i < OperacionesCheckin.lck.size(); i++) {
            Checkin c = (Checkin) OperacionesCheckin.lck.get(i);
            if (c.getIdHabitacion() == idHab) {
                return true;
            }
        }
        return false;
    }

    public static int noches(LocalDateTime ingreso, LocalDateTime salida) {
        long n = salida.toLocalDate().toEpochDay() - ingreso.toLocalDate().toEpochDay();
        if (n < 1) {
            return 1;
        }
        return (int) n;
    }

    public static BigDecimal[] totalesVenta(LinkedList<ProductoVendido> items) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal iva5 = BigDecimal.ZERO;
        BigDecimal iva19 = BigDecimal.ZERO;
        BigDecimal exento = BigDecimal.ZERO;
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            Producto p = OperacionesProducto.buscar(pv.getIdProducto());
            String iva = "Exento";
            if (p.getIva() == 5) {
                iva = "5%";
            }
            if (p.getIva() == 19) {
                iva = "19%";
            }
            BigDecimal base = pv.getPrecioUnitario().multiply(new BigDecimal(pv.getCantidad()));
            BigDecimal[] r = OperacionesVenta.calcular(base, iva);
            total = total.add(r[0]);
            iva5 = iva5.add(r[1]);
            iva19 = iva19.add(r[2]);
            exento = exento.add(r[3]);
        }
        return new BigDecimal[]{total, iva5, iva19, exento};
    }

    public static String sinExistencia(LinkedList<ProductoVendido> items) {
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            Producto p = OperacionesProducto.buscar(pv.getIdProducto());
            if (p == null) {
                return "(producto eliminado)";
            }
            if (p.getExistencia() < pv.getCantidad()) {
                return p.getDescripcion();
            }
        }
        return null;
    }

    public static void guardarProductos(long idVenta, LinkedList<ProductoVendido> items) {
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            OperacionesProductoVendido.Guardar(new ProductoVendido(OperacionesProductoVendido.nuevoId(), pv.getIdProducto(), idVenta, pv.getCantidad(), pv.getPrecioUnitario()));
            Producto p = OperacionesProducto.buscar(pv.getIdProducto());
            p.setExistencia(p.getExistencia() - (int) pv.getCantidad());
        }
    }

    public static LinkedList<ProductoVendido> productosDeVenta(long idVenta) {
        LinkedList<ProductoVendido> items = new LinkedList();
        for (int i = 0; i < OperacionesProductoVendido.lpv.size(); i++) {
            ProductoVendido pv = (ProductoVendido) OperacionesProductoVendido.lpv.get(i);
            if (pv.getIdVenta() == idVenta) {
                items.add(new ProductoVendido(pv.getIdProductoV(), pv.getIdProducto(), pv.getIdVenta(), pv.getCantidad(), pv.getPrecioUnitario()));
            }
        }
        return items;
    }

    public static void devolverProductos(long idVenta) {
        for (int i = 0; i < OperacionesProductoVendido.lpv.size(); i++) {
            ProductoVendido pv = (ProductoVendido) OperacionesProductoVendido.lpv.get(i);
            if (pv.getIdVenta() == idVenta) {
                Producto p = OperacionesProducto.buscar(pv.getIdProducto());
                if (p != null) {
                    p.setExistencia(p.getExistencia() + (int) pv.getCantidad());
                }
            }
        }
        OperacionesProductoVendido.eliminarDeVenta(idVenta);
    }

    public static boolean productoTieneVentas(long idProducto) {
        for (int i = 0; i < OperacionesProductoVendido.lpv.size(); i++) {
            ProductoVendido pv = (ProductoVendido) OperacionesProductoVendido.lpv.get(i);
            if (pv.getIdProducto() == idProducto) {
                return true;
            }
        }
        return false;
    }
}
