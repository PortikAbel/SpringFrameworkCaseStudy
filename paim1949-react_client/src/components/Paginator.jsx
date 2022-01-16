import React from "react";
import { Pagination } from "react-bootstrap";

export default class Paginator extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { count, active, changePage } = this.props;
    let items = [];
    for (let num = 1; num <= count; num++) {
      items.push(
        <Pagination.Item
          key={num}
          active={num === active}
          onClick={() => changePage(num)}>
          {num}
        </Pagination.Item>
      );
    }

    return (<Pagination>{items}</Pagination>);
  }
}