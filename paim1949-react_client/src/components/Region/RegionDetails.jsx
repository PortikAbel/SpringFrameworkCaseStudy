import autoBind from "auto-bind";
import React from "react";
import { Badge } from "react-bootstrap";
import { Link } from "react-router-dom";
import { findRegionById } from "../../service/region";

export default class RegionDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      region: {}
    }
    autoBind(this);
  }
  
  async componentDidMount() {
    const region = await findRegionById(this.props.match.params.id);
    this.setState({ region });
  }

  render() {
    const { region } = this.state;
    return (
      <>
        <h1>
          <Badge className="badge bg-primary">{region.name}</Badge>
          <Link to={`/regions/${region.id}/update`}>
            <img src="/resources/update.png" alt="update" className="icon"/>
          </Link>
        </h1>
      </>
    )
  }
}