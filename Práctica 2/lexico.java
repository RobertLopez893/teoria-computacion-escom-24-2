/*López Reyes José Roberto 
Jiménez Paulino Erick*/

import java.util.*;
import java.io.*;

public class lexico {
    public static boolean comentario = false;
    public static char aux = ' ';
    public static boolean desigual = false;

    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(Arrays.asList(
            "abstract", "continue", "for", "new", "switch", "assert", "default", "goto",
            "package", "synchronized", "boolean", "do", "if", "private", "this", "break",
            "double", "implements", "protected", "throw", "byte", "else", "import",
            "public", "thows", "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try", "char", "final", "interface",
            "static", "void", "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while"));

    public static void main(String[] args) {
        String ruta = "EjemploPracticaAnalizador.java";
        try {
            File archivo = new File(ruta);
            Scanner scanner = new Scanner(archivo);
            int noLinea = 1;
            int errores = 0;
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                boolean aceptado = automata(linea);
                if (aceptado == false) {
                    System.out.println("Error en la línea " + noLinea);
                    errores++;
                }
                noLinea++;
            }
            scanner.close();
            if (errores == 0) {
                System.out.println("No hay errores de análisis léxico en el archivo " + ruta);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se ha podido leer el archivo");
        }
    }

    public static boolean automata(String linea) {
        int q = 0;
        boolean aceptado = false;
        StringBuilder palabra = new StringBuilder();

        for (char c : linea.toCharArray()) {
            if (c == '{' || c == '}' || c == '[' || c == ']' || c == '(' || c == ')' || c == ';' || c == ','
                    || c == '"' || c == '&' || c == '|') {
                c = ' ';
            }
            switch (q) {
                // Caso inicial
                case 0:
                    if (c == '0') {
                        q = 3;
                    } else if (c >= '1' && c <= '9') {
                        q = 2;
                    } else if (c == '+' || c == '-') {
                        q = 1;
                    } else if (Character.isLetter(c)) {
                        q = 13;
                        palabra.append(c);
                    } else if (c == '_' || c == '$') {
                        q = 16;
                    } else if (comentario == true) {
                        q = 18;
                    } else if (c == '/') {
                        q = 14;
                    } else if (c == ' ' || c == '=') {
                        q = 0;
                    } else {
                        q = 7;
                    }
                    break;

                // Caso que acepta los signos
                case 1:
                    q = (c >= '1' && c <= '9') ? 2 : (c == '0') ? 3 : 13;
                    break;

                // Acepta números sin signo
                case 2:
                    q = (c >= '0' && c <= '9' || c == ';') ? 2 : (c == '.') ? 9 : (c == ' ' || c == ',') ? 0 : 7;
                    break;

                // Cuando se da 0, puede ser decimal, hexadecimal u octal (o 0 solo)
                case 3:
                    q = (c == 'x') ? 4
                            : (c == '.') ? 9 : (c >= '0' && c <= '7') ? 5 : (c == ' ' || c == ',' || c == ';') ? 0 : 7;
                    break;

                // Caso de los hexadecimales
                case 4:
                    q = ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || c == ';') ? 4
                            : (c == ' ' || c == ',') ? 0 : 7;
                    break;

                // Caso de los octales
                case 5:
                    q = (c >= '0' && c <= '7' || c == ';') ? 5 : (c == ' ' || c == ',') ? 0 : 7;
                    break;

                // Estado muerto
                case 7:
                    break;

                // Caso después de un punto decimal
                case 9:
                    q = (c >= '0' && c <= '9') ? 10 : 7;
                    break;

                // Caso del decimal
                case 10:
                    q = (c >= '0' && c <= '9' || c == ';') ? 10 : (c == 'E') ? 11 : (c == ' ' || c == ',') ? 0 : 7;
                    break;

                // Decimal con exponente
                case 11:
                    q = (c == '+' || c == '-' || (c >= '1' && c <= '9')) ? 12 : 7;
                    break;

                // Decimal sin exponente
                case 12:
                    q = (c >= '1' && c <= '9' || c == ';') ? 12 : (c == ' ' || c == ',') ? 0 : 7;
                    break;

                // Si es una letra, manda aquí
                case 13:
                    if (Character.isLetterOrDigit(c) || c == '_' || c == '$') {
                        palabra.append(c);
                    } else if (c == ' ' || c == '.') {
                        if (esPalabraReservada(palabra.toString())) {
                            q = 17;
                        } else if (esIdentificador(palabra.toString())) {
                            q = 16;
                        } else {
                            q = 7;
                        }
                        palabra.setLength(0);
                    } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '<' || c == '>'
                            || c == '=' || c == '!') {
                        if (esPalabraReservada(palabra.toString())) {
                            q = 7;
                        } else if (esIdentificador(palabra.toString())) {
                            aux = c;
                            q = 16;
                        } else {
                            q = 7;
                        }
                        palabra.setLength(0);
                    } else {
                        q = 7;
                    }
                    break;

                // Estado de aceptación para comentarios
                case 14:
                    if (c == '/') {
                        q = 15;
                    } else if (c == '*') {
                        comentario = true;
                        q = 18;
                    } else {
                        q = 7;
                    }
                    break;

                // Estado de aceptación que permite A-Z, a-z, 0-9, _
                case 15:
                    q = 15;
                    break;

                // Estado de los identificadores
                case 16:
                    if (aux == ' ') {
                        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                            q = 21;
                        } else if (Character.isLetter(c)) {
                            q = 13;
                            palabra.append(c);
                        } else if (c == '<' || c == '>' || c == '=') {
                            q = 20;
                        } else if (c == '!') {
                            q = 20;
                            desigual = true;
                        }
                    } else {
                        if (((aux == '=' || aux == '<' || aux == '>') && (c == ' ' || c == '=')) ||
                                ((aux == '!') && (c != '=')) ||
                                (Character.isLetter(c))) { 
                            q = 0;
                        }else if(((aux == '+' || aux == '-' || aux == '*' || aux == '/' || aux == '%') && c == ' ')){
                            q = 21;
                        }
                        aux = ' ';
                    }
                    break;

                // Estado de las palabras reservadas
                case 17:
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '<' || c == '>' || c == '='
                            || c == '!') {
                        q = 7;
                    } else if (Character.isLetter(c)) {
                        q = 13;
                        palabra.append(c);
                    } else if (Character.isDigit(c)) {
                        q = 2;
                    } else {
                        q = 0;
                    }
                    break;

                // Estado de comentarios de más de una línea
                case 18:
                    if (c == '*') {
                        q = 19;
                    } else {
                        q = 18;
                    }
                    break;

                // Termina el comentario en una línea
                case 19:
                    if (c == '/') {
                        comentario = false;
                        q = 0;
                    } else {
                        q = 18;
                    }
                    break;

                // Algunos operadores de comparación
                case 20:
                    if (desigual == false) {
                        if (c == '=' || c == ' ') {
                            q = 0;
                        } else {
                            q = 7;
                        }
                    } else if (desigual == false) {
                        if (c == '=') {
                            q = 0;
                        } else {
                            q = 7;
                        }
                    }
                    break;

                case 21:
                    if (c == ' ') {
                        q = 21;
                    } else if (Character.isDigit(c)) {
                        q = 2;
                    } else if (Character.isLetter(c)) {
                        palabra.append(c);
                        q = 13;
                    }
                    break;
            }
        }
        if (q == 0 || q == 2 || q == 4 || q == 5 || q == 10 || q == 12 || q == 15 || q == 16 || q == 17 || q == 18
                || q == 19) {
            aceptado = true;
        }
        return aceptado;
    }

    private static boolean esPalabraReservada(String palabra) {
        return PALABRAS_RESERVADAS.contains(palabra);
    }

    private static boolean esIdentificador(String palabra) {
        boolean id = true;

        for (int i = 1; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '$') {
                return false;
            }
        }

        return id;
    }
}