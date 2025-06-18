import java.util.*;
import java.util.regex.*;

public class expresiones {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expresion;
        boolean exp;
        Pattern patron = Pattern.compile("^[-+]?\\d*[.]?\\d*[+-]?\\d*[.]?\\d*[i]$");

        int opc;
        do {
            System.out.println("\nEXPRESIONES REGULARES");
            System.out.println("Seleccione una opción:\n1. Ingresar cadena\n2. Salir");
            opc = scanner.nextInt();
            scanner.nextLine();

            switch (opc) {
                case 1:
                    System.out.print("Ingrese un número complejo (x+yi): ");
                    expresion = scanner.nextLine();
                    exp = patron.matcher(expresion).matches();
                    if (exp == true) {
                        System.out.println("Palabra correcta");
                    } else {
                        System.out.println("Palabra incorrecta");
                    }
                    break;

                case 2:
                    System.out.println("Fin del programa.");
                    break;

                default:
                    System.out.println("Seleccione una de las opciones disponibles.");
                    break;
            }
        } while (opc != 2);
        scanner.close();
    }
}
