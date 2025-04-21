import React from 'react';

interface HeaderMenuProps {
    children: React.ReactNode;
    currentMode: 'light' | 'dark';
    onToggleTheme: () => void;
}


const HeaderMenu: React.FC<HeaderMenuProps> = ({ children, currentMode, onToggleTheme }) => {
    return (
        <div>
            
        </div>
    );
};

export default HeaderMenu;