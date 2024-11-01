import React from "react";

export default function Tile({ number, isMarked }) {
  return (
    <td className={`tile ${isMarked ? "marked" : ""}`}>
      {number}
    </td>
  );
}
