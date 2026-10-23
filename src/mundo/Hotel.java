package mundo;

import java.time.LocalDateTime;

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
}
