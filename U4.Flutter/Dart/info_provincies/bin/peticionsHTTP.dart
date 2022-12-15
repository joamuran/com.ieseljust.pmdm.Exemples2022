import 'dart:convert';
import 'package:http/http.dart' as http;
//import 'package:http/http.dart';

class PeticionsHTTP {
  static void requestAndShow(String url) {
    /* 
    Aquest mètode fa una petició HTTP i mostra el resultat per pantalla
    */

    // El mètode http.get rep com a argument un objecte de tipus URI,
    // que obtenim a partir de l'string amb la URI. La resposta que
    // obté és un Future, que es resoldrà posteriorment.

    var response = http.get(Uri.parse(url));
    // Quan es resol el Future, s'obté un objecte JSON (data)
    response.then((data) {
      dynamic body;
      // Aquest JSON té un component statusCode, amb el codi
      // de la resposta HTTP.

      if (data.statusCode == 200) {
        // El JSON Data té un altre component body, amb el cos
        // de la resposta. Fem ús de la funció jsonDecode per
        // obtenir una llista de l'objecte JSON, que és en aquest
        // cas una llista amb les províncies.
        body = jsonDecode(data.body);

        // body = jsonDecode(data.body) as List;

        // Una vegada tenim les províncies, les mostrem per pantalla
        print(body.toString());
      }
    });
  }

  static dynamic requestSync(String url) async {
    // Fem ús d'await per esperar la resposta a la petició.
    // Quan rebem la resposta, aquesta la tindrem directament en data.
    //
    var data = await http.get(Uri.parse(url));

    // Si la petició es correcta, retornem la llista de províncies
    if (data.statusCode == 200) {
      return jsonDecode(data.body);
    } else {
      // En cas contrari, retornem null
      return null;
    }
  }

  static Future<dynamic> request(url) {
    // Retornem un tipus Future, que es resoldrà
    // a la resposta de la petició.
    var response = http.get(Uri.parse(url));

    // Amb then, definim l'acció a realitzar
    // mitjançant una funció anònima, que es
    // dispararà quan el Future s'haja resolt, és a dir,
    // quan s'haja rebut resposta del servidor.

    return response.then((data) {
      List? response;

      if (data.statusCode == 200) {
        response = jsonDecode(data.body) as List;
        return response;
      } else {
        return null;
      }
    });
  }

  static void requestCallbacks(String url,
      {required var onResponse, required var onError}) {
    // Rep dos callbacks, un si la petició té èxit i altre
    // si dóna error

    var response = http.get(Uri.parse(url));

    response.then((data) {
      if (data.statusCode == 200) {
        onResponse(jsonDecode(data.body));
      } else {
        onError(data.statusCode);
      }
    });
  }
}
