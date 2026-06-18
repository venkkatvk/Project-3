// Subsystem Name: Client Bootloader Engine
// Technology Stack: React 18 Core DOM Mount / ESM Loader
// File Location: Frontend/src/main.jsx

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './app.jsx'; // Pointing accurately to your lowercase app.jsx file
import './index.css';

// Hijacking the static HTML root layout and binding the reactive engine stack
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);