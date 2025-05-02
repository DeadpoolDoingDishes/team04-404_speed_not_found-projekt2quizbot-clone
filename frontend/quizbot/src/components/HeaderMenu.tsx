import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
    AppBar,
    Box,
    Container,
    Link,
    Toolbar,
    Typography,
    Button, IconButton,
} from '@mui/material';

import {
    LightMode as LightModeIcon,
    DarkMode as DarkModeIcon,

} from '@mui/icons-material';

interface HeaderMenuProps {
    children: React.ReactNode;
    currentMode: 'light' | 'dark';
    onToggleTheme: () => void;
}


const HeaderMenu: React.FC<HeaderMenuProps> = ({ children, currentMode, onToggleTheme }) => {
    return (
        <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        <Link component={RouterLink} to="/" color="inherit" underline="none">
                            QuizBot
                        </Link>
                    </Typography>
                    <IconButton
                        color="inherit"
                        aria-label={`Switch to ${currentMode === 'dark' ? 'light' : 'dark'} mode`}
                        onClick={onToggleTheme}
                    >
                        {currentMode === 'dark' ? <LightModeIcon /> : <DarkModeIcon />}
                    </IconButton>

                    <Button color="inherit" component={RouterLink} to="/generate">
                        Generate
                    </Button>
                    <Button color="inherit" component={RouterLink} to="/practice">
                        Practice
                    </Button>
                </Toolbar>
            </AppBar>
            <Container component="main" sx={{ mt: 4, mb: 4, flex: 1 }}>
                {children}
            </Container>
            <Box
                component="footer"
                sx={{
                    py: 3,
                    px: 2,
                    mt: 'auto',
                    backgroundColor: (theme) => theme.palette.background.paper,
                }}
            >
                <Container maxWidth="sm">
                    <Typography variant="body2" color="text.secondary" align="center">
                        © {new Date().getFullYear()} QuizBot. All rights reserved.
                    </Typography>
                </Container>
            </Box>
        </Box>
    );
};

export default HeaderMenu;