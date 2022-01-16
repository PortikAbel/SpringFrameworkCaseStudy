import React from "react";
import autoBind from "auto-bind";
import { Link } from 'react-router-dom';

export default class Region extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      err: null
    };

    autoBind(this);
  }

  render() {
    const { region, index, relIndex, deleteCommand } = this.props;
    const { err } = this.state;

    if (err) {
      return(<div>{err}</div>);
    }

    return (
      <tr>
        <td>{relIndex}</td>
        <td>{region.name}</td>
        <td>
          <Link to={`/regions/${region.id}`}>
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