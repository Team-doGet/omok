function addClass(element, className) {
  if (!element.classList.contains(className)) {
    element.classList.add(className);
  }
}
function removeClass(element, className) {
  if (element.classList.contains(className)) {
    element.classList.remove(className);
  }
}

// const sender = "[[${user.username}]]"; // 사용자명 설정
let stompClient = null;

//스토리지
const conversation = [];
// 대화 내역을 로컬 스토리지에 저장하는 함수
function saveConversationToLocalStorage() {
  localStorage.setItem('conversation', JSON.stringify(conversation));
}

// chat-container 요소의 높이를 기준으로 스크롤 위치를 계산하는 함수
function scrollToBottom() {
  const chatContainer = document.getElementById('chatMessages');
  chatContainer.scrollTop = chatContainer.scrollHeight;
}

function showMessage(message) {
  const chatMessages = document.getElementById('chatMessages');
  const p = document.createElement('p');
  p.textContent = `${message.content}`;
  if (message.sender !== 'system') {
    p.textContent = `${message.sender} : ${p.textContent}`;
  }
  if (message.time !== undefined) {
    const span = document.createElement('span');
    span.textContent = '[' + new Date(message.time) + ']';
    p.appendChild(span);
  }
  chatMessages.appendChild(p);
  scrollToBottom();
}

//user list
function displayChatParticipants(participants) {
  var participantsList = document.getElementById('participants-list');
  participantsList.innerHTML = ''; // 기존 목록 초기화
  participants.forEach((participant) => {
    var participantItem = document.createElement('div');
    var win_ratio = `${((participant.win / (participant.win + participant.loss)) * 100).toFixed(2)}`;
    participantItem.classList.add('user-list-item');
    if (isNaN(win_ratio)) {
      win_ratio = 0;
    }
    participantItem.textContent = `${participant.sender}(${participant.win}승 / ${participant.loss}패) 승률 : ${win_ratio}%`;
    participantsList.appendChild(participantItem);
  });
}

function connect() {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    // 대화를 받아오는 구독
    stompClient.subscribe('/topic/public', (message) => {
      showMessage(JSON.parse(message.body));
      conversation.push(JSON.parse(message.body));
      saveConversationToLocalStorage();
    });
    // 대화 참여자 목록을 받아오는 구독
    stompClient.subscribe('/topic/chatParticipants', (message) => {
      const participants = JSON.parse(message.body);
      displayChatParticipants(participants.userList);
    });
    // 게임 매칭 구독
    stompClient.subscribe('/topic/match', (message) => {
      const matchInfo = JSON.parse(message.body);
      const url = stompClient['ws']['_transport']['url'];
      const urlObject = new URL(url);
      const sessionId = urlObject.pathname.split('/')[3];
      gameMatch(sessionId, matchInfo);
    });

    // 로비에 입장 시 대화 참여자 목록을 요청
    const userInfoDTO = {
      sender,
      win,
      loss,
      content: '대화명',
    };
    stompClient.send('/app/lobby/join', {}, JSON.stringify(userInfoDTO));
  });
}
function disconnect() {
  if (stompClient !== null) {
    // 대화 떠남
    stompClient.send('/app/lobby/leave', {}, JSON.stringify({ sender }));
    stompClient.disconnect();
  }
  console.log('Disconnected');
}

function sendMessage() {
  const messageInput = document.getElementById('chat-input');
  const message = messageInput.value;
  if (message === '') return;
  const chatMessageDTO = {
    content: message,
    sender,
    time: new Date(),
  };
  stompClient.send('/app/lobby/chat', {}, JSON.stringify(chatMessageDTO));
  messageInput.value = '';
}

const matchStartEvent = () => {
  sendMatchMessage('start');
  sendMatch('start');
  const start = document.getElementById('start-button');
  const cancel = document.getElementById('cancel-button');
  addClass(start, 'none');
  removeClass(cancel, 'none');
};
const matchCancelEvent = () => {
  sendMatchMessage('cancel');
  sendMatch('cancel');
  const start = document.getElementById('start-button');
  const cancel = document.getElementById('cancel-button');
  removeClass(start, 'none');
  addClass(cancel, 'none');
};

const sendMatchMessage = (type) => {
  const MatchRequest = {
    content: `${sender}님께서 매칭을 ${type}하였습니다.`,
    sender,
    type,
  };
  stompClient.send('/app/lobby/matchMessage', {}, JSON.stringify(MatchRequest));
};
const sendMatch = (type) => {
  const MatchRequest = {
    content: `${sender}님께서 매칭을 ${type}하였습니다.`,
    sender,
    type,
  };
  stompClient.send('/app/lobby/match', {}, JSON.stringify(MatchRequest));
};
const gameMatch = (id, info) => {
  console.log(id, info.player1, info.player2);
  if (id === info.player1 || id === info.player2) {
    location.href = '/play';
  }
};

// 로컬 스토리지에서 대화 내역을 가져오는 함수
function getConversationFromLocalStorage() {
  const storedConversation = localStorage.getItem('conversation');
  if (storedConversation) {
    return JSON.parse(storedConversation);
  }
  return [];
}

// 페이지 로드 시에 저장된 대화 내역을 가져와 화면에 표시
window.onload = function () {
  // 저장된 대화 내역 가져오기
  const storedConversation = getConversationFromLocalStorage();
  // 대화 내역을 화면에 표시
  for (let i = 0; i < storedConversation.length; i++) {
    const message = storedConversation[i];
    conversation.push(message);
    showMessage(message);
  }
};

document.getElementById('chatForm').addEventListener('submit', function (event) {
  event.preventDefault();
  sendMessage();
});

connect();
addEventListener('beforeunload', (event) => {
  const userDisconnectMessage = { username };
  stompClient.send('/app/lobby/leave', {}, JSON.stringify(userDisconnectMessage));
});

window.addEventListener('beforeunload', () => {
  const userDisconnectMessage = { username };
  stompClient.send('/app/lobby/leave', {}, JSON.stringify(userDisconnectMessage));
});
