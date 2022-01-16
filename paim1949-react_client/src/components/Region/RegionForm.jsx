import autoBind from 'auto-bind';
import React from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';

import { createRegion, updateRegion, findRegionById } from '../../service/region';

export default class RegionForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      err: null
    };
    autoBind(this);
  }

  async componentDidMount() {
    if (!this.props.newRegion) {
      const region = await findRegionById(this.props.match.params.id);
      this.setState({ region });
    }
  }

  async handleCreate(event) {
    event.preventDefault();
    const name = event.target.elements.name.value;
    const region = { name };
    await createRegion(region);
    this.props.history.push('/');
  }

  async handleUpdate(event) {
    event.preventDefault();
    const name = event.target.elements.name.value;
    const region = { name };
    await updateRegion(region);
    this.props.history.push('/');
  }

  render() {
    const { newRegion } = this.props;
    const { region } = this.state;
    return (
      <Form onSubmit={newRegion
          ? this.handleCreate
          : this.handleUpdate}>
        <Row>
          <Form.Group as={Col} controlId='name'>
            <Form.Label>Name</Form.Label>
            <Form.Control defaultValue={region?.name}/>
          </Form.Group>
        </Row>
        <Button variant="primary" type="submit">
          {newRegion? 'Create' : 'Update'}
        </Button>
      </Form>
    );
  }
}