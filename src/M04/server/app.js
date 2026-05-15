const express = require('express');
const path = require('path');
const hbs = require('hbs');
const MySQL = require('./utilsMySQL');

const app = express();
const port = 3000;
const isProxmox = !!process.env.PM2_HOME;

const db = new MySQL();
if (!isProxmox) {
  db.init({
    host: '127.0.0.1',
    port: 3306,
    user: 'admincivil6',
    password: 'Civil6Admin',
    database: 'civilizations_db'
  });
} else {
  db.init({
    host: '127.0.0.1',
    port: 3306,
    user: 'admincivil6',
    password: 'Civil6Admin',
    database: 'civilizations_db'
  });
}

app.use(express.static('public'));
app.use(express.urlencoded({ extended: true }));

app.use((req, res, next) => {
  res.setHeader('Cache-Control', 'no-store, no-cache, must-revalidate, proxy-revalidate');
  res.setHeader('Pragma', 'no-cache');
  res.setHeader('Expires', '0');
  res.setHeader('Surrogate-Control', 'no-store');
  next();
});

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');
hbs.registerPartials(path.join(__dirname, 'views', 'partials'));

hbs.registerHelper('eq', (a, b) => a === b);
hbs.registerHelper('subtract', (a, b) => (Number(a) || 0) - (Number(b) || 0));

// 1. Página Inicio
app.get('/', async (req, res) => {
  try {
    const rawBattles = await db.query(`
      SELECT bs.*, c.name AS civ_name, u.username
      FROM Battle_stats bs
      JOIN Civilization_stats c ON bs.civilization_id = c.civilization_id
      JOIN Users u ON c.user_id = u.user_id
      ORDER BY bs.num_battle DESC LIMIT 2
    `);
    const battles = db.table_to_json(rawBattles, { num_battle: 'number', wood_acquired: 'number', iron_acquired: 'number' });
 
    const rawPartidas = await db.query(`
      SELECT u.username, c.name, c.food_amount, c.wood_amount, c.iron_amount, c.mana_amount, c.battles_counter 
      FROM Civilization_stats c 
      JOIN Users u ON c.user_id = u.user_id 
      ORDER BY c.battles_counter DESC
    `);
    const partidas = db.table_to_json(rawPartidas);
 
    res.render('index', { title: 'Inicio', battles, partidas });
  } catch (err) {
    console.error(err);
    res.status(500).send("Error BD");
  }
});

// 2. Mi Ciudad (Formulario de Login)
app.get('/mi-ciudad', (req, res) => {
  res.render('mi-ciudad', { title: 'Acceso a tu Ciudad' });
});

// 2.1 Mi Ciudad (Procesar Login)
app.post('/mi-ciudad', async (req, res) => {
  const { username, password } = req.body;
  try {
    const rawUser = await db.query(`SELECT user_id, username FROM Users WHERE username = '${username}' AND password_hash = '${password}'`);
    
    if (rawUser.length > 0) {
      const user = rawUser[0];
      const rawCivs = await db.query(`SELECT * FROM Civilization_stats WHERE user_id = ${user.user_id}`);
      const civs = db.table_to_json(rawCivs);
      
      res.render('mi-ciudad', { title: 'Tus Partidas', user, civs });
    } else {
      res.render('mi-ciudad', { title: 'Acceso a tu Ciudad', error: 'Usuario o contraseña incorrectos. Inténtalo de nuevo.' });
    }
  } catch (err) {
    console.error(err);
    res.status(500).send("Error BD");
  }
});

// 2.2 Mi Ciudad (Detalles en grande)
app.get('/mi-ciudad/detalles/:id', async (req, res) => {
  const civId = req.params.id;
  try {
    const rawStats = await db.query(`SELECT * FROM Civilization_stats WHERE civilization_id = ${civId}`);
    const stats = rawStats.length > 0 ? db.table_to_json(rawStats)[0] : null;

    if (!stats) return res.redirect('/mi-ciudad');

    // Consultar tropas de ataque y defensa para dar más detalle
    const rawAtk = await db.query(`SELECT type, count(*) as qty FROM attack_units_stats WHERE civilization_id = ${civId} GROUP BY type`);
    const atkUnits = db.table_to_json(rawAtk);
    
    const rawDef = await db.query(`SELECT type, count(*) as qty FROM defense_units_stats WHERE civilization_id = ${civId} GROUP BY type`);
    const defUnits = db.table_to_json(rawDef);

    res.render('mi-ciudad-detalles', { title: `Detalles: ${stats.name}`, stats, atkUnits, defUnits });
  } catch (err) {
    console.error(err);
    res.status(500).send("Error BD");
  }
});

// 3. Batallas
app.get('/batallas', async (req, res) => {
  try {
    const rawTotal = await db.query(`SELECT COUNT(*) as total FROM Battle_stats`);
    const totalBattles = rawTotal.length > 0 ? rawTotal[0].total : 0;
 
    // Traer todas las batallas con nombre de civilización y jugador
    const rawBattles = await db.query(`
      SELECT 
        bs.civilization_id,
        bs.num_battle,
        bs.wood_acquired,
        bs.iron_acquired,
        c.name   AS civ_name,
        u.username
      FROM Battle_stats bs
      JOIN Civilization_stats c ON bs.civilization_id = c.civilization_id
      JOIN Users u              ON c.user_id = u.user_id
      ORDER BY bs.num_battle DESC
    `);
    const battles = db.table_to_json(rawBattles, { num_battle: 'number', wood_acquired: 'number', iron_acquired: 'number' });
 
    // Para cada batalla, detectar victoria leyendo la última línea del log
    const battlesWithResult = await Promise.all(battles.map(async (b) => {
      try {
        const rawLastLog = await db.query(`
          SELECT log_entry FROM Battle_log
          WHERE civilization_id = ${b.civilization_id} AND num_battle = ${b.num_battle}
          ORDER BY num_line DESC LIMIT 5
        `);
        const lastLines = rawLastLog.map(r => (r.log_entry || '').toUpperCase());
        let result = 'unknown';
        for (const line of lastLines) {
          if (line.includes('VICTORIA') || line.includes('VICTORY') || line.includes('WIN')) {
            result = 'victoria'; break;
          }
          if (line.includes('DERROTA') || line.includes('DEFEAT') || line.includes('LOSS') || line.includes('LOST')) {
            result = 'derrota'; break;
          }
        }
 
        // Bajas totales propias y enemigas
        const rawCivDrops = await db.query(`
          SELECT COALESCE(SUM(drops), 0) AS total_drops
          FROM Civilization_defense_stats
          WHERE civilization_id = ${b.civilization_id} AND num_battle = ${b.num_battle}
        `);
        const rawEnemyDrops = await db.query(`
          SELECT COALESCE(SUM(drops), 0) AS total_drops
          FROM Enemy_attack_stats
          WHERE civilization_id = ${b.civilization_id} AND num_battle = ${b.num_battle}
        `);
 
        return {
          ...b,
          result,
          civ_drops:   rawCivDrops[0]?.total_drops   ?? 0,
          enemy_drops: rawEnemyDrops[0]?.total_drops  ?? 0,
        };
      } catch {
        return { ...b, result: 'unknown', civ_drops: 0, enemy_drops: 0 };
      }
    }));
 
    res.render('batallas', { title: 'Registro de Batallas', totalBattles, battles: battlesWithResult });
  } catch (err) {
    console.error(err);
    res.status(500).send("Error BD");
  }
});

// 4. Informes de Batalla
app.get('/informe', async (req, res) => {
  try {
    const idBatalla = req.query.informe;
    const idCiv     = req.query.civ;        // <-- ahora se pasa también el civilization_id
    if (!idBatalla || !idCiv) return res.redirect('/batallas');
 
    // Log de combate
    const rawLogs = await db.query(`
      SELECT log_entry FROM Battle_log
      WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}
      ORDER BY num_line ASC
    `);
    const logs = db.table_to_json(rawLogs);
 
    // Resumen de recursos + civilización + jugador
    const rawSummary = await db.query(`
      SELECT bs.*, c.name AS civ_name, u.username
      FROM Battle_stats bs
      JOIN Civilization_stats c ON bs.civilization_id = c.civilization_id
      JOIN Users u              ON c.user_id = u.user_id
      WHERE bs.civilization_id = ${idCiv} AND bs.num_battle = ${idBatalla}
    `);
    const summary = rawSummary.length > 0 ? db.table_to_json(rawSummary)[0] : null;
 
    // Bajas propias (defensa) por tipo
    const rawCivAtk = await db.query(`
      SELECT type, initial, drops FROM Civilization_attack_stats
      WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}
    `);
    const civAtkStats = db.table_to_json(rawCivAtk, { initial: 'number', drops: 'number' });
 
    const rawCivDef = await db.query(`
      SELECT type, initial, drops FROM Civilization_defense_stats
      WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}
    `);
    const civDefStats = db.table_to_json(rawCivDef, { initial: 'number', drops: 'number' });
 
    // Bajas enemigas por tipo
    const rawEnemyAtk = await db.query(`
      SELECT type, initial, drops FROM Enemy_attack_stats
      WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}
    `);
    const enemyAtkStats = db.table_to_json(rawEnemyAtk, { initial: 'number', drops: 'number' });
 
    // Detectar resultado en las últimas líneas del log
    let battleResult = 'unknown';
    const lastLines = logs.slice(-5).map(l => (l.log_entry || '').toUpperCase());
    for (const line of lastLines) {
      if (line.includes('VICTORIA') || line.includes('VICTORY') || line.includes('WIN')) {
        battleResult = 'victoria'; break;
      }
      if (line.includes('DERROTA') || line.includes('DEFEAT') || line.includes('LOSS') || line.includes('LOST')) {
        battleResult = 'derrota'; break;
      }
    }
 
    res.render('informe', {
      title: `Informe Batalla #${idBatalla}`,
      logs,
      summary,
      idBatalla,
      idCiv,
      battleResult,
      civAtkStats,
      civDefStats,
      enemyAtkStats,
    });
  } catch (err) {
    console.error(err);
    res.status(500).send("Error BD");
  }
});

// 5. Programadores
app.get('/programadores', (req, res) => {
  res.render('programadores', { title: 'Equip de Desenvolupament' });
});

const httpServer = app.listen(port, () => {
  console.log(`http://localhost:${port}`);
  console.log(`http://localhost:${port}/mi-ciudad`);
  console.log(`http://localhost:${port}/batallas`);
  console.log(`http://localhost:${port}/programadores`);
});

process.on('SIGINT', async () => {
  await db.end();
  httpServer.close();
  process.exit(0);
});