class Comarca {
  String nom;
  String capital;
  String poblacio;
  String? provincia; // Provincia pot ser nul

  // Constructor amb pas d'arguments per nom
  Comarca(
      {this.provincia,
      required this.nom,
      required this.capital,
      required this.poblacio});
}
