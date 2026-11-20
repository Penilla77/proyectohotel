/* Comportamiento visual del sistema de hotel.
   Todo ocurre en el navegador con datos de ejemplo: no hay base de datos ni servidor. */

/* Bootstrap 6 se entrega como módulo ES, por eso se importa desde el CDN (no hay variable global "bootstrap"). */
import('https://cdn.jsdelivr.net/npm/bootstrap@6.0.0-alpha.1/dist/js/bootstrap.bundle.min.js').then(function (bootstrap) {
  'use strict';

  function dinero(n) {
    return '$ ' + Math.round(n).toLocaleString('es-CO');
  }

  function aviso(texto, tipo) {
    var cont = document.getElementById('avisos');
    if (!cont) { return; }
    var el = document.createElement('div');
    el.className = 'toast theme-' + (tipo || 'success');
    el.setAttribute('role', 'alert');
    el.setAttribute('aria-live', 'assertive');
    el.setAttribute('aria-atomic', 'true');
    el.innerHTML = '<div class="toast-header"><strong class="me-auto">Hotel</strong>' +
      '<button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Cerrar"></button></div>' +
      '<div class="toast-body"></div>';
    el.querySelector('.toast-body').textContent = texto;
    cont.appendChild(el);
    var t = bootstrap.Toast.getOrCreateInstance(el, { delay: 4500 });
    el.addEventListener('hidden.bs.toast', function () { el.remove(); });
    t.show();
  }

  /* ---------- Tablas: buscar ---------- */
  document.querySelectorAll('[data-filtro]').forEach(function (input) {
    input.addEventListener('input', function () {
      var tabla = document.querySelector(input.dataset.filtro);
      var q = input.value.toLowerCase();
      tabla.querySelectorAll('tbody tr').forEach(function (tr) {
        tr.hidden = q !== '' && tr.textContent.toLowerCase().indexOf(q) === -1;
      });
    });
  });

  /* ---------- Lista + panel lateral (clientes, habitaciones, reservas...) ---------- */
  var panel = document.getElementById('panelForm');
  if (panel) {
    var form = document.querySelector(panel.dataset.form);
    var oc = bootstrap.Drawer.getOrCreateInstance(panel);
    var titulo = panel.querySelector('.drawer-title');
    var tabla = document.querySelector('table[data-form]');

    function modo(edicion) {
      panel.querySelectorAll('[data-solo-nuevo]').forEach(function (b) { b.hidden = edicion; });
      panel.querySelectorAll('[data-solo-edicion]').forEach(function (b) { b.hidden = !edicion; });
      titulo.textContent = edicion ? panel.dataset.tituloEditar : panel.dataset.tituloNuevo;
    }
    function marcar(fila) {
      tabla.querySelectorAll('tr.table-active').forEach(function (x) { x.classList.remove('table-active'); x.removeAttribute('aria-current'); });
      if (fila) { fila.classList.add('table-active'); fila.setAttribute('aria-current', 'true'); }
    }
    function vaciar() {
      form.reset();
      form.dispatchEvent(new Event('hotel:vaciado'));
    }

    document.querySelectorAll('[data-nuevo]').forEach(function (b) {
      b.addEventListener('click', function () { vaciar(); marcar(null); modo(false); oc.show(); });
    });

    tabla.querySelectorAll('tbody tr[data-fill], tbody tr[data-protegido]').forEach(function (tr) {
      function elegir() {
        if (tr.dataset.protegido === '1') { aviso(tr.dataset.motivo || 'Este registro está protegido.', 'info'); return; }
        vaciar();
        marcar(tr);
        var datos = JSON.parse(tr.dataset.fill);
        Object.keys(datos).forEach(function (campo) {
          var el = form.elements[campo];
          if (el) { el.value = datos[campo]; el.dispatchEvent(new Event('input', { bubbles: true })); el.dispatchEvent(new Event('change', { bubbles: true })); }
        });
        modo(true);
        oc.show();
      }
      tr.addEventListener('click', elegir);
      tr.addEventListener('keydown', function (e) {
        if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); elegir(); }
      });
    });

    panel.addEventListener('shown.bs.drawer', function () {
      var primero = form.querySelector('input:not([readonly]):not([type=hidden]), select');
      if (primero) { primero.focus(); }
    });
    panel.addEventListener('hidden.bs.drawer', function () { marcar(null); });

    form.addEventListener('submit', function (e) {
      e.preventDefault();
      if (!form.checkValidity()) {
        
        aviso('Revisa los campos marcados en rojo.', 'warning');
        var mal = form.querySelector(':invalid');
        if (mal) { mal.focus(); }
        return;
      }
      var accion = e.submitter && e.submitter.dataset.accion;
      aviso(accion === 'modificar' ? 'Registro modificado.' : 'Registro guardado.', 'success');
      oc.hide();
    });
    form.addEventListener('reset', function () { setTimeout(function () { form.dispatchEvent(new Event('hotel:vaciado')); }, 0); });

    var btnEliminar = panel.querySelector('[data-eliminar]');
    var modalEliminar = document.getElementById('modalEliminar');
    if (btnEliminar && modalEliminar) {
      btnEliminar.addEventListener('click', function () {
        panel.addEventListener('hidden.bs.drawer', function abrir() {
          panel.removeEventListener('hidden.bs.drawer', abrir);
          bootstrap.Dialog.getOrCreateInstance(modalEliminar).show();
        });
        oc.hide();
      });
      document.getElementById('confirmarEliminar').addEventListener('click', function () {
        bootstrap.Dialog.getInstance(modalEliminar).hide();
        aviso('Registro eliminado.', 'success');
      });
    }
  }

  /* ---------- Check-in: noches y reserva ---------- */
  var ingreso = document.getElementById('ingreso');
  var salida = document.getElementById('salida');
  var noches = document.getElementById('noches');
  function calcularNoches() {
    if (!ingreso || !salida || !noches || !ingreso.value || !salida.value) { noches && (noches.value = ''); return; }
    var a = new Date(ingreso.value.substring(0, 10) + 'T00:00:00');
    var b = new Date(salida.value.substring(0, 10) + 'T00:00:00');
    var n = Math.round((b - a) / 86400000);
    noches.value = n < 1 ? 1 : n;
  }
  if (ingreso && salida) {
    ingreso.addEventListener('input', calcularNoches);
    salida.addEventListener('input', calcularNoches);
    ingreso.form.addEventListener('hotel:vaciado', calcularNoches);
  }
  var reserva = document.getElementById('reserva');
  if (reserva) {
    reserva.addEventListener('change', function () {
      var op = reserva.options[reserva.selectedIndex];
      if (!op.dataset.cliente) { return; }
      document.getElementById('cliente').value = op.dataset.cliente;
      document.getElementById('habitacion').value = op.dataset.habitacion;
      ingreso.value = op.dataset.ingreso;
      salida.value = op.dataset.salida;
      calcularNoches();
    });
  }

  /* ---------- Ventas: resumen, totales y cuenta abierta ---------- */
  var carritoTabla = document.getElementById('carrito');
  if (carritoTabla) {
    var items = [];
    var cuentaCargada = false;
    var selCliente = document.getElementById('cliente');
    var selProducto = document.getElementById('producto');
    var cantidad = document.getElementById('cantidad');
    var banner = document.getElementById('bannerCuenta');
    var tituloVentas = document.getElementById('tituloVentas');
    var btnGuardarVenta = document.getElementById('btnGuardarVenta');
    var formVenta = document.getElementById('formVenta');

    function pintarResumen() {
      var cuerpo = carritoTabla.querySelector('tbody');
      cuerpo.innerHTML = '';
      var exento = 0, iva5 = 0, iva19 = 0, total = 0;
      items.forEach(function (it, i) {
        var base = it.precio * it.cant;
        var i5 = it.iva === 5 ? base * 0.05 : 0;
        var i19 = it.iva === 19 ? base * 0.19 : 0;
        if (it.iva === 0) { exento += base; }
        iva5 += i5; iva19 += i19; total += base + i5 + i19;
        var tr = document.createElement('tr');
        tr.innerHTML = '<td></td><td class="num"></td><td class="num"></td><td></td><td class="num"></td>' +
          '<td class="text-end"><button type="button" class="btn-outline theme-danger btn-sm btn-icon" title="Quitar"><i class="bi bi-x-lg" aria-hidden="true"></i><span class="visually-hidden"></span></button></td>';
        var td = tr.children;
        td[0].textContent = it.nombre;
        td[1].textContent = it.cant;
        td[2].textContent = dinero(it.precio);
        td[3].textContent = it.iva === 0 ? 'Exento' : it.iva + '%';
        td[4].textContent = dinero(base);
        td[5].querySelector('.visually-hidden').textContent = 'Quitar ' + it.nombre;
        td[5].querySelector('button').addEventListener('click', function () { items.splice(i, 1); pintarResumen(); });
        cuerpo.appendChild(tr);
      });
      document.getElementById('vacioResumen').hidden = items.length > 0;
      document.getElementById('tExento').textContent = dinero(exento);
      document.getElementById('tIva5').textContent = dinero(iva5);
      document.getElementById('tIva19').textContent = dinero(iva19);
      document.getElementById('tTotal').textContent = dinero(total);
    }

    function clienteCambio() {
      var op = selCliente.options[selCliente.selectedIndex];
      if (op && op.dataset.cuenta) {
        cuentaCargada = true;
        items = JSON.parse(op.dataset.consumos);
        banner.hidden = false;
        document.getElementById('textoCuenta').textContent = 'Cuenta ' + op.dataset.cuenta + ' abierta · Habitación ' + op.dataset.habitacion;
        tituloVentas.textContent = 'Ventas · Cuenta ' + op.dataset.cuenta + ' abierta · Habitación ' + op.dataset.habitacion;
        tituloVentas.classList.add('fg-primary');
        btnGuardarVenta.innerHTML = '<i class="bi bi-journal-text me-1" aria-hidden="true"></i>Actualizar cuenta';
      } else {
        if (cuentaCargada) { items = []; }
        cuentaCargada = false;
        banner.hidden = true;
        tituloVentas.textContent = 'Ventas';
        tituloVentas.classList.remove('fg-primary');
        btnGuardarVenta.innerHTML = '<i class="bi bi-receipt me-1" aria-hidden="true"></i>Guardar venta';
      }
      pintarResumen();
    }
    selCliente.addEventListener('change', clienteCambio);
    formVenta.addEventListener('reset', function () {
      setTimeout(function () { items = []; cuentaCargada = false; clienteCambio(); }, 0);
    });

    document.getElementById('btnAgregar').addEventListener('click', function () {
      var op = selProducto.options[selProducto.selectedIndex];
      var cant = parseInt(cantidad.value, 10);
      if (!op.value) { aviso('Elige un producto.', 'warning'); selProducto.focus(); return; }
      if (isNaN(cant) || cant <= 0) { cantidad.classList.add('is-invalid'); aviso('La cantidad debe ser un número mayor a 0.', 'warning'); cantidad.focus(); return; }
      var existencia = parseInt(op.dataset.existencia, 10);
      var yaEnResumen = 0;
      items.forEach(function (it) { if (it.nombre === op.dataset.nombre) { yaEnResumen += it.cant; } });
      if (cant + yaEnResumen > existencia) { aviso('No hay existencia suficiente. Quedan ' + (existencia - yaEnResumen) + ' unidades.', 'danger'); return; }
      cantidad.classList.remove('is-invalid');
      var existente = items.filter(function (it) { return it.nombre === op.dataset.nombre; })[0];
      if (existente) { existente.cant += cant; }
      else { items.push({ nombre: op.dataset.nombre, cant: cant, precio: parseInt(op.dataset.precio, 10), iva: parseInt(op.dataset.iva, 10) }); }
      cantidad.value = '';
      pintarResumen();
    });

    formVenta.addEventListener('submit', function (e) {
      e.preventDefault();
      if (!formVenta.checkValidity()) { aviso('Revisa los campos marcados en rojo.', 'warning'); return; }
      if (cuentaCargada) {
        aviso('Cuenta actualizada. Se cobra en el checkout.', 'success');
      } else if (items.length === 0) {
        aviso('Agrega al menos un producto a la venta.', 'warning');
      } else {
        aviso('Venta guardada y factura generada.', 'success');
        items = []; pintarResumen();
      }
    });
    clienteCambio();
  }

  /* ---------- Cuentas y checkout ---------- */
  var tablaCuentas = document.getElementById('tablaCuentas');
  if (tablaCuentas) {
    var cuentaActual = null;
    var lineasActual = [];
    var btnCheckout = document.getElementById('btnCheckout');

    function calcular(lineas) {
      var r = { exento: 0, iva5: 0, iva19: 0, total: 0 };
      lineas.forEach(function (l) {
        var base = l.precio * l.cant;
        if (l.iva === 0) { r.exento += base; r.total += base; }
        if (l.iva === 5) { r.iva5 += base * 0.05; r.total += base * 1.05; }
        if (l.iva === 19) { r.iva19 += base * 0.19; r.total += base * 1.19; }
      });
      return r;
    }

    tablaCuentas.querySelectorAll('tbody tr[data-cuenta]').forEach(function (tr) {
      function elegirCuenta() {
        tablaCuentas.querySelectorAll('tr.table-active').forEach(function (x) { x.classList.remove('table-active'); x.removeAttribute('aria-current'); });
        tr.classList.add('table-active');
        tr.setAttribute('aria-current', 'true');
        cuentaActual = tr;
        lineasActual = JSON.parse(tr.dataset.lineas);
        var cuerpo = document.querySelector('#tablaDetalle tbody');
        cuerpo.innerHTML = '';
        lineasActual.forEach(function (l) {
          var fila = document.createElement('tr');
          fila.innerHTML = '<td></td><td class="num"></td><td class="num"></td><td></td><td class="num"></td>';
          fila.children[0].textContent = l.nombre;
          fila.children[1].textContent = l.cant;
          fila.children[2].textContent = dinero(l.precio);
          fila.children[3].textContent = l.iva === 0 ? 'Exento' : l.iva + '%';
          fila.children[4].textContent = dinero(l.precio * l.cant);
          cuerpo.appendChild(fila);
        });
        document.getElementById('detalleVacio').hidden = true;
        document.getElementById('totalEstimado').textContent = dinero(calcular(lineasActual).total);
        document.getElementById('tituloDetalle').textContent = 'Detalle de la cuenta ' + tr.dataset.cuenta + ' · ' + tr.dataset.cliente;
        btnCheckout.hidden = false;
      }
      tr.addEventListener('click', elegirCuenta);
      tr.addEventListener('keydown', function (e) { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); elegirCuenta(); } });
    });

    btnCheckout.addEventListener('click', function () {
      document.getElementById('checkoutTexto').textContent = '¿Hacer el checkout de la cuenta ' + cuentaActual.dataset.cuenta + '? Se cobra ' +
        dinero(calcular(lineasActual).total) + ' y la habitación ' + cuentaActual.dataset.habitacion + ' queda libre.';
      bootstrap.Dialog.getOrCreateInstance(document.getElementById('modalCheckout')).show();
    });

    function rellenar(texto, ancho, derecha) { return derecha ? String(texto).padStart(ancho) : String(texto).padEnd(ancho); }

    document.getElementById('confirmarCheckout').addEventListener('click', function () {
      var pago = document.getElementById('pago').value;
      var r = calcular(lineasActual);
      var numero = document.getElementById('siguienteFactura').value;
      var ahora = new Date();
      var fecha = ahora.toLocaleDateString('es-CO', { day: '2-digit', month: '2-digit', year: 'numeric' }) + ' ' +
        ahora.toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit', hour12: false });
      var t = 'FACTURA No. ' + numero + '\nFecha:   ' + fecha + '\nCliente: ' + cuentaActual.dataset.cliente + '\nPago:    ' + pago + '\n\n';
      t += rellenar('Detalle', 24) + ' ' + rellenar('Cant.', 6, true) + ' ' + rellenar('Precio', 12, true) + ' ' + rellenar('Subtotal', 14, true) + '\n';
      lineasActual.forEach(function (l) {
        t += rellenar(l.nombre, 24) + ' ' + rellenar(l.cant, 6, true) + ' ' + rellenar(dinero(l.precio), 12, true) + ' ' + rellenar(dinero(l.precio * l.cant), 14, true) + '\n';
      });
      t += '\n' + rellenar('Exento', 44) + ' ' + rellenar(dinero(r.exento), 14, true) + '\n' +
        rellenar('IVA 5%', 44) + ' ' + rellenar(dinero(r.iva5), 14, true) + '\n' +
        rellenar('IVA 19%', 44) + ' ' + rellenar(dinero(r.iva19), 14, true) + '\n' +
        rellenar('TOTAL', 44) + ' ' + rellenar(dinero(r.total), 14, true) + '\n';
      document.getElementById('textoFactura').textContent = t;
      bootstrap.Dialog.getInstance(document.getElementById('modalCheckout')).hide();
      cuentaActual.remove();
      document.querySelector('#tablaDetalle tbody').innerHTML = '';
      document.getElementById('detalleVacio').hidden = false;
      document.getElementById('totalEstimado').textContent = dinero(0);
      document.getElementById('tituloDetalle').textContent = 'Detalle de la cuenta seleccionada';
      btnCheckout.hidden = true;
      bootstrap.Dialog.getOrCreateInstance(document.getElementById('modalFactura')).show();
      aviso('Checkout hecho: la cuenta se cerró y la factura quedó en el historial.', 'success');
    });
  }

  /* ---------- Historial de facturas ---------- */
  var tablaFacturas = document.getElementById('tablaFacturas');
  if (tablaFacturas) {
    tablaFacturas.querySelectorAll('tbody tr[data-detalle]').forEach(function (tr) {
      function ver() {
        tablaFacturas.querySelectorAll('tr.table-active').forEach(function (x) { x.classList.remove('table-active'); x.removeAttribute('aria-current'); });
        tr.classList.add('table-active');
        tr.setAttribute('aria-current', 'true');
        document.getElementById('detalleFactura').textContent = tr.dataset.detalle;
      }
      tr.addEventListener('click', ver);
      tr.addEventListener('keydown', function (e) { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); ver(); } });
    });
  }

  /* ---------- Reportes ---------- */
  var selReporte = document.getElementById('selReporte');
  if (selReporte) {
    function mostrarReporte() {
      document.querySelectorAll('.reporte').forEach(function (el) { el.hidden = el.dataset.rep !== selReporte.value; });
    }
    selReporte.addEventListener('change', mostrarReporte);
    document.getElementById('btnActualizar').addEventListener('click', function () { aviso('Reporte actualizado.', 'info'); });
    mostrarReporte();
  }
});
