import React, { useState, useEffect } from "react";
import Player from "./Player";
import Referee from "./Refree";
import WinnerChecker from "./WinningChecker";
import "./css/BingoGame.css";

export default function BingoGame() {
  const [players, setPlayers] = useState(5);
  const [rows, setRows] = useState(5);
  const [cols, setCols] = useState(5);
  const [maxNumber, setMaxNumber] = useState(30);
  const [gameStart, setGameStart] = useState(false);
  const [calledNumbers, setCalledNumbers] = useState([]);
  const [isGameOver, setIsGameOver] = useState(false);
  const [winningPlayers, setWinningPlayers] = useState([]);

  const handleRowsChange = (e) => setRows(parseInt(e.target.value));
  const handleColsChange = (e) => setCols(parseInt(e.target.value));
  const handleMaxNumberChange = (e) => setMaxNumber(parseInt(e.target.value));
  const handlePlayersChange = (e) => setPlayers(parseInt(e.target.value));

  const handleGameStart = () => {
    setIsGameOver(false);
    setCalledNumbers([]);
    setWinningPlayers([]);
    setGameStart(true);
  };

  // For Refree
  const callNumber = () => {
    if (isGameOver) return;

    let randomNumber;
    while (true) {
      randomNumber = Math.floor(Math.random() * maxNumber) + 1;
      if (!calledNumbers.includes(randomNumber)) break;
    }

    setCalledNumbers([...calledNumbers, randomNumber]);
  };

  // For Players
  const handleWinners = (playerIndex) => {
    setWinningPlayers((prev) => [...prev, playerIndex]);
  };

  return (
    <div className="bingo-container">
      <h1>빙고 게임</h1>
      <div className="input-container">
        <label>
          플레이어 수
          <input type="number" value={players} onChange={handlePlayersChange} />
        </label>
        <label>
          행 수
          <input type="number" value={rows} onChange={handleRowsChange} />
        </label>
        <label>
          열 수
          <input type="number" value={cols} onChange={handleColsChange} />
        </label>
        <label>
          최대 숫자
          <input
            type="number"
            value={maxNumber}
            onChange={handleMaxNumberChange}
          />
        </label>
        <button onClick={handleGameStart}> 시작하기 </button>
      </div>
      {gameStart && (
        <>
          <Referee
            callNumber={callNumber}
            isGameOver={isGameOver}
            calledNumbers={calledNumbers}
          />
          <div className="boards-container">
            {!isNaN(players) &&
              Array(players)
                .fill()
                .map((undefined, playerIndex) => (
                  <Player
                    key={playerIndex}
                    playerIndex={playerIndex}
                    rows={rows}
                    cols={cols}
                    maxNumber={maxNumber}
                    calledNumbers={calledNumbers}
                    handleWin={() => handleWinners(playerIndex)}
                    isGameOver={isGameOver}
                  />
                ))}
          </div>
          <WinnerChecker
            winningPlayers={winningPlayers}
            players={players}
            setIsGameOver={setIsGameOver}
          />
        </>
      )}
    </div>
  );
}
