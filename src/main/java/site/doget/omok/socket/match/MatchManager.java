package site.doget.omok.socket.match;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MatchManager {
    private final Queue<String> matchQueue = new ConcurrentLinkedQueue<>();

    // 소켓 추가
    public void addSocketId(String socketId) {
        matchQueue.add(socketId);
    }

    // 소켓 삭제
    public void removeSocketId(String socketId) {
        matchQueue.remove(socketId);
    }

    // 소켓 꺼내기
    public String queuePoll() {
        return matchQueue.poll();
    }

    // 소켓 크기 반환
    public Integer queueSize() {
        return matchQueue.size();
    }
}