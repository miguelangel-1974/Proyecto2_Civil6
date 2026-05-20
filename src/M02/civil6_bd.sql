-- =================================================================
-- 1. LIMPIEZA PREVIA
-- ================================================================
DROP DATABASE IF EXISTS civilizations_db;

-- =================================================================
-- 2. CREACIÓN Y SELECCIÓN DE LA BASE DE DATOS
-- =================================================================
CREATE DATABASE IF NOT EXISTS civilizations_db;
USE civilizations_db;

-- =================================================================
-- 3. CREACIÓN DE TABLAS
-- =================================================================

-- TABLA DE USUARIOS
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

-- TABLA PRINCIPAL (Configurada para 1 usuario = 1 partida)
CREATE TABLE Civilization_stats (
    civilization_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100),
    wood_amount INT DEFAULT 0,
    iron_amount INT DEFAULT 0,
    food_amount INT DEFAULT 0,
    mana_amount INT DEFAULT 0,
    magicTower_counter INT DEFAULT 0,
    church_counter INT DEFAULT 0,
    farm_counter INT DEFAULT 0,
    smithy_counter INT DEFAULT 0,
    carpentry_counter INT DEFAULT 0,
    technology_defense_level INT DEFAULT 0,
    technology_attack_level INT DEFAULT 0,
    battles_counter INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- TABLAS DE UNIDADES (Referencian al civilization_id de la partida actual)
CREATE TABLE attack_units_stats (
    civilization_id INT NOT NULL,
    unit_id INT NOT NULL,
    type VARCHAR(50), 
    armor INT,
    base_damage INT,
    experience INT DEFAULT 0,
    sanctified BOOLEAN,
    PRIMARY KEY (civilization_id, unit_id),
    FOREIGN KEY (civilization_id) REFERENCES Civilization_stats(civilization_id) ON DELETE CASCADE
);

CREATE TABLE defense_units_stats (
    civilization_id INT NOT NULL,
    unit_id INT NOT NULL,
    type VARCHAR(50), 
    armor INT,
    base_damage INT,
    experience INT DEFAULT 0,
    sanctified BOOLEAN,
    PRIMARY KEY (civilization_id, unit_id),
    FOREIGN KEY (civilization_id) REFERENCES Civilization_stats(civilization_id) ON DELETE CASCADE
);

CREATE TABLE special_units_stats (
    civilization_id INT NOT NULL,
    unit_id INT NOT NULL,
    type VARCHAR(50), 
    armor INT,
    base_damage INT,
    experience INT DEFAULT 0,
    PRIMARY KEY (civilization_id, unit_id),
    FOREIGN KEY (civilization_id) REFERENCES Civilization_stats(civilization_id) ON DELETE CASCADE
);

-- TABLA GENERAL DE BATALLAS
CREATE TABLE Battle_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    wood_acquired INT DEFAULT 0,
    iron_acquired INT DEFAULT 0,
    PRIMARY KEY (civilization_id, num_battle),
    FOREIGN KEY (civilization_id) REFERENCES Civilization_stats(civilization_id) ON DELETE CASCADE
);

-- TABLAS DE REGISTROS DE BATALLAS
CREATE TABLE Battle_log (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    num_line INT NOT NULL,
    log_entry TEXT,
    PRIMARY KEY (civilization_id, num_battle, num_line),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES Battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

CREATE TABLE Civilization_attack_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    initial INT,
    drops INT,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES Battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

CREATE TABLE Civilization_defense_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    initial INT,
    drops INT,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES Battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

CREATE TABLE Civilization_special_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    initial INT,
    drops INT,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES Battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

CREATE TABLE Enemy_attack_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    initial INT,
    drops INT,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES Battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

CREATE TABLE Civilization_buildings (
    civilization_id INT NOT NULL,
    building_id     INT NOT NULL,
    type            VARCHAR(50) NOT NULL,
    pos_x           INT NOT NULL,
    pos_y           INT NOT NULL,
    PRIMARY KEY (civilization_id, building_id),
    FOREIGN KEY (civilization_id) REFERENCES Civilization_stats(civilization_id) ON DELETE CASCADE
);