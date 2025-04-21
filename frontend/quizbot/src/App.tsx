import './App.css';
import './index.css';
import React, {useMemo, useState} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import HeaderMenu from './components/HeaderMenu';

import { makeAppTheme } from './theme/Theme';
import { ThemeProvider, CssBaseline, PaletteMode } from '@mui/material';

function App() {
    const [mode, setMode] = useState<PaletteMode>('dark');

    const theme = useMemo(() => makeAppTheme(mode), [mode]);
  return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Router>
                <HeaderMenu
                    currentMode={mode}
                    onToggleTheme={() =>
                        setMode((prev) => (prev === 'dark' ? 'light' : 'dark'))
                    }
                >
                    <Routes>
                        <Route path="/"        element={<Home />} />
                    </Routes>
                </HeaderMenu>
            </Router>
        </ThemeProvider>
  );
}

export default App;
