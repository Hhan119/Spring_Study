package com.example.boot14.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.boot14.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("SELECT cr FROM ChatRoom cr " +
	           "JOIN ChatParticipant cp1 ON cr.id = cp1.room.id " +
	           "JOIN ChatParticipant cp2 ON cr.id = cp2.room.id " +
	           "WHERE cp1.user.id = :userId1 AND cp2.user.id = :userId2 AND cr.type = 'ONE_ON_ONE'")
	    Optional<ChatRoom> findOneOnOneRoom(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

	    @Query("SELECT cr FROM ChatRoom cr " +
	           "WHERE cr.type = 'GROUP' AND cr.id IN (" +
	           "   SELECT cp.room.id FROM ChatParticipant cp " +
	           "   WHERE cp.user.id IN :userIds " +
	           "   GROUP BY cp.room.id " +
	           "   HAVING COUNT(cp.user.id) = :size)")
	    Optional<ChatRoom> findGroupRoomByParticipants(@Param("userIds") List<Long> userIds, @Param("size") Long size);
	}