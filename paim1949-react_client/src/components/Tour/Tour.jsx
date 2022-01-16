import React from "react";
import autoBind from "auto-bind";
import { Link } from 'react-router-dom';

export default class Tour extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      err: null
    };

    autoBind(this);
  }

  render() {
    const { tour, index, relIndex, deleteCommand } = this.props;
    const { err } = this.state;

    if (err) {
      return(<div>{err}</div>);
    }

    return (
      <tr>
        <td>{relIndex}</td>
        <td>{tour.region.name}</td>
        <td>{tour.distanceInKm}</td>
        <td>{tour.elevationInM}</td>
        <td>
          <Link to={`/tours/${tour.id}`}>
            <img src="/resources/detail.png" alt="update" className="icon"/>
          </Link>
        </td>
        <td>
          <img src="/resources/delete.png" alt="delete" className="icon"
          onClick={() => deleteCommand(index)}/>
        </td>
      </tr>
    )
  }
}