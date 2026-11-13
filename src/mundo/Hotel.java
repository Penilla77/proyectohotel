package mundo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Hotel {

    public static boolean vigente(String estado) {
        return estado.equals(EstadoReserva.PENDIENTE.name()) || estado.equals(EstadoReserva.CONFIRMADA.name());
    }

    public static boolean cruce(LocalDateTime ent1, LocalDateTime sal1, LocalDateTime ent2, LocalDateTime sal2) {
        return ent1.isBefore(sal2) && ent2.isBefore(sal1);
    }

    public static boolean habitacionLibre(int idHab, LocalDateTime ent, LocalDateTime sal, int ignorarReserva, int ignorarCheckin) throws Exception {
        LinkedList<Reserva> lr = OperacionesReserva.listar();
        for (int i = 0; i < lr.size(); i++) {
            Reserva r = (Reserva) lr.get(i);
            if (r.getIdHabitacion() == idHab && vigente(r.getEstado())
                    && r.getIdReserva() != ignorarReserva
                    && cruce(ent, sal, r.getFechaEntrada(), r.getFechaSalida())) {
                return false;
            }
        }
        LinkedList<Checkin> lck = OperacionesCheckin.listar();
        for (int i = 0; i < lck.size(); i++) {
            Checkin c = (Checkin) lck.get(i);
            if (c.getIdHabitacion() == idHab && c.getIdCheckin() != ignorarCheckin
                    && cruce(ent, sal, c.getFechaIngreso(), c.getFechaSalida())) {
                return false;
            }
        }
        return true;
    }

    public static void actualizarHabitacion(int idHab) throws Exception {
        Habitacion h = OperacionesHabitacion.buscar(idHab);
        if (h == null) {
            return;
        }
        if (h.getEstado().equals("Mantenimiento")) {
            OperacionesHabitacion.actualizarEstado(idHab, "Mantenimiento", 0);
            return;
        }
        LinkedList<Checkin> lck = OperacionesCheckin.listar();
        for (int i = 0; i < lck.size(); i++) {
            Checkin c = (Checkin) lck.get(i);
            if (c.getIdHabitacion() == idHab) {
                OperacionesHabitacion.actualizarEstado(idHab, "Ocupada", c.getIdCliente());
                return;
            }
        }
        Reserva proxima = null;
        LinkedList<Reserva> lr = OperacionesReserva.listar();
        for (int i = 0; i < lr.size(); i++) {
            Reserva r = (Reserva) lr.get(i);
            if (r.getIdHabitacion() == idHab && vigente(r.getEstado())) {
                if (proxima == null || r.getFechaEntrada().isBefore(proxima.getFechaEntrada())) {
                    proxima = r;
                }
            }
        }
        if (proxima != null) {
            OperacionesHabitacion.actualizarEstado(idHab, "Reservada", proxima.getIdCliente());
            return;
        }
        OperacionesHabitacion.actualizarEstado(idHab, "Disponible", 0);
    }

    public static void actualizarTodas() throws Exception {
        LinkedList<Habitacion> lh = OperacionesHabitacion.listar();
        for (int i = 0; i < lh.size(); i++) {
            Habitacion h = (Habitacion) lh.get(i);
            actualizarHabitacion(h.getId());
        }
    }

    public static boolean clienteTieneRegistros(int idCliente) throws Exception {
        LinkedList<Reserva> lr = OperacionesReserva.listar();
        for (int i = 0; i < lr.size(); i++) {
            Reserva r = (Reserva) lr.get(i);
            if (r.getIdCliente() == idCliente && vigente(r.getEstado())) {
                return true;
            }
        }
        LinkedList<Checkin> lck = OperacionesCheckin.listar();
        for (int i = 0; i < lck.size(); i++) {
            Checkin c = (Checkin) lck.get(i);
            if (c.getIdCliente() == idCliente) {
                return true;
            }
        }
        LinkedList<Venta> lv = OperacionesVenta.listar();
        for (int i = 0; i < lv.size(); i++) {
            Venta v = (Venta) lv.get(i);
            if (v.getIdCliente() == idCliente) {
                return true;
            }
        }
        return false;
    }

    public static boolean habitacionTieneRegistros(int idHab) throws Exception {
        LinkedList<Reserva> lr = OperacionesReserva.listar();
        for (int i = 0; i < lr.size(); i++) {
            Reserva r = (Reserva) lr.get(i);
            if (r.getIdHabitacion() == idHab && vigente(r.getEstado())) {
                return true;
            }
        }
        LinkedList<Checkin> lck = OperacionesCheckin.listar();
        for (int i = 0; i < lck.size(); i++) {
            Checkin c = (Checkin) lck.get(i);
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

    public static BigDecimal[] totalesVenta(LinkedList<ProductoVendido> items) throws Exception {
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

    public static String sinExistencia(LinkedList<ProductoVendido> items) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            Producto p = OperacionesProducto.buscar(pv.getIdProducto());
            if (p == null) {
                return "(producto eliminado)";
            }
            if (esHabitacion(p)) {
                continue;
            }
            if (p.getExistencia() < pv.getCantidad()) {
                return p.getDescripcion();
            }
        }
        return null;
    }

    public static void guardarLineas(long idVenta, LinkedList<ProductoVendido> items) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            OperacionesProductoVendido.Guardar(new ProductoVendido(OperacionesProductoVendido.nuevoId(), pv.getIdProducto(), idVenta, pv.getCantidad(), pv.getPrecioUnitario()));
        }
    }

    public static void guardarProductos(long idVenta, LinkedList<ProductoVendido> items) throws Exception {
        guardarLineas(idVenta, items);
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            if (!esHabitacion(OperacionesProducto.buscar(pv.getIdProducto()))) {
                OperacionesProducto.cambiarExistencia(pv.getIdProducto(), -(int) pv.getCantidad());
            }
        }
    }

    public static LinkedList<ProductoVendido> productosDeVenta(long idVenta) throws Exception {
        LinkedList<ProductoVendido> items = new LinkedList();
        LinkedList<ProductoVendido> lpv = OperacionesProductoVendido.listar();
        for (int i = 0; i < lpv.size(); i++) {
            ProductoVendido pv = (ProductoVendido) lpv.get(i);
            if (pv.getIdVenta() == idVenta) {
                items.add(pv);
            }
        }
        return items;
    }

    public static void devolverProductos(long idVenta) throws Exception {
        LinkedList<ProductoVendido> items = productosDeVenta(idVenta);
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            if (!esHabitacion(OperacionesProducto.buscar(pv.getIdProducto()))) {
                OperacionesProducto.cambiarExistencia(pv.getIdProducto(), (int) pv.getCantidad());
            }
        }
        OperacionesProductoVendido.eliminarDeVenta(idVenta);
    }

    public static boolean esHabitacion(Producto p) throws Exception {
        return p != null && OperacionesCategoria.esHabitacion(p.getCodcategoria());
    }

    public static Producto productoHabitacion() throws Exception {
        Categoria cat = OperacionesCategoria.buscarPorDetalle("Habitación");
        if (cat == null) {
            throw new Exception("No existe la categoría Habitación");
        }
        LinkedList<Producto> lp = OperacionesProducto.listar();
        for (int i = 0; i < lp.size(); i++) {
            if (lp.get(i).getCodcategoria() == cat.getCodCategoria()) {
                return lp.get(i);
            }
        }
        throw new Exception("No existe un producto en la categoría Habitación");
    }

    public static void abrirCuenta(Checkin c) throws Exception {
        OperacionesCuenta.Guardar(new Cuenta(OperacionesCuenta.nuevoId(), c.getIdCheckin(), c.getIdCliente(), c.getIdHabitacion(), c.getFechaIngreso(), "Abierta", 0));
    }

    public static boolean cuentaTieneConsumos(int idCheckin) throws Exception {
        Cuenta cu = OperacionesCuenta.buscarDeCheckin(idCheckin);
        return cu != null && !OperacionesCuentaProducto.listarDeCuenta(cu.getIdCuenta()).isEmpty();
    }

    public static LinkedList<ProductoVendido> consumosDeCuenta(int idCuenta) throws Exception {
        LinkedList<ProductoVendido> items = new LinkedList();
        LinkedList<CuentaProducto> cargos = OperacionesCuentaProducto.listarDeCuenta(idCuenta);
        for (int i = 0; i < cargos.size(); i++) {
            CuentaProducto cp = (CuentaProducto) cargos.get(i);
            items.add(new ProductoVendido(0, cp.getIdProducto(), 0, cp.getCantidad(), cp.getPrecioUnitario()));
        }
        return items;
    }

    public static long cantidadEnCuenta(int idCuenta, long idProducto) throws Exception {
        long cantidad = 0;
        LinkedList<CuentaProducto> cargos = OperacionesCuentaProducto.listarDeCuenta(idCuenta);
        for (int i = 0; i < cargos.size(); i++) {
            CuentaProducto cp = (CuentaProducto) cargos.get(i);
            if (cp.getIdProducto() == idProducto) {
                cantidad = cantidad + cp.getCantidad();
            }
        }
        return cantidad;
    }

    public static void reemplazarConsumos(int idCuenta, LinkedList<ProductoVendido> items) throws Exception {
        Cuenta cu = OperacionesCuenta.buscar(idCuenta);
        if (cu == null || !cu.getEstado().equals("Abierta")) {
            throw new Exception("La cuenta no está abierta");
        }
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            Producto p = OperacionesProducto.buscar(pv.getIdProducto());
            if (p == null) {
                throw new Exception("Producto no encontrado");
            }
            if (esHabitacion(p)) {
                throw new Exception("La habitación se cobra sola en el checkout");
            }
            long antes = cantidadEnCuenta(idCuenta, pv.getIdProducto());
            if (pv.getCantidad() > antes && p.getVencimiento().isBefore(LocalDate.now())) {
                throw new Exception("El producto " + p.getDescripcion() + " está vencido, no se puede vender");
            }
            if (pv.getCantidad() - antes > p.getExistencia()) {
                throw new Exception("No hay existencia suficiente de " + p.getDescripcion());
            }
        }
        LinkedList<CuentaProducto> actuales = OperacionesCuentaProducto.listarDeCuenta(idCuenta);
        for (int i = 0; i < actuales.size(); i++) {
            CuentaProducto cp = (CuentaProducto) actuales.get(i);
            OperacionesProducto.cambiarExistencia(cp.getIdProducto(), (int) cp.getCantidad());
            OperacionesCuentaProducto.eliminar(cp.getIdCuentaProducto());
        }
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            OperacionesCuentaProducto.Guardar(new CuentaProducto(OperacionesCuentaProducto.nuevoId(), idCuenta, pv.getIdProducto(), pv.getCantidad(), pv.getPrecioUnitario()));
            OperacionesProducto.cambiarExistencia(pv.getIdProducto(), -(int) pv.getCantidad());
        }
    }

    public static LinkedList<ProductoVendido> lineasCuenta(Cuenta cu) throws Exception {
        LinkedList<ProductoVendido> items = new LinkedList();
        Checkin k = null;
        if (cu.getIdCheckin() != 0) {
            k = OperacionesCheckin.buscar(cu.getIdCheckin());
        }
        Habitacion h = OperacionesHabitacion.buscar(cu.getIdHabitacion());
        if (k != null && h != null) {
            items.add(new ProductoVendido(0, productoHabitacion().getIdProducto(), 0, k.getNoches(), h.getPrecioNoche()));
        }
        LinkedList<CuentaProducto> cargos = OperacionesCuentaProducto.listarDeCuenta(cu.getIdCuenta());
        for (int i = 0; i < cargos.size(); i++) {
            CuentaProducto cp = (CuentaProducto) cargos.get(i);
            items.add(new ProductoVendido(0, cp.getIdProducto(), 0, cp.getCantidad(), cp.getPrecioUnitario()));
        }
        return items;
    }

    public static BigDecimal totalCuenta(int idCuenta) throws Exception {
        Cuenta cu = OperacionesCuenta.buscar(idCuenta);
        return totalesVenta(lineasCuenta(cu))[0];
    }

    public static Factura checkout(int idCuenta, String pago) throws Exception {
        Cuenta cu = OperacionesCuenta.buscar(idCuenta);
        if (cu == null || !cu.getEstado().equals("Abierta")) {
            throw new Exception("La cuenta no está abierta");
        }
        LinkedList<ProductoVendido> items = lineasCuenta(cu);
        BigDecimal[] r = totalesVenta(items);
        long id = OperacionesVenta.nuevoId();
        OperacionesVenta.Guardar(new Venta(id, LocalDateTime.now().withNano(0), r[0], r[1], r[2], r[3], pago, cu.getIdCliente()));
        guardarLineas(id, items);
        OperacionesCuenta.cerrar(idCuenta, id);
        if (cu.getIdCheckin() != 0) {
            OperacionesCheckin.eliminar(cu.getIdCheckin());
        }
        actualizarHabitacion(cu.getIdHabitacion());
        return generarFactura(id);
    }

    public static Factura generarFactura(long idVenta) throws Exception {
        Venta v = OperacionesVenta.buscar(idVenta);
        long idFactura = OperacionesFactura.nuevoId();
        Factura f = new Factura(idFactura, idVenta, v.getFecha(), OperacionesCliente.nombreCompleto(v.getIdCliente()), v.getTotal(), textoFactura(idFactura, idVenta));
        OperacionesFactura.Guardar(f);
        return f;
    }

    public static String textoFactura(long idFactura, long idVenta) throws Exception {
        Venta v = OperacionesVenta.buscar(idVenta);
        LinkedList<ProductoVendido> items = productosDeVenta(idVenta);
        StringBuilder sb = new StringBuilder();
        sb.append("FACTURA No. ").append(idFactura).append("\n");
        sb.append("Fecha:   ").append(Tablas.fecha(v.getFecha())).append("\n");
        sb.append("Cliente: ").append(OperacionesCliente.nombreCompleto(v.getIdCliente())).append("\n");
        sb.append("Pago:    ").append(v.getTipoPago()).append("\n\n");
        sb.append(String.format("%-24s %6s %12s %14s%n", "Detalle", "Cant.", "Precio", "Subtotal"));
        for (int i = 0; i < items.size(); i++) {
            ProductoVendido pv = (ProductoVendido) items.get(i);
            Producto p = OperacionesProducto.buscar(pv.getIdProducto());
            String nombre = "(producto eliminado)";
            if (p != null) {
                nombre = p.getDescripcion();
            }
            BigDecimal sub = pv.getPrecioUnitario().multiply(new BigDecimal(pv.getCantidad()));
            sb.append(String.format("%-24s %6d %12s %14s%n", nombre, pv.getCantidad(), Tablas.dinero(pv.getPrecioUnitario()), Tablas.dinero(sub)));
        }
        sb.append("\n");
        sb.append(String.format("%-44s %14s%n", "Exento", Tablas.dinero(v.getExento())));
        sb.append(String.format("%-44s %14s%n", "IVA 5%", Tablas.dinero(v.getIva5())));
        sb.append(String.format("%-44s %14s%n", "IVA 19%", Tablas.dinero(v.getIva19())));
        sb.append(String.format("%-44s %14s%n", "TOTAL", Tablas.dinero(v.getTotal())));
        return sb.toString();
    }

    public static boolean productoTieneVentas(long idProducto) throws Exception {
        LinkedList<ProductoVendido> lpv = OperacionesProductoVendido.listar();
        for (int i = 0; i < lpv.size(); i++) {
            ProductoVendido pv = (ProductoVendido) lpv.get(i);
            if (pv.getIdProducto() == idProducto) {
                return true;
            }
        }
        return false;
    }

}
