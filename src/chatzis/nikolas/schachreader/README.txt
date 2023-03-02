This is a default chess game played via the console.

Implement new board with: Board.createNewBoard();

and a defined scenario with: Board.createNewBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");



KNOWN BUGS:
While checking all possible moves in depth 4 the number '197281' is returned instead of '197742'.
To this date I have not found the missing moves.
