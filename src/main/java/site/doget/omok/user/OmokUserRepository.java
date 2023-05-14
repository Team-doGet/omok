package site.doget.omok.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface OmokUserRepository extends JpaRepository<OmokUser, Long> {
    OmokUser findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE OmokUser u SET u.win = u.win + 1 WHERE u.username = :username")
    void incrementWinByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("UPDATE OmokUser u SET u.loss = u.loss + 1 WHERE u.username = :username")
    void incrementLossByUsername(@Param("username") String username);
}
