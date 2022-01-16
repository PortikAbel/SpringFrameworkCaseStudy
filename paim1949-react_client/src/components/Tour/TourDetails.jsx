import autoBind from "auto-bind";
import React from "react";
import { Badge } from "react-bootstrap";
import { Link } from "react-router-dom";
import { findTourById } from "../../service/tour";

export default class TourDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      tour: {}
    }
    autoBind(this);
  }
  
  async componentDidMount() {
    const tour = await findTourById(this.props.match.params.id);
    this.setState({ tour });
  }

  render() {
    const { tour } = this.state;
    return (
      <>
        <h1>
          <Badge className="badge bg-primary">{tour.region.name}</Badge>
          <Link to={`/tours/${tour.id}/update`}>
            <img src="/resources/update.png" alt="update" className="icon"/>
          </Link>
        </h1>
      </>
    )
  }
}