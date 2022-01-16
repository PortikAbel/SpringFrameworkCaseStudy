import React from "react";
import autoBind from "auto-bind";
import { Col, Row, Table } from "react-bootstrap";

import Region from "./Region";
import Paginator from "../Paginator";

import { findRegions, deleteRegionById } from "../../service/region";
import RegionFilterForm from "./RegionFilterForm";

export default class Regions extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      regions: [],
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
      const result = await findRegions(index, pageSize, filters);
      this.setState(result);
    } catch (err) {
      this.setState({err});
    }
  }

  async deleteRegion(index) {
    try {
      let regions = [...this.state.regions];
      await deleteRegionById(regions[index].id)
      regions.splice(index, 1);
      this.setState({ regions });
    } catch (err) {
      this.setState({ err });
    }
  }

  async onSearch(filters) {
    this.setState({filters});
    await this.loadPage(this.state.pageIndex);
  }

  render() {
    const { regions, totalCount, pageIndex, pageSize } = this.state;

    return (
      regions.length === 0
      ? <div>No regions to show.</div>
      : <Row>
          <Col>
            <RegionFilterForm onSearch={this.onSearch}/>
          </Col>
          <Col>
            <Table striped bordered hover>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                </tr>
              </thead>
              <tbody>
                {
                  regions.map((region, i) => (
                    <Region
                      key={region.id}
                      region={region}
                      index={i}
                      relIndex={(pageIndex-1)*pageSize + i + 1}
                      deleteCommand={this.deleteRegion}
                    />
                  ))
                }
              </tbody>
            </Table>
            <Paginator
              count={Math.ceil(regions.length*1.0/totalCount)}
              active={pageIndex}
              changePage={this.loadPage}/>
          </Col>
        </Row>
    )
  }
}