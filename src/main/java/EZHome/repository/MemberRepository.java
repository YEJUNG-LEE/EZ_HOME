package EZHome.repository;

import EZHome.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    Member findByMembAddress1(String membAddress1);
}
