import React, { Component } from 'react';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import './styles/global.scss';
import Header from './components/Header';
import Main from './components/Main';
import Login from './components/Login';
import NavBar from './components/NavBar';
import Test from './test/Test'
import BottomNav from './components/BottomNav.js';
import FoodPage from './components/FoodPage';

const theme = createMuiTheme({
  palette: {
    primary: {
      // light: will be calculated from palette.primary.main,
      main: '#64b5f6',
      // dark: will be calculated from palette.primary.main,
      // contrastText: will be calculated to contrast with palette.primary.main
    },
    secondary: {
      main: '#BCAAA4',
      // dark: will be calculated from palette.secondary.main,
      contrastText: '#ffcc00',
    },
    // error: will use the default color
  },

  typography: {
    useNextVariants: true,
  },
});


class App extends Component {
  render() {
    return (
      <MuiThemeProvider theme={theme}>
        <div>
          
          <Main />


        </div>
      </MuiThemeProvider>

    );
  }
}

export default App;