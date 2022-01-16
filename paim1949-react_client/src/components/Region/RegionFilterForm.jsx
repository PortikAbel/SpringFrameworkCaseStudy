import autoBind from "auto-bind";
import React from "react";
import { Badge, Button, Col, Form, Row } from "react-bootstrap";

export default class RegionFilterForm extends React.Component {
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
          <Form.Group as={Row} controlId='name'>
            <Form.Label column>Name:</Form.Label>
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