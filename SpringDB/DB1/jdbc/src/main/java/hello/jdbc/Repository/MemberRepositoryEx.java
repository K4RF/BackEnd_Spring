package hello.jdbc.Repository;

import hello.jdbc.domain.Member;

import java.sql.SQLException;

public interface MemberRepositoryEx {
    Member save(Member member) throws SQLException;

    Member findById(String memberId);

    void update(String memberId, int money);

    void delete(String memberId);
}
