# 🏰 Civilizations

> Projecte AWS — Curs 25-26

Un joc d'estratègia per interfície gràfica en Java on gestiones una civilització, construeixes edificis, entrenes unitats militars i et defenses dels atacs continus d'exèrcits enemics.

---

## 📋 Descripció

En **Civilizations** ets el líder d'una petita civilització. Disposes de recursos (Menjar, Fusta, Ferro, Maná), tecnologies, edificis i un exèrcit format per unitats d'atac, defensa i especials. Cada 3 minuts un exèrcit enemic t'atacarà, i hauràs de gestionar bé les teves defenses per sobreviure.

### Característiques principals

- Generació periòdica de recursos (Menjar, Fusta, Ferro, Maná)
- Construcció d'edificis: Granja, Fusteria, Ferreria, Torre Màgica, Església
- Millora de tecnologies d'atac i defensa
- Creació d'unitats militars: Espadatxí, Llancer, Ballesta, Canó, Torre de Fletxes, Catapulta, Torre Llançacoets, Mag, Sacerdot
- Mecànica de batalla per torns amb probabilitats configurable
- Sistema de residus (loot) en derrota d'unitats
- Unitats santificades quan hi ha sacerdots a l'exèrcit
- Experiència acumulada per unitats supervivents
- Generació d'exèrcits enemics progressivament més forts
- Reporti de batalla: resum + desenvolupament pas a pas
- Persistència de dades en base de dades Oracle
- Pàgina web amb dades actualitzades (M04)

---

## 🗂️ Estructura del repositori

```
Proyecto2_Civil6/
├── src/
│   ├── M01/
│   │   └── Manual_Civilizations_Civil6_Aitor_Miguel_Styven.pdf
│   ├── M02/
│   │   ├── Diagrama_BD.png
│   │   └── civil6_bd.sql
│   ├── M03/
│   │   ├── Excepciones/
│   │   │   ├── BuildingException.java
│   │   │   ├── ResourceException.java
│   │   │   └── ResourceGenerator.java
│   │   ├── GUI/
│   │   │   ├── EdificioColocado.java
│   │   │   ├── PanelGestorPartida.java
│   │   │   ├── PanelInicioSesion.java
│   │   │   ├── PanelJuego.java
│   │   │   └── VentanaPrincipal.java
│   │   ├── Unidades/
│   │   │   ├── UnidadesDefensivas/
│   │   │   │   ├── ArrowTower.java
│   │   │   │   ├── Catapult.java
│   │   │   │   ├── DefenseUnit.java
│   │   │   │   └── RocketLauncherTower.java
│   │   │   ├── UnidadesEspeciales/
│   │   │   │   ├── Magician.java
│   │   │   │   ├── Priest.java
│   │   │   │   └── SpecialUnit.java
│   │   │   └── UnidadesOfensivas/
│   │   │       ├── AttackUnit.java
│   │   │       ├── Cannon.java
│   │   │       ├── Crossbow.java
│   │   │       ├── Spearman.java
│   │   │       └── Swordsman.java
│   │   ├── img/
│   │   ├── Battle.java
│   │   ├── Civilization.java
│   │   ├── ConexionBD.java
│   │   ├── EnemyArmyGenerator.java
│   │   ├── Main.java
│   │   ├── MilitaryUnit.java
│   │   └── Variables.java
│   ├── M04/
│   │   ├── public/
│   │   │   ├── img/
│   │   │   │   ├── aitor.png
│   │   │   │   ├── logo.png
│   │   │   │   ├── miguel.png
│   │   │   │   ├── pantalla_inicial.png
│   │   │   │   ├── partida.png
│   │   │   │   └── styven.png
│   │   │   └── estils.css
│   │   ├── server/
│   │   │   ├── views/
│   │   │   │   ├── partials/
│   │   │   │   │   ├── footer.hbs
│   │   │   │   │   └── header.hbs
│   │   │   │   ├── batallas.hbs
│   │   │   │   ├── index.hbs
│   │   │   │   ├── informe.hbs
│   │   │   │   ├── layout.hbs
│   │   │   │   ├── mi-ciudad-detalles.hbs
│   │   │   │   ├── mi-ciudad.hbs
│   │   │   │   └── programadores.hbs
│   │   │   ├── app.js
│   │   │   └── utilsMySQL.js
│   │   ├── package-lock.json
│   │   └── package.json
│   └── M05/
│       └── Diagrama_UML.png
├── .classpath
├── .gitignore
├── .project
└── README.md
```

---

## ⚙️ Requisits previs

| Eina | Versió mínima |
|------|---------------|
| Java JDK | 25+ |
| MySQL Server | 8.0+ |
| JDBC Driver (ojdbc) | 21+ |
| Node.js *(només M04)* | 18+ |

---

## 🚀 Instal·lació i execució

### 1. Clonar el repositori

```bash
git clone https://github.com/miguelangel-1974/Proyecto2_Civil6.git
cd Proyecto2_Civil6
```

### 2. Configurar la base de dades

Executa els scripts SQL que trobaràs a la carpeta `M02/` connectant-te al MySQL:

```
mysql -u root -p < src/M02/civil6_bd.sql
```

### 3. Executar l'aplicació Java

---

## 🎮 Com jugar

En arrencar l'aplicació es mostrarà un menú principal de inici de sesio:

```
Iniciar sesion
Crear usuario
```

Despres podras crear una partida o continuar una començada

```
Crear partida
Cargar partida
```

- Els recursos es generen automàticament cada minut.
- Cada 3 minuts un exèrcit enemic atacarà la teva civilització.
- Pots consultar les batalles i el seu desenvolupament pas a pas.

---

## 🏛️ Arquitectura del projecte

```
Variables (interface)
      │
      ├── MilitaryUnit (interface)
      │         │
      │    ┌────┴──────┐
      │  AttackUnit  DefenseUnit  SpecialUnit
      │    │              │            │
      │  Swordsman    ArrowTower   Magician
      │  Spearman     Catapult     Priest
      │  Crossbow     RocketLauncherTower
      │  Cannon
      │
Civilization ──────── Battle
      │
ResourceException
BuildingException
```

Consulta el diagrama de classes complet a `M05/`.

---

## 🗄️ Base de dades

Les taules principals són:

| Tabla | Descripción |
|-------|-------------|
| `Users` | Usuarios del juego (user_id, username, password_hash) |
| `Civilization_stats` | Recursos, edificios, tecnologías y contador de batallas de cada civilización |
| `attack_units_stats` | Estado de las unidades de ataque (armor, base_damage, experience, sanctified) |
| `defense_units_stats` | Estado de las unidades de defensa |
| `special_units_stats` | Estado de las unidades especiales (Mago, Sacerdote) |
| `Battle_stats` | Resumen de cada batalla: recursos obtenidos y resultado (win/lose) |
| `Battle_log` | Log completo del desarrollo de la batalla en texto |
| `Civilization_attack_stats` | Bajas de unidades de ataque propias por batalla |
| `Civilization_defense_stats` | Bajas de unidades de defensa propias por batalla |
| `Civilization_special_stats` | Bajas de unidades especiales propias por batalla |
| `Enemy_attack_stats` | Unidades del ejército enemigo por batalla |
| `Civilization_buildings` | Edificios construidos con su posición en el mapa (pos_x, pos_y) |

---

## 🌐 Pàgines web (M04)

| Pàgina | Descripció |
|--------|-----------|
| `/` | Portada amb informació del joc i totes les partides |
| `/mi-ciudad` | Inici de sesio per conectarte amb les teves partides y verue les estadistiques |
| `/batallas` | Informe detallat de totes les batalles |
| `/civilitzacio` | Recursos actuals de la civilització |
| `/programadores` | Informació de l'equip |

---

## 🔀 Control de versions (Git)

- Branca principal: `main`
- Branca de preproducció: `preproduccion`
- Cada membre del grup treballa a la seva branca personal i fa merge a `preproduccion`
---

## 👥 Equip

| Nom | Tasques principals |
|-----|--------------------|
| *Styven* | *Creació de la pagina web y enllaç amb la base de dades creada.* |
| *Miguel* | *Desenvolupament del joc y programació de mecaniques.* |
| *Aitor* | *Creació de la base de dades y programació de funcionalitats.* |

---

## 📄 Llicència

Projecte acadèmic — Curs 25-26. Tots els drets reservats als autors.
