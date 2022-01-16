import React from "react";
import autoBind from "auto-bind";
import { Badge, Col, Row, Table } from "react-bootstrap";

import Tour from "./Tour";
import Paginator from "../Paginator";

import { findTours, deleteTourById } from "../../service/tour";
import TourFilterForm from "./TourFilterForm";
import SortingTableHeading from "../SortingTableHeading";

export default class Tours extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      tours: [],
      totalCount: 0,
      pageSize: 10,
      pageIndex: 1,
      filters: {},
      sorting: {},
      err: null
    };
    autoBind(this);
  }

  async componentDidMount() {
    await this.loadPage(this.state.pageIndex);
  }

  async loadPage(index) {
    this.setState({pageIndex: index});
    try {
      const { pageSize, filters, sorting } = this.state;
      const result = await findTours(index, pageSize, filters, sorting);
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
    this.setState({ filters, pageIndex: 0 });
    await this.loadPage(this.state.pageIndex);
  }

  async onSort(sorting) {
    const oldSorting = this.state.sorting;
    if (oldSorting.name === sorting.name 
      && oldSorting.direction === sorting.direction) {
      sorting = {};
    }
    this.setState({sorting}, async () => await this.loadPage(this.state.pageIndex));
  }

  render() {
    const { tours, totalCount, pageIndex, pageSize, sorting } = this.state;

    return (
      <Row>
        <Col>
          <TourFilterForm onSearch={this.onSearch}/>
        </Col>
        {tours.length === 0
        ? <div>No tours to show.</div>
        : <Col>
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>#</th>
                  <SortingTableHeading title='Region' name='region' sorting={sorting} onSort={this.onSort}/>
                  <SortingTableHeading title='Distance' name='distanceInKm' sorting={sorting} onSort={this.onSort}/>
                  <SortingTableHeading title='Elevtion' name='elevationInM' sorting={sorting} onSort={this.onSort}/>
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
              count={Math.ceil(totalCount*1.0/pageSize)}
              active={pageIndex}
              changePage={this.loadPage}/>
          </Col>
        }
      </Row>
    )
  }
}