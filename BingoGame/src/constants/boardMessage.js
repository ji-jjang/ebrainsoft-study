export const BOARD_MESSAGES = {
  INVALID_MAX_NUMBER_MSG: (min) => `최대 숫자는 ${min} 이상이어야 합니다.`,
  GAME_END_MSG: (winners) =>
    `최종 우승자: ${winners.map((index) => `플레이어 ${index + 1}`).join(", ")}번 플레이어가 우승했습니다.`,
};
