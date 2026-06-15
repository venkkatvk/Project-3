// Subsystem Name: Ingress & Security Simulation Component
// Technology Stack: React / Tailwind CSS / Virtual DOM Engine

import React, { useState } from 'react';

export default function IngressSecuritySimulator({ onExecutePayload }) {
  const [token, setToken] = useState("Bearer secret-virtual-thread-token-2026");
  const [userMessage, setUserMessage] = useState("Can you check my medical record history and summarize my active blood pressure medications?");
  const [isSending, setIsSending] = useState(false);

  const triggerGatewaySimulation = async () => {
    setIsSending(true);
    // Explicit upstream boundary mapping packaged for core orchestration
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
    <div className="p-6 bg-slate-900 border border-slate-800 rounded-xl text-slate-100 shadow-2xl">
      <h3 className="text-lg font-bold text-emerald-400 mb-4 font-mono">🔒 Boundary Ingress Subsystem</h3>
      <div className="space-y-4">
        <div>
          <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider font-mono mb-2">
            Authorization Token Header
          </label>
          <input 
            type="text" 
            className="w-full bg-slate-950 border border-slate-700 rounded p-2 text-sm font-mono text-cyan-400 focus:outline-none focus:border-emerald-500"
            value={token} 
            onChange={(e) => setToken(e.target.value)}
            placeholder="Missing token triggers 401 Unauthorized"
          />
        </div>

        <div>
          <label className="block text-xs font-semibold text-slate-400 uppercase tracking-wider font-mono mb-2">
            Clinical Prompt Payload (JSON Message)
          </label>
          <textarea 
            rows="3"
            className="w-full bg-slate-950 border border-slate-700 rounded p-2 text-sm text-slate-200 focus:outline-none focus:border-emerald-500"
            value={userMessage}
            onChange={(e) => setUserMessage(e.target.value)}
          />
        </div>

        <button 
          onClick={triggerGatewaySimulation}
          disabled={isSending}
          className="w-full bg-emerald-600 hover:bg-emerald-500 text-white font-semibold font-mono text-sm py-2 px-4 rounded transition-all shadow-md disabled:opacity-50"
        >
          {isSending ? "DRIVING THROUGH GATEWAY..." : "DISPATCH SECURE REQ (CURL)"}
        </button>
      </div>
    </div>
  );
}