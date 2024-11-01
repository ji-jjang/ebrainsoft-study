import React, { useEffect } from "react";
import { BOARD_MESSAGES } from "./constants/boardMessage";

export default function WinnerChecker({ winningPlayers, players, setIsGameOver }) {
  useEffect(() => {
    if (winningPlayers.length > 0) {
      setIsGameOver(true);
      alert(BOARD_MESSAGES.GAME_END_MSG(winningPlayers));
    }
  }, [winningPlayers]);

  return null;
}
