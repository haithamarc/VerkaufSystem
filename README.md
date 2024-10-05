# Software Engineering Projekt - Team 05 - Bikes5

Dieses Projekt ist eine Webanwendung zum Verwalten von Fahrrädern, Bestellungen und Lagerbeständen. Es verwendet Jakarta EE, PostgreSQL und ist für WildFly konfiguriert.

1. Voraussetzungen

- Java JDK 19
- Maven 3.6.0 oder höher
- PostgreSQL 9.6 oder höher
- WildFly 24.0.0.Final oder höher

2. Installation

- Klone das Repository in einen lokalen Ordner.

- Stelle sicher, dass PostgreSQL installiert ist und läuft. Erstelle eine neue Datenbank mit dem Namen bikes und importiere die Datenbankstruktur und -inhalte aus dem bereitgestellten SQL-Skript.

- Füge die PostgreSQL JDBC-Treiberdatei postgresql-42.5.1.jar zum WildFly-Server hinzu.

- Erstelle eine neue JNDI-Datenquelle java:jboss/datasources/bikes5 in WildFly mit den Verbindungsdetails zur bikes-Datenbank und den Sicherheitseinstellungen für Benutzername und Passwort.

- Stelle sicher, dass die folgenden WildFly-Konfigurationen vorgenommen wurden:
    /subsystem=elytron/policy=jacc:add(jacc-policy={})
    /subsystem=undertow/application-security-domain=other
    :write-attribute(name=integrated-jaspi, value=false)

- Führe mvn clean install aus, um das Projekt zu bauen und die WAR-Datei zu generieren.

- Deploye die generierte WAR-Datei in WildFly.

3. Verwendung

- Öffne die Anwendung im Webbrowser und navigiere zu http://localhost:8080/team-05/.

- Logge dich anhand der E-Mail und Telefonnummer (ohne Vorwahl) einer Entität "Staff" ein. Jeder Staff ist mit einer Rolle verbunden :

* ADMIN: Zugriff auf alle Funktionen des Systems, inklusive Mitarbeiterdaten
* USER1: Lesender und schreibender Zugriff auf Master- und Detailseiten, aber kein Zugriff auf Mitarbeiterdaten
* USER2: Nur lesender Zugriff auf Masterseiten und kein Zugriff auf Mitarbeiterdaten

Die Anwendung ermöglicht das Anzeigen, Erstellen, Bearbeiten und Löschen von Fahrrädern, Kunden, Bestellungen, Personal und verwandten Entitäten.

4. Dokumentation

Eine detaillierte Dokumentation der Anwendungsfunktionalitäten ist im generierten Javadoc zu finden.

5. Beitragende

- Marion Donald Tchokote Ngayap
- Claude Djatou Lakoudij
- Haitham Albagdady
- Max Rollwage