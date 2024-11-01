import React from "react";

export default function Referee({ callNumber, isGameOver, calledNumbers }) {
  return (
    <div className="referee-container">
      <button onClick={callNumber} disabled={isGameOver}>CALL</button>
      <div>Called Numbers: {calledNumbers.join(", ")}</div>
    </div>
  );
}