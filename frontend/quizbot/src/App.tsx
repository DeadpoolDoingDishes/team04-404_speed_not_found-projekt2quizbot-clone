import React, {useMemo, useState} from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import './App.css';

function App() {
  return (
    <ThemeProvider theme={theme}>
    </ThemeProvider>
  );
}

export default App;
