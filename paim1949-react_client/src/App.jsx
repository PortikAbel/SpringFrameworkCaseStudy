import React from "react";
import autoBind from "auto-bind";
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import NavBar from "./components/NavBar";
import Regions from "./components/Region/Regions";
import RegionForm from "./components/Region/RegionForm";
import RegionDetails from "./components/Region/RegionDetails";
import Tours from "./components/Tour/Tours";
import TourDetails from "./components/Tour/TourDetails";

export default class App extends React.Component {
  constructor(props) {
    super(props);
    autoBind(this);
  }

  render () {
    return (
      <BrowserRouter>
        <Route component={NavBar}/>
        <main>
          <Switch>
            <Route exact path="/" component={Regions}/>
            <Route exact path="/tours" component={Tours}/>
            <Route exact path="/regions/create" component={(params) => <RegionForm newRegion={true} {...params}/>} />
            <Route exact path="/regions/:id" component={RegionDetails} />
            <Route exact path="/tours/:id" component={TourDetails} />
            <Route exact path="/regions/:id/update" component={(params) => <RegionForm newRegion={false} {...params}/>} />
          </Switch>
        </main>
      </BrowserRouter>
    )
  }
}