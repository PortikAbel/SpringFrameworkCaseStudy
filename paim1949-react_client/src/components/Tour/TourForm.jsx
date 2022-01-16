import autoBind from 'auto-bind';
import React from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';

import { createTour, updateTour, findTourById } from '../../service/tour';

export default class TourForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      err: null
    };
    autoBind(this);
  }

  async componentDidMount() {
    if (!this.props.newTour) {
      const tour = await findTourById(this.props.match.params.id);
      this.setState({ tour });
    }
  }

  async handleCreate(event) {
    event.preventDefault();
    const name = event.target.elements.name.value;
    const tour = { name };
    await createTour(tour);
    this.props.history.push('/');
  }

  async handleUpdate(event) {
    event.preventDefault();
    const name = event.target.elements.name.value;
    const tour = { name };
    await updateTour(tour);
    this.props.history.push('/');
  }

  render() {
    const { newTour } = this.props;
    const { tour } = this.state;
    return (
      <Form onSubmit={newTour
          ? this.handleCreate
          : this.handleUpdate}>
        <Row>
          <Form.Group as={Col} controlId='name'>
            <Form.Label>Name</Form.Label>
            <Form.Control defaultValue={tour?.name}/>
          </Form.Group>
        </Row>
        <Button variant="primary" type="submit">
          {newTour? 'Create' : 'Update'}
        </Button>
      </Form>
    );
  }
}