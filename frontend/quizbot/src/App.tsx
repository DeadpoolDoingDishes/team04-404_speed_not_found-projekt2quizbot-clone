//import { ThemeProvider, createTheme } from '@mui/material/styles';
import './App.css';
import React, {useMemo, useState} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import {CssBaseline} from "@mui/material";
//const theme = useMemo(() => createTheme({}), []);


function App() {
  return (
      <><CssBaseline/><Router>
        <Routes>
          <Route path="/" element={<Home/>}/>
        </Routes>
      </Router></>
  );
}

export default App;
