import React, { useState, useEffect } from "react";
import "./css/BingoGame.css";
import { ERROR_MESSAGES } from "./constants/errorMessage";

export default function BingoGame() {
  const [rows, setRows] = useState(1);
  const [cols, setCols] = useState(5);
  const [players, setPlayers] = useState(1);
  const [boards, setBoards] = useState([]);
  const [maxNumber, setMaxNumber] = useState(30);

  const handleRowsChange = (e) => setRows(parseInt(e.target.value));
  const handleColsChange = (e) => setCols(parseInt(e.target.value));
  const handleMaxNumberChange = (e) => setMaxNumber(parseInt(e.target.value));
  const handlePlayersChange = (e) => setPlayers(parseInt(e.target.value));

  const generateBoards = () => {
    let limit = rows * cols;
    if (maxNumber < limit) {
      alert(ERROR_MESSAGES.INVALID_MAX_NUMBER(limit));
      return;
    }

    const boards = [];
    for (let p = 0; p < players; ++p) {
      const board = [];
      for (let i = 0; i < rows; ++i) {
        const row = [];
        for (let j = 0; j < cols; ++j) {
          row.push(Math.floor(Math.random() * maxNumber) + 1);
        }
        board.push(row);
      }
      boards.push(board);
    }
    setBoards(boards);
  };

  useEffect(() => {
    generateBoards();
  }, [rows, cols, players]);

  return (
    <div className="bingo-container">
      <h1>Bingo Game</h1>
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
        <button onClick={generateBoards}> 시작하기 </button>
      </div>

      <div className="boards-container">
        {boards.map((board, playerIndex) => (
          <div key={playerIndex} className="player-board">
            <h2 className="player-name">플레이어 {playerIndex + 1}</h2>
            <table className="board-table">
              <tbody>
                {board.map((row, rowIndex) => (
                  <tr key={rowIndex}>
                    {row.map((number, colIndex) => (
                      <td className="board-cell" key={colIndex}>
                        {number}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))}
      </div>
    </div>
  );
}
