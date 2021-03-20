import React from "react";
import { Route, Switch, Redirect } from "react-router-dom";
import logo from './logo.svg';
import './App.css';
import { Container } from "semantic-ui-react";
import Navigation from './components/navigation/navigation';

class App extends React.Component {
  render = () => 
  <div className="App">
      <Navigation />
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
      <Container>
        <Switch>
          <Route
            exact path="/Home"
            render={props => (
              <div></div>
            )}
          />
          <Route exact path="/">
            <Redirect to="/Home" />
          </Route>
        </Switch>
      </Container>
    </div>;
}

export default App;
