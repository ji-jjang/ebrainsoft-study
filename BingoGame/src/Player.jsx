import React, { useEffect } from "react";
import "./css/BingoGame.css";

export default function Player({
  board,
  playerIndex,
  calledNumbers,
  gameEnd,
  rows,
  cols,
}) {
  useEffect(() => {
    if (checkBingo(board, calledNumbers)) {
      gameEnd();
    }
  }, [calledNumbers]);

  const checkBingo = (board, calledNumbers) => {
    for (let row = 0; row < rows; ++row) {
      let cnt = 0;
      for (let col = 0; col < cols; ++col) {
        if (calledNumbers.includes(board[row][col])) ++cnt;
      }
      if (cnt == cols) return true;
    }

    for (let col = 0; col < cols; ++col) {
      let cnt = 0;
      for (let row = 0; row < rows; ++row) {
        if (calledNumbers.includes(board[row][col])) ++cnt;
      }
      if (cnt == rows) return true;
    }
    return false;
  };

  return (
    <div className="player-board">
      <h2 className="player-name">플레이어 {playerIndex + 1}</h2>
      <table key={calledNumbers.length} className="board-table">
        <tbody>
          {board.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {row.map((number, colIndex) => (
                <td
                  className={`board-cell ${calledNumbers.includes(number) ? "marked" : ""}`}
                  key={colIndex}
                >
                  {number}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
