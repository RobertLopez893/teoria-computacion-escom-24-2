/*López Reyes José Roberto 
Jiménez Paulino Erick*/

import java.util.*;

public class PDA {
    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(Arrays.asList(
            "auto", "break", "case", "char", "const", "continue", "default", "do",
            "double", "else", "enum", "extern", "float", "for", "goto", "if", "inline",
            "int", "long", "register", "restrict", "return", "short", "signed",
            "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned",
            "void", "volatile", "while"));

    private Stack<Character> pila;
    private String estado;
    private List<String> pasosDerivacion;

    public PDA() {
        this.pila = new Stack<>();
        this.pila.push('Z'); // 'Z' es el símbolo de fondo de la pila
        this.estado = "q0";
        this.pasosDerivacion = new ArrayList<>();
    }

    private void transicion(char c) {
        switch (estado) {
            case "q0":
                if (Character.isLetter(c) || c == '_') {
                    estado = "q1";
                    pasosDerivacion.add("q0 -> q1: Leyendo identificador/numero");
                } else if (c == '(') {
                    pila.push('(');
                    pasosDerivacion.add("q0 -> q0: Leyendo '('");
                } else {
                    estado = "error";
                }
                break;

            case "q1":
                if (c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                    estado = "q2";
                    pasosDerivacion.add("q1 -> q2: Leyendo operador");
                } else if (Character.isLetterOrDigit(c) || c == '_') {
                    pasosDerivacion.add("q1: Leyendo identificador/numero");
                } else if (c == ';') {
                    estado = "q3";
                    pasosDerivacion.add("q1 -> q3: Leyendo ';'");
                } else if (c == ')') {
                    if (!pila.isEmpty() && pila.peek() == '(') {
                        pila.pop();
                        pasosDerivacion.add("q1 -> q1: Leyendo ')'");
                    } else {
                        estado = "error";
                    }
                } else {
                    estado = "error";
                }
                break;

            case "q2":
                if (Character.isLetter(c) || c == '_') {
                    estado = "q1";
                    pasosDerivacion.add("q2 -> q1: Leyendo identificador/numero");
                } else if (Character.isDigit(c)) {
                    estado = "q1";
                    pasosDerivacion.add("q2 -> q1: Leyendo numero");
                } else if (c == '(') {
                    pila.push('(');
                    pasosDerivacion.add("q2 -> q0: Leyendo '('");
                } else {
                    estado = "error";
                }
                break;

            default:
                estado = "error";
        }
    }

    public boolean procesarCadena(String cadenaEntrada) {
        String[] tokens = cadenaEntrada.split("\\s+");

        for (String token : tokens) {
            if (PALABRAS_RESERVADAS.contains(token)) {
                return false;
            }

            for (char c : token.toCharArray()) {
                transicion(c);
                if (estado.equals("error")) {
                    return false;
                }
            }
        }
        return estado.equals("q3") && pila.size() == 1;
    }

    public String obtenerArbolDerivacion() {
        StringBuilder arbol = new StringBuilder();
        arbol.append("Arbol de Derivacion:\n");
        arbol.append("S\n");
        for (String paso : pasosDerivacion) {
            arbol.append("  |\n");
            arbol.append("  -> ").append(paso).append("\n");
        }
        return arbol.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese una expresion en lenguaje C:");
        String cadenaEntrada = scanner.nextLine();

        PDA pda = new PDA();
        boolean esValida = pda.procesarCadena(cadenaEntrada);

        if (esValida) {
            System.out.println("La cadena pertenece a la gramatica.");
            System.out.println(pda.obtenerArbolDerivacion());
        } else {
            System.out.println("La cadena NO pertenece a la gramatica.");
        }
        scanner.close();
    }
}