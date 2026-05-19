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

    const rawBattles = await db.query(`
      SELECT 
        bs.civilization_id, bs.num_battle, bs.wood_acquired, bs.iron_acquired, bs.result,
        c.name AS civ_name, u.username,
        COALESCE((SELECT SUM(drops) FROM Civilization_attack_stats  WHERE civilization_id = bs.civilization_id AND num_battle = bs.num_battle), 0) +
        COALESCE((SELECT SUM(drops) FROM Civilization_defense_stats WHERE civilization_id = bs.civilization_id AND num_battle = bs.num_battle), 0) +
        COALESCE((SELECT SUM(drops) FROM Civilization_special_stats  WHERE civilization_id = bs.civilization_id AND num_battle = bs.num_battle), 0) AS civ_drops,
        COALESCE((SELECT SUM(drops) FROM Enemy_attack_stats          WHERE civilization_id = bs.civilization_id AND num_battle = bs.num_battle), 0) AS enemy_drops
      FROM Battle_stats bs
      JOIN Civilization_stats c ON bs.civilization_id = c.civilization_id
      JOIN Users u ON c.user_id = u.user_id
      ORDER BY bs.num_battle DESC
    `);
    const battles = db.table_to_json(rawBattles, {
      num_battle: 'number', wood_acquired: 'number', iron_acquired: 'number',
      civ_drops: 'number', enemy_drops: 'number'
    });

    res.render('batallas', { title: 'Registro de Batallas', totalBattles, battles });
  } catch (err) {
    console.error(err);
    res.status(500).send("Error BD");
  }
});

// 4. Informes de Batalla
app.get('/informe', async (req, res) => {
  try {
    const idBatalla = req.query.informe;
    const idCiv     = req.query.civ;
    if (!idBatalla || !idCiv) return res.redirect('/batallas');

    const rawLog = await db.query(`
      SELECT log_text FROM Battle_log
      WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}
    `);
    const logText = rawLog.length > 0 ? rawLog[0].log_text : null;

    const rawSummary = await db.query(`
      SELECT bs.*, c.name AS civ_name, u.username
      FROM Battle_stats bs
      JOIN Civilization_stats c ON bs.civilization_id = c.civilization_id
      JOIN Users u ON c.user_id = u.user_id
      WHERE bs.civilization_id = ${idCiv} AND bs.num_battle = ${idBatalla}
    `);
    const summary = rawSummary.length > 0 ? db.table_to_json(rawSummary)[0] : null;

    const rawCivAtk   = await db.query(`SELECT type, initial, drops FROM Civilization_attack_stats  WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}`);
    const rawCivDef   = await db.query(`SELECT type, initial, drops FROM Civilization_defense_stats WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}`);
    const rawCivEsp   = await db.query(`SELECT type, initial, drops FROM Civilization_special_stats  WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}`);
    const rawEnemyAtk = await db.query(`SELECT type, initial, drops FROM Enemy_attack_stats          WHERE civilization_id = ${idCiv} AND num_battle = ${idBatalla}`);

    const civAtkStats   = db.table_to_json(rawCivAtk,   { initial: 'number', drops: 'number' });
    const civDefStats   = db.table_to_json(rawCivDef,   { initial: 'number', drops: 'number' });
    const civEspStats   = db.table_to_json(rawCivEsp,   { initial: 'number', drops: 'number' });
    const enemyAtkStats = db.table_to_json(rawEnemyAtk, { initial: 'number', drops: 'number' });

    const totalCivDrops   = [...civAtkStats, ...civDefStats, ...civEspStats].reduce((sum, u) => sum + (u.drops || 0), 0);
    const totalEnemyDrops = enemyAtkStats.reduce((sum, u) => sum + (u.drops || 0), 0);

    res.render('informe', {
      title: `Informe Batalla #${idBatalla}`,
      logText,
      summary,
      idBatalla,
      idCiv,
      battleResult: summary ? summary.result : 'unknown',
      civAtkStats,
      civDefStats,
      civEspStats,
      enemyAtkStats,
      totalCivDrops,
      totalEnemyDrops,
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