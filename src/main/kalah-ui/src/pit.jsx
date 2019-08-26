import React, { Component } from "react";

class Pit extends Component {
  render() {
    const { pit, onClick } = this.props;
    const className = `pit${pit.isKalah ? " kalah" : ""}`;
    console.log("classname : ", className);
    return (
      <div className={className} onClick={() => onClick(pit)}>
        <span>[{pit.id}]</span>
        <br />
        <span>{pit.stones}</span>
      </div>
    );
  }
}

export default Pit;
