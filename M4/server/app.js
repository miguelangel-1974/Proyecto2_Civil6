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
    host: '127.0.0.1', // No posis 'localhost', posa la IP
    port: 3307,
    user: 'root',
    password: 'rootcivil6',
    database: 'civilizations_db'
  });
} else {
  db.init({
    host: '127.0.0.1',
    port: 3306,
    user: 'root',
    password: 'rootcivil6',
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

// Helpers
hbs.registerHelper('eq', (a, b) => a == b);
hbs.registerHelper('gt', (a, b) => a > b);

// Partials
hbs.registerPartials(path.join(__dirname, 'views', 'partials'));

// --- ROUTES ---

// Index
app.get('/', async (req, res) => {
  try {
    const rows = await db.query("SELECT num_battle, log_entry FROM Battle_log ORDER BY num_battle DESC LIMIT 2");
    const battlesJson = db.table_to_json(rows, { num_battle: 'number', log_entry: 'string' });
    const commonData = JSON.parse(fs.readFileSync(path.join(__dirname, 'data', 'common.json'), 'utf8'));

    res.render('index', { ultimesBatalles: battlesJson, common: commonData });
  } catch (err) {
    console.error(err);
    res.status(500).send('Error BD a la Principal');
  }
});

// Batalles
app.get('/batalles', async (req, res) => {
  try {
    const rows = await db.query("SELECT num_battle, log_entry FROM Battle_log ORDER BY num_battle DESC");
    const battlesJson = db.table_to_json(rows, { num_battle: 'number', log_entry: 'string' });
    const commonData = JSON.parse(fs.readFileSync(path.join(__dirname, 'data', 'common.json'), 'utf8'));
    res.render('batalles', { batalles: battlesJson, common: commonData });
  } catch (err) { res.status(500).send('Error BD'); }
});

// Civilitzacio
app.get('/civilitzacio', async (req, res) => {
  try {
    const rows = await db.query("SELECT wood, gold, iron, mana FROM Civilization_stats LIMIT 1");
    const r = rows[0] || {};
    const recursosList = [
      { nom_recurs: 'Fusta', quantitat: r.wood || 0 },
      { nom_recurs: 'Or', quantitat: r.gold || 0 },
      { nom_recurs: 'Ferro', quantitat: r.iron || 0 },
      { nom_recurs: 'Mana', quantitat: r.mana || 0 }
    ];
    const commonData = JSON.parse(fs.readFileSync(path.join(__dirname, 'data', 'common.json'), 'utf8'));
    res.render('civilitzacio', { recursos: recursosList, common: commonData });
  } catch (err) { res.status(500).send('Error BD'); }
});

// Informe
app.get('/informe', async (req, res) => {
  try {
    const id = req.query.informe || 1;
    const rows = await db.query("SELECT wood_gained, gold_gained, food_gained FROM Battle_stats WHERE num_battle = ?", [id]);
    const s = rows[0] || { wood_gained: 0, gold_gained: 0, food_gained: 0 };
    const commonData = JSON.parse(fs.readFileSync(path.join(__dirname, 'data', 'common.json'), 'utf8'));
    res.render('informe', { id_batalla: id, fusta: s.wood_gained, or: s.gold_gained, menjar: s.food_gained, common: commonData });
  } catch (err) { res.status(500).send('Error BD'); }
});

// Programadors
app.get('/programadors', (req, res) => {
  const commonData = JSON.parse(fs.readFileSync(path.join(__dirname, 'data', 'common.json'), 'utf8'));
  res.render('programadors', { common: commonData });
});

// Start server
const httpServer = app.listen(port, () => {
  console.log(`http://localhost:${port}`);
  console.log(`http://localhost:${port}/batalles`);
  console.log(`http://localhost:${port}/civilitzacio`);
  console.log(`http://localhost:${port}/informe`);
  console.log(`http://localhost:${port}/programadors`);
});

// Graceful shutdown
process.on('SIGINT', async () => {
  await db.end();
  httpServer.close();
  process.exit(0);
});