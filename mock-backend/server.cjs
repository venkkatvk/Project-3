const http = require('http');

const PORT = 8080;

function generateResponse(message) {
  const msg = message.toLowerCase();

  if (msg.includes('blood pressure') || msg.includes('hypertension') || msg.includes('medication')) {
    return "Patient has 3 active antihypertensive medications on file: Lisinopril 10mg (once daily), Amlodipine 5mg (once daily), and Hydrochlorothiazide 25mg (once daily). Most recent BP reading: 128/82 mmHg. Adherence score: 87%. Recommend follow-up in 30 days and a Comprehensive Metabolic Panel in 90 days.";
  }
  if (msg.includes('medical record') || msg.includes('history')) {
    return "Medical record summary retrieved. Patient has 4 active diagnoses: Stage 1 Hypertension, Type 2 Diabetes (controlled), Hyperlipidemia, and Mild Osteoarthritis. Last visit: 2026-05-14. No outstanding referrals. Next scheduled appointment: 2026-07-02.";
  }
  if (msg.includes('lab') || msg.includes('test') || msg.includes('result')) {
    return "Most recent lab results (2026-06-01): HbA1c 6.8% (target <7%), LDL 98 mg/dL (target <100mg/dL), eGFR 72 mL/min/1.73m² (normal range), Potassium 4.1 mEq/L (normal). All values within therapeutic targets. No immediate clinical action required.";
  }
  if (msg.includes('diagnos') || msg.includes('condition')) {
    return "Active conditions: Hypertension (ICD-10: I10), Type 2 Diabetes Mellitus (E11.9), Hyperlipidemia (E78.5). Risk stratification: MODERATE. Preventive care gap: Pneumococcal vaccine due, colorectal screening overdue by 6 months.";
  }
  if (msg.includes('allerg')) {
    return "Known allergies on file: Penicillin (reaction: rash, severity: moderate), Sulfa drugs (reaction: anaphylaxis, severity: severe). No food allergies documented. Last allergy review: 2026-01-20.";
  }

  return `Query processed. Based on available clinical data, the following assessment was generated for: "${message.substring(0, 60)}${message.length > 60 ? '...' : ''}". No contraindications detected. Security perimeter cleared. All PHI access logged under audit trail ID #${Math.floor(Math.random() * 90000) + 10000}.`;
}

let requestCount = 0;

function corsHeaders(res) {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization, X-Enterprise-Token');
}

const server = http.createServer((req, res) => {
  corsHeaders(res);

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);

  if (req.method === 'POST' && req.url === '/api/v1/chat') {
    const authHeader = req.headers['authorization'] || '';
    const enterpriseToken = req.headers['x-enterprise-token'] || '';

    if (!authHeader && !enterpriseToken) {
      res.writeHead(401, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ error: 'UNAUTHORIZED', message: 'Missing or invalid Enterprise token.' }));
      return;
    }

    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
      try {
        const parsed = JSON.parse(body);
        const message = parsed.message || '';
        console.log(`[GATEWAY] Processing: "${message.substring(0, 60)}"`);

        const delay = Math.floor(Math.random() * 60) + 15;
        requestCount++;

        setTimeout(() => {
          res.writeHead(200, { 'Content-Type': 'application/json' });
          res.end(JSON.stringify({
            answer: generateResponse(message),
            model: 'gpt-4o',
            tokens: Math.floor(Math.random() * 200) + 80,
            cacheStatus: Math.random() > 0.5 ? 'HIT' : 'MISS',
            requestId: `req_${Date.now()}`,
          }));
        }, delay);
      } catch (e) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ error: 'BAD_REQUEST', message: 'Invalid JSON body.' }));
      }
    });
    return;
  }

  if (req.method === 'GET' && req.url === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'UP', service: 'AI Gateway Mock', port: PORT }));
    return;
  }

  res.writeHead(404, { 'Content-Type': 'application/json' });
  res.end(JSON.stringify({ error: 'NOT_FOUND' }));
});

server.listen(PORT, 'localhost', () => {
  console.log(`AI Gateway Mock Backend running on http://localhost:${PORT}`);
});
