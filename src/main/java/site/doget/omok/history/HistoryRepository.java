package site.doget.omok.history;

import org.springframework.data.jpa.repository.JpaRepository;
import site.doget.omok.user.OmokUser;

public interface HistoryRepository extends JpaRepository<OmokUser, Long> {
}
