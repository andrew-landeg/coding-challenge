import React, { Component } from "react";
import { PitModel } from "./pitModel";
import Pit from "./pit";
import axios from "axios";

class KalahLayout extends Component {
  state = {
    pits: [],
    game: null,
    gameState: null
  };

  constructor(props) {
    super(props);
    this.state.pits = [];
    for (let id = 1; id <= 14; id++) {
      const isKalah = id === 7 || id === 14;
      const model = new PitModel(id, 0, isKalah);
      this.state.pits.push(model);
    }
  }
  findPit = id => {
    const pit = this.state.pits.find(p => p.id === id);
    console.log(pit);
    return pit;
  };

  handlePitClick = pit => {
    console.log("pitClicked", pit);
  };

  handleCreateGame = () => {
    console.log("handleCreateGame");
    axios
      .post("/games")
      .then(response => this.handleCreateGameResponse(response));
  };

  handleCreateGameResponse = response => {
    const game = { ...response.data };
    axios.get("/games/" + game.id).then(response => {
      const gameState = { ...response.data };
      const pits = [...this.state.pits];
      gameState.pits = { ...response.data.pits };
      for (let pitId = 1; pitId <= 14; pitId++) {
        this.findPit(pitId).stones = gameState.pits[pitId];
      }
      this.setState({ game, gameState, pits });
    });
  };

  handlePitClick = pit => {
    console.log("handlePitClick");
    const { game } = this.state;
    const url = `/games/${game.id}/pits/${pit.id}`;
    axios.put(url).then(dontCare => {
      axios.get("/games/" + game.id).then(response => {
        const gameState = { ...response.data };
        const pits = [...this.state.pits];
        gameState.pits = { ...response.data.pits };
        for (let pitId = 1; pitId <= 14; pitId++) {
          this.findPit(pitId).stones = gameState.pits[pitId];
        }
        this.setState({ game, gameState, pits });
      });
    });
  };

  updateGameState = () => {
    const { game } = this.state;
    console.log("updateGameState state", this.state);
    console.log("game", game);
  };

  render = () => {
    return (
      <div>
        <h2>Kalah</h2>
        {this.renderGame()}
        <button onClick={this.handleCreateGame}>Create game</button>
        <div class="game-board">
          <Pit onClick={this.handlePitClick} pit={this.findPit(14)} />
          <div class="pits-zone">
            <Pit onClick={this.handlePitClick} pit={this.findPit(13)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(12)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(11)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(10)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(9)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(8)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(1)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(2)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(3)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(4)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(5)} />
            <Pit onClick={this.handlePitClick} pit={this.findPit(6)} />
          </div>
          <Pit onClick={this.handlePitClick} pit={this.findPit(7)} />
        </div>
      </div>
    );
  };

  renderGame = () => {
    const { gameState } = this.state;
    if (!gameState) {
      return <p>No game active</p>;
    }
    return (
      <React.Fragment>
        <span class="bold">gameId</span>
        <span>{gameState.id}</span>
        <br />
        <span class="bold">player</span>
        <span>{gameState.currentPlayer}</span>
        <br />
      </React.Fragment>
    );
  };
}

export default KalahLayout;
