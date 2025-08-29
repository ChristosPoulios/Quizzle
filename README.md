# Quizzle

Quizzle ist eine Java-basierte Quizanwendung mit persistenter Speicherung in einer MariaDB-Datenbank und als Fallback für die persistente Speicherung
eine Datei-basierte Speicherung,

## Features

- Erstellen, Verwalten und Durchführen von Quizzen  
- Speicherung aller Daten in einer MariaDB-Datenbank  
- Zentrale Konfiguration per properties-Datei  
- Verwendung des MariaDB JDBC-Treibers (liegt im Ordner `lib/`)  

## Voraussetzungen

- Java Development Kit (JDK) 8 oder neuer  
- MariaDB-Server (installiert und gestartet)  

## Setup und Installation

1. MariaDB-Server starten  
2. Das SQL-Skript `quizzle.sql` im Ordner `src/persistence/mariadb/` ausführen  
3. Datei `config.properties` mit passenden Einstellungen anlegen (siehe Beispiel unten)  
4. Projekt mit Java-IDE oder über Konsole kompilieren und starten  

## Beispiel: config.properties

```
application.title=Quizzle
application.version=1.0
database.driver=org.mariadb.jdbc.Driver
database.password=password
database.url=url_to_database
database.user=user
theme.default=System
window.height=768
window.maximized=false
window.width=1024
```

Passen Sie insbesondere folgende Einträge auf die lokale Umgebung an:  
- `database.url` (z. B. `jdbc:mariadb://localhost:3306/quizzle_db`)  
- `database.user` und `database.password` entsprechend Ihren Datenbankdaten  

## Verzeichnisstruktur

- `src/` – Der gesamte Quellcode  
- `lib/` – Externe Bibliotheken, z. B. den mariadb-java-client  
- `config.properties` – Zentrale Konfigurationsdatei  
- `src/persistence/mariadb/quizzle.sql` – SQL-Setupskript zur Datenbankinitialisierung  

## Lizenz

Dieses Projekt steht unter der MIT-Lizenz.

