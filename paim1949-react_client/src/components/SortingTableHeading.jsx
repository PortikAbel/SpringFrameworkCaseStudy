import autoBind from "auto-bind";
import React from "react";
import { Badge } from "react-bootstrap";

export default class SortingTableHeading extends React.Component {
  constructor(props) {
    super(props);
    autoBind(this);
  }

  render() {
    const { title, name, sorting, onSort } = this.props;

    return (
      <th>{title}
        <Badge onClick={() => onSort({name, direction: 'asc'})}
          className={` badge bg-${
            sorting.name === name && 
            sorting.direction === 'asc' 
              ? 'primary'
              : 'secondary'}`}>
                <span className="no-text-select">&#9651;</span>
        </Badge>
        <Badge onClick={() => onSort({name, direction: 'desc'})}
          className={`badge bg-${
            sorting.name === name && 
            sorting.direction === 'desc'
              ? 'primary'
              : 'secondary'}`}>
                <span className="no-text-select">&#9661;</span>
        </Badge>
      </th>
    );
  }
}