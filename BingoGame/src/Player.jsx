import React, { useState, useEffect } from "react";
import Board from "./Board";
import { shuffle } from "./utils";
import "./css/BingoGame.css";

export default function Player({
  playerIndex,
  rows,
  cols,
  maxNumber,
  calledNumbers,
  handleWin,
  isGameOver
}) {
  const [board, setBoard] = useState([]);

  const generateBoard = () => {

    let limit = rows * cols;
    console.log("limit: " + limit);

    if (maxNumber < limit) {
      alert(BOARD_MESSAGES.INVALID_MAX_NUMBER_MSG(limit));
      return;
    }

    let selectedNumbers = [];
    for (let i = 1; i <= maxNumber; ++i) selectedNumbers.push(i);

    const newBoard = [];
    let index = 0;
    shuffle(selectedNumbers);
    for (let i = 0; i < rows; ++i) {
      const row = [];
      for (let j = 0; j < cols; ++j) {
        row.push(selectedNumbers[index++]);
      }
      newBoard.push(row);
    }
    setBoard(newBoard);
  };

  const checkBingo = (board, calledNumbers) => {

    if (!board || board.length === 0) return false;

    let cnt = 0;
    for (let row = 0; row < rows; ++row) {
      cnt = 0;
      for (let col = 0; col < cols; ++col) {
        if (calledNumbers.includes(board[row][col])) ++cnt;
      }
      if (cnt == cols) return true;
    }

    for (let col = 0; col < cols; ++col) {
      cnt = 0;
      for (let row = 0; row < rows; ++row) {
        if (calledNumbers.includes(board[row][col])) ++cnt;
      }
      if (cnt == rows) return true;
    }

    if (cols == rows) {
      cnt = 0;
      for (let row = 0; row < rows; ++row) {
        if (calledNumbers.includes(board[row][row])) ++cnt;
      }
      if (cnt == rows) return true;

      let col = cols - 1;
      cnt = 0;
      for (let row = 0; row < rows; ++row) {
        if (calledNumbers.includes(board[row][col])) ++cnt;
        --col;
      }
      if (cnt == rows) return true;
    }
    return false;
  };

  useEffect(() => {
    generateBoard();
  }, [rows, cols, maxNumber]);

  useEffect(() => {
    if (!isGameOver && checkBingo(board, calledNumbers)) {
      handleWin();
    }
  }, [calledNumbers, isGameOver]);

  return (
    <div className="player-board">
      <h2 className="player-name">플레이어 {playerIndex + 1}</h2>
      <Board board={board} calledNumbers={calledNumbers} />
    </div>
  );
}
