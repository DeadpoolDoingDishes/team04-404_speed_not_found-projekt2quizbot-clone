
import { ThemeProvider, createTheme } from '@mui/material/styles';
import './App.css';
import {useMemo} from "react";
const theme = useMemo(() => createTheme({}), []);


function App() {
  return (
    <ThemeProvider theme={theme}>
    </ThemeProvider>
  );
}

export default App;
