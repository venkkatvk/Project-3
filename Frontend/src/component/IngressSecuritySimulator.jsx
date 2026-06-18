// Subsystem Name: Aesthetic Ingress Control Layer
// Technology Stack: React / Tailwind Engine Framework
// File Location: Frontend/src/component/IngressSecuritySimulator.jsx

import React, { useState } from 'react';

export default function IngressSecuritySimulator({ onExecutePayload }) {
  const [token, setToken] = useState("Bearer secret-virtual-thread-token-2026");
  const [userMessage, setUserMessage] = useState("Can you check my medical record history and summarize my active blood pressure medications?");
  const [isSending, setIsSending] = useState(false);

  const triggerGatewaySimulation = async () => {
    setIsSending(true);
    // Declarative payload packaging for upstream ingestion
    const payload = {
      headers: {
        "Authorization": token,
        "Content-Type": "application/json"
      },
      body: { message: userMessage }
    };
    
    await onExecutePayload(payload);
    setIsSending(false);
  };

  return (
    <div className="relative group overflow-hidden rounded-2xl p-[1px] bg-gradient-to-b from-slate-700 to-slate-900 shadow-2xl transition-all duration-300 hover:shadow-emerald-950/30">
      {/* Decorative Outer Aura */}
      <div className="absolute -inset-y-12 -inset-x-12 bg-gradient-to-r from-emerald-500/10 to-cyan-500/10 rounded-full blur-3xl opacity-0 group-hover:opacity-100 transition-opacity duration-700" />
      
      {/* Main Glass Containment Module */}
      <div className="relative bg-slate-900/90 backdrop-blur-xl p-6 rounded-2xl space-y-5">
        
        {/* Header Indicator Group */}
        <div className="flex items-center justify-between border-b border-slate-800 pb-3">
          <div className="flex items-center space-x-2">
            <span className="relative flex h-2 w-2">
              <span className={`animate-ping absolute inline-flex h-full w-full rounded-full opacity-75 ${isSending ? 'bg-amber-400' : 'bg-emerald-400'}`} />
              <span className={`relative inline-flex rounded-full h-2 w-2 ${isSending ? 'bg-amber-500' : 'bg-emerald-500'}`} />
            </span>
            <h3 className="text-sm font-black font-mono tracking-wider text-slate-200 uppercase">
              🔒 Ingress Boundary Controller
            </h3>
          </div>
          <span className="text-[10px] font-mono bg-slate-950 px-2 py-0.5 rounded border border-slate-800 text-slate-500">
            SECURE-NODE-v1
          </span>
        </div>

        {/* Input Field: Security Identity Header */}
        <div className="space-y-1.5">
          <div className="flex justify-between items-center font-mono text-[11px]">
            <label className="font-bold text-slate-400 uppercase tracking-wide">
              HTTP Authorization Header
            </label>
            <span className="text-cyan-400 font-semibold">Bearer Standard</span>
          </div>
          <div className="relative rounded-lg bg-slate-950 border border-slate-800 focus-within:border-emerald-500/50 transition-colors">
            <input 
              type="text" 
              className="w-full bg-transparent p-3 text-xs font-mono text-cyan-400 placeholder-slate-700 focus:outline-none"
              value={token} 
              onChange={(e) => setToken(e.target.value)}
              placeholder="Authorization token string..."
            />
          </div>
        </div>

        {/* Text Area: Medical Data Context Payload */}
        <div className="space-y-1.5">
          <label className="block font-mono text-[11px] font-bold text-slate-400 uppercase tracking-wide">
            Clinical Telemetry Input (JSON Parameter)
          </label>
          <div className="relative rounded-lg bg-slate-950 border border-slate-800 focus-within:border-emerald-500/50 transition-colors">
            <textarea 
              rows="4"
              className="w-full bg-transparent p-3 text-xs text-slate-300 leading-relaxed focus:outline-none resize-none"
              value={userMessage}
              onChange={(e) => setUserMessage(e.target.value)}
            />
          </div>
        </div>

        {/* Execution Engine Dispatch Controller */}
        <button 
          onClick={triggerGatewaySimulation}
          disabled={isSending}
          className="relative w-full overflow-hidden group/btn font-mono text-xs font-bold uppercase tracking-widest py-3 px-4 rounded-xl transition-all shadow-lg active:scale-[0.99] disabled:opacity-50 disabled:pointer-events-none bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white"
        >
          <div className="absolute inset-0 w-full h-full bg-gradient-to-r from-transparent via-white/10 to-transparent -translate-x-full group-hover/btn:animate-[shimmer_1.5s_infinite]" />
          <span className="relative flex items-center justify-center space-x-2">
            <span>{isSending ? "DRIVING PROTOCOL STACK..." : "DISPATCH VECTOR PAYLOAD"}</span>
          </span>
        </button>

      </div>
    </div>
  );
}