package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import mundo.Habitacion;
import mundo.OperacionesCliente;
import mundo.OperacionesHabitacion;
import mundo.OperacionesReserva;
import mundo.OperacionesVenta;
import mundo.Reserva;
import mundo.Tablas;
import mundo.Venta;

public final class Estilo {

    public static final Color ACENTO = new Color(0x1F5C4A);
    public static final Color ROJO = new Color(0xB5473B);
    public static final Color AZUL = new Color(0x3B6E8F);
    public static final Color GRIS = new Color(0xE3DFD2);
    public static final Color FONDO = new Color(0xF6F1E4);
    public static final Color PANEL = new Color(0xFFFDF7);
    public static final Color TEXTO = new Color(0x1F2A24);
    public static final Color TEXTO_SUAVE = new Color(0x6B7468);
    public static final Color BORDE = new Color(0xD6CFBA);
    public static final Color HOVER = new Color(0xE4ECDD);
    public static final Color ENCAB = new Color(0x1F5C4A);
    public static final Color ENCTX = new Color(0xFFFFFF);
    public static final Color FILA2 = new Color(0xF8F3E6);
    public static final Color SEL = new Color(0xCFE3D4);
    public static final Color BANDA = new Color(0x1F5C4A);
    public static final Color BSUB = new Color(0xD6E6DC);

    private static boolean listo = false;

    private Estilo() {
    }

    public static Font fuente(int estilo, float tamano) {
        return new Font("Segoe UI", estilo, 13).deriveFont(estilo, tamano);
    }

    /** Nimbus + colores base. Se puede llamar muchas veces; solo actua la primera. */
    public static void instalar() {
        if (listo) {
            return;
        }
        listo = true;
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {

        }
        UIManager.put("control", FONDO);
        UIManager.put("nimbusFocus", ACENTO);

        UIManager.put("MenuBar.background", PANEL);
        UIManager.put("Menu.background", PANEL);
        UIManager.put("Menu.foreground", TEXTO);
        UIManager.put("Menu.selectionBackground", HOVER);
        UIManager.put("Menu.selectionForeground", TEXTO);
        UIManager.put("MenuItem.background", PANEL);
        UIManager.put("MenuItem.foreground", TEXTO);
        UIManager.put("MenuItem.selectionBackground", HOVER);
        UIManager.put("MenuItem.selectionForeground", TEXTO);
        UIManager.put("PopupMenu.background", PANEL);
        UIManager.put("ComboBox.background", PANEL);
        UIManager.put("ComboBox.foreground", TEXTO);
        UIManager.put("ComboBox.selectionBackground", HOVER);
        UIManager.put("ComboBox.selectionForeground", TEXTO);

        UIManager.put("Table.background", PANEL);
        UIManager.put("Table.alternateRowColor", FILA2);
        UIManager.put("OptionPane.messageFont", fuente(Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", fuente(Font.PLAIN, 13));
    }

    public static void portada(JFrame f, JMenuBar barra, String ventana, String titulo, String subtitulo) {
        f.setTitle(ventana);
        menu(barra);

        JLabel t = new JLabel(titulo);
        t.setFont(fuente(Font.BOLD, 24));
        t.setForeground(Color.WHITE);
        JLabel s = new JLabel(subtitulo);
        s.setFont(fuente(Font.PLAIN, 13));
        s.setForeground(BSUB);

        JPanel rapidos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rapidos.setOpaque(false);
        String[] nombresBoton = {"Nuevo cliente", "Nueva reserva", "Check-in", "Nueva venta", "Productos"};
        int[] formularios = {0, 2, 3, 4, 6};
        for (int i = 0; i < 5; i++) {
            final int cual = formularios[i];
            JButton b = new JButton(nombresBoton[i]);
            boton(b, PANEL);
            b.setForeground(ACENTO);
            b.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    abrir(cual);
                }
            });
            rapidos.add(b);
        }

        JPanel encabezado = new JPanel(new GridBagLayout());
        encabezado.setBackground(BANDA);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 32, 16, 32));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        encabezado.add(t, c);
        c.insets = new Insets(2, 0, 0, 0);
        encabezado.add(s, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        encabezado.add(rapidos, c);

        String[] rotulos = {"Habitaciones disponibles", "Habitaciones ocupadas", "Reservas activas", "Ventas de hoy"};
        JLabel[] cifras = new JLabel[4];
        JPanel resumen = new JPanel(new GridLayout(1, 4, 16, 0));
        resumen.setOpaque(false);
        for (int i = 0; i < 4; i++) {
            cifras[i] = new JLabel("0");
            cifras[i].setFont(fuente(Font.BOLD, 28));
            cifras[i].setForeground(ACENTO);
            JLabel rotulo = new JLabel(rotulos[i]);
            rotulo.setFont(fuente(Font.PLAIN, 13));
            rotulo.setForeground(TEXTO_SUAVE);
            JPanel caja = new JPanel(new BorderLayout(0, 4));
            caja.setBackground(PANEL);
            caja.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE),
                    BorderFactory.createEmptyBorder(14, 18, 14, 18)));
            caja.add(cifras[i], BorderLayout.CENTER);
            caja.add(rotulo, BorderLayout.SOUTH);
            resumen.add(caja);
        }

        JPanel mapa = new JPanel(new GridLayout(0, 4, 14, 14));
        mapa.setOpaque(false);
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.add(mapa, BorderLayout.NORTH);

        JLabel tituloMapa = new JLabel("Mapa de habitaciones");
        tituloMapa.setFont(fuente(Font.BOLD, 15));
        tituloMapa.setForeground(TEXTO);
        JPanel abajo = new JPanel(new BorderLayout(0, 10));
        abajo.setOpaque(false);
        abajo.add(tituloMapa, BorderLayout.NORTH);
        abajo.add(contenedor, BorderLayout.CENTER);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 20));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(24, 32, 28, 32));
        cuerpo.add(resumen, BorderLayout.NORTH);
        cuerpo.add(abajo, BorderLayout.CENTER);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        raiz.add(encabezado, BorderLayout.NORTH);
        raiz.add(cuerpo, BorderLayout.CENTER);

        estadisticas(cifras);
        llenarMapa(mapa);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                estadisticas(cifras);
                llenarMapa(mapa);
            }
        });

        f.setContentPane(raiz);
        f.setMinimumSize(new Dimension(820, 540));
        f.setPreferredSize(new Dimension(1100, 660));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private static void llenarMapa(JPanel mapa) {
        mapa.removeAll();
        for (int i = 0; i < OperacionesHabitacion.lh.size(); i++) {
            Habitacion h = (Habitacion) OperacionesHabitacion.lh.get(i);
            Color color = new Color(0x6B6F72);
            if (h.getEstado().equals("Disponible")) {
                color = new Color(0x2E7D4F);
            }
            if (h.getEstado().equals("Ocupada")) {
                color = new Color(0xB5473B);
            }
            if (h.getEstado().equals("Reservada")) {
                color = new Color(0xB7791F);
            }

            JLabel numero = new JLabel(h.getNumero());
            numero.setFont(fuente(Font.BOLD, 26));
            numero.setForeground(TEXTO);
            JLabel tipo = new JLabel(h.getTipoHabitacion() + " · " + Tablas.dinero(h.getPrecioNoche()));
            tipo.setFont(fuente(Font.PLAIN, 13));
            tipo.setForeground(TEXTO_SUAVE);
            String estado = h.getEstado();
            if (h.getIdCliente() != 0) {
                estado = estado + " · " + OperacionesCliente.nombreCompleto(h.getIdCliente());
            }
            JLabel lblEstado = new JLabel(estado);
            lblEstado.setFont(fuente(Font.BOLD, 12));
            lblEstado.setForeground(color);

            JPanel textos = new JPanel(new BorderLayout(0, 2));
            textos.setOpaque(false);
            textos.add(tipo, BorderLayout.NORTH);
            textos.add(lblEstado, BorderLayout.SOUTH);

            JPanel casilla = new JPanel(new BorderLayout(0, 14));
            casilla.setBackground(PANEL);
            casilla.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(5, 0, 0, 0, color),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(BORDE),
                            BorderFactory.createEmptyBorder(12, 16, 12, 16))));
            casilla.add(numero, BorderLayout.NORTH);
            casilla.add(textos, BorderLayout.SOUTH);
            mapa.add(casilla);
        }
        mapa.revalidate();
        mapa.repaint();
    }

    private static void estadisticas(JLabel[] cifras) {
        int libres = 0;
        int ocupadas = 0;
        for (int i = 0; i < OperacionesHabitacion.lh.size(); i++) {
            Habitacion h = (Habitacion) OperacionesHabitacion.lh.get(i);
            if (h.getEstado().equals("Disponible")) {
                libres++;
            }
            if (h.getEstado().equals("Ocupada")) {
                ocupadas++;
            }
        }
        int reservas = 0;
        for (int i = 0; i < OperacionesReserva.lr.size(); i++) {
            Reserva r = (Reserva) OperacionesReserva.lr.get(i);
            if (r.getEstado().equals("Activa")) {
                reservas++;
            }
        }
        BigDecimal ventasHoy = BigDecimal.ZERO;
        for (int i = 0; i < OperacionesVenta.lv.size(); i++) {
            Venta v = (Venta) OperacionesVenta.lv.get(i);
            if (v.getFecha().toLocalDate().equals(LocalDate.now())) {
                ventasHoy = ventasHoy.add(v.getTotal());
            }
        }
        cifras[0].setText("" + libres);
        cifras[1].setText("" + ocupadas);
        cifras[2].setText("" + reservas);
        cifras[3].setText(Tablas.dinero(ventasHoy));
    }

    private static void abrir(int cual) {
        if (cual == 0) {
            new FormCliente().setVisible(true);
        }
        if (cual == 1) {
            new FormHabitacion().setVisible(true);
        }
        if (cual == 2) {
            new FormReserva().setVisible(true);
        }
        if (cual == 3) {
            new FormCheckin().setVisible(true);
        }
        if (cual == 4) {
            new FormVenta().setVisible(true);
        }
        if (cual == 5) {
            new FormReportes().setVisible(true);
        }
        if (cual == 6) {
            new FormProducto().setVisible(true);
        }
    }

    public static void menu(JMenuBar barra) {
        barra.setUI(new BasicMenuBarUI());
        barra.setOpaque(true);
        barra.setBackground(PANEL);
        barra.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE));
        for (int i = 0; i < barra.getMenuCount(); i++) {
            JMenu m = barra.getMenu(i);
            m.setUI(new BasicMenuUI());
            m.setOpaque(true);
            m.setBackground(PANEL);
            m.setForeground(TEXTO);
            m.setFont(fuente(Font.PLAIN, 13));
            m.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
            m.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            m.getPopupMenu().setBorder(BorderFactory.createLineBorder(BORDE));
            for (Component k : m.getMenuComponents()) {
                if (k instanceof JMenuItem it && !(k instanceof JMenu)) {
                    it.setUI(new BasicMenuItemUI());
                    it.setOpaque(true);
                    it.setBackground(PANEL);
                    it.setForeground(TEXTO);
                    it.setFont(fuente(Font.PLAIN, 13));
                    it.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 28));
                    it.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        }
    }

    public static void formulario(JFrame f) {
        Container cp = f.getContentPane();
        cp.setBackground(FONDO);
        recorrer(cp);
    }

    private static void recorrer(Container c) {
        for (Component k : c.getComponents()) {
            if (k instanceof JButton b) {
                boton(b, GRIS);
            } else if (k instanceof JTextField t) {
                campo(t);
            } else if (k instanceof JComboBox<?> cb) {
                combo(cb);
            } else if (k instanceof JLabel l) {
                l.setFont(fuente(Font.PLAIN, 13));
                l.setForeground(TEXTO);
            } else if (k instanceof JScrollPane sp) {
                sp.getVerticalScrollBar().setUI(new BarraUI());
                sp.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
                sp.getHorizontalScrollBar().setUI(new BarraUI());
                sp.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
                sp.setCorner(JScrollPane.LOWER_RIGHT_CORNER, null);
                JPanel esquina = new JPanel();
                esquina.setBackground(ENCAB);
                sp.setCorner(JScrollPane.UPPER_RIGHT_CORNER, esquina);
            } else if (k instanceof SelectorFecha) {
                continue;
            } else if (k instanceof Container cc) {
                recorrer(cc);
            }
        }
    }

    public static void tabla(JTable t) {
        t.setFont(fuente(Font.PLAIN, 13));
        t.setForeground(TEXTO);
        t.setBackground(PANEL);
        t.setRowHeight(27);
        t.setShowGrid(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(BORDE);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(SEL);
        t.setSelectionForeground(TEXTO);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setPreferredSize(new Dimension(100, 30));
        t.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabla, Object valor,
                    boolean seleccionado, boolean foco, int fila, int columna) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
                l.setOpaque(true);
                l.setBackground(ENCAB);
                l.setForeground(ENCTX);
                l.setFont(fuente(Font.BOLD, 12));
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)));
                return l;
            }
        });
        DefaultTableCellRenderer celdas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabla, Object valor,
                    boolean sel, boolean foco, int fila, int columna) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tabla, valor, sel, foco, fila, columna);
                l.setFont(fuente(Font.PLAIN, 13));
                l.setForeground(TEXTO);
                l.setBackground(sel ? SEL : (fila % 2 == 0 ? PANEL : FILA2));
                if (!sel && valor instanceof String e) {
                    boolean osc = luminancia(PANEL) < 0.4;
                    Color k = null;
                    if (e.equals("Disponible") || e.equals("Activa")) { k = osc ? new Color(0x57C48A) : new Color(0x2E7D4F); }
                    if (e.equals("Ocupada")) { k = osc ? new Color(0xF0817D) : new Color(0xB5473B); }
                    if (e.equals("Reservada")) { k = osc ? new Color(0xE6B450) : new Color(0xB7791F); }
                    if (e.equals("Mantenimiento") || e.equals("Cancelada")) { k = osc ? new Color(0xA0A7AE) : new Color(0x6B6F72); }
                    if (e.equals("Completada")) { k = osc ? new Color(0x7FA8F0) : new Color(0x3B6E8F); }
                    if (k != null) {
                        l.setBackground(mezclar(PANEL, k, osc ? 0.28 : 0.16));
                        l.setForeground(k);
                        l.setFont(fuente(Font.BOLD, 12));
                    }
                }
                return l;
            }
        };
        celdas.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        t.setDefaultRenderer(Object.class, celdas);
        t.setDefaultRenderer(Integer.class, celdas);
        t.setDefaultRenderer(Long.class, celdas);
        ajustarColumnas(t);
    }

    public static void ajustarColumnas(JTable t) {
        java.awt.FontMetrics fm = t.getFontMetrics(t.getFont());
        java.awt.FontMetrics fh = t.getFontMetrics(fuente(Font.BOLD, 12));
        int total = 0;
        int[] ancho = new int[t.getColumnCount()];
        for (int c = 0; c < ancho.length; c++) {
            int max = fh.stringWidth(String.valueOf(t.getColumnModel().getColumn(c).getHeaderValue()));
            for (int f = 0; f < t.getRowCount(); f++) {
                Object v = t.getValueAt(f, c);
                if (v != null) {
                    max = Math.max(max, fm.stringWidth(v.toString()));
                }
            }
            ancho[c] = max + 20;
            total += ancho[c];
        }
        double factor = total < 900 ? 900.0 / total : 1.0;
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        for (int c = 0; c < ancho.length; c++) {
            t.getColumnModel().getColumn(c).setPreferredWidth((int) (ancho[c] * factor));
        }
    }

    public static void combo(JComboBox<?> cb) {
        cb.setUI(new ComboUI());
        cb.setFont(fuente(Font.PLAIN, 13));
        cb.setForeground(TEXTO);
        cb.setBackground(PANEL);
        cb.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        @SuppressWarnings("unchecked")
        JComboBox<Object> combo = (JComboBox<Object>) cb;
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean seleccionado, boolean foco) {

                JList<?> lista = list;
                if (lista == null) {
                    lista = new JList<>();
                }
                JLabel l = (JLabel) super.getListCellRendererComponent(lista, value, index, seleccionado, foco);
                l.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                return l;
            }
        });
    }

    public static void campo(JTextField t) {
        t.setUI(new BasicTextFieldUI());
        t.setColumns(18);
        t.setFont(fuente(Font.PLAIN, 13));
        t.setForeground(TEXTO);
        t.setBackground(PANEL);
        t.setCaretColor(TEXTO);
        t.setSelectionColor(SEL);
        t.setBorder(bordeCampo(BORDE));
        t.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                t.setBorder(bordeCampo(ACENTO));
            }

            @Override
            public void focusLost(FocusEvent e) {
                t.setBorder(bordeCampo(BORDE));
            }
        });
    }

    private static Border bordeCampo(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8));
    }

    public static void boton(JButton b, Color base) {
        b.setUI(new BotonUI(base));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setRolloverEnabled(true);
        b.setFont(fuente(Font.PLAIN, 13));
        b.setForeground(luminancia(base) > 0.75 ? TEXTO : Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static double luminancia(Color c) {
        return (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255.0;
    }

    private static Color mezclar(Color a, Color b, double p) {
        int r = (int) Math.round(a.getRed() + (b.getRed() - a.getRed()) * p);
        int g = (int) Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * p);
        int bl = (int) Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * p);
        return new Color(r, g, bl);
    }

    private static final class ComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            BasicArrowButton flecha = new BasicArrowButton(SwingConstants.SOUTH,
                    PANEL, PANEL, TEXTO_SUAVE, PANEL);
            flecha.setBorder(BorderFactory.createEmptyBorder());
            return flecha;
        }
    }

    private static final class BotonUI extends BasicButtonUI {

        private final Color base;

        BotonUI(Color base) {
            this.base = base;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Color col = base;
            if (!b.isEnabled()) {
                col = BORDE;
            } else if (m.isPressed()) {
                col = mezclar(base, Color.BLACK, 0.18);
            } else if (m.isRollover()) {
                col = mezclar(base, Color.BLACK, 0.08);
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(col);
            g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 6, 6);
            if (luminancia(base) > 0.75) {
                g2.setColor(BORDE);
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 6, 6);
            }
            g2.dispose();
            super.paint(g, c);
        }
    }

    private static final class BarraUI extends BasicScrollBarUI {

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return botonVacio();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return botonVacio();
        }

        private JButton botonVacio() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            b.setMinimumSize(new Dimension(0, 0));
            b.setMaximumSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle r) {
            g.setColor(FONDO);
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle r) {
            if (r.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isThumbRollover() || isDragging) {
                g2.setColor(ACENTO);
            } else {
                g2.setColor(mezclar(BORDE, Color.BLACK, 0.12));
            }
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 6, 6);
            g2.dispose();
        }
    }
}
