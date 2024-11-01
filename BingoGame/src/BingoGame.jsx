import React, { useState, useEffect } from "react";
import "./css/BingoGame.css";
import { BOARD_MESSAGES } from "./constants/boardMessage";
import Player from "./Player";
import Referee from "./Refree";
import WinnerChecker from "./WinningChecker";

const shuffle = (array) => {
  array.sort(() => Math.random() - 0.5);
};

export default function BingoGame() {
  const [players, setPlayers] = useState(1);
  const [rows, setRows] = useState(1);
  const [cols, setCols] = useState(5);
  const [boards, setBoards] = useState([]);
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
    setBoards([]);
    generateBoards();
    setIsGameOver(false);
    setCalledNumbers([]);
    setWinningPlayers([]);
    setGameStart(true);
  };

  const generateBoards = () => {
    let limit = rows * cols;

    if (maxNumber < limit) {
      alert(BOARD_MESSAGES.INVALID_MAX_NUMBER_MSG(limit));
      return;
    }

    let selectedNumbers = [];
    for (let i = 1; i <= maxNumber; ++i) selectedNumbers.push(i);

    const newBoards = [];
    for (let p = 0; p < players; ++p) {
      const board = [];
      let index = 0;
      shuffle(selectedNumbers);
      for (let i = 0; i < rows; ++i) {
        const row = [];
        for (let j = 0; j < cols; ++j) {
          row.push(selectedNumbers[index++]);
        }
        board.push(row);
      }
      newBoards.push(board);
    }
    setBoards(newBoards);
  };

  const callNumber = () => {
    if (isGameOver) return;

    let randomNumber;
    while (true) {
      randomNumber = Math.floor(Math.random() * maxNumber) + 1;
      if (!calledNumbers.includes(randomNumber)) break;
    }

    setCalledNumbers([...calledNumbers, randomNumber]);
  };

  const handleWinners = (playerIndex) => {
    setWinningPlayers((prev) => {
      const newWinners = [...prev, playerIndex];
      return newWinners;
    });
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
            {boards.map((board, playerIndex) => (
              <Player
                key={playerIndex}
                board={board}
                playerIndex={playerIndex}
                calledNumbers={calledNumbers}
                gameEnd={() => handleWinners(playerIndex)}
                rows={rows}
                cols={cols}
              />
            ))}
          </div>
          <WinnerChecker winningPlayers={winningPlayers} players={players} setIsGameOver={setIsGameOver} />
        </>
      )}
    </div>
  );
}
