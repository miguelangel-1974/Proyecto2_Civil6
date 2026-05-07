const express = require('express');
const fs = require('fs');
const path = require('path');
const hbs = require('hbs');
const MySQL = require('./utilsMySQL');

const app = express();
const port = 3000;

// Detectar si estem al Proxmox (si és pm2)
const isProxmox = !!process.env.PM2_HOME;

// Iniciar connexió MySQL
const db = new MySQL();
if (!isProxmox) {
  db.init({
    host: 'localhost',
    port: 3306,
    user: 'appuser',
    password: '1234',
    database: 'civilizations_db'
  });
} else {
  db.init({
    host: '127.0.0.1',
    port: 3306,
    user: 'root',
    password: '1234',
    database: 'civilizations_db'
  });
}

// Static files
app.use(express.static('public'))
app.use(express.urlencoded({ extended: true }))

// Disable cache
app.use((req, res, next) => {
  res.setHeader('Cache-Control', 'no-store, no-cache, must-revalidate, proxy-revalidate');
  res.setHeader('Pragma', 'no-cache');
  res.setHeader('Expires', '0');
  res.setHeader('Surrogate-Control', 'no-store');
  next();
});

// Handlebars
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');

// Registrar "Helpers .hbs"
hbs.registerHelper('eq', (a, b) => a == b);
hbs.registerHelper('gt', (a, b) => a > b);

// Partials de Handlebars
hbs.registerPartials(path.join(__dirname, 'views', 'partials'));

// --- RUTES ---

// 1. Pàgina Principal (Inici)
app.get('/', async (req, res) => {
  try {
    const battlesRows = await db.query(`
      SELECT id_battle, date, result 
      FROM Battle_log 
      ORDER BY date DESC 
      LIMIT 2;
    `);

    const battlesJson = db.table_to_json(battlesRows, {
      id_battle: 'number',
      date: 'string',
      result: 'string'
    });

    res.render('index', {
      ultimesBatalles: battlesJson
    });
  } catch (err) {
    console.error(err);
    res.status(500).send('Error BD');
  }
});

// 2. Batalles (Llistat complet)
app.get('/batalles', async (req, res) => {
  try {
    const allBattlesRows = await db.query("SELECT id_battle, date, result FROM Battle_log ORDER BY date DESC");
    const totalRows = await db.query("SELECT COUNT(*) as total FROM Battle_log");

    const battlesJson = db.table_to_json(allBattlesRows, {
      id_battle: 'number',
      date: 'string',
      result: 'string'
    });

    res.render('batalles', {
      batalles: battlesJson,
      totalBatalles: totalRows[0].total
    });
  } catch (err) {
    console.error(err);
    res.status(500).send('Error BD');
  }
});

// 3. Informe de batalla (?informe=id)
app.get('/informe', async (req, res) => {
  try {
    const id = req.query.informe;
    const statsRows = await db.query("SELECT * FROM Battle_stats WHERE id_battle = ?", [id]);

    res.render('informe', {
      id_batalla: id,
      fusta: statsRows[0]?.wood_gained || 0,
      or: statsRows[0]?.gold_gained || 0,
      menjar: statsRows[0]?.food_gained || 0
    });
  } catch (err) {
    console.error(err);
    res.status(500).send('Error BD');
  }
});

// 4. Civilització (Recursos)
app.get('/civilitzacio', async (req, res) => {
  try {
    const civRows = await db.query("SELECT * FROM Civilization_stats LIMIT 1");
    
    const recursosJson = [
      { nom_recurs: 'Fusta', quantitat: civRows[0].wood },
      { nom_recurs: 'Or', quantitat: civRows[0].gold },
      { nom_recurs: 'Ferro', quantitat: civRows[0].iron },
      { nom_recurs: 'Mana', quantitat: civRows[0].mana }
    ];

    res.render('civilitzacio', {
      recursos: recursosJson
    });
  } catch (err) {
    console.error(err);
    res.status(500).send('Error BD');
  }
});

// 5. Programadors
app.get('/programadors', (req, res) => {
  res.render('programadors');
});

// Start server (Estructura demanada)
const httpServer = app.listen(port, () => {
  console.log(`http://localhost:${port}`);
  console.log(`http://localhost:${port}/batalles`);
  console.log(`http://localhost:${port}/informe`);
  console.log(`http://localhost:${port}/civilitzacio`);
  console.log(`http://localhost:${port}/programadors`);
});

// Graceful shutdown
process.on('SIGINT', async () => {
  await db.end();
  httpServer.close();
  process.exit(0);
});