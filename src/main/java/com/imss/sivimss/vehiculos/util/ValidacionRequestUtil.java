package com.imss.sivimss.vehiculos.util;

public class ValidacionRequestUtil {
    private ValidacionRequestUtil() {
    }

    public static boolean validarInt(Integer valor){
        boolean correcto=false;
        if(valor!=null && valor>0){
            correcto=true;
        }
        return correcto;
    }
}
