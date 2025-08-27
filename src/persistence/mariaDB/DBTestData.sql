-- Quizzle Testdaten - Teil 1 von 4
-- 10 Themen mit je 15 Fragen und 4 Antworten pro Frage

USE quizzle_db;

-- Themen einfügen
INSERT INTO Theme (id, title, description) VALUES
                                               (1, 'Mathematik', 'Grundlagen der Mathematik, Algebra und Geometrie'),
                                               (2, 'Geschichte', 'Wichtige historische Ereignisse und Persönlichkeiten'),
                                               (3, 'Geographie', 'Länder, Hauptstädte, Flüsse und geografische Besonderheiten'),
                                               (4, 'Biologie', 'Lebewesen, Anatomie und biologische Prozesse'),
                                               (5, 'Physik', 'Naturgesetze, Kräfte und physikalische Phänomene'),
                                               (6, 'Chemie', 'Elemente, Verbindungen und chemische Reaktionen'),
                                               (7, 'Literatur', 'Berühmte Autoren und literarische Werke'),
                                               (8, 'Sport', 'Sportarten, Regeln und berühmte Sportler'),
                                               (9, 'Musik', 'Komponisten, Instrumente und Musiktheorie'),
                                               (10, 'Technologie', 'Computer, Internet und moderne Technologien');

-- THEMA 1: MATHEMATIK (Fragen 1-15)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (1, 'Ableitung von x²', 'Was ist die Ableitung von x²?', 1),
                                                      (2, 'Kreiszahl Pi', 'Welcher Wert entspricht ungefähr der Kreiszahl π?', 1),
                                                      (3, 'Quadratwurzel', 'Was ist die Quadratwurzel von 64?', 1),
                                                      (4, 'Pythagoras', 'Wie lautet der Satz des Pythagoras?', 1),
                                                      (5, 'Primzahl', 'Welche der folgenden Zahlen ist eine Primzahl?', 1),
                                                      (6, 'Prozentrechnung', 'Was sind 25% von 200?', 1),
                                                      (7, 'Flächeninhalt Kreis', 'Wie berechnet man den Flächeninhalt eines Kreises?', 1),
                                                      (8, 'Gleichung lösen', 'Was ist x in der Gleichung 2x + 6 = 14?', 1),
                                                      (9, 'Winkel im Dreieck', 'Wie viel Grad haben die Innenwinkel eines Dreiecks zusammen?', 1),
                                                      (10, 'Fakultät', 'Was ist 5! (5 Fakultät)?', 1),
                                                      (11, 'Logarithmus', 'Was ist log₁₀(100)?', 1),
                                                      (12, 'Bruchrechnung', 'Was ist 3/4 + 1/8?', 1),
                                                      (13, 'Negative Zahlen', 'Was ist (-3) × (-4)?', 1),
                                                      (14, 'Quadratische Gleichung', 'Welche Formel löst ax² + bx + c = 0?', 1),
                                                      (15, 'Geometrie', 'Wie viele Ecken hat ein Oktagon?', 1);

-- Antworten Mathematik (IDs 1-60)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (1, '2x', TRUE, 1), (2, 'x', FALSE, 1), (3, 'x²', FALSE, 1), (4, '2x²', FALSE, 1),
                                                           (5, '3,14159', TRUE, 2), (6, '2,71828', FALSE, 2), (7, '1,41421', FALSE, 2), (8, '3,0', FALSE, 2),
                                                           (9, '8', TRUE, 3), (10, '6', FALSE, 3), (11, '32', FALSE, 3), (12, '16', FALSE, 3),
                                                           (13, 'a² + b² = c²', TRUE, 4), (14, 'a + b = c', FALSE, 4), (15, 'a × b = c²', FALSE, 4), (16, 'a² - b² = c²', FALSE, 4),
                                                           (17, '17', TRUE, 5), (18, '23', TRUE, 5), (19, '15', FALSE, 5), (20, '21', FALSE, 5),
                                                           (21, '50', TRUE, 6), (22, '25', FALSE, 6), (23, '75', FALSE, 6), (24, '100', FALSE, 6),
                                                           (25, 'π × r²', TRUE, 7), (26, '2 × π × r', FALSE, 7), (27, 'π × d', FALSE, 7), (28, 'r² × 4', FALSE, 7),
                                                           (29, '4', TRUE, 8), (30, '3', FALSE, 8), (31, '5', FALSE, 8), (32, '7', FALSE, 8),
                                                           (33, '180°', TRUE, 9), (34, '360°', FALSE, 9), (35, '90°', FALSE, 9), (36, '270°', FALSE, 9),
                                                           (37, '120', TRUE, 10), (38, '25', FALSE, 10), (39, '15', FALSE, 10), (40, '60', FALSE, 10),
                                                           (41, '2', TRUE, 11), (42, '10', FALSE, 11), (43, '100', FALSE, 11), (44, '1', FALSE, 11),
                                                           (45, '7/8', TRUE, 12), (46, '4/12', FALSE, 12), (47, '1/2', FALSE, 12), (48, '5/8', FALSE, 12),
                                                           (49, '12', TRUE, 13), (50, '-12', FALSE, 13), (51, '7', FALSE, 13), (52, '-7', FALSE, 13),
                                                           (53, 'x = (-b ± √(b²-4ac)) / 2a', TRUE, 14), (54, 'x = -b / 2a', FALSE, 14), (55, 'x = √(b²-4ac)', FALSE, 14), (56, 'x = a + b + c', FALSE, 14),
                                                           (57, '8', TRUE, 15), (58, '6', FALSE, 15), (59, '10', FALSE, 15), (60, '12', FALSE, 15);

-- THEMA 2: GESCHICHTE (Fragen 16-30)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (16, 'Erster Bundeskanzler', 'Wer war der erste Bundeskanzler der Bundesrepublik Deutschland?', 2),
                                                      (17, 'Zweiter Weltkrieg', 'Wann begann der Zweite Weltkrieg?', 2),
                                                      (18, 'Berliner Mauer', 'In welchem Jahr wurde die Berliner Mauer gebaut?', 2),
                                                      (19, 'Französische Revolution', 'Wann begann die Französische Revolution?', 2),
                                                      (20, 'Römisches Reich', 'Wer war der erste römische Kaiser?', 2),
                                                      (21, 'Amerika Entdeckung', 'In welchem Jahr entdeckte Kolumbus Amerika?', 2),
                                                      (22, 'Deutsche Wiedervereinigung', 'Wann fand die deutsche Wiedervereinigung statt?', 2),
                                                      (23, 'Erster Weltkrieg', 'Von wann bis wann dauerte der Erste Weltkrieg?', 2),
                                                      (24, 'Napoleon', 'In welcher Schlacht wurde Napoleon endgültig besiegt?', 2),
                                                      (25, 'Kalter Krieg', 'Welche Supermächte standen sich im Kalten Krieg gegenüber?', 2),
                                                      (26, 'Mittelalter', 'Was war ein Ritter im Mittelalter?', 2),
                                                      (27, 'Antike Griechenland', 'Wer war Sokrates?', 2),
                                                      (28, 'Industrielle Revolution', 'Wann begann die Industrielle Revolution?', 2),
                                                      (29, 'DDR', 'Wofür stand die Abkürzung DDR?', 2),
                                                      (30, 'Reformation', 'Wer begann die Reformation?', 2);

-- Antworten Geschichte (IDs 61-120)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (61, 'Konrad Adenauer', TRUE, 16), (62, 'Willy Brandt', FALSE, 16), (63, 'Helmut Kohl', FALSE, 16), (64, 'Ludwig Erhard', FALSE, 16),
                                                           (65, '1939', TRUE, 17), (66, '1938', FALSE, 17), (67, '1940', FALSE, 17), (68, '1941', FALSE, 17),
                                                           (69, '1961', TRUE, 18), (70, '1959', FALSE, 18), (71, '1963', FALSE, 18), (72, '1965', FALSE, 18),
                                                           (73, '1789', TRUE, 19), (74, '1776', FALSE, 19), (75, '1792', FALSE, 19), (76, '1804', FALSE, 19),
                                                           (77, 'Augustus', TRUE, 20), (78, 'Julius Caesar', FALSE, 20), (79, 'Nero', FALSE, 20), (80, 'Trajan', FALSE, 20),
                                                           (81, '1492', TRUE, 21), (82, '1490', FALSE, 21), (83, '1498', FALSE, 21), (84, '1500', FALSE, 21),
                                                           (85, '1990', TRUE, 22), (86, '1989', FALSE, 22), (87, '1991', FALSE, 22), (88, '1988', FALSE, 22),
                                                           (89, '1914-1918', TRUE, 23), (90, '1912-1916', FALSE, 23), (91, '1915-1919', FALSE, 23), (92, '1913-1917', FALSE, 23),
                                                           (93, 'Waterloo', TRUE, 24), (94, 'Austerlitz', FALSE, 24), (95, 'Jena', FALSE, 24), (96, 'Leipzig', FALSE, 24),
                                                           (97, 'USA', TRUE, 25), (98, 'Sowjetunion', TRUE, 25), (99, 'China', FALSE, 25), (100, 'England', FALSE, 25),
                                                           (101, 'Krieger zu Pferd', TRUE, 26), (102, 'Adliger Grundbesitzer', TRUE, 26), (103, 'Einfacher Bauer', FALSE, 26), (104, 'Handwerker', FALSE, 26),
                                                           (105, 'Griechischer Philosoph', TRUE, 27), (106, 'Römischer Kaiser', FALSE, 27), (107, 'Mathematiker', FALSE, 27), (108, 'Dichter', FALSE, 27),
                                                           (109, '18. Jahrhundert', TRUE, 28), (110, '16. Jahrhundert', FALSE, 28), (111, '19. Jahrhundert', FALSE, 28), (112, '17. Jahrhundert', FALSE, 28),
                                                           (113, 'Deutsche Demokratische Republik', TRUE, 29), (114, 'Deutsche Demokratische Region', FALSE, 29), (115, 'Deutsches Demokratisches Reich', FALSE, 29), (116, 'Deutsche Diktatur Regime', FALSE, 29),
                                                           (117, 'Martin Luther', TRUE, 30), (118, 'Johannes Calvin', FALSE, 30), (119, 'Papst Leo X.', FALSE, 30), (120, 'Thomas Müntzer', FALSE, 30);

-- THEMA 3: GEOGRAPHIE (Fragen 31-45)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (31, 'Höchster Berg', 'Wie heißt der höchste Berg der Welt?', 3),
                                                      (32, 'Hauptstadt Kanada', 'Was ist die Hauptstadt von Kanada?', 3),
                                                      (33, 'Längster Fluss', 'Welcher ist der längste Fluss der Welt?', 3),
                                                      (34, 'Größter Kontinent', 'Welcher ist der größte Kontinent der Erde?', 3),
                                                      (35, 'Australien Hauptstadt', 'Wie heißt die Hauptstadt von Australien?', 3),
                                                      (36, 'Mittelmeer Inseln', 'Welche Inseln liegen im Mittelmeer?', 3),
                                                      (37, 'Sahara', 'In welchem Kontinent liegt die Sahara?', 3),
                                                      (38, 'Tiefster Punkt', 'Wie heißt der tiefste Punkt der Erde?', 3),
                                                      (39, 'Amazonas', 'Durch welche Länder fließt der Amazonas?', 3),
                                                      (40, 'Alpen', 'In welchen Ländern liegen die Alpen?', 3),
                                                      (41, 'Pazifik', 'Welcher ist der größte Ozean?', 3),
                                                      (42, 'Vulkane', 'Wo gibt es die meisten aktiven Vulkane?', 3),
                                                      (43, 'Wüsten', 'Welche ist die größte Wüste der Welt?', 3),
                                                      (44, 'Europa Flüsse', 'Welcher Fluss fließt durch Paris?', 3),
                                                      (45, 'Asien Länder', 'Welches ist das bevölkerungsreichste Land der Welt?', 3);

-- Antworten Geographie (IDs 121-180)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (121, 'Mount Everest', TRUE, 31), (122, 'K2', FALSE, 31), (123, 'Mont Blanc', FALSE, 31), (124, 'Kilimandscharo', FALSE, 31),
                                                           (125, 'Ottawa', TRUE, 32), (126, 'Toronto', FALSE, 32), (127, 'Montreal', FALSE, 32), (128, 'Vancouver', FALSE, 32),
                                                           (129, 'Nil', TRUE, 33), (130, 'Amazonas', TRUE, 33), (131, 'Mississippi', FALSE, 33), (132, 'Jangtse', FALSE, 33),
                                                           (133, 'Asien', TRUE, 34), (134, 'Afrika', FALSE, 34), (135, 'Europa', FALSE, 34), (136, 'Amerika', FALSE, 34),
                                                           (137, 'Canberra', TRUE, 35), (138, 'Sydney', FALSE, 35), (139, 'Melbourne', FALSE, 35), (140, 'Perth', FALSE, 35),
                                                           (141, 'Sizilien', TRUE, 36), (142, 'Sardinien', TRUE, 36), (143, 'Kreta', FALSE, 36), (144, 'Malta', FALSE, 36),
                                                           (145, 'Afrika', TRUE, 37), (146, 'Asien', FALSE, 37), (147, 'Australien', FALSE, 37), (148, 'Amerika', FALSE, 37),
                                                           (149, 'Marianengraben', TRUE, 38), (150, 'Totes Meer', FALSE, 38), (151, 'Grand Canyon', FALSE, 38), (152, 'Death Valley', FALSE, 38),
                                                           (153, 'Brasilien', TRUE, 39), (154, 'Peru', TRUE, 39), (155, 'Kolumbien', FALSE, 39), (156, 'Venezuela', FALSE, 39),
                                                           (157, 'Deutschland', TRUE, 40), (158, 'Österreich', TRUE, 40), (159, 'Polen', FALSE, 40), (160, 'Tschechien', FALSE, 40),
                                                           (161, 'Pazifischer Ozean', TRUE, 41), (162, 'Atlantischer Ozean', FALSE, 41), (163, 'Indischer Ozean', FALSE, 41), (164, 'Arktischer Ozean', FALSE, 41),
                                                           (165, 'Pazifischer Feuerring', TRUE, 42), (166, 'Island', FALSE, 42), (167, 'Italien', FALSE, 42), (168, 'Hawaii', FALSE, 42),
                                                           (169, 'Antarktis', TRUE, 43), (170, 'Sahara', FALSE, 43), (171, 'Gobi', FALSE, 43), (172, 'Kalahari', FALSE, 43),
                                                           (173, 'Seine', TRUE, 44), (174, 'Loire', FALSE, 44), (175, 'Rhône', FALSE, 44), (176, 'Garonne', FALSE, 44),
                                                           (177, 'China', TRUE, 45), (178, 'Indien', FALSE, 45), (179, 'USA', FALSE, 45), (180, 'Indonesien', FALSE, 45);

-- Quizzle Testdaten - Teil 2 von 4
-- Themen: Biologie, Physik, Chemie

-- THEMA 4: BIOLOGIE (Fragen 46-60)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (46, 'DNA Funktion', 'Was ist die Hauptfunktion der DNA?', 4),
                                                      (47, 'Photosynthese', 'Was benötigen Pflanzen für die Photosynthese?', 4),
                                                      (48, 'Herz Kammern', 'Wie viele Kammern hat das menschliche Herz?', 4),
                                                      (49, 'Wirbeltiere', 'Welche Tiere gehören zu den Wirbeltieren?', 4),
                                                      (50, 'Zellorganellen', 'Welche Organelle ist für die Energieproduktion zuständig?', 4),
                                                      (51, 'Blutgruppen', 'Welche Blutgruppen gibt es im AB0-System?', 4),
                                                      (52, 'Atmung', 'Welches Gas nehmen wir beim Einatmen auf?', 4),
                                                      (53, 'Evolution', 'Wer entwickelte die Evolutionstheorie?', 4),
                                                      (54, 'Nervensystem', 'Was ist die Grundeinheit des Nervensystems?', 4),
                                                      (55, 'Verdauung', 'Wo beginnt die Verdauung der Nahrung?', 4),
                                                      (56, 'Genetik', 'Was sind Chromosomen?', 4),
                                                      (57, 'Ökosystem', 'Was sind Produzenten in einem Ökosystem?', 4),
                                                      (58, 'Stoffwechsel', 'Was ist der Grundumsatz?', 4),
                                                      (59, 'Immunsystem', 'Welche Zellen gehören zum Immunsystem?', 4),
                                                      (60, 'Fortpflanzung', 'Wie lange dauert eine normale Schwangerschaft beim Menschen?', 4);

-- Antworten Biologie (IDs 181-240)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (181, 'Genetische Information speichern', TRUE, 46), (182, 'Energie produzieren', FALSE, 46), (183, 'Sauerstoff transportieren', FALSE, 46), (184, 'Nahrung verdauen', FALSE, 46),
                                                           (185, 'Sonnenlicht', TRUE, 47), (186, 'Kohlendioxid', TRUE, 47), (187, 'Sauerstoff', FALSE, 47), (188, 'Stickstoff', FALSE, 47),
                                                           (189, '4', TRUE, 48), (190, '2', FALSE, 48), (191, '3', FALSE, 48), (192, '6', FALSE, 48),
                                                           (193, 'Säugetiere', TRUE, 49), (194, 'Vögel', TRUE, 49), (195, 'Insekten', FALSE, 49), (196, 'Spinnen', FALSE, 49),
                                                           (197, 'Mitochondrium', TRUE, 50), (198, 'Zellkern', FALSE, 50), (199, 'Ribosom', FALSE, 50), (200, 'Golgi-Apparat', FALSE, 50),
                                                           (201, 'A', TRUE, 51), (202, 'B', TRUE, 51), (203, 'AB', FALSE, 51), (204, '0', FALSE, 51),
                                                           (205, 'Sauerstoff', TRUE, 52), (206, 'Kohlendioxid', FALSE, 52), (207, 'Stickstoff', FALSE, 52), (208, 'Wasserstoff', FALSE, 52),
                                                           (209, 'Charles Darwin', TRUE, 53), (210, 'Gregor Mendel', FALSE, 53), (211, 'Louis Pasteur', FALSE, 53), (212, 'Alexander Fleming', FALSE, 53),
                                                           (213, 'Neuron', TRUE, 54), (214, 'Synapse', FALSE, 54), (215, 'Axon', FALSE, 54), (216, 'Dendrit', FALSE, 54),
                                                           (217, 'Im Mund', TRUE, 55), (218, 'Im Magen', FALSE, 55), (219, 'Im Darm', FALSE, 55), (220, 'In der Speiseröhre', FALSE, 55),
                                                           (221, 'Träger der Erbinformation', TRUE, 56), (222, 'Zellorganellen', FALSE, 56), (223, 'Proteine', FALSE, 56), (224, 'Enzyme', FALSE, 56),
                                                           (225, 'Grüne Pflanzen', TRUE, 57), (226, 'Bakterien', FALSE, 57), (227, 'Pilze', FALSE, 57), (228, 'Tiere', FALSE, 57),
                                                           (229, 'Energieverbrauch in Ruhe', TRUE, 58), (230, 'Maximaler Energieverbrauch', FALSE, 58), (231, 'Verdauungsenergie', FALSE, 58), (232, 'Bewegungsenergie', FALSE, 58),
                                                           (233, 'Weiße Blutkörperchen', TRUE, 59), (234, 'Antikörper', TRUE, 59), (235, 'Rote Blutkörperchen', FALSE, 59), (236, 'Blutplättchen', FALSE, 59),
                                                           (237, '9 Monate', TRUE, 60), (238, '8 Monate', FALSE, 60), (239, '10 Monate', FALSE, 60), (240, '7 Monate', FALSE, 60);

-- THEMA 5: PHYSIK (Fragen 61-75)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (61, 'Lichtgeschwindigkeit', 'Wie schnell ist das Licht im Vakuum?', 5),
                                                      (62, 'Newton Gesetze', 'Wie lautet Newtons erstes Gesetz?', 5),
                                                      (63, 'Energie Formen', 'Welche Energieformen gibt es?', 5),
                                                      (64, 'Elektrizität', 'Was ist elektrischer Strom?', 5),
                                                      (65, 'Wellenlehre', 'Was charakterisiert eine Welle?', 5),
                                                      (66, 'Thermodynamik', 'Was besagt der erste Hauptsatz der Thermodynamik?', 5),
                                                      (67, 'Magnetismus', 'Was erzeugt ein Magnetfeld?', 5),
                                                      (68, 'Optik', 'Welche Eigenschaft hat weißes Licht?', 5),
                                                      (69, 'Mechanik', 'Was ist Beschleunigung?', 5),
                                                      (70, 'Atomphysik', 'Woraus bestehen Atome?', 5),
                                                      (71, 'Schall', 'Wie breitet sich Schall aus?', 5),
                                                      (72, 'Gravitation', 'Was verursacht die Gravitation?', 5),
                                                      (73, 'Druck', 'Wie wird Druck definiert?', 5),
                                                      (74, 'Radioaktivität', 'Was sind Alphastrahlen?', 5),
                                                      (75, 'Quantenphysik', 'Was ist ein Photon?', 5);

-- Antworten Physik (IDs 241-300)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (241, '299.792.458 m/s', TRUE, 61), (242, '300.000 km/s', FALSE, 61), (243, '250.000 km/s', FALSE, 61), (244, '400.000 km/s', FALSE, 61),
                                                           (245, 'Trägheitsgesetz', TRUE, 62), (246, 'Aktionsgesetz', FALSE, 62), (247, 'Reaktionsgesetz', FALSE, 62), (248, 'Gravitationsgesetz', FALSE, 62),
                                                           (249, 'Kinetische Energie', TRUE, 63), (250, 'Potentielle Energie', TRUE, 63), (251, 'Negative Energie', FALSE, 63), (252, 'Imaginäre Energie', FALSE, 63),
                                                           (253, 'Bewegung von Ladungsträgern', TRUE, 64), (254, 'Bewegung von Atomen', FALSE, 64), (255, 'Bewegung von Licht', FALSE, 64), (256, 'Bewegung von Neutronen', FALSE, 64),
                                                           (257, 'Frequenz', TRUE, 65), (258, 'Wellenlänge', TRUE, 65), (259, 'Masse', FALSE, 65), (260, 'Temperatur', FALSE, 65),
                                                           (261, 'Energieerhaltung', TRUE, 66), (262, 'Entropiezunahme', FALSE, 66), (263, 'Temperaturkonstanz', FALSE, 66), (264, 'Druckerhaltung', FALSE, 66),
                                                           (265, 'Bewegte elektrische Ladungen', TRUE, 67), (266, 'Permanentmagnete', TRUE, 67), (267, 'Neutrale Teilchen', FALSE, 67), (268, 'Licht', FALSE, 67),
                                                           (269, 'Zusammensetzung aller Farben', TRUE, 68), (270, 'Keine Farbe', FALSE, 68), (271, 'Nur rotes Licht', FALSE, 68), (272, 'Nur blaues Licht', FALSE, 68),
                                                           (273, 'Geschwindigkeitsänderung', TRUE, 69), (274, 'Geschwindigkeit', FALSE, 69), (275, 'Weg', FALSE, 69), (276, 'Zeit', FALSE, 69),
                                                           (277, 'Protonen', TRUE, 70), (278, 'Neutronen', TRUE, 70), (279, 'Photonen', FALSE, 70), (280, 'Quarks', FALSE, 70),
                                                           (281, 'Als Welle durch Medien', TRUE, 71), (282, 'Als Teilchen durch Vakuum', FALSE, 71), (283, 'Nur durch Flüssigkeiten', FALSE, 71), (284, 'Nur durch Festkörper', FALSE, 71),
                                                           (285, 'Masse', TRUE, 72), (286, 'Geschwindigkeit', FALSE, 72), (287, 'Temperatur', FALSE, 72), (288, 'Druck', FALSE, 72),
                                                           (289, 'Kraft pro Fläche', TRUE, 73), (290, 'Kraft pro Zeit', FALSE, 73), (291, 'Masse pro Volumen', FALSE, 73), (292, 'Energie pro Zeit', FALSE, 73),
                                                           (293, 'Heliumkerne', TRUE, 74), (294, 'Elektronen', FALSE, 74), (295, 'Neutronen', FALSE, 74), (296, 'Protonen', FALSE, 74),
                                                           (297, 'Lichtteilchen', TRUE, 75), (298, 'Elektron', FALSE, 75), (299, 'Proton', FALSE, 75), (300, 'Neutron', FALSE, 75);

-- THEMA 6: CHEMIE (Fragen 76-90)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (76, 'Periodensystem', 'Wie sind die Elemente im Periodensystem angeordnet?', 6),
                                                      (77, 'Wassermolekül', 'Welche Atome sind in einem Wassermolekül?', 6),
                                                      (78, 'pH-Wert', 'Was bedeutet ein pH-Wert von 7?', 6),
                                                      (79, 'Chemische Bindung', 'Was sind Ionen?', 6),
                                                      (80, 'Oxidation', 'Was passiert bei einer Oxidation?', 6),
                                                      (81, 'Edelgase', 'Welche Eigenschaft haben Edelgase?', 6),
                                                      (82, 'Säuren', 'Was charakterisiert eine Säure?', 6),
                                                      (83, 'Katalysator', 'Was bewirkt ein Katalysator?', 6),
                                                      (84, 'Atombau', 'Wo befinden sich die Elektronen?', 6),
                                                      (85, 'Chemische Reaktion', 'Was bleibt bei einer chemischen Reaktion erhalten?', 6),
                                                      (86, 'Kohlenstoff', 'Welche Bindungen kann Kohlenstoff eingehen?', 6),
                                                      (87, 'Metalle', 'Welche Eigenschaften haben Metalle?', 6),
                                                      (88, 'Aggregatzustände', 'Welche Aggregatzustände gibt es?', 6),
                                                      (89, 'Brennvorgang', 'Was ist Verbrennung chemisch gesehen?', 6),
                                                      (90, 'Lösungen', 'Was ist eine gesättigte Lösung?', 6);

-- Antworten Chemie (IDs 301-360)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (301, 'Nach Ordnungszahl', TRUE, 76), (302, 'Nach Masse', FALSE, 76), (303, 'Nach Größe', FALSE, 76), (304, 'Nach Farbe', FALSE, 76),
                                                           (305, 'Wasserstoff', TRUE, 77), (306, 'Sauerstoff', TRUE, 77), (307, 'Stickstoff', FALSE, 77), (308, 'Kohlenstoff', FALSE, 77),
                                                           (309, 'Neutral', TRUE, 78), (310, 'Basisch', FALSE, 78), (311, 'Sauer', FALSE, 78), (312, 'Alkalisch', FALSE, 78),
                                                           (313, 'Geladene Teilchen', TRUE, 79), (314, 'Neutrale Atome', FALSE, 79), (315, 'Moleküle', FALSE, 79), (316, 'Elektronen', FALSE, 79),
                                                           (317, 'Elektronenabgabe', TRUE, 80), (318, 'Elektronenaufnahme', FALSE, 80), (319, 'Protonenabgabe', FALSE, 80), (320, 'Neutronenaufnahme', FALSE, 80),
                                                           (321, 'Chemisch inert', TRUE, 81), (322, 'Vollständige Außenschale', TRUE, 81), (323, 'Hohe Reaktivität', FALSE, 81), (324, 'Metallischer Charakter', FALSE, 81),
                                                           (325, 'Gibt Protonen ab', TRUE, 82), (326, 'Niedriger pH-Wert', TRUE, 82), (327, 'Nimmt Protonen auf', FALSE, 82), (328, 'Hoher pH-Wert', FALSE, 82),
                                                           (329, 'Beschleunigt Reaktionen', TRUE, 83), (330, 'Wird nicht verbraucht', TRUE, 83), (331, 'Wird verbraucht', FALSE, 83), (332, 'Verlangsamt Reaktionen', FALSE, 83),
                                                           (333, 'In der Atomhülle', TRUE, 84), (334, 'Im Atomkern', FALSE, 84), (335, 'Außerhalb des Atoms', FALSE, 84), (336, 'Überall im Atom', FALSE, 84),
                                                           (337, 'Die Masse', TRUE, 85), (338, 'Die Anzahl der Atome', TRUE, 85), (339, 'Das Volumen', FALSE, 85), (340, 'Die Farbe', FALSE, 85),
                                                           (341, 'Einfachbindungen', TRUE, 86), (342, 'Mehrfachbindungen', TRUE, 86), (343, 'Nur Ionenbindungen', FALSE, 86), (344, 'Keine Bindungen', FALSE, 86),
                                                           (345, 'Elektrische Leitfähigkeit', TRUE, 87), (346, 'Metallglanz', TRUE, 87), (347, 'Geringe Dichte', FALSE, 87), (348, 'Schlechte Leitfähigkeit', FALSE, 87),
                                                           (349, 'Fest', TRUE, 88), (350, 'Flüssig', TRUE, 88), (351, 'Gasförmig', FALSE, 88), (352, 'Plasma', FALSE, 88),
                                                           (353, 'Oxidation mit Sauerstoff', TRUE, 89), (354, 'Exotherme Reaktion', TRUE, 89), (355, 'Endotherme Reaktion', FALSE, 89), (356, 'Reduktion', FALSE, 89),
                                                           (357, 'Maximale Konzentration erreicht', TRUE, 90), (358, 'Verdünnte Lösung', FALSE, 90), (359, 'Übersättigte Lösung', FALSE, 90), (360, 'Reine Substanz', FALSE, 90);
-- Quizzle Testdaten - Teil 3 von 4
-- Themen: Literatur, Sport, Musik

-- THEMA 7: LITERATUR (Fragen 91-105)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (91, 'Goethe Werke', 'Welches berühmte Werk schrieb Johann Wolfgang von Goethe?', 7),
                                                      (92, 'Shakespeare', 'Welche Tragödie schrieb William Shakespeare?', 7),
                                                      (93, 'Deutsche Literatur', 'Wer schrieb "Die Leiden des jungen Werther"?', 7),
                                                      (94, 'Nobelpreis Literatur', 'Welcher deutsche Autor erhielt den Literaturnobelpreis?', 7),
                                                      (95, 'Märchen', 'Wer sammelte deutsche Volksmärchen?', 7),
                                                      (96, 'Romantik', 'Welche Epoche folgte auf die Klassik?', 7),
                                                      (97, 'Moderne Literatur', 'Wer schrieb "Der Prozess"?', 7),
                                                      (98, 'Gedichtformen', 'Was ist ein Sonett?', 7),
                                                      (99, 'Weltliteratur', 'Wer schrieb "Don Quijote"?', 7),
                                                      (100, 'Deutsche Klassik', 'Welche Autoren gehören zur Weimarer Klassik?', 7),
                                                      (101, 'Epische Werke', 'Was ist ein Roman?', 7),
                                                      (102, 'Lyrik', 'Was charakterisiert ein Gedicht?', 7),
                                                      (103, 'Drama', 'Aus wie vielen Akten besteht ein klassisches Drama?', 7),
                                                      (104, 'Literaturkritik', 'Was ist eine Metapher?', 7),
                                                      (105, 'Moderne Autoren', 'Wer schrieb "Im Westen nichts Neues"?', 7);

-- Antworten Literatur (IDs 361-420)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (361, 'Faust', TRUE, 91), (362, 'Die Räuber', FALSE, 91), (363, 'Nathan der Weise', FALSE, 91), (364, 'Emilia Galotti', FALSE, 91),
                                                           (365, 'Hamlet', TRUE, 92), (366, 'Romeo und Julia', TRUE, 92), (367, 'Othello', FALSE, 92), (368, 'König Lear', FALSE, 92),
                                                           (369, 'Johann Wolfgang von Goethe', TRUE, 93), (370, 'Friedrich Schiller', FALSE, 93), (371, 'Heinrich Heine', FALSE, 93), (372, 'Gotthold Ephraim Lessing', FALSE, 93),
                                                           (373, 'Thomas Mann', TRUE, 94), (374, 'Hermann Hesse', TRUE, 94), (375, 'Bertolt Brecht', FALSE, 94), (376, 'Heinrich Böll', FALSE, 94),
                                                           (377, 'Gebrüder Grimm', TRUE, 95), (378, 'Hans Christian Andersen', FALSE, 95), (379, 'Ludwig Bechstein', FALSE, 95), (380, 'Wilhelm Hauff', FALSE, 95),
                                                           (381, 'Romantik', TRUE, 96), (382, 'Realismus', FALSE, 96), (383, 'Naturalismus', FALSE, 96), (384, 'Expressionismus', FALSE, 96),
                                                           (385, 'Franz Kafka', TRUE, 97), (386, 'Thomas Mann', FALSE, 97), (387, 'Hermann Hesse', FALSE, 97), (388, 'Robert Musil', FALSE, 97),
                                                           (389, '14 Verszeilen', TRUE, 98), (390, 'Feste Reimordnung', TRUE, 98), (391, '12 Verszeilen', FALSE, 98), (392, 'Freie Rhythmen', FALSE, 98),
                                                           (393, 'Miguel de Cervantes', TRUE, 99), (394, 'Lope de Vega', FALSE, 99), (395, 'Federico García Lorca', FALSE, 99), (396, 'Pedro Calderón', FALSE, 99),
                                                           (397, 'Goethe', TRUE, 100), (398, 'Schiller', TRUE, 100), (399, 'Heine', FALSE, 100), (400, 'Kleist', FALSE, 100),
                                                           (401, 'Längere Erzählung', TRUE, 101), (402, 'Fiktionale Handlung', TRUE, 101), (403, 'Kurze Geschichte', FALSE, 101), (404, 'Wahre Begebenheit', FALSE, 101),
                                                           (405, 'Rhythmische Sprache', TRUE, 102), (406, 'Verdichtete Form', TRUE, 102), (407, 'Prosaische Darstellung', FALSE, 102), (408, 'Lange Erzählung', FALSE, 102),
                                                           (409, '5 Akte', TRUE, 103), (410, '3 Akte', FALSE, 103), (411, '4 Akte', FALSE, 103), (412, '6 Akte', FALSE, 103),
                                                           (413, 'Bildlicher Vergleich', TRUE, 104), (414, 'Sprachliches Bild', TRUE, 104), (415, 'Wörtliche Bedeutung', FALSE, 104), (416, 'Direkte Aussage', FALSE, 104),
                                                           (417, 'Erich Maria Remarque', TRUE, 105), (418, 'Ernst Jünger', FALSE, 105), (419, 'Ludwig Renn', FALSE, 105), (420, 'Arnold Zweig', FALSE, 105);

-- THEMA 8: SPORT (Fragen 106-120)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (106, 'Fußball Weltmeisterschaft', 'Wie oft fand die FIFA-WM in Deutschland statt?', 8),
                                                      (107, 'Olympische Spiele', 'Alle wie viele Jahre finden Olympische Sommerspiele statt?', 8),
                                                      (108, 'Tennis Grand Slam', 'Welche Turniere gehören zum Tennis Grand Slam?', 8),
                                                      (109, 'Basketball', 'Wie viele Spieler stehen beim Basketball auf dem Feld?', 8),
                                                      (110, 'Leichtathletik', 'Welche Disziplinen gehören zum Zehnkampf?', 8),
                                                      (111, 'Schwimmen', 'Welche Schwimmstile gibt es?', 8),
                                                      (112, 'Motorsport', 'Was ist die Formel 1?', 8),
                                                      (113, 'Wintersport', 'Welche Disziplinen gehören zum Biathlon?', 8),
                                                      (114, 'Mannschaftssport', 'Wie viele Spieler hat eine Handballmannschaft auf dem Feld?', 8),
                                                      (115, 'Radsport', 'Wie heißt das berühmteste Radrennen der Welt?', 8),
                                                      (116, 'Kampfsport', 'Welche Gürtelfarben gibt es im Karate?', 8),
                                                      (117, 'Wassersport', 'Was ist Rudern?', 8),
                                                      (118, 'Turnen', 'Welche Geräte gibt es im Kunstturnen der Frauen?', 8),
                                                      (119, 'Extremsport', 'Was ist Bungee-Jumping?', 8),
                                                      (120, 'Deutsche Sportler', 'Welcher deutsche Tennisspieler gewann Wimbledon?', 8);

-- Antworten Sport (IDs 421-480)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (421, '2 Mal', TRUE, 106), (422, '1 Mal', FALSE, 106), (423, '3 Mal', FALSE, 106), (424, '4 Mal', FALSE, 106),
                                                           (425, '4 Jahre', TRUE, 107), (426, '2 Jahre', FALSE, 107), (427, '5 Jahre', FALSE, 107), (428, '3 Jahre', FALSE, 107),
                                                           (429, 'Wimbledon', TRUE, 108), (430, 'US Open', TRUE, 108), (431, 'Davis Cup', FALSE, 108), (432, 'Masters', FALSE, 108),
                                                           (433, '5 pro Team', TRUE, 109), (434, '6 pro Team', FALSE, 109), (435, '4 pro Team', FALSE, 109), (436, '7 pro Team', FALSE, 109),
                                                           (437, 'Laufen', TRUE, 110), (438, 'Springen', TRUE, 110), (439, 'Schwimmen', FALSE, 110), (440, 'Radfahren', FALSE, 110),
                                                           (441, 'Freistil', TRUE, 111), (442, 'Brustschwimmen', TRUE, 111), (443, 'Rückenschwimmen', FALSE, 111), (444, 'Schmetterling', FALSE, 111),
                                                           (445, 'Motorsport-Weltmeisterschaft', TRUE, 112), (446, 'Schnellste Rennwagen', TRUE, 112), (447, 'Motorradrennen', FALSE, 112), (448, 'Rallye-Sport', FALSE, 112),
                                                           (449, 'Langlauf', TRUE, 113), (450, 'Schießen', TRUE, 113), (451, 'Abfahrt', FALSE, 113), (452, 'Springen', FALSE, 113),
                                                           (453, '7 Spieler', TRUE, 114), (454, '6 Spieler', FALSE, 114), (455, '8 Spieler', FALSE, 114), (456, '5 Spieler', FALSE, 114),
                                                           (457, 'Tour de France', TRUE, 115), (458, 'Giro d\'Italia', FALSE, 115), (459, 'Vuelta', FALSE, 115), (460, 'Deutschland Tour', FALSE, 115),
                                                           (461, 'Weiß', TRUE, 116), (462, 'Schwarz', TRUE, 116), (463, 'Rot', FALSE, 116), (464, 'Blau', FALSE, 116),
                                                           (465, 'Wassersport mit Booten', TRUE, 117), (466, 'Mannschaftssport', FALSE, 117), (467, 'Wintersport', FALSE, 117), (468, 'Motorsport', FALSE, 117),
                                                           (469, 'Schwebebalken', TRUE, 118), (470, 'Stufenbarren', FALSE, 118), (471, 'Reck', FALSE, 118), (472, 'Ringe', FALSE, 118),
                                                           (473, 'Sprung mit Gummiseil', TRUE, 119), (474, 'Von hohen Objekten', TRUE, 119), (475, 'Schwimmen', FALSE, 119), (476, 'Klettern', FALSE, 119),
                                                           (477, 'Boris Becker', TRUE, 120), (478, 'Michael Stich', TRUE, 120), (479, 'Tommy Haas', FALSE, 120), (480, 'Alexander Zverev', FALSE, 120);

-- THEMA 9: MUSIK (Fragen 121-135)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (121, 'Klassische Musik', 'Welcher Komponist schrieb die 9. Sinfonie?', 9),
                                                      (122, 'Musikinstrumente', 'Zu welcher Familie gehört die Violine?', 9),
                                                      (123, 'Musiktheorie', 'Wie viele Linien hat ein Notensystem?', 9),
                                                      (124, 'Opern', 'Wer komponierte "Die Zauberflöte"?', 9),
                                                      (125, 'Musikepochen', 'Welche Epoche kam nach dem Barock?', 9),
                                                      (126, 'Deutsche Komponisten', 'Welcher Komponist wurde in Salzburg geboren?', 9),
                                                      (127, 'Musikalische Formen', 'Was ist eine Fuge?', 9),
                                                      (128, 'Moderne Musik', 'Welches Instrument ist charakteristisch für Jazz?', 9),
                                                      (129, 'Kirchenmusik', 'Was ist ein Choral?', 9),
                                                      (130, 'Musiknotation', 'Was bedeutet "forte" in der Musik?', 9),
                                                      (131, 'Orchester', 'Welche Instrumentengruppen gibt es im Orchester?', 9),
                                                      (132, 'Volkslied', 'Was ist ein Volkslied?', 9),
                                                      (133, 'Musikgeschichte', 'In welchem Jahrhundert lebte Bach?', 9),
                                                      (134, 'Rhythmus', 'Was ist ein Takt?', 9),
                                                      (135, 'Harmonielehre', 'Was ist ein Akkord?', 9);

-- Antworten Musik (IDs 481-540)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
                                                           (481, 'Ludwig van Beethoven', TRUE, 121), (482, 'Wolfgang Amadeus Mozart', FALSE, 121), (483, 'Franz Schubert', FALSE, 121), (484, 'Johannes Brahms', FALSE, 121),
                                                           (485, 'Streichinstrumente', TRUE, 122), (486, 'Blasinstrumente', FALSE, 122), (487, 'Schlaginstrumente', FALSE, 122), (488, 'Tasteninstrumente', FALSE, 122),
                                                           (489, '5 Linien', TRUE, 123), (490, '4 Linien', FALSE, 123), (491, '6 Linien', FALSE, 123), (492, '7 Linien', FALSE, 123),
                                                           (493, 'Wolfgang Amadeus Mozart', TRUE, 124), (494, 'Ludwig van Beethoven', FALSE, 124), (495, 'Franz Schubert', FALSE, 124), (496, 'Robert Schumann', FALSE, 124),
                                                           (497, 'Klassik', TRUE, 125), (498, 'Romantik', FALSE, 125), (499, 'Renaissance', FALSE, 125), (500, 'Moderne', FALSE, 125),
                                                           (501, 'Wolfgang Amadeus Mozart', TRUE, 126), (502, 'Ludwig van Beethoven', FALSE, 126), (503, 'Johann Sebastian Bach', FALSE, 126), (504, 'Franz Schubert', FALSE, 126),
                                                           (505, 'Polyphonie Komposition', TRUE, 127), (506, 'Stimmverflechtung', TRUE, 127), (507, 'Einfache Melodie', FALSE, 127), (508, 'Tanzform', FALSE, 127),
                                                           (509, 'Saxophon', TRUE, 128), (510, 'Trompete', TRUE, 128), (511, 'Geige', FALSE, 128), (512, 'Klavier', FALSE, 128),
                                                           (513, 'Kirchenlied', TRUE, 129), (514, 'Mehrstimmiger Gesang', TRUE, 129), (515, 'Instrumentalstück', FALSE, 129), (516, 'Solostück', FALSE, 129),
                                                           (517, 'Laut', TRUE, 130), (518, 'Leise', FALSE, 130), (519, 'Schnell', FALSE, 130), (520, 'Langsam', FALSE, 130),
                                                           (521, 'Streicher', TRUE, 131), (522, 'Bläser', TRUE, 131), (523, 'Schlagzeug', FALSE, 131), (524, 'Elektronische Instrumente', FALSE, 131),
                                                           (525, 'Traditionelles Lied', TRUE, 132), (526, 'Mündlich überliefert', TRUE, 132), (527, 'Kunstlied', FALSE, 132), (528, 'Moderne Komposition', FALSE, 132),
                                                           (529, '18. Jahrhundert', TRUE, 133), (530, '17. Jahrhundert', FALSE, 133), (531, '19. Jahrhundert', FALSE, 133), (532, '16. Jahrhundert', FALSE, 133),
                                                           (533, 'Rhythmische Einheit', TRUE, 134), (534, 'Melodische Phrase', FALSE, 134), (535, 'Harmonische Folge', FALSE, 134), (536, 'Dynamische Stufe', FALSE, 134),
                                                           (537, 'Mehrere Töne gleichzeitig', TRUE, 135), (538, 'Harmonische Einheit', TRUE, 135), (539, 'Einzelner Ton', FALSE, 135), (540, 'Melodische Linie', FALSE, 135);
-- Quizzle Testdaten - Teil 4 von 4 (FINALE)
-- Thema: Technologie

-- THEMA 10: TECHNOLOGIE (Fragen 136-150)
INSERT INTO Questions (id, title, text, theme_id) VALUES
                                                      (136, 'Computer Geschichte', 'Wer gilt als Vater des Computers?', 10),
                                                      (137, 'Internet Protokolle', 'Wofür steht HTTP?', 10),
                                                      (138, 'Programmierung', 'Was ist eine Programmiersprache?', 10),
                                                      (139, 'Hardware', 'Was ist die CPU?', 10),
                                                      (140, 'Software', 'Was ist ein Betriebssystem?', 10),
                                                      (141, 'Netzwerke', 'Was bedeutet WLAN?', 10),
                                                      (142, 'Datenbanken', 'Was ist SQL?', 10),
                                                      (143, 'Künstliche Intelligenz', 'Was versteht man unter KI?', 10),
                                                      (144, 'Sicherheit', 'Was ist ein Virus in der Informatik?', 10),
                                                      (145, 'Mobile Technologie', 'Was ist ein Smartphone?', 10),
                                                      (146, 'Cloud Computing', 'Was bedeutet "Cloud"?', 10),
                                                      (147, 'Soziale Medien', 'Welche Plattformen gehören zu sozialen Medien?', 10),
                                                      (148, 'E-Commerce', 'Was ist Online-Shopping?', 10),
                                                      (149, 'Digitale Medien', 'Was sind Pixelgrafiken?', 10),
                                                      (150, 'Zukunftstechnologie', 'Was ist Virtual Reality?', 10);

-- Antworten Technologie (IDs 541-600)
INSERT INTO Answers (id, text, isCorrect, question_id) VALUES
-- Frage 136: Computer Geschichte
(541, 'Konrad Zuse', TRUE, 136), (542, 'Alan Turing', TRUE, 136), (543, 'Bill Gates', FALSE, 136), (544, 'Steve Jobs', FALSE, 136),

-- Frage 137: HTTP
(545, 'HyperText Transfer Protocol', TRUE, 137), (546, 'High Tech Transfer Protocol', FALSE, 137), (547, 'Home Text Transport Protocol', FALSE, 137), (548, 'Hyper Terminal Transfer Protocol', FALSE, 137),

-- Frage 138: Programmiersprache
(549, 'Formale Sprache für Computer', TRUE, 138), (550, 'Anweisungen für Maschinen', TRUE, 138), (551, 'Natürliche Sprache', FALSE, 138), (552, 'Gesprochene Sprache', FALSE, 138),

-- Frage 139: CPU
(553, 'Central Processing Unit', TRUE, 139), (554, 'Computer Processing Unit', FALSE, 139), (555, 'Central Program Unit', FALSE, 139), (556, 'Computer Program Unit', FALSE, 139),

-- Frage 140: Betriebssystem
(557, 'Grundsoftware des Computers', TRUE, 140), (558, 'Verwaltet Hardware und Software', TRUE, 140), (559, 'Anwendungsprogramm', FALSE, 140), (560, 'Spielsoftware', FALSE, 140),

-- Frage 141: WLAN
(561, 'Wireless Local Area Network', TRUE, 141), (562, 'Wide Local Area Network', FALSE, 141), (563, 'Wired Local Area Network', FALSE, 141), (564, 'World Local Area Network', FALSE, 141),

-- Frage 142: SQL
(565, 'Structured Query Language', TRUE, 142), (566, 'Standard Query Language', FALSE, 142), (567, 'System Query Language', FALSE, 142), (568, 'Simple Query Language', FALSE, 142),

-- Frage 143: Künstliche Intelligenz
(569, 'Maschinelles Lernen', TRUE, 143), (570, 'Simulation menschlicher Intelligenz', TRUE, 143), (571, 'Natürliche Intelligenz', FALSE, 143), (572, 'Emotionale Intelligenz', FALSE, 143),

-- Frage 144: Computer Virus
(573, 'Schädliche Software', TRUE, 144), (574, 'Selbst reproduzierende Programme', TRUE, 144), (575, 'Nützliche Software', FALSE, 144), (576, 'Hardware-Komponente', FALSE, 144),

-- Frage 145: Smartphone
(577, 'Mobiltelefon mit Computerfunktionen', TRUE, 145), (578, 'Internetzugang', TRUE, 145), (579, 'Nur zum Telefonieren', FALSE, 145), (580, 'Desktop Computer', FALSE, 145),

-- Frage 146: Cloud Computing
(581, 'Internet-basierte Dienste', TRUE, 146), (582, 'Externe Datenspeicherung', TRUE, 146), (583, 'Lokale Festplatte', FALSE, 146), (584, 'CD-ROM Speicher', FALSE, 146),

-- Frage 147: Soziale Medien
(585, 'Facebook', TRUE, 147), (586, 'Instagram', TRUE, 147), (587, 'Microsoft Word', FALSE, 147), (588, 'Adobe Photoshop', FALSE, 147),

-- Frage 148: E-Commerce
(589, 'Elektronischer Handel', TRUE, 148), (590, 'Kauf über Internet', TRUE, 148), (591, 'Nur im Laden kaufen', FALSE, 148), (592, 'Tauschhandel', FALSE, 148),

-- Frage 149: Pixelgrafiken
(593, 'Rasterbasierte Bilder', TRUE, 149), (594, 'Aus Bildpunkten aufgebaut', TRUE, 149), (595, 'Vektorbasierte Bilder', FALSE, 149), (596, 'Textdateien', FALSE, 149),

-- Frage 150: Virtual Reality
(597, 'Künstlich erzeugte Realität', TRUE, 150), (598, 'Immersive Technologie', TRUE, 150), (599, 'Normale Realität', FALSE, 150), (600, 'Augmented Reality', FALSE, 150);
