# Frontend del hotel (Bootstrap 6)

Diseño visual del sistema de hotel hecho con los componentes de **Bootstrap 6.0.0-alpha.1** (https://getbootstrap.com/docs/6.0/). Es **solo frontend**: no hay servidor ni base de datos, y los datos que se ven son de ejemplo (los mismos de `../sql/hotel.sql`).

## Cómo verlo

Abre `index.html` con doble clic. Necesita internet, porque Bootstrap 6 y Bootstrap Icons se cargan desde un CDN (`cdn.jsdelivr.net`).

## Componentes de Bootstrap usados

Se parte de los ejemplos oficiales de la documentación (en el mismo estilo del esqueleto `proyecto java`):

- **Ejemplo "Dashboard"**: barra superior (`navbar`), menú lateral (`sidebar` con `drawer` en celular) y encabezado de página.
- `card`, `table`, `badge`, `alert`, `toast`.
- Formularios con `form-field`, `form-label`, `form-control` y validación con `data-bs-validate`.
- Panel lateral del formulario con `drawer`, y confirmaciones con `dialog`.
- Botones `btn-solid`, `btn-outline` con `theme-primary`, `theme-danger`, etc.

## Páginas

| Página | Qué muestra |
|---|---|
| `index.html` | Panel: cifras del día, mapa de habitaciones, próximos movimientos y cuentas abiertas |
| `clientes.html` | Lista de clientes y formulario |
| `habitaciones.html` | Habitaciones (estado, tipo, precio, capacidad) |
| `reservas.html` | Reservas con los estados PENDIENTE, CONFIRMADA, CANCELADA y FINALIZADA |
| `checkin.html` | Check-in: al elegir una reserva se llenan los datos, y las noches se calculan con las fechas |
| `cuentas.html` | Cuentas abiertas con su detalle y el checkout, que muestra la factura |
| `categorias.html` | Categorías (la categoría Habitación está protegida) |
| `productos.html` | Productos con IVA e inventario |
| `ventas.html` | Ventas con resumen y totales. Si el cliente tiene cuenta abierta, la carga y se edita ahí |
| `facturas.html` | Historial de facturas: cada una se puede ver completa |
| `reportes.html` | Los 8 reportes, elegibles desde una lista |

## Qué funciona sin base de datos

- Validación de los formularios (campos obligatorios, correo, números).
- Las pantallas de registro muestran primero la lista con su buscador. "Nuevo…" abre un panel lateral con el formulario; elegir una fila (con clic o con Enter) lo abre con sus datos, para guardar cambios o eliminar.
- Accesibilidad: foco visible, filas elegibles con teclado, encabezados de tabla con `scope`, títulos por sección y enlace para saltar el menú.
- Ventas: agregar y quitar productos, totales con IVA (exento, 5% y 19%), validación de existencia y la cuenta abierta del cliente.
- Cuentas: elegir una cuenta, hacer el checkout y ver la factura generada.
- Guardar, modificar y eliminar solo muestran un aviso: **nada se guarda**.

## Archivos

- `assets/css/estilo.css`: las reglas del ejemplo Dashboard y unos ajustes pequeños.
- `assets/js/app.js`: el comportamiento de arriba, en JavaScript puro (importa Bootstrap 6 como módulo desde el CDN).
- El menú lateral está repetido en cada página; si cambias una opción, hay que cambiarla en todas.

## Para conectarlo después

Cada campo de los formularios tiene `name` e `id` (`nombre`, `documento`, `precio`…), y las tablas tienen un `id` fijo, para enlazarlos con el backend sin rehacer el diseño. Los nombres son parecidos a los atributos de las clases del proyecto, pero no idénticos (por ejemplo `precio` en vez de `precioNoche`).
