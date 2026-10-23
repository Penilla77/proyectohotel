package mundo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Tablas {

    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String fecha(LocalDateTime f) {
        if (f == null) {
            return "";
        }
        return f.format(FECHA);
    }

    public static String dinero(BigDecimal v) {
        return "$ " + v.intValue();
    }

    public static DefaultTableModel modelo(String[] columnas, Object[][] datos) {
        return new DefaultTableModel(datos, columnas);
    }

    public static JTable tabla(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(25);
        t.setDefaultEditor(Object.class, null);
        return t;
    }
}
