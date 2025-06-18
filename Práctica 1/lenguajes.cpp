//López Reyes José Roberto
//Jímenez Paulino Erick
#include <iostream>
#include <vector>
#include <sstream>
#include <algorithm>
#include <string>
#include <cstdlib>
#include <ctime>

using namespace std;

// Función para ingresar símbolos por separado
vector<char> separados(vector<char> alfabeto)
{
    // Pedimos al usuario que ingrese los símbolos del alfabeto separados por comas
    cout << "Ingrese los simbolos del alfabeto separados por comas: ";
    string input;
    getline(cin, input);

    // Utilizamos un stringstream para separar los símbolos
    stringstream ss(input);
    char simbolo;
    while (ss >> simbolo)
    {
        // Agregamos el símbolo al vector si no está repetido
        if (find(alfabeto.begin(), alfabeto.end(), simbolo) == alfabeto.end())
        {
            alfabeto.push_back(simbolo);
        }
        // Ignoramos la coma
        ss.ignore();
    }

    return alfabeto;
}

// Función para ingresar el alfabeto con un rango
vector<char> rango(vector<char> alfabeto)
{
    cout << "Ingrese el rango del alfabeto: ";
    char input[3];
    cin >> input;

    if (input[1] != '-')
    {
        return alfabeto;
    }
    else
    {
        for (int x = input[0]; x < input[2] + 1; x++)
        {
            if (find(alfabeto.begin(), alfabeto.end(), x) == alfabeto.end())
            {
                alfabeto.push_back(x);
            }
        }
    }
    cin.ignore();
    return alfabeto;
}

// Función para comprobar si w1 es prefijo de w2.
bool prefijo(string w1, string w2)
{
    bool pre = false;
    string comp;

    if (w1.size() > w2.size())
    {
        return pre;
    }

    comp = w2.substr(0, w1.size());

    if (w1 == comp)
    {
        pre = true;
    }

    return pre;
}

// Función para comprobar si w1 es sufijo de w2.
bool sufijo(string w1, string w2)
{
    bool suf = false;
    string comp;

    if (w1.size() > w2.size())
    {
        return suf;
    }

    comp = w2.substr(w2.size() - w1.size(), w2.size());

    if (w1 == comp)
    {
        suf = true;
    }

    return suf;
}

// Función para comprobar si w1 es subcadena de w2.
bool subcadena(string w1, string w2)
{
    bool subcad = false;
    size_t comp;

    if (w1.size() > w2.size())
    {
        return subcad;
    }

    comp = w2.find(w1);

    if (comp != string::npos)
    {
        subcad = true;
    }

    return subcad;
}

// Función para comprobar si w1 es subsecuencia de w2.
bool subsecuencia(string w1, string w2)
{
    bool subsec = false;
    int i = 0, j = 0;

    while (i < w2.length() && j < w1.length())
    {
        if (w2[i] == w1[j])
        {
            j++;
        }
        i++;
    }

    if (j == w1.length())
    {
        subsec = true;
    }

    return subsec;
}

// Generación de los lenguajes
vector<string> lenguaje(vector<char> alfabeto, int np, int l)
{
    vector<string> leng;
    vector<string> repetidos;

    while (repetidos.size() < np)
    {
        string pal;
        for (int y = 0; y < l; y++)
        {
            int id = rand() % alfabeto.size();
            pal.push_back(alfabeto[id]);
        }

        bool unico = true;
        for (const string& palabra : repetidos)
        {
            if (palabra == pal)
            {
                unico = false;
                break;
            }
        }

        if (unico == true)
        {
            leng.push_back(pal);
            repetidos.push_back(pal);
        }
    }

    return leng;
}

// Diferencia de lenguajes
vector<string> diferencia(vector<string> l1, vector<string> l2, int np)
{
    vector<string> ld;
    for (int x = 0; x < np; x++)
    {
        bool coincidencia = false;
        for(int y = 0; y < np; y++){
        if (l1[x] == l2[y])
        {
            coincidencia = true;
            break;            
        }
        }
        if(coincidencia == false){
            ld.push_back(l1[x]);
        }
    }

    return ld;
}

//Realiza las concatenaciones correspondientes para la potencia
void concatenar(vector<char>& alfabeto, int potencia, string actual, vector<string>& pot) {
    if (potencia == 0) {
        pot.push_back(actual);
        return;
    }
    for (char c : alfabeto) {
        string conc = actual + c;
        concatenar(alfabeto, potencia - 1, conc, pot);
    }
}

//Aplica la potencia y lleva a la función recursiva concatenar
vector<string> potenciaAlf(vector<char> alfabeto, int potencia) {
    vector<string> pot;
    string actual = "";
    if(potencia == 0){
        pot.push_back("\u03BB");
        return pot;
    }else if(potencia > 0){
        concatenar(alfabeto, potencia, actual, pot);
    }else if(potencia < 0){
        concatenar(alfabeto, abs(potencia), actual, pot);
        reverse(pot.begin(), pot.end());
    }    
    return pot;
}

int main()
{
    // Variables declaradas
    vector<char> alfabeto;
    vector<string> leng1, leng2, ld, pot;
    int opc, np, l, potencia;
    bool pre, suf, subsec, subcad;    
    srand(time(0));

    cout << "Seleccione una opción:\n1. Ingresar el alfabeto por separado\n2. Ingresar un rango\n";
    cin >> opc;
    cin.ignore();

    do
    {
        switch (opc)
        {
        case 1:
            alfabeto = separados(alfabeto);
            break;

        case 2:
            alfabeto = rango(alfabeto);
            break;

        default:
            cout << "Seleccione una de las opciones disponibles.";
            break;
        }
    } while (opc != 1 && opc != 2);

    // Verificamos que el alfabeto tenga al menos tres símbolos
    if (alfabeto.size() < 3)
    {
        cout << "Error: El alfabeto debe contener al menos tres simbolos.";
        return 1; // Terminamos el programa con un código de error
    }

    // Mostramos los símbolos del alfabeto
    cout << "Los simbolos del alfabeto son: ";
    for (char c : alfabeto)
    {
        cout << c << " ";
    }
    cout << "\n";

    // Leer y validar la cadena w1
    string w1;
    bool w1_valida = false;
    while (!w1_valida)
    {
        cout << "Ingrese la cadena w1 (elemento del alfabeto): ";
        getline(cin, w1);

        // Validar la cadena w1
        w1_valida = true;
        for (char c : w1)
        {
            if (find(alfabeto.begin(), alfabeto.end(), c) == alfabeto.end())
            {
                cout << "Error: La cadena contiene un simbolo que no pertenece al alfabeto.\n";
                w1_valida = false;
                break;
            }
        }
    }

    // Leer y validar la cadena w2
    string w2;
    bool w2_valida = false;
    while (!w2_valida)
    {
        cout << "Ingrese la cadena w2 (elemento del alfabeto): ";
        getline(cin, w2);

        // Validar la cadena w2
        w2_valida = true;
        for (char c : w2)
        {
            if (find(alfabeto.begin(), alfabeto.end(), c) == alfabeto.end())
            {
                cout << "Error: La cadena contiene un simbolo que no pertenece al alfabeto.\n";
                w2_valida = false;
                break;
            }
        }
    }

    if (w1 == "")
    {
        w1 = "\u03BB";
    }

    if (w2 == "")
    {
        w2 = "\u03BB";
    }
    // Mostrar las cadenas w1 y w2
    cout << "La cadena w1 es: " << w1;
    cout << "\n";
    cout << "La cadena w2 es: " << w2;
    cout << "\n";

    if (w1 == w2 || w1 == "\u03BB")
    {
        cout << "La cadena w1 es prefijo de la cadena w2.";
        cout << "\n";
        cout << "La cadena w1 es sufijo de la cadena w2.";
        cout << "\n";
        cout << "La cadena w1 es subcadena de la cadena w2.";
        cout << "\n";
        cout << "La cadena w1 es subsecuencia de la cadena w2.";
        cout << "\n";
    }
    else
    {
        pre = prefijo(w1, w2);

        if (pre == true)
        {
            cout << "La cadena w1 es prefijo propio de la cadena w2.";
            cout << "\n";
        }
        else
        {
            cout << "La cadena w1 NO prefijo de la cadena w2.";
            cout << "\n";
        }

        suf = sufijo(w1, w2);

        if (suf == true)
        {
            cout << "La cadena w1 es sufijo propio de la cadena w2.";
            cout << "\n";
        }
        else
        {
            cout << "La cadena w1 NO es sufijo de la cadena w2.";
            cout << "\n";
        }

        subcad = subcadena(w1, w2);

        if (subcad == true)
        {
            cout << "La cadena w1 es subcadena propia de la cadena w2.";
            cout << "\n";
        }
        else
        {
            cout << "La cadena w1 NO es subcadena propia de la cadena w2.";
            cout << "\n";
        }

        subsec = subsecuencia(w1, w2);

        if (subsec == true)
        {
            cout << "La cadena w1 es subsecuencia propia de la cadena w2.";
            cout << "\n";
        }
        else
        {
            cout << "La cadena w1 NO es subsecuencia propia de la cadena w2.";
            cout << "\n";
        }
    }

    cout << "Ingrese el número de elementos de los lenguajes: ";
    cin >> np;

    cout << "Ingrese el tamaño de los elementos de los lenguajes: ";
    cin >> l;

    leng1 = lenguaje(alfabeto, np, l);
    leng2 = lenguaje(alfabeto, np, l);

    // Mostramos los lenguajes
    cout << "Lenguaje 1: ";
    for (string s : leng1)
    {
        cout << s << " ";
    }
    cout << "\n";

    cout << "Lenguaje 2: ";
    for (string s : leng2)
    {
        cout << s << " ";
    }
    cout << "\n";

    ld = diferencia(leng1, leng2, np);

    cout << "Lenguaje Diferencia: ";

    if (ld.size() == 0)
    {
        cout << "\u03BB";
    }
    else
    {
        for (string s : ld)
        {
            cout << s << " ";
        }
    }
    cout << "\n";

    do
    {
        cout << "Escriba la potencia a la que quiere elevar el alfabeto (de -5 a 5): ";
        cin >> potencia;

        if (potencia < -5 || potencia > 5)
        {
            cout << "Número fuera del rango";
            cout << "\n";
        }
    } while (potencia < -5 || potencia > 5);

    pot = potenciaAlf(alfabeto, potencia);

    cout << "Potencia del alfabeto: ";
    for (string s : pot)
    {
        cout << s << " ";
    }    
    cout << "\n";

    return 0;
}