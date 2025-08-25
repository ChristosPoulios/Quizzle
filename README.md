# Quizzle

## Programm starten

### Voraussetzungen

1. **Java Development Kit (JDK)** installiert (mindestens Java 8)
2. **MariaDB Server** installiert und gestartet
3. Eine Datenbank namens `quizzle_db` auf dem MariaDB Server

### Datenbank einrichten

1. MariaDB Server starten
2. Eine Datenbank mit dem Namen `quizzle_db` erstellen
2. quizzle.sql ausführen (im Ordner src/persistence/mariadb/)
3. Sicherstellen, dass die Verbindungsdaten in `config.properties` korrekt sind:
   - Host: localhost:3306
   - Datenbank: quizzle_db
   - Benutzer: root
   - Passwort: (wie in config.properties konfiguriert)

### Hinweise

- Das Programm benötigt eine aktive Verbindung zur MariaDB-Datenbank
- Die MariaDB JDBC-Treiber sind bereits in `lib/mariadb-java-client-3.5.5.jar` enthalten
- Bei Problemen die Datenbankverbindung in `config.properties` überprüfen