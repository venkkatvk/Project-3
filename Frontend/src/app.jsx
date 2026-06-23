import React, { useState } from 'react';
import IngressSecuritySimulator from './component/IngressSecuritySimulator.jsx';

export default function App() {
  const [gatewayMetrics, setGatewayMetrics] = useState(null);
  const [liveTerminalLogs, setLiveTerminalLogs] = useState([]);

  const executeGatewayTransaction = async (packagedPayload) => {
    const startTime = performance.now();
    const timestamp = new Date().toLocaleTimeString();

    setLiveTerminalLogs(prev => [`[${timestamp}] 📡 Dispatching payload to AI gateway...`, ...prev]);

    try {
      const response = await fetch("/api/v1/chat", {
        method: "POST",
        headers: packagedPayload.headers,
        body: JSON.stringify(packagedPayload.body)
      });

      const latency = (performance.now() - startTime).toFixed(2);

      if (response.ok) {
        const data = await response.json();
        setGatewayMetrics({ status: 200, latency, error: null, cache: data.cacheStatus, tokens: data.tokens });
        setLiveTerminalLogs(prev => [
          `[${timestamp}] 🟩 HTTP 200 OK — ${latency}ms\n\n${data.answer}\n\n── Model: ${data.model} · Tokens: ${data.tokens} · Cache: ${data.cacheStatus} · ID: ${data.requestId}`,
          ...prev
        ]);
      } else {
        const errData = await response.json().catch(() => ({}));
        setGatewayMetrics({ status: response.status, latency, error: `HTTP ${response.status}` });
        setLiveTerminalLogs(prev => [
          `[${timestamp}] 🟥 REJECTED — HTTP ${response.status} (${latency}ms)\n${errData.message || 'Unauthorized or bad request.'}`,
          ...prev
        ]);
      }
    } catch (error) {
      const latency = (performance.now() - startTime).toFixed(2);
      setGatewayMetrics({ status: "ERR", latency, error: error.message });
      setLiveTerminalLogs(prev => [
        `[${timestamp}] ❌ CONNECTION FAILED — Gateway unreachable (${latency}ms)\n${error.message}`,
        ...prev
      ]);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 p-8 font-sans">
      <header className="mb-8 border-b border-slate-800 pb-4 font-mono">
        <h1 className="text-2xl font-black text-transparent bg-clip-text bg-gradient-to-r from-emerald-400 to-cyan-400 tracking-tight">
          HEALTHCARE 360 // AI PERIMETER GATEWAY DASHBOARD
        </h1>
        <p className="text-xs text-slate-500 mt-1">
          SYSTEM RUNTIME ENVIRONMENT: JAVA 21 // SPRING BOOT 3.4.0 // VIRTUAL THREAD POOL
        </p>
      </header>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="space-y-6 lg:col-span-1">
          <IngressSecuritySimulator onExecutePayload={executeGatewayTransaction} />

          {gatewayMetrics && (
            <div className="p-4 bg-slate-900 border border-slate-800 rounded-xl font-mono">
              <h4 className="text-xs text-slate-400 uppercase font-bold tracking-wider mb-2">⚡ Telemetry Metrics</h4>
              <div className="grid grid-cols-2 gap-2 text-center">
                <div className="bg-slate-950 p-2 rounded border border-slate-800">
                  <div className="text-xs text-slate-500">HTTP STATUS</div>
                  <div className={`text-sm font-bold ${gatewayMetrics.status === 200 ? 'text-emerald-400' : 'text-rose-500'}`}>
                    {gatewayMetrics.status}
                  </div>
                </div>
                <div className="bg-slate-950 p-2 rounded border border-slate-800">
                  <div className="text-xs text-slate-500">LATENCY</div>
                  <div className={`text-sm font-bold ${parseFloat(gatewayMetrics.latency) < 100 ? 'text-cyan-400' : 'text-amber-400'}`}>
                    {gatewayMetrics.latency}ms
                  </div>
                </div>
                {gatewayMetrics.tokens && (
                  <div className="bg-slate-950 p-2 rounded border border-slate-800">
                    <div className="text-xs text-slate-500">TOKENS</div>
                    <div className="text-sm font-bold text-purple-400">{gatewayMetrics.tokens}</div>
                  </div>
                )}
                {gatewayMetrics.cache && (
                  <div className="bg-slate-950 p-2 rounded border border-slate-800">
                    <div className="text-xs text-slate-500">CACHE</div>
                    <div className={`text-sm font-bold ${gatewayMetrics.cache === 'HIT' ? 'text-emerald-400' : 'text-slate-400'}`}>
                      {gatewayMetrics.cache}
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>

        <div className="lg:col-span-2 bg-slate-950 border border-slate-800 rounded-xl p-4 shadow-inner flex flex-col h-[500px]">
          <h3 className="text-sm font-bold text-slate-400 font-mono uppercase tracking-wider mb-3">🖥️ Live System Console Streams</h3>
          <div className="flex-1 bg-black p-4 rounded font-mono text-xs text-emerald-500 overflow-y-auto space-y-3 border border-slate-900 whitespace-pre-wrap">
            {liveTerminalLogs.length === 0 ? (
              <span className="text-slate-600 animate-pulse">// Idle pipeline. Awaiting ingress simulation dispatch signals...</span>
            ) : (
              liveTerminalLogs.map((log, index) => <div key={index} className="border-b border-slate-900 pb-2">{log}</div>)
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
