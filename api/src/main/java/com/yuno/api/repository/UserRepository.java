package com.yuno.api.repository;

import com.yuno.api.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //khai báo đây là Repository
//một Interface (giao diện) kế thừa JpaRepository
public interface UserRepository extends JpaRepository<User, Integer> {
                                                //<entity, kdl khóa chính>
    //extends vậy để spring tự cung cấp hàm thay vì xài lệnh sql
    Optional<User> findByUsername(String username);
}
