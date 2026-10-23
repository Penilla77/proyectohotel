package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.LinkedList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class SelectorFecha extends JPanel {

    private LocalDateTime valor;
    private YearMonth mes;
    private boolean ajustando = false;

    private final JTextField campo = new JTextField();
    private final JButton abrir = new JButton("▾");
    private final JPopupMenu popup = new JPopupMenu();
    private final JPanel dias = new JPanel(new GridLayout(0, 7, 2, 2));
    private final JLabel titulo = new JLabel("", SwingConstants.CENTER);
    private final JSpinner horas = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
    private final JSpinner minutos = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
    private final LinkedList<Runnable> oyentes = new LinkedList<>();

    public SelectorFecha() {
        this(LocalDateTime.now());
    }

    public SelectorFecha(LocalDateTime inicial) {
        super(new BorderLayout(4, 0));
        setOpaque(false);

        campo.setEditable(false);
        campo.setBackground(Estilo.PANEL);
        campo.setFont(Estilo.fuente(Font.PLAIN, 13));
        campo.setForeground(Estilo.TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.BORDE, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        campo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        campo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrar();
            }
        });

        abrir.addActionListener(e -> mostrar());
        Estilo.boton(abrir, Estilo.GRIS);
        abrir.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(campo, BorderLayout.CENTER);
        add(abrir, BorderLayout.EAST);

        armarPopup();
        setValor(inicial);
    }

    private void armarPopup() {
        JButton anterior = flecha("‹");
        JButton siguiente = flecha("›");
        anterior.addActionListener(e -> {
            mes = mes.minusMonths(1);
            dibujar();
        });
        siguiente.addActionListener(e -> {
            mes = mes.plusMonths(1);
            dibujar();
        });
        titulo.setFont(Estilo.fuente(Font.BOLD, 13));
        titulo.setForeground(Estilo.TEXTO);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.setOpaque(false);
        arriba.add(anterior, BorderLayout.WEST);
        arriba.add(titulo, BorderLayout.CENTER);
        arriba.add(siguiente, BorderLayout.EAST);

        horas.setEditor(new JSpinner.NumberEditor(horas, "00"));
        minutos.setEditor(new JSpinner.NumberEditor(minutos, "00"));
        horas.addChangeListener(e -> cambioHora());
        minutos.addChangeListener(e -> cambioHora());
        JLabel hl = new JLabel("Hora");
        hl.setFont(Estilo.fuente(Font.PLAIN, 13));
        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        abajo.setOpaque(false);
        abajo.add(hl);
        abajo.add(horas);
        abajo.add(new JLabel(":"));
        abajo.add(minutos);

        dias.setOpaque(false);

        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Estilo.PANEL);
        raiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        raiz.add(arriba, BorderLayout.NORTH);
        raiz.add(dias, BorderLayout.CENTER);
        raiz.add(abajo, BorderLayout.SOUTH);
        raiz.setPreferredSize(new Dimension(280, 290));

        popup.setBorder(BorderFactory.createLineBorder(Estilo.BORDE));
        popup.add(raiz);
    }

    private JButton flecha(String texto) {
        JButton b = new JButton(texto);
        Estilo.boton(b, Estilo.PANEL);
        b.setFont(Estilo.fuente(Font.BOLD, 16));
        b.setBorder(BorderFactory.createEmptyBorder(0, 10, 2, 10));
        return b;
    }

    private void mostrar() {
        mes = YearMonth.from(valor);
        dibujar();
        popup.show(campo, 0, campo.getHeight());
    }

    private void dibujar() {
        String nombre = mes.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "CO"));
        titulo.setText(nombre.substring(0, 1).toUpperCase() + nombre.substring(1) + " " + mes.getYear());

        dias.removeAll();
        String[] semana = {"L", "M", "M", "J", "V", "S", "D"};
        for (String d : semana) {
            JLabel l = new JLabel(d, SwingConstants.CENTER);
            l.setFont(Estilo.fuente(Font.BOLD, 12));
            l.setForeground(Estilo.TEXTO_SUAVE);
            dias.add(l);
        }
        int vacios = mes.atDay(1).getDayOfWeek().getValue() - 1;
        for (int i = 0; i < vacios; i++) {
            dias.add(new JLabel(""));
        }
        for (int d = 1; d <= mes.lengthOfMonth(); d++) {
            final LocalDate fecha = mes.atDay(d);
            JButton b = new JButton("" + d);
            boolean elegido = fecha.equals(valor.toLocalDate());
            Estilo.boton(b, elegido ? Estilo.ACENTO : Estilo.PANEL);
            b.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            if (fecha.equals(LocalDate.now()) && !elegido) {
                b.setForeground(Estilo.ACENTO);
                b.setFont(Estilo.fuente(Font.BOLD, 13));
            }
            b.addActionListener(e -> {
                valor = LocalDateTime.of(fecha, valor.toLocalTime());
                actualizar();
                popup.setVisible(false);
            });
            dias.add(b);
        }
        dias.revalidate();
        dias.repaint();
    }

    private void cambioHora() {
        if (ajustando) {
            return;
        }
        valor = valor.withHour((Integer) horas.getValue()).withMinute((Integer) minutos.getValue());
        actualizar();
    }

    private void actualizar() {
        campo.setText(valor.format(mundo.Tablas.FECHA));
        for (Runnable r : oyentes) {
            r.run();
        }
    }

    public LocalDateTime getValor() {
        return valor;
    }

    public void setValor(LocalDateTime nuevo) {
        valor = nuevo.withSecond(0).withNano(0);
        ajustando = true;
        horas.setValue(valor.getHour());
        minutos.setValue(valor.getMinute());
        ajustando = false;
        campo.setText(valor.format(mundo.Tablas.FECHA));
    }

    public void alCambiar(Runnable r) {
        oyentes.add(r);
    }
}
