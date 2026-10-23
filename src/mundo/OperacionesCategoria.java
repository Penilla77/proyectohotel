package mundo;

import java.util.LinkedList;

public class OperacionesCategoria {
public static int con=0;
public static LinkedList<Categoria> lcat = new LinkedList();

public static void Guardar(Categoria c){
    lcat.add(con++, c);
}

public static int nuevoCod(){
    int mayor=0;
    for(int i=0;i<lcat.size();i++)
    {
        Categoria c1=(Categoria)lcat.get(i);
        if(c1.getCodCategoria() > mayor) {
            mayor = c1.getCodCategoria();
        }
    }
    return mayor + 1;
}

public static Categoria buscar(int cod) {
       Categoria c1=null;
       for(int i=0;i<lcat.size();i++)
       {
           c1=(Categoria)lcat.get(i);
           if(cod == c1.getCodCategoria()) {
               return c1;
           }
       }

       return null;
}

public static String detalle(int cod) {
       Categoria c1 = buscar(cod);
       if(c1 == null) {
           return "(sin categoría)";
       }
       return c1.getDetalle();
}

}
