const http = require('http');

const PORT = 8080;

const mockResponses = [
  "Based on your medical records, I've identified 3 active blood pressure medications: Lisinopril 10mg (daily), Amlodipine 5mg (daily), and Hydrochlorothiazide 25mg (daily). Your last BP reading on file was 128/82 mmHg — within acceptable range. Recommend scheduling a follow-up in 30 days.",
  "Vector cache HIT (semantic similarity: 0.94). Retrieving cached clinical response for hypertension profile query. Patient record last updated: 2026-06-01. Embedding model: text-embedding-3-small. Response latency: 12ms.",
  "AI Gateway processing complete. Semantic routing applied. No PHI exposure detected. Security filter: PASSED. Cache layer: MISS — generating new embedding. OpenAI API call dispatched on Virtual Thread pool.",
  "Clinical decision support engaged. Risk stratification: LOW. Medication adherence score: 87%. Suggested intervention: Continue current regimen. Next lab panel recommended: Comprehensive Metabolic Panel in 90 days.",
  "Perimeter security PASSED. Enterprise token validated. Ingress pipeline nominal. Processing clinical query through RAG pipeline — 4 relevant document chunks retrieved from pgvector store.",
];

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
        console.log(`[GATEWAY] Processing: "${message.substring(0, 60)}..."`);

        const simulatedLatency = Math.floor(Math.random() * 60) + 15;
        const response = mockResponses[requestCount % mockResponses.length];
        requestCount++;

        setTimeout(() => {
          res.writeHead(200, { 'Content-Type': 'text/plain' });
          res.end(`[AI GATEWAY RESPONSE]\n\n${response}\n\n---\nModel: gpt-4o | Tokens: ${Math.floor(Math.random() * 300) + 100} | Cache: ${Math.random() > 0.5 ? 'HIT' : 'MISS'} | Latency: ${simulatedLatency}ms`);
        }, simulatedLatency);
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
  console.log('Endpoints:');
  console.log(`  POST /api/v1/chat  — AI gateway chat endpoint`);
  console.log(`  GET  /health       — Health check`);
});
