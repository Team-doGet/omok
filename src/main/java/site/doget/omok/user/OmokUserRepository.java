package site.doget.omok.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OmokUserRepository extends JpaRepository<OmokUser, Long> {
    OmokUser findByUsername(String username);
}
