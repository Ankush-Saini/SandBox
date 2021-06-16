import "./App.css";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Users from "./components/Users";
function App() {
  return (
    <div className="App">
      <header className="App-header">Notes Manager Client</header>
      <body>
        <Router>
          <Switch>
            <Route path="/" exact>
              {" "}
              <Users />
            </Route>
          </Switch>
        </Router>
      </body>
    </div>
  );
}

export default App;
