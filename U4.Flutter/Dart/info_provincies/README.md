# Info Províncies

Aplicació Dart amb diversos exemples d'ús de la llibrería HTTP, atacant a l'API [node-comarques-rest-server](https://github.com/joamuran/node-comarques-rest-server).

## Exemples

Per llançar un o altre exemple, fem ús de:

```
dart run bin/info_provincies.dart exempleX
```

Sent `exempleX` l'exemple que volem comprovar.

Els diferents exemples són:

* **Exemple 1**. Realitza una petició HTTP i mostra la resposta directament.
* **Exemple 2**. Realitza una petició HTTP per obtenir les províncies de forma síncrona i les imprimeix.
* **Exemple 3**. Realitza una petició HTTP i obté les províncies fent ús d'un `Future`.
* **Exemple 4**. Realitza una petició HTTP i indica què fer amb la resposta fent ús callbacks

Exemples amb diverses peticions encadenades:

* **Exemple 5**. Realitza una petició HTTP i obté de forma síncrona la llista de províncies. Per a cada província, realitza una nova petició HTTP de manera que obté les comarques d'aquesta, i mostra la comarca junt amb la capital.

* **Exemple 6**. Relitza la consulta de l'exemple 5, però ara, crea una objecte de tipus Comarca per emmagatzemar la informació, que mostrarà posteriorment.

* **Exemple 7**. Fa el mateix que l'exemple 6, però afig també la informació sobre la província a cada comarca.

* **Exemple 8**. Realtza una petició HTTP síncrona i crea una llista de comarques, però ara Mitjançant mapat d'estructures.

* **Exemple 9**. Obté la llista de províncies i després de comarques per província fent ús de callbacks.

Tot i que les funcions que ofereix la llibrería *http* són molt senzilles, ens hem definit per fer els exemples de forma més còmoda la classe *PeticionsHTTP*, que conté els tres mètodes següents:

* `static void requestAndShow(String url)`: Realitza una petició HTTP i mostra el resultat per pantalla. Si hi ha un error en la connexió o en la petició, el mostra també.

* `static dynamic requestSync(String url) async`: Fa una petició síncrona sobre la URL indicada, fent ús d'`async/await` per tal d'esperar la resposta a la petició. Quan es rep la resposta, aquesta es retorna, ja siguen les dades en sí o un error.

* `static Future<dynamic> request(url)`: Realitza la petició asíncrona, retornant un tipus Future, que es resoldrà a la resposta de la petició.