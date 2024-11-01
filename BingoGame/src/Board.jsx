import React from "react";
import Tile from "./Tile";
import "./css/BingoGame.css";

export default function Board({ board, calledNumbers }) {
  return (
    <table className="board-table">
      <tbody>
        {board.map((row, rowIndex) => (
          <tr key={rowIndex}>
            {row.map((number, colIndex) => (
              <Tile
                key={colIndex}
                number={number}
                isMarked={calledNumbers.includes(number)}
              />
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
