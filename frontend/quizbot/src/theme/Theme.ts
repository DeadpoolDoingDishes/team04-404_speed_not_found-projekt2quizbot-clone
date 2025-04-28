// src/theme.ts
import { createTheme, PaletteMode } from '@mui/material';
import { ThemeOptions } from '@mui/material/styles';

const commonTypography: ThemeOptions['typography'] = {
    fontFamily: [
        '"Segoe UI"',
        'Roboto',
        '"Helvetica Neue"',
        'Arial',
        'sans-serif',
    ].join(','),

    h1: {
        fontFamily: '"Fascinate Inline", cursive',
        fontSize: '4rem',
    },
};

const getDesignTokens = (mode: PaletteMode): ThemeOptions => ({
    palette: {
        mode,
        primary:   { main: '#5266f6' },
        secondary: { main: '#B700FF' },
        background: {
            default: mode === 'dark' ? '#001438' : 'rgb(255,255,255)',
            paper:   mode === 'dark' ? 'rgba(0,0,0,0.62)' : 'rgba(255,255,255,0.62)',
        },
    },
    typography: commonTypography,
    components: {
        MuiCssBaseline: {
            styleOverrides: {
                body: {
                    background: mode === 'dark'
                        ? 'linear-gradient(125deg, rgba(0,20,56,1) 0%, rgba(113,8,179,1) 76%, rgba(183,0,255,1) 100%)'
                        : 'linear-gradient(147deg, rgba(199,223,255,1) 23%, rgba(199,223,255,1) 42%, rgba(194,147,255,1) 85%)',
                    backgroundAttachment: 'fixed',
                    backgroundRepeat: 'no-repeat',
                    minHeight: '100vh',
                    transition: 'background 0.3s',
                },
            },
        },
    },
});

export function makeAppTheme(mode: PaletteMode) {
    return createTheme(getDesignTokens(mode));
}
