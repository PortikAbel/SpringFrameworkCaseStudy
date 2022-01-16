import React from "react";
import autoBind from "auto-bind";
import { Col, Row, Table } from "react-bootstrap";

import Tour from "./Tour";
import Paginator from "../Paginator";

import { findTours, deleteTourById } from "../../service/tour";
import TourFilterForm from "./TourFilterForm";

export default class Tours extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      tours: [],
      totalCount: 0,
      pageSize: 10,
      pageIndex: 1,
      filters: {},
      err: null
    };
    autoBind(this);
  }

  async componentDidMount() {
    await this.loadPage(this.state.pageIndex);
  }

  async loadPage(index) {
    try {
      const { pageSize, filters } = this.state;
      const result = await findTours(index, pageSize, filters);
      this.setState(result);
    } catch (err) {
      this.setState({err});
    }
  }

  async deleteTour(index) {
    try {
      let tours = [...this.state.tours];
      await deleteTourById(tours[index].id)
      tours.splice(index, 1);
      this.setState({ tours });
    } catch (err) {
      this.setState({ err });
    }
  }

  async onSearch(filters) {
    this.setState({filters});
    await this.loadPage(this.state.pageIndex);
  }

  render() {
    const { tours, totalCount, pageIndex, pageSize } = this.state;

    return (
      tours.length === 0
      ? <div>No tours to show.</div>
      : <Row>
          <Col>
            <TourFilterForm onSearch={this.onSearch}/>
          </Col>
          <Col>
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Region</th>
                  <th>Distance</th>
                  <th>Elevtion</th>
                </tr>
              </thead>
              <tbody>
                {
                  tours.map((tour, i) => (
                    <Tour
                      key={tour.id}
                      tour={tour}
                      index={i}
                      relIndex={(pageIndex-1)*pageSize + i + 1}
                      deleteCommand={this.deleteTour}
                    />
                  ))
                }
              </tbody>
            </Table>
            <Paginator
              count={Math.ceil(tours.length*1.0/totalCount)}
              active={pageIndex}
              changePage={this.loadPage}/>
          </Col>
        </Row>
    )
  }
}