import autoBind from "auto-bind";
import React from "react";
import { Badge, Button, Col, Form, Row } from "react-bootstrap";

export default class TourFilterForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      filters: {}
    };
    autoBind(this);
  }

  handleChange(event) {
    let { filters }= this.state;
    filters[event.target.id] = event.target.value;
    this.setState({ filters });
  }

  handleReset(event) {
    let { filters } = this.state;
    delete filters[event.target.htmlFor];
    document.getElementById(event.target.htmlFor).value = null;
    this.setState({ filters });
  }

  handleSearch(event) {
    event.preventDefault();
    this.props.onSearch(this.state.filters);
  }

  render() {
    return (
      <>
        <h2><Badge className="badge bg-light">filters</Badge></h2>
        <Form onSubmit={this.handleSearch}>
          <Form.Group as={Row} controlId='minElevation'>
            <Form.Label column>Minimum elevation:</Form.Label>
            <Col>
              <Form.Control onChange={this.handleChange}/>
            </Col>
            <Form.Label column onClick={this.handleReset}>
              X
            </Form.Label>
          </Form.Group>
          <Form.Group as={Row} controlId='maxElevation'>
            <Form.Label column>Maximum elevation:</Form.Label>
            <Col>
              <Form.Control onChange={this.handleChange}/>
            </Col>
            <Form.Label column onClick={this.handleReset}>
              X
            </Form.Label>
          </Form.Group>
          <Form.Group as={Row} controlId='minDistance'>
            <Form.Label column>Minimum distance:</Form.Label>
            <Col>
              <Form.Control onChange={this.handleChange}/>
            </Col>
            <Form.Label column onClick={this.handleReset}>
              X
            </Form.Label>
          </Form.Group>
          <Form.Group as={Row} controlId='maxDistance'>
            <Form.Label column>Maximum distance:</Form.Label>
            <Col>
              <Form.Control onChange={this.handleChange}/>
            </Col>
            <Form.Label column onClick={this.handleReset}>
              X
            </Form.Label>
          </Form.Group>
          <Form.Group as={Row} controlId='minDaysRecommended'>
            <Form.Label column>Minimum days recommended:</Form.Label>
            <Col>
              <Form.Control onChange={this.handleChange}/>
            </Col>
            <Form.Label column onClick={this.handleReset}>
              X
            </Form.Label>
          </Form.Group>
          <Form.Group as={Row} controlId='maxDaysRecommended'>
            <Form.Label column>Maximum days recommended:</Form.Label>
            <Col>
              <Form.Control onChange={this.handleChange}/>
            </Col>
            <Form.Label column onClick={this.handleReset}>
              X
            </Form.Label>
          </Form.Group>
          <Button variant="secondary" type="submit">
            Search
          </Button>
        </Form>
      </>
    )
  }
}