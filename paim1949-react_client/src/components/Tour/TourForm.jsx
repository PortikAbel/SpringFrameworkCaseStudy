import React from 'react';
import autoBind from 'auto-bind';
import { Form, Row, Col, Button } from 'react-bootstrap';
import { findRegions } from '../../service/region';

import { createTour, updateTour, findTourById } from '../../service/tour';

export default class TourForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      tour: null,
      regions: [],
      err: null
    };
    autoBind(this);
  }

  async componentDidMount() {
    const { regions } = await findRegions();
    this.setState({regions});
    if (!this.props.newTour) {
      const tour = await findTourById(this.props.match.params.id);
      this.setState({ tour });
    }
  }

  async handleCreate(event) {
    event.preventDefault();
    const tour = this.getFormData(event.target.elements);
    await createTour(tour);
    this.props.history.push('/');
  }

  async handleUpdate(event) {
    event.preventDefault();
    const tour = this.getFormData(event.target.elements);
    await updateTour(tour, this.props.match.params.id);
    this.props.history.push('/');
  }

  getFormData(elements) {
    const signShape = elements.signShape.value;
    const signColour = elements.signColour.value;
    const distanceInKm = elements.distanceInKm.value;
    const elevationInM = elements.elevationInM.value;
    const daysRecommended = elements.daysRecommended.value;
    const region = this.state.regions[elements.region.value];
    return { signShape, signColour, distanceInKm, elevationInM, daysRecommended, region };
  }

  render() {
    const { newTour } = this.props;
    const { tour, regions } = this.state;
    return (
      <Form onSubmit={newTour
          ? this.handleCreate
          : this.handleUpdate}>
        <Form.Group as={Row} controlId='signShape'>
          <Form.Label column>Shape of sign:</Form.Label>
          <Col>
            <Form.Control as="select" value={tour?.signShape}>
              <option value="LINE">line</option>
              <option value="CIRCLE">circle</option>
              <option value="TRIANGLE">triangle</option>
              <option value="CROSS">cross</option>
            </Form.Control>
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId='signColour'>
          <Form.Label column>Colour of sign:</Form.Label>
          <Col>
            <Form.Control as="select" value={tour?.signColour}>
              <option value="RED">red</option>
              <option value="BLUE">blue</option>
              <option value="YELLOW">yellow</option>
            </Form.Control>
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId='distanceInKm'>
          <Form.Label column>distance:</Form.Label>
          <Col>
            <Form.Control type="number" defaultValue={tour?.distanceInKm}/>
          </Col>
          <Form.Label column>km</Form.Label>
        </Form.Group>
        <Form.Group as={Row} controlId='elevationInM'>
          <Form.Label column>elevation:</Form.Label>
          <Col>
            <Form.Control type="number" defaultValue={tour?.elevationInM}/>
          </Col>
          <Form.Label column>m</Form.Label>
        </Form.Group>
        <Form.Group as={Row} controlId='daysRecommended'>
          <Form.Label column>days recommended:</Form.Label>
          <Col>
            <Form.Control type="number" defaultValue={tour?.daysRecommended}/>
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId='region'>
          <Form.Label column>region:</Form.Label>
          <Col>
            <Form.Control as="select">
              {
                regions.map((region, i) => 
                  <option value={i} key={region.id} 
                    selected={region.id === tour?.region.id}>
                      {region.name}
                  </option>)
              }
            </Form.Control>
          </Col>
        </Form.Group>
        <Button variant="primary" type="submit">
          {newTour? 'Create' : 'Update'}
        </Button>
      </Form>
    );
  }
}