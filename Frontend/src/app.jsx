// Subsystem Name: Core Dashboard Orchestration Layer
// Technology Stack: React Root Workspace Engine (App.jsx)
// File Location: Frontend/src/app.jsx

import React, { useState } from 'react';
import IngressSecuritySimulator from './component/IngressSecuritySimulator.jsx';

export default function App() {
  const [gatewayMetrics, setGatewayMetrics] = useState(null);
  const [liveTerminalLogs, setLiveTerminalLogs] = useState([]);

  // Centralized async dispatcher to route requests across boundary ports
  const executeGatewayTransaction = async (packagedPayload) => {
    const startTime = performance.now();
    const timestamp = new Date().toLocaleTimeString();
    
    try {
      setLiveTerminalLogs(prev => [`[${timestamp}] 📡 Initiating virtual thread handshake to endpoint port 8081...`, ...prev]);
      
      const response = await fetch("http://localhost:8081/api/v1/chat", {
        method: "POST",
        headers: packagedPayload.headers,
        body: JSON.stringify(packagedPayload.body)
      });
      
      const endTime = performance.now();
      const latency = (endTime - startTime).toFixed(2);
      const dataText = await response.text();
      
      if (response.ok) {
        setGatewayMetrics({ status: 200, latency, error: null });
        setLiveTerminalLogs(prev => [
          `[${timestamp}] 🟩 HTTP 200 OK — Handshake Resolved (${latency}ms)\n--------------------------------------------------\n${dataText}`,
          ...prev
        ]);
      } else {
        setGatewayMetrics({ status: response.status, latency, error: `HTTP ${response.status}` });
        setLiveTerminalLogs(prev => [
          `[${timestamp}] 🟥 HTTP ${response.status} UNAUTHORIZED — Security interceptor dropped unauthenticated packet.`,
          ...prev
        ]);
      }
    } catch (error) {
      const endTime = performance.now();
      setGatewayMetrics({ status: "CRASH", latency: (endTime - startTime).toFixed(2), error: error.message });
      setLiveTerminalLogs(prev => [
        `[${timestamp}] ❌ TRANSPORT FAILURE — Core citadel on port 8081 is sleeping or unreachable. Check local Docker/Java states.`,
        ...prev
      ]);
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 p-6 md:p-12 relative flex flex-col justify-between selection:bg-emerald-500/20 selection:text-emerald-400">
      
      {/* Background Sacred Geometric Lights */}
      <div className="absolute top-0 left-1/4 w-96 h-96 bg-emerald-500/5 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute bottom-12 right-1/4 w-96 h-96 bg-cyan-500/5 rounded-full blur-3xl pointer-events-none" />

      <div className="max-w-7xl w-full mx-auto space-y-8 flex-1">
        {/* Top Operational Header */}
        <header className="flex flex-col md:flex-row md:items-center md:justify-between border-b border-slate-900 pb-6 space-y-4 md:space-y-0">
          <div>
            <div className="flex items-center space-x-3">
              <h1 className="text-xl md:text-2xl font-black font-mono tracking-wider bg-gradient-to-r from-slate-100 via-slate-300 to-slate-500 bg-clip-text text-transparent">
                HEALTHCARE 360 // <span className="text-emerald-400">GATEWAY CONSOLE</span>
              </h1>
              <span className="animate-pulse bg-emerald-500/10 text-emerald-400 text-[10px] font-mono px-2 py-0.5 rounded border border-emerald-500/20 uppercase tracking-widest">
                System Live
              </span>
            </div>
            <p className="text-xs text-slate-500 font-mono mt-1 uppercase tracking-wide">
              Production Simulation Matrix // Virtual Thread Engine
            </p>
          </div>
          <div className="text-left md:text-right font-mono text-[10px] text-slate-600 bg-slate-900/40 p-3 rounded-xl border border-slate-900 backdrop-blur-sm">
            <div>HOST: ENGINE_NODE_LOCAL</div>
            <div>COMPILER: JAVA 25 // SPRING BOOT 3.4.0</div>
          </div>
        </header>

        {/* Master Asymmetric Layout Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
          
          {/* Left Column Controls (Width: 5/12) */}
          <div className="lg:col-span-5 space-y-6">
            <IngressSecuritySimulator onExecutePayload={executeGatewayTransaction} />
            
            {/* Real-Time Telemetry Monitor */}
            {gatewayMetrics && (
              <div className="bg-slate-900/40 border border-slate-900 rounded-2xl p-5 font-mono shadow-xl backdrop-blur-xl space-y-4">
                <h4 className="text-xs text-slate-400 uppercase font-black tracking-widest flex items-center space-x-2">
                  <span>⚡ Telemetry Indicators</span>
                </h4>
                <div className="grid grid-cols-2 gap-4">
                  <div className="bg-slate-950 p-3 rounded-xl border border-slate-900">
                    <div className="text-[10px] text-slate-500 uppercase font-bold tracking-wider mb-1">HTTP RESPONSE</div>
                    <div className={`text-base font-black ${gatewayMetrics.status === 200 ? 'text-emerald-400' : 'text-rose-500'}`}>
                      {gatewayMetrics.status}
                    </div>
                  </div>
                  <div className="bg-slate-950 p-3 rounded-xl border border-slate-900">
                    <div className="text-[10px] text-slate-500 uppercase font-bold tracking-wider mb-1">WIRE LATENCY</div>
                    <div className="text-base font-black text-cyan-400">{gatewayMetrics.latency}<span className="text-xs text-slate-600 font-medium">ms</span></div>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Right Column Terminal Stream (Width: 7/12) */}
          <div className="lg:col-span-7 bg-slate-900/30 border border-slate-900 rounded-2xl p-5 shadow-2xl backdrop-blur-xl flex flex-col h-[520px]">
            <div className="flex items-center justify-between mb-4 border-b border-slate-900 pb-2">
              <h3 className="text-xs font-black text-slate-400 font-mono uppercase tracking-widest">
                🖥️ Live Diagnostic Stream Console
              </h3>
              <div className="flex space-x-1.5">
                <span className="w-2.5 h-2.5 rounded-full bg-slate-800" />
                <span className="w-2.5 h-2.5 rounded-full bg-slate-800" />
                <span className="w-2.5 h-2.5 rounded-full bg-slate-800" />
              </div>
            </div>
            
            {/* Scrollable Shell Output Block */}
            <div className="flex-1 bg-slate-950 p-4 rounded-xl font-mono text-xs text-slate-300 overflow-y-auto space-y-4 border border-slate-900/60 shadow-inner leading-relaxed">
              {liveTerminalLogs.length === 0 ? (
                <div className="text-slate-600 animate-pulse font-mono text-center pt-24">
                  // Awaiting packet transmission. Ingress channel is listening...
                </div>
              ) : (
                liveTerminalLogs.map((log, index) => (
                  <div key={index} className="border-b border-slate-900 pb-3 last:border-0 whitespace-pre-wrap">
                    {log}
                  </div>
                ))
              )}
            </div>
          </div>

        </div>
      </div>

      {/* Decorative Branding Footer */}
      <footer className="mt-12 pt-6 border-t border-slate-900 text-center font-mono text-[10px] text-slate-600 tracking-widest uppercase">
        Healthcare 360 Ecosystem // Protected System Node
      </footer>
    </div>
  );
}