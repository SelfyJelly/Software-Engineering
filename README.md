# Software-Engineering - Task Organizer📋

Ein Java-basiertes Software-Engineering-Projekt zur Entwicklung einer Desktop-Anwendung für die Aufgabenverwaltung.
Das Projekt wird schrittweise im Rahmen mehrerer Übungen entwickelt. Jede Übung erweitert die bestehende Anwendung um neue Funktionen. Dabei liegt der Fokus auf einer klaren Softwarearchitektur, der Trennung von Verantwortlichkeiten und der Wiederverwendbarkeit des Codes.

---

## Projektübersicht
Der Task Organizer soll Benutzern ermöglichen, ihre Aufgaben effizient zu organisieren und zu verwalten.

### Geplante Funktionen
- Aufgaben erstellen, anzeigen, bearbeiten und löschen
- Aufgaben nach Priorität filtern
- Aufgaben anhand ihres Fälligkeitsdatums anzeigen
- Geschätzten Arbeitsaufwand berechnen
- Aufgaben dauerhaft in einer SQLite-Datenbank speichern
- Aufgaben über eine grafische Benutzeroberfläche verwalten
---
## Übungen
### Übung 01
Zunächst wird die grundlegende Aufgabenlogik implementiert. 
- Modellierung von Aufgaben und Prioritäten
- Implementierung der Geschäftslogik
- Verarbeitung und Filterung von Aufgaben
- Einlesen von Aufgaben aus CSV-Dateien

### Übung 02
Anschließend wird eine SQLite-Datenbank integriert, um Aufgaben dauerhaft zu speichern. 
- Integration einer SQLite-Datenbank
- Datenbankzugriff über JDBC und ORMLite
- Implementierung von CRUD-Operationen (Create, Read, Update, Delete)
- Trennung von Datenbankmodellen und Geschäftslogik
- Verwendung des Repository-Patterns

### Übung 03: 
Im nächsten Schritt wird eine grafische Benutzeroberfläche (GUI) entwickelt.
- Darstellung der gespeicherten Aufgaben
- Benutzerinteraktion über eine GUI
- Erstellung und Bearbeitung von Aufgaben über die Oberfläche
- Anbindung der GUI an die bestehende Geschäftslogik
---
## Verwendete Technologien
- Java
- SQLite
- JDBC
- ORMLite
- Git & Github
- VS Code / Eclips
---
## Projektziel
Das Endergebnis soll eine funktionsfähige Desktop-Anwendung sein, mit der Benutzer Aufgaben erstellen, anzeigen, bearbeiten und löschen können. Zusätzlich sollen Aufgaben nach Priorität und Fälligkeitsdatum gefiltert sowie der geschätzte Arbeitsaufwand berechnet werden.

Ziel des Projekts ist es, eine strukturierte Java-Anwendung zu entwickeln, bei der Benutzeroberfläche, Programmlogik und Datenbankzugriff sauber voneinander getrennt sind.
