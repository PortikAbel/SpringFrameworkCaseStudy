import React from "react";
import autoBind from 'auto-bind';
import { Link } from 'react-router-dom';
import { Button } from 'react-bootstrap';

export default class NavBar extends React.Component {
  constructor(props) {
    super(props);
    autoBind(this);
  }

  render() {
    return (
      <nav>
        <Button variant="link"><Link to="/">Home</Link></Button>
        <Button variant="link"><Link to="/tours">Tours</Link></Button>
        <Button variant="link"><Link to="/regions/create">Add region</Link></Button>
      </nav>
    )
  }
}