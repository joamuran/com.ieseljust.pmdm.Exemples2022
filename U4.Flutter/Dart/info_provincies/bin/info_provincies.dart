import 'dart:io';
import 'comarca.dart';
import 'peticionsHTTP.dart';

// dart run bin/info_provincies.dart exemple1

String baseUrl = "https://node-comarques-rest-server-production.up.railway.app";
String url = "$baseUrl/api/provincies";
void main(List<String> args) async {
  // Es defineix com async perquè dins farem operacions asíncrones
  if (args.isNotEmpty) {
    switch (args[0]) {
      case "exemple1":
        exemple1();
        break;
      case "exemple2":
        exemple2();
        break;
      case "exemple3":
        exemple3();
        break;
      case "exemple4":
        exemple4();
        break;
      case "exemple5":
        exemple5();
        break;
      case "exemple6":
        exemple6();
        break;
      case "exemple7":
        exemple7();
        break;
      case "exemple8":
        exemple8();
        break;
      case "exemple9":
        exemple9();
        break;
    }
  }
}

void exemple1() {
  /* Exemple 1:
     Realitza una petició HTTP i 
     mostra la resposta directament 
  */
  PeticionsHTTP.requestAndShow(url);
}

void exemple2() async {
  /* Exemple 2:
     Realitza una petició HTTP per obtenir les
     províncies de forma síncrona i les imprimim.

     És important fer ús de l'await per obtenir 
     la resposta en lloc d'un Future 
  */

  var provincies = await PeticionsHTTP.requestSync(url);
  print("Resposta de requestSync: ${provincies.toString()}");
}

void exemple3() {
  /* Exemple 3:
     Realitza una petició HTTP i 
    obté les províncies fent ús d'un Future
  */
  var provinciesFuture = PeticionsHTTP.request(url);
  provinciesFuture.then((data) {
    print("Resposta asíncrona amb Futures: ${data.toString()}");
  });
}

void exemple4() {
  /* Exemple 4:
     Realitza una petició HTTP i indica
     què fer amb la resposta fent ús callbacks
  */

  PeticionsHTTP.requestCallbacks(url, onResponse: (data) {
    print("Resposta asíncrona amb callbacks: ${data.toString()}");
  }, onError: (data) {
    print("Error: ${data.toString()}");
  });
}

/* Múltiples peticions */

void exemple5() async {
/* Exemple 5:
   Realitza una petició HTTP i obté de forma
   síncrona la llista de províncies.
   Per a cada província, realitza una nova 
   petició HTTP de manera que obté les 
   comarques d'aquesta, i mostra la comarca
   junt amb la capital.
  */

  var llistaProvincies = await PeticionsHTTP.request(url);

  for (var provincia in (llistaProvincies as List)) {
    String urlComarques = "$baseUrl/api/comarques/$provincia";
    // Per a cada província, fem una nova consulta
    var comarques = await PeticionsHTTP.requestSync(urlComarques);
    //print(comarques.runtimeType);
    print("\nComarques de $provincia\n");
    for (var comarca in (comarques as List)) {
      print("${comarca["comarca"]}, capital ${comarca["capital"]}");
    }
  }
}

void exemple6() async {
  /* Exemple 6:
    Relitza la consulta de l'exemple 5, però
    ara, crea una objecte de tipus Comarca per
    emmagatzemar la informació,, que després mostrarà.
  */

  var llistaProvincies = await PeticionsHTTP.request(url);

  for (var provincia in (llistaProvincies as List)) {
    String urlComarques = "$baseUrl/api/comarques/$provincia";
    // Per a cada província, fem una nova consulta
    var comarques = await PeticionsHTTP.requestSync(urlComarques);
    List<Comarca> llistaComarques = [];

    // Si volem saber el tipus de la dada
    //print(comarques.runtimeType);

    for (var comarca in (comarques as List)) {
      llistaComarques.add(Comarca(
          nom: comarca["comarca"],
          capital: comarca["capital"],
          poblacio: comarca["poblacio"]));
    }

    for (var comarca in llistaComarques) {
      print("${comarca.nom}, capital ${comarca.capital}, "
          "amb població de ${comarca.poblacio} habitants.");
    }
  }
}

void exemple7() async {
  /* Exemple 7:
    Fa el mateix que l'exemple 6, però 
    afig també la informació sobre la 
    província a cada comarca.
   */

  var llistaProvincies2 = await PeticionsHTTP.request(url);

  for (var provincia in (llistaProvincies2 as List)) {
    String urlComarques = "$baseUrl/api/comarques/$provincia";
    // Per a cada província, fem una nova consulta
    var comarques = await PeticionsHTTP.requestSync(urlComarques);
    List<Comarca> llistaComarques = [];

    //print(comarques.runtimeType);
    //print("\nComarques de $provincia\n");
    for (var comarca in (comarques as List)) {
      llistaComarques.add(Comarca(
          nom: comarca["comarca"],
          capital: comarca["capital"],
          poblacio: comarca["poblacio"],
          provincia: provincia));
    }

    for (var comarca in llistaComarques) {
      print("${comarca.nom} (${comarca.provincia}),"
          "capital ${comarca.capital}, "
          "amb població de ${comarca.poblacio} habitants.");
    }
  }
}

void exemple8() async {
  /* Exemple 8:
    Realtza una petició HTTP síncrona i 
    crea una llista de comarques, però ara
    Mitjançant mapat d'estructures.

  */

  var llistaProvincies3 = await PeticionsHTTP.request(url);

  for (var provincia in (llistaProvincies3 as List)) {
    String urlComarques = "$baseUrl/api/comarques/$provincia";
    // Per a cada província, fem una nova consulta
    var comarques = await PeticionsHTTP.requestSync(urlComarques);
    List<Comarca>? llistaComarques = [];

    llistaComarques = (comarques as List)
        .map((comarca) => Comarca(
            nom: comarca["comarca"],
            capital: comarca["capital"],
            poblacio: comarca["poblacio"],
            provincia: provincia))
        .toList();

    for (var comarca in llistaComarques) {
      print("${comarca.nom} (${comarca.provincia}),"
          "capital ${comarca.capital}, "
          "amb població de ${comarca.poblacio} habitants.");
    }
  }
}

void exemple9() {
  /* Exemple 9:
     Obté la llista de províncies i després de 
     comarques per província fent ús de callbacks.
  */

// Realitzem la petició HTTP
  PeticionsHTTP.requestCallbacks(url, onResponse: (provincies) {
    // Primer callback. Quan rebem la llista de províncies,
    // la recorrem i fem per a cada província una nova
    // petició HTTP per obtindre lescomarques dela província.
    for (var provincia in provincies) {
      String urlComarques = "$baseUrl/api/comarques/$provincia";
      PeticionsHTTP.requestCallbacks(urlComarques, onResponse: (comarques) {
        // En aquest callback, ja hem obtés la llista de comarques
        // com a resposta, de manera que ara el que fem serà mostrar-les.
        for (var comarca in comarques) {
          print(
              "${comarca["comarca"]} ($provincia), capital ${comarca["capital"]} (Població: ${comarca["poblacio"]})}");
        }
      }, onError: (data) {
        print("Error: ${data.toString()}");
      });
    }
  }, onError: (data) {
    print("Error: ${data.toString()}");
  });
}
