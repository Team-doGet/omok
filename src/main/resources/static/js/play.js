
let stompClient = null;
const connect = (board, userA, userB) => {
    //소켓
    const socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe(
            `/topic/game/${gameId}`,
            function (message) {
                const { sender, turn, x, y, stone } = JSON.parse(message.body);
                if (sender !== user) {
                    Omok.turn = turn;
                    board.setStone(x, y, stone);
                    board.print();
                    if (board.checkVictory(x, y, stone)) {
                        sendEnd(sender, stone);
                        return;
                    }
                    Omok.play(board, userA, userB);
                }
            }
        );
        stompClient.subscribe(
            `/topic/game/${gameId}/end`,
            function (message) {
                const { winner, stone } = JSON.parse(message.body);
                alert(winner + " " + "님이 승리하였습니다. 게임을 종료합니다.");
                location.href = "/";
            }
        );
        const url = stompClient["ws"]["_transport"]["url"];
        const urlObject = new URL(url);
        const sessionId = urlObject.pathname.split("/")[3];
        const destination = `/app/game/${gameId}/join`;
        const message = JSON.stringify({
            playerName1: player1,
            playerName2: player2,
            playerSession1:
                user === player1 ? sessionId : "",
            playerSession2:
                user === player2 ? sessionId : "",
        });
        stompClient.send(destination, {}, message);
    });
};

const sendSetStone = (position, stone, turn) => {
    const destination = `/app/game/${gameId}/setStone`;
    const [x, y] = position;
    const message = JSON.stringify({
        sender: user,
        x,
        y,
        stone,
        turn,
    });
    stompClient.send(destination, {}, message);
};
const sendEnd = (winner, stone) => {
    const destination = `/app/game/${gameId}/end`;
    stompClient.send(
        destination,
        {},
        JSON.stringify({
            sender: user,
            loss: user,
            stone,
            winner,
        })
    );
};

class Player {
    constructor(name, stone) {
        this.name = name;
        this.stone = stone;
    }
}

class Board {
    constructor(size) {
        this.size = size;
        this.map = new Array(size)
            .fill()
            .map(() => new Array(size).fill(true));
    }
    print() {
        const board = document.getElementById("board");
        board.innerHTML = ``;
        for (let row = 0; row < this.size; row++) {
            let output = `<div class="row"><div class="bounding w15"></div>`;
            for (let col = 0; col < this.size; col++) {
                output +=
                    this.map[row][col] === true
                        ? `<input class="tyle" value="${row}/${col}">`
                        : `<div class="setStone">${this.map[row][col]}</div>`;
            }
            board.innerHTML +=
                output + `<div class="bounding w15"></div></div>`;
        }
        const h15 = `<div class="bounding h15"></div>`;
        const bound = `<div class="row">${h15.repeat(this.size + 1)}</div>`;
        board.innerHTML = bound + board.innerHTML + bound;
    }
    checkBounds(arr) {
        return (
            arr[0] >= 0 &&
            arr[0] < this.size &&
            arr[1] >= 0 &&
            arr[1] < this.size
        );
    }
    setStone(row, col, stoneType) {
        if (this.checkBounds([row, col]) && this.map[row][col]) {
            this.map[row][col] = stoneType;
            return true;
        }
        return false;
    }
    checkVictory(row, col, stoneType) {
        const count = [0, 0, 0, 0];
        for (let i = -4; i < 5; i++) {
            const value = [
                [row - i, col + 0], //상하
                [row + 0, col - i], //좌우
                [row - i, col - i], //좌상우하
                [row - i, col + i], //우상좌하
            ];
            for (let j = 0; j < count.length; j++) {
                if (
                    this.checkBounds(value[j]) &&
                    stoneType === this.map[value[j][0]][value[j][1] * 1]
                ) {
                    if (++count[j] >= 5) return true;
                } else {
                    count[j] = 0;
                }
            }
        }
        return false;
    }
}

class Omok {
    static turn = 0;
    static play(board, userA, userB) {
        const point = document.querySelectorAll(".tyle");
        point.forEach((element) => {
            element.addEventListener("click", (event) => {
                let player = this.turn % 2 == 0 ? userA : userB;
                if (player.name === user) {
                    const position = element.value.split("/").map((e) => {
                        return Number(e);
                    });
                    if (!board.setStone(...position, player.stone)) {
                        alert("둘 수 없는 자리입니다.");
                        return;
                    }
                    board.print();
                    sendSetStone(position, player.stone, ++Omok.turn);
                    if (board.checkVictory(...position, player.stone)) {
                        alert(
                            player.name + "님이 승리하였습니다. 게임을 종료합니다."
                        );
                        this.end();
                        return;
                    }
                }
            });
        });
    }
    static end() {
        const point = document.querySelectorAll(".tyle");
        point.forEach((element) => {
            element.removeEventListener();
        });
    }
    static main() {
        let userA = new Player(player1, "⚫");
        let userB = new Player(player2, "⚪");
        let board = new Board(19);
        connect(board, userA, userB);
        board.print();
        this.play(board, userA, userB);
    }
}

Omok.main();